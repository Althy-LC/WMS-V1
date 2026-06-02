package com.example1.wms.Controller;

import com.example1.wms.DTO.AiChatRequest;
import com.example1.wms.DTO.AiChatResponse;
import com.example1.wms.Result;
import com.example1.wms.Service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI智能助手控制器
 */
@RestController
@RequestMapping("/ai")
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;

    /**
     * AI智能问答接口
     * @param request 用户自然语言查询
     * @return AI分析结果
     */
    @PostMapping("/chat")
    public Result<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        AiChatResponse response = aiAssistantService.chat(request);
        return Result.success(response);
    }
}
