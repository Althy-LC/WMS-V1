package com.example1.wms.Mapper;

import com.example1.wms.POJO.AiChatLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI聊天日志Mapper
 */
@Mapper
public interface AiChatLogMapper {
    /**
     * 插入AI聊天日志
     */
    void insert(AiChatLog log);
}
