package com.example.Meeting_Notes_Summariser.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.huggingface.HuggingfaceChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AIProviderConfig {

    @Bean("openAIChatClient")
    ChatClient openAIchatClient(OpenAiChatModel openAiChatModel
    ,SimpleLoggerAdvisor simpleLoggerAdvisor,SafeGuardAdvisor safeGuardAdvisor){

        return ChatClient.builder(openAiChatModel).defaultAdvisors(safeGuardAdvisor,simpleLoggerAdvisor).build();
    }

//    @Bean("huggingfaceChatClient")
//    ChatClient huggingfaceChatClient(HuggingfaceChatModel huggingfaceChatModel){
//        return ChatClient.builder(huggingfaceChatModel).build();
//    }

    @Bean
    SimpleLoggerAdvisor simpleLoggerAdvisor(){
        return new SimpleLoggerAdvisor();
    }

    @Bean
    SafeGuardAdvisor safeGuardAdvisor(){
        return new SafeGuardAdvisor(
                List.of("password","ssn","credit card","hack")
        );
    }
}
