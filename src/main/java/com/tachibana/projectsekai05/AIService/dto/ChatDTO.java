package com.tachibana.projectsekai05.AIService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI 聊天请求
 */
@Data
@Schema(description = "AI 聊天请求")
public class ChatDTO {

    @NotBlank(message = "会话ID不能为空")
    @Schema(description = "会话ID，同一会话共享上下文记忆", example = "user-1")
    private String sessionId;

    @NotBlank(message = "消息不能为空")
    @Schema(description = "用户消息", example = "介绍一下《葬送的芙莉莲》")
    private String message;
}