package com.tachibana.projectsekai05.AIService.service.impl;

import com.tachibana.projectsekai05.AIService.AiErrorUtil;
import com.tachibana.projectsekai05.AIService.Assistant;
import com.tachibana.projectsekai05.AIService.dto.ChatDTO;
import com.tachibana.projectsekai05.AIService.service.AIChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * AI 聊天服务实现：委托 {@link Assistant}（RAG 检索 + Redis 会话记忆由 langchain4j 自动注入）
 * <p>
 * 流式方法对异常做 onErrorResume 兜底：SSE 响应头发出后，后端异常只能通过关闭连接结束，
 * 前端 fetch 无法感知 HTTP 层面的错误（表现为"空回复"）。因此把异常转成 `[error] ...` 帧
 * 推给前端，让用户看到真实原因。
 */
@Slf4j
@Service
public class AIChatServiceImpl implements AIChatService {

    private final Assistant assistant;

    public AIChatServiceImpl(Assistant assistant) {
        this.assistant = assistant;
    }

    @Override
    public String chat(ChatDTO dto) {
        return assistant.chat(dto.getSessionId(), dto.getMessage());
    }

    @Override
    public Flux<String> chatStream(ChatDTO dto) {
        return assistant.chatStream(dto.getSessionId(), dto.getMessage())
                .onErrorResume(e -> {
                    log.error("AI 流式对话失败, sessionId={}", dto.getSessionId(), e);
                    return Flux.just("[error] " + AiErrorUtil.friendly(e));
                });
    }
}