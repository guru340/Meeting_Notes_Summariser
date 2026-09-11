package com.example.Meeting_Notes_Summariser.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIProviderConfig {

    @Bean("openAIChatClient")
    ChatClient openAIchatClient(OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel).build();
    }
}
