package com.example1.wms.Util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
/**
 * @author Althy
 * @Create 2026/5/25 15:28
 * @Description
 * * Redis分布式锁工具类
 *  * 支持看门狗自动续期机制
 */
@Component
public class RedisLockUtil implements DisposableBean {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 看门狗线程池
    private static final ScheduledExecutorService WATCHDOG_EXECUTOR = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(),
            r -> {
                Thread thread = new Thread(r, "redis-lock-watchdog");
                thread.setDaemon(true);
                return thread;
            }
    );

    // 存储每个锁的看门狗任务
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> WATCHDOG_FUTURES = new ConcurrentHashMap<>();

    // 存储每个锁的唯一标识
    private static final ConcurrentHashMap<String, String> LOCK_VALUES = new ConcurrentHashMap<>();

    // Lua脚本：原子性地删除锁（先校验所有权）
    private static final String UNLOCK_LUA_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end";

    //封装lua脚本对象
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(UNLOCK_LUA_SCRIPT, Long.class);

    // 续期间隔(秒) - 锁超时时间的1/3
    private static final long RENEWAL_INTERVAL = 1;

    //尝试获取分布式锁
    public boolean tryLock(String lockKey, long leaseTime) {
        String lockValue = UUID.randomUUID().toString();

        // 使用SET命令原子性地设置key, NX表示只有key不存在时才设置, EX设置过期时间
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, leaseTime, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(success)) {
            // 保存锁的唯一标识
            LOCK_VALUES.put(lockKey, lockValue);
            // 启动看门狗
            startWatchdog(lockKey, lockValue, leaseTime);
            return true;
        }

        return false;
    }

    //启动看门狗自动续期
    private void startWatchdog(String lockKey, String lockValue, long leaseTime) {
        // 定时任务定期续期，保存ScheduledFuture以便后续取消
        ScheduledFuture<?> future = WATCHDOG_EXECUTOR.scheduleAtFixedRate(() -> {
            try {
                // 验证锁是否仍然属于当前线程
                String currentValue = redisTemplate.opsForValue().get(lockKey);
                if (lockValue.equals(currentValue)) {
                    // 续期
                    redisTemplate.expire(lockKey, leaseTime, TimeUnit.SECONDS);
                } else {
                    // 锁已不属于当前线程或已过期，停止续期
                    stopWatchdog(lockKey);
                }
            } catch (Exception e) {
                // 记录日志但不抛出异常,避免影响主流程
                System.err.println("Redis锁续期失败: " + e.getMessage());
                stopWatchdog(lockKey);
            }
        }, RENEWAL_INTERVAL, RENEWAL_INTERVAL, TimeUnit.SECONDS);

        // 保存future引用
        ScheduledFuture<?> existing = WATCHDOG_FUTURES.putIfAbsent(lockKey, future);
        if (existing != null) {
            // 如果已存在看门狗任务，取消新创建的并保留原有的
            future.cancel(false);
        }
    }

    //停止看门狗

    private void stopWatchdog(String lockKey) {
        ScheduledFuture<?> future = WATCHDOG_FUTURES.remove(lockKey);
        if (future != null) {
            future.cancel(false);
        }
        LOCK_VALUES.remove(lockKey);
    }

    //释放分布式锁

    public void unlock(String lockKey) {
        // 停止看门狗
        stopWatchdog(lockKey);

        // 使用Lua脚本原子性地删除锁（先校验所有权）
        String lockValue = LOCK_VALUES.get(lockKey);
        if (lockValue != null) {
            try {
                Long result = redisTemplate.execute(
                        UNLOCK_SCRIPT,
                        Collections.singletonList(lockKey),
                        lockValue
                );
                if (result == null || result == 0) {
                    System.err.println("释放锁失败，锁不属于当前线程或已过期: " + lockKey);
                }
            } catch (Exception e) {
                System.err.println("释放锁异常: " + e.getMessage());
            }
        }
    }


    //销毁时关闭线程池
    @Override
    public void destroy() {
        // 取消所有看门狗任务
        WATCHDOG_FUTURES.values().forEach(future -> future.cancel(false));
        WATCHDOG_FUTURES.clear();
        LOCK_VALUES.clear();

        // 关闭线程池
        WATCHDOG_EXECUTOR.shutdown();
        try {
            if (!WATCHDOG_EXECUTOR.awaitTermination(5, TimeUnit.SECONDS)) {
                WATCHDOG_EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            WATCHDOG_EXECUTOR.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
