package com.example1.wms.DTO;

import lombok.Data;

/**
 * AI聊天请求DTO
 */
@Data
public class AiChatRequest {
    // 用户输入的自然语言查询
    private String query;
}
