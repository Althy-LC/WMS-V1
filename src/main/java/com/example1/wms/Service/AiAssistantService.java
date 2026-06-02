package com.example1.wms.Service;

import com.example1.wms.DTO.AiChatRequest;
import com.example1.wms.DTO.AiChatResponse;

/**
 * AI智能助手服务接口
 */
public interface AiAssistantService {

    /**
     * 处理用户自然语言查询，返回AI分析结果
     * @param request 用户查询请求
     * @return AI响应结果
     */
    AiChatResponse chat(AiChatRequest request);
}
