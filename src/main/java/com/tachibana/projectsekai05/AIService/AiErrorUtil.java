package com.tachibana.projectsekai05.AIService;

import dev.langchain4j.exception.AuthenticationException;
import dev.langchain4j.exception.HttpException;
import dev.langchain4j.exception.ModelNotFoundException;
import dev.langchain4j.exception.TimeoutException;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * AI 调用错误转友好提示
 * <p>
 * langchain4j 会把 OpenAI 兼容网关的 HTTP 响应按状态码映射成异常，
 * 但异常 message 可能是整页 HTML（如网关 404 页面），不直接展示给用户。
 * 这里统一收敛成简洁、可操作的中文提示，供流式（onErrorResume）与同步（GlobalExceptionHandler）共用。
 */
public final class AiErrorUtil {

    private AiErrorUtil() {
    }

    public static String friendly(Throwable e) {
        if (e instanceof AuthenticationException) {
            return "AI 服务鉴权失败，请检查 api-key 配置";
        }
        if (e instanceof ModelNotFoundException) {
            return "AI 服务返回 404：请检查 base-url（应指向 OpenAI 兼容端点，如 https://opencode.ai/zen/go/v1）与模型名";
        }
        if (e instanceof HttpException http) {
            return "AI 服务返回 HTTP " + http.statusCode() + "，请检查 api-key / base-url / 模型名配置";
        }
        if (e instanceof TimeoutException || e instanceof ResourceAccessException
                || hasCause(e, ConnectException.class, SocketTimeoutException.class)) {
            return "无法连接 AI 服务，请检查网络或 base-url 配置";
        }
        String msg = e.getMessage();
        if (msg == null || msg.isBlank()) {
            return e.getClass().getSimpleName();
        }
        if (msg.startsWith("<!DOCTYPE") || msg.startsWith("<html")) {
            return "AI 服务返回异常响应，请检查 base-url 是否指向 OpenAI 兼容端点";
        }
        return msg.length() > 200 ? msg.substring(0, 200) + "…" : msg;
    }

    @SafeVarargs
    private static boolean hasCause(Throwable e, Class<? extends Throwable>... types) {
        Throwable t = e;
        while (t != null) {
            for (Class<? extends Throwable> type : types) {
                if (type.isInstance(t)) {
                    return true;
                }
            }
            t = t.getCause();
        }
        return false;
    }
}