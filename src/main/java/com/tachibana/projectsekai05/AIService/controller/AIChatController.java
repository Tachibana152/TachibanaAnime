package com.tachibana.projectsekai05.AIService.controller;

import com.tachibana.projectsekai05.AIService.dto.ChatDTO;
import com.tachibana.projectsekai05.AIService.service.AIChatService;
import com.tachibana.projectsekai05.common.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * AI 智能助手接口
 */
@Tag(name = "AI 智能助手")
@RestController
@RequestMapping("/api/ai")
public class AIChatController {

    private final AIChatService aiChatService;

    public AIChatController(AIChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @Operation(summary = "AI 聊天（同步）", description = "返回完整回复，需登录")
    @PostMapping("/chat")
    public R<String> chat(@Valid @RequestBody ChatDTO dto) {
        return R.success(aiChatService.chat(dto));
    }

    @Operation(summary = "AI 聊天（流式 SSE）", description = "text/event-stream 逐字推送，需登录")
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@Valid @RequestBody ChatDTO dto) {
        return aiChatService.chatStream(dto);
    }
}