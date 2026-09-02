# AI 会话记忆调用链（RedisChatMemoryStore）

## 概述

`RedisChatMemoryStore` 是 langchain4j `ChatMemoryStore` 接口的 Redis 实现，
用于把每个会话（`@MemoryId`）的聊天历史持久化到 Redis，服务重启后记忆不丢失。
它不直接由业务代码调用，而是被 langchain4j 的 `MessageWindowChatMemory` 在读写消息时回调。

## 依赖方向（谁注入谁）

```
JsonRedisTemplate ──构造器注入──> RedisChatMemoryStore ──构造器注入──> ChatMemoryConfig
```

- `RedisChatMemoryStore`（`@Component`）依赖 `JsonRedisTemplate`，Spring 自动注入（`RedisChatMemoryStore.java:31`）。
- `ChatMemoryConfig` 构造器接收 `RedisChatMemoryStore`（`ChatMemoryConfig.java:27`）。

## 运行时调用链（谁真正调用）

```
Assistant(@MemoryId sessionId)  --每次对话-->  ChatMemoryProvider.get(sessionId)
                                                      │ 创建/复用
                                                      ▼
                                             MessageWindowChatMemory（滑动窗口 20 条）
                                                      │ 读写/淘汰时回调 ChatMemoryStore 接口
                                                      ▼
                                             RedisChatMemoryStore
                                               ├─ getMessages()    读记忆
                                               ├─ updateMessages() 写记忆
                                               └─ deleteMessages() 删记忆
                                                      ▼
                                                  Redis chat:memory:{sessionId}
```

## 关键点

- `ChatMemoryConfig` 里的 `chatMemoryStore` 字段最终传给
  `MessageWindowChatMemory.builder().chatMemoryStore(...)`（`ChatMemoryConfig.java:36`），而非直接调用。
- 真正触发 `RedisChatMemoryStore` 三个方法的是 langchain4j 的 memory 对象。
- `ChatMemoryConfig` 与 `RagConfig` 都是普通 Spring `@Configuration`（Spring 读取类），
  其产出的 `ChatMemoryProvider` / `ContentRetriever` bean 由 langchain4j-spring-boot4-starter
  自动扫描并注入 `@AiService` 的 `Assistant`，业务代码中无需手动 `@Autowired`。

## 完整关系图

```mermaid
flowchart TB
    subgraph Spring[Spring 容器 - 读取 @Configuration 并注册 bean]
        CC[ChatMemoryConfig] --> CP[ChatMemoryProvider bean]
        RC[RagConfig] --> CR[ContentRetriever bean]
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
    AIS -.每会话建记忆.-> RM
```
