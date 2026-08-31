package com.tachibana.projectsekai05.AIService.config;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;


@Component
public class JsonRedisTemplate extends RedisTemplate<String, Object>{

    public JsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        // 构造函数注入 RedisConnectionFactory，设置到父类
        super.setConnectionFactory(redisConnectionFactory);
        
        // 使用 Jackson 3 的通用 Serializer（默认类型信息 + java.time 原生支持）
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .enableUnsafeDefaultTyping()
                .build();
        
        // String 类型的 key/value 序列化
        super.setKeySerializer(StringRedisSerializer.UTF_8);
        super.setValueSerializer(serializer);
        
        // Hash 类型的 key/value 序列化
        super.setHashKeySerializer(StringRedisSerializer.UTF_8);
        super.setHashValueSerializer(serializer);
    }
}