
# WMS - 智能仓储管理系统

基于Spring Boot + MyBatis的企业级仓储管理系统，具备高并发防超卖能力、智能拣货路径优化和全链路追溯功能，并集成AI智能助手。

## 技术栈

- Spring Boot 3.3.6
- MyBatis 3.0.4
- MySQL 8.0.33
- Redis (分布式锁)
-Spring AI 阿里巴巴 1.1.2.2（通义千问DashScope）

## 核心亮点

### ️ 三层库存模型防超卖
- **预占库存 → 锁定库存 → 扣减库存**三级状态机设计
- Redis分布式锁保证高并发场景下的数据一致性
- 事务隔离级别优化，避免脏读和幻读问题

### 🚀 波次合并与S型路径优化
- 订单波次合并算法，批量处理提升拣货效率
- S型拣货路径规划，减少行走距离
- 动态批次划分策略，平衡系统负载

###  全链路库存流水追溯
- 从采购入库到销售出库的完整操作日志
- 支持正向追踪和反向溯源双向查询
- 细粒度记录库存变动原因、时间和操作人员

### AI智能助手 (v2.0新增)
集成通义千问大模型，支持自然语言查询仓储数据。

#### 支持的查询类型

1. **SKU信息查询**
   - 示例：`帮我查询SKU-00000001的信息`
   - 支持多种SKU格式：SKU-00000001、SKU0001、SK0001等

2. **库存查询**
   - 示例：`查询SKU-00000001的现有库存`
   - 自动聚合所有仓库的库存总量

3. **出库TOP排行**
   - 示例：`帮我查询近20天出库top3商品`
   - 支持自定义时间范围和TOP数量
   - 按出库数量降序排列，返回表格格式结果

#### API接口

```
POST /ai/chat
Content-Type: application/json

{
    "message": "查询SKU-00000001的库存"
}
```

## 快速开始

### API Key配置

在 `application.properties` 中配置通义千问API Key：

```属性
# 推荐使用环境变量方式
spring.ai.alibaba.dashscope.api-key=${DASHSCOPE_API_KEY}
```

获取API Key: https://dashscope.console.aliyun.com/

### 数据库初始化

执行以下SQL文件创建AI聊天日志表：

```bash
mysql -u root -p your_database < src/main/resources/sql/ai_chat_log.sql
```

### 编译运行

```bash
mvn clean package
mvn spring-boot:run
```

## 版本历史

### v2.0 (2026-06-02) - AI智能助手

**新增功能**
- 集成通义千问DashScope API，实现智能意图识别
- 支持SKU信息查询：自然语言识别SKU编码，查询商品详细信息
- 支持库存查询：实时查询指定SKU的仓库库存总量
- 支持出库TOP排行：查询指定时段内销售出库TOP N商品
- 添加AI聊天日志记录功能，便于追踪和分析

**优化改进**
- 优化SKU编码正则匹配，支持多种格式（SKU-00000001、SKU0001等）
- 改进SQL聚合查询，正确计算多仓库库存总和
- 增强错误提示，明确告知用户失败原因
- 添加详细的日志输出，便于调试和问题排查

**技术调整**
- Spring Boot版本从4.0.6降级到3.3.6以解决兼容性问题
- MyBatis版本从4.0.1降级到3.0.4
- 新增spring-ai-alibaba-starter-dashscope依赖

### v1.0 - 基础仓储功能
- SKU商品管理
- 仓库库存管理
- 销售出库管理
- 采购入库管理

