package com.example1.wms.POJO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI聊天日志实体类
 */
@Data
public class AiChatLog {
    private Long id;
    // 用户输入的自然语言查询
    private String userQuery;
    // AI返回的结果
    private String aiResponse;
    // 使用的模型名称
    private String model;
    // 提示词模板
    private String promptTemplate;
    // 查询耗时(毫秒)
    private Long durationMs;
    // 状态: 1-成功, 0-失败
    private Integer status;
    // 错误信息
    private String errorMsg;
    private LocalDateTime createTime;
}
