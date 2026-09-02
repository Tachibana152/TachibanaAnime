package com.tachibana.projectsekai05.AIService;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

@AiService
public interface Assistant {

    /**
     * 带记忆的多轮对话（同步）
     *
     * @param sessionId 会话 ID，同一会话共享上下文记忆
     * @param message   用户消息
     * @return AI 回复
     */
    @SystemMessage("""
            你是一个乐于助人的 AI 助手。
            系统会为你检索相关知识库内容并放入对话上下文中。
            回答时请优先依据知识库内容作答；若知识库中没有相关信息，请如实说明并基于你的常识回答。
            请记住与用户的对话内容，回答时保持上下文连贯。
            回答要简洁、准确、友好。
            """)
    String chat(@MemoryId String sessionId, @UserMessage String message);

    /**
     * 带记忆的多轮对话（流式）
     * <p>
     * 返回 {@link Flux}，AI 回复会逐字/逐块推送，
     * 与 {@link #chat(String, String)} 共享同一套会话记忆。
     *
     * @param sessionId 会话 ID，同一会话共享上下文记忆
     * @param message   用户消息
     * @return 可订阅增量 token 的响应式流
     */
    @SystemMessage("""
            你是一个乐于助人的 AI 助手。
            系统会为你检索相关知识库内容并放入对话上下文中。
            回答时请优先依据知识库内容作答；若知识库中没有相关信息，请如实说明并基于你的常识回答。
            请记住与用户的对话内容，回答时保持上下文连贯。
            回答要简洁、准确、友好。
            """)
    Flux<String> chatStream(@MemoryId String sessionId, @UserMessage String message);
}
