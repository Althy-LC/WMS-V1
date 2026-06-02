package com.example1.wms.Service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.example1.wms.DTO.AiChatRequest;
import com.example1.wms.DTO.AiChatResponse;
import com.example1.wms.Mapper.*;
import com.example1.wms.POJO.AiChatLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AI智能助手服务实现类
 */
@Slf4j
@Service
public class AiAssistantServiceImpl implements AiAssistantService {

    @Autowired
    private DashScopeChatModel chatModel;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private SaleDetailMapper saleDetailMapper;

    @Autowired
    private AiChatLogMapper aiChatLogMapper;

    private static final String PROMPT_TEMPLATE_PATH = "prompts/wms-assistant-prompt.st";

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        long startTime = System.currentTimeMillis();
        AiChatLog chatLog = new AiChatLog();
        chatLog.setUserQuery(request.getQuery());
        chatLog.setModel("qwen-plus");

        try {
            // 1. 根据用户查询解析并查询数据库
            String businessData = queryBusinessData(request.getQuery());

            // 2. 加载Prompt模板
            String promptTemplate = loadPromptTemplate();

            // 3. 使用PromptTemplate组装提示词
            PromptTemplate template = new PromptTemplate(promptTemplate);
            Map<String, Object> model = Map.of(
                "businessData", businessData != null && !businessData.isEmpty() ? businessData : "暂无数据",
                "userQuery", request.getQuery()
            );
            Prompt prompt = template.create(model);

            chatLog.setPromptTemplate(promptTemplate);

            // 4. 调用大模型
            ChatResponse response = chatModel.call(new Prompt(List.of(new UserMessage(prompt.getContents()))));
            String aiResult = response.getResult().getOutput().getText();

            // 5. 记录成功日志
            long duration = System.currentTimeMillis() - startTime;
            chatLog.setAiResponse(aiResult);
            chatLog.setDurationMs(duration);
            chatLog.setStatus(1);
            aiChatLogMapper.insert(chatLog);

            // 6. 返回结果
            AiChatResponse aiResponse = new AiChatResponse();
            aiResponse.setResult(aiResult);
            aiResponse.setQuery(request.getQuery());
            return aiResponse;

        } catch (Exception e) {
            log.error("AI助手调用失败", e);
            long duration = System.currentTimeMillis() - startTime;
            chatLog.setDurationMs(duration);
            chatLog.setStatus(0);
            chatLog.setErrorMsg(e.getMessage());
            aiChatLogMapper.insert(chatLog);

            AiChatResponse aiResponse = new AiChatResponse();
            aiResponse.setResult("系统异常，请稍后重试");
            aiResponse.setQuery(request.getQuery());
            return aiResponse;
        }
    }

    /**
     * 根据用户自然语言查询业务数据
     */
    private String queryBusinessData(String userQuery) {
        StringBuilder result = new StringBuilder();

        // 判断是否为库存查询
        if (isStockQuery(userQuery)) {
            String stockData = queryStockData(userQuery);
            if (stockData != null && !stockData.isEmpty()) {
                result.append("【库存数据】\n").append(stockData).append("\n\n");
            }
        }

        // 判断是否为出库TOP查询
        if (isOutboundTopQuery(userQuery)) {
            String outboundData = queryOutboundTopData(userQuery);
            if (outboundData != null && !outboundData.isEmpty()) {
                result.append("【出库排行数据】\n").append(outboundData).append("\n\n");
            }
        }

        // 判断是否为SKU查询
        if (isSkuQuery(userQuery)) {
            String skuData = querySkuData(userQuery);
            if (skuData != null && !skuData.isEmpty()) {
                result.append("【商品信息】\n").append(skuData).append("\n\n");
            }
        }

        return result.toString();
    }

    /**
     * 判断是否为库存查询
     */
    private boolean isStockQuery(String userQuery) {
        return userQuery.contains("库存") || userQuery.contains("现有库存") || 
               userQuery.contains("剩余库存") || userQuery.contains("可用库存");
    }

    /**
     * 判断是否为出库TOP查询
     */
    private boolean isOutboundTopQuery(String userQuery) {
        return userQuery.contains("top") || userQuery.contains("TOP") || 
               userQuery.contains("排行") || userQuery.contains("最多");
    }

    /**
     * 判断是否为SKU查询
     */
    private boolean isSkuQuery(String userQuery) {
        return userQuery.contains("sku") || userQuery.contains("SKU") || 
               userQuery.contains("商品") || userQuery.contains("编码");
    }

    /**
     * 查询库存数据
     */
    private String queryStockData(String userQuery) {
        try {
            // 提取SKU编码
            String skuCode = extractSkuCode(userQuery);
            log.info("从查询语句中提取的SKU编码: {}", skuCode);
            
            if (skuCode != null) {
                Long skuId = skuMapper.getid(skuCode);
                log.info("SKU编码 {} 对应的ID: {}", skuCode, skuId);
                
                if (skuId != null) {
                    Map<String, Object> stockInfo = stockMapper.getTotal(null, skuId);
                    if (stockInfo != null && !stockInfo.isEmpty()) {
                        return formatStockData(stockInfo);
                    } else {
                        return "SKU " + skuCode + " 在系统中存在，但暂无库存数据";
                    }
                } else {
                    return "系统中未找到SKU编码为 [" + skuCode + "] 的商品，请检查编码是否正确";
                }
            }
            // 如果没有指定SKU，返回所有库存概览
            return "请指定SKU编码查询具体库存，例如：查询SKU SK0001的库存";
        } catch (Exception e) {
            log.error("查询库存数据失败", e);
            return "查询库存时发生异常：" + e.getMessage();
        }
    }

    /**
     * 查询出库TOP数据
     */
    private String queryOutboundTopData(String userQuery) {
        try {
            // 提取天数和TOP数量
            int days = extractDays(userQuery);
            int topN = extractTopN(userQuery);

            log.info("查询近{}天出库TOP{}商品", days, topN);
            
            // 调用Mapper查询
            List<Map<String, Object>> topList = saleDetailMapper.getOutboundTopN(days, topN);
            
            log.info("查询结果数量: {}", topList != null ? topList.size() : 0);
            
            if (topList == null || topList.isEmpty()) {
                return "近" + days + "天内暂无已出库（状态=4）的销售记录";
            }
            
            // 格式化为表格
            StringBuilder sb = new StringBuilder();
            sb.append("| 排名 | SKU编码 | 商品名称 | 出库数量 |\n");
            sb.append("|------|---------|----------|----------|\n");
            
            int rank = 1;
            for (Map<String, Object> item : topList) {
                sb.append("| ").append(rank++).append(" | ")
                  .append(item.get("skuCode")).append(" | ")
                  .append(item.get("skuName")).append(" | ")
                  .append(item.get("totalQuantity")).append(" |\n");
            }
            
            return sb.toString();
        } catch (Exception e) {
            log.error("查询出库TOP数据失败", e);
            return "查询出库排行时发生异常：" + e.getMessage();
        }
    }

    /**
     * 查询SKU数据
     */
    private String querySkuData(String userQuery) {
        try {
            String skuCode = extractSkuCode(userQuery);
            if (skuCode != null) {
                var sku = skuMapper.code(skuCode, false);
                if (sku != null) {
                    return "SKU编码: " + sku.getSkuCode() + 
                           ", 名称: " + sku.getSkuName() + 
                           ", 规格: " + sku.getSpec() + 
                           ", 单位: " + sku.getUnit();
                }
            }
        } catch (Exception e) {
            log.error("查询SKU数据失败", e);
        }
        return "";
    }

    /**
     * 从查询语句中提取SKU编码
     */
    private String extractSkuCode(String userQuery) {
        // 匹配多种SKU编码格式：
        // SKU-00000001, SKU00000001, SK00000001, sk-00000001等
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "(?:SKU|SK)[-]?[A-Za-z]?\\d+", 
            java.util.regex.Pattern.CASE_INSENSITIVE
        );
        java.util.regex.Matcher matcher = pattern.matcher(userQuery);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 从查询语句中提取天数
     */
    private int extractDays(String userQuery) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)\\s*天");
        java.util.regex.Matcher matcher = pattern.matcher(userQuery);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 7; // 默认7天
    }

    /**
     * 从查询语句中提取TOP数量
     */
    private int extractTopN(String userQuery) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(?:top|TOP)(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(userQuery);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        // 尝试中文表述
        pattern = java.util.regex.Pattern.compile("前(\\d+)");
        matcher = pattern.matcher(userQuery);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 10; // 默认TOP10
    }

    /**
     * 格式化库存数据
     */
    private String formatStockData(Map<String, Object> stockInfo) {
        // 检查是否有有效数据
        if (stockInfo == null || stockInfo.isEmpty()) {
            return "该商品暂无库存记录";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("| 字段 | 数量 |\n");
        sb.append("|------|------|\n");
        
        boolean hasData = false;
        for (Map.Entry<String, Object> entry : stockInfo.entrySet()) {
            Object value = entry.getValue();
            // 如果值为null或0，显示为0
            if (value == null) {
                value = 0;
            }
            sb.append("| ").append(entry.getKey()).append(" | ").append(value).append(" |\n");
            hasData = true;
        }
        
        if (!hasData) {
            return "该商品暂无库存记录";
        }
        
        return sb.toString();
    }

    /**
     * 加载Prompt模板
     */
    private String loadPromptTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource(PROMPT_TEMPLATE_PATH);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            log.error("加载Prompt模板失败，使用默认模板", e);
            return getDefaultPromptTemplate();
        }
    }

    /**
     * 默认Prompt模板
     */
    private String getDefaultPromptTemplate() {
        return "你是仓储数据分析专员，仅使用传入的业务数据回答，禁止编造数据。\n" +
               "无数据回复暂无对应仓储数据，结果优先表格输出。\n\n" +
               "业务数据：{businessData}\n" +
               "用户问题：{userQuery}\n\n" +
               "请回答：";
    }
}
