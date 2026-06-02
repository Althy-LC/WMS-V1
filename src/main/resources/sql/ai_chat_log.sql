-- AI聊天日志表
CREATE TABLE IF NOT EXISTS `ai_chat_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_query` TEXT NOT NULL COMMENT '用户输入的自然语言查询',
    `ai_response` TEXT COMMENT 'AI返回的结果',
    `model` VARCHAR(50) DEFAULT NULL COMMENT '使用的模型名称',
    `prompt_template` TEXT COMMENT '提示词模板',
    `duration_ms` BIGINT DEFAULT NULL COMMENT '查询耗时(毫秒)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1-成功, 0-失败',
    `error_msg` TEXT COMMENT '错误信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI聊天日志表';
