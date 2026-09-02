package com.tachibana.projectsekai05.AIService.service;

import com.tachibana.projectsekai05.AIService.dto.ChatDTO;
import reactor.core.publisher.Flux;

/**
 * AI 聊天服务
 */
public interface AIChatService {

    /**
     * AI 聊天（同步），返回完整回复
     */
    String chat(ChatDTO dto);

    /**
     * AI 聊天（流式），返回可订阅的增量 token 流
     */
    Flux<String> chatStream(ChatDTO dto);
}