package com.example1.wms.DTO;

import lombok.Data;

/**
 * AI聊天响应DTO
 */
@Data
public class AiChatResponse {
    // AI返回的结构化结果
    private String result;
    // 原始查询语句
    private String query;
}
