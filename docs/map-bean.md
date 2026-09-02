flowchart TB
subgraph Spring[Spring 容器 - 读取 @Configuration 并注册 bean]
CC[ChatMemoryConfig] --> CP[ChatMemoryProvider bean]
RC[RagConfig] --> EM[EmbeddingModel bean]
RC --> JU[UnifiedJedis bean]
RC --> ES[EmbeddingStore bean]
RC --> CR[ContentRetriever bean]
RC --> AR[ApplicationRunner bean]
RC --> RM[RedisChatMemoryStore bean]
RC -.注入.-> RM
end

    subgraph LangChain4j[langchain4j-spring-boot4-starter 自动装配]
        AIS[Assistant @AiService 代理]
        AIS -->|扫描并注入| CP
        AIS -->|扫描并注入| CR
        AIS -->|@MemoryId 调用| CP
    end

    subgraph Redis[Redis]
        VEC[(向量库 embedding-index)]
        MEM[(chat:memory:{id})]
    end

    CP -->|MessageWindowChatMemory| RM
    RM --> MEM
    ES --> VEC
    CR -.检索.-> ES
    CR -.嵌入.-> EM
    AIS -.每会话建记忆.-> RM