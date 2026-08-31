package com.tachibana.projectsekai05.AIService.memoryStore;

import com.tachibana.projectsekai05.AIService.config.JsonRedisTemplate;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 Redis 的会话记忆持久化存储。
 * <p>
 * 每个会话（memoryId）对应一个 Redis key，value 为整个会话所有消息的 JSON。
 * 借助 {@link ChatMessageSerializer}/{@link ChatMessageDeserializer}
 * 完成 ChatMessage 与 JSON 的互相转换，消息被淘汰时会同步从 Redis 移除。
 */
@Component
@Slf4j
public class RedisChatMemoryStore implements ChatMemoryStore {

    /** Redis key 前缀，避免与其它业务 key 冲突 */
    private static final String KEY_PREFIX = "chat:memory:";

    private final JsonRedisTemplate redisTemplate;

    public RedisChatMemoryStore(JsonRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        Object value = redisTemplate.opsForValue().get(key(memoryId));
        if (value == null) {
            return new ArrayList<>();
        }
        List<ChatMessage> messages = ChatMessageDeserializer.messagesFromJson(value.toString());
        log.debug("[记忆:{}] 从 Redis 读取 {} 条消息", memoryId, messages.size());
        return messages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = ChatMessageSerializer.messagesToJson(messages);
        redisTemplate.opsForValue().set(key(memoryId), json);
        log.debug("[记忆:{}] 写入 Redis 共 {} 条消息", memoryId, messages.size());
    }

    @Override
    public void deleteMessages(Object memoryId) {
        redisTemplate.delete(key(memoryId));
        log.debug("[记忆:{}] 已从 Redis 删除", memoryId);
    }

    private String key(Object memoryId) {
        return KEY_PREFIX + memoryId;
    }
}
