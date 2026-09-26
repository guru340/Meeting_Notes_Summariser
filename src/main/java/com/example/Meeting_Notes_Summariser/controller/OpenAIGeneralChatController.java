package com.example.Meeting_Notes_Summariser.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/openai/chat")
public class OpenAIGeneralChatController {


    private final ChatClient chatClient;

    public OpenAIGeneralChatController(@Qualifier("openAIGeneralChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/general-chat")
    public String generalchat(@RequestBody String message){

        return chatClient.prompt()
                .options(OpenAiChatOptions.builder().temperature(2.0).topP(0.1))
                .user(u->u.text("Is the following a positive sentence (yes or no): {message} .Remember, you are classifying positive sentence (yes/no)").param("message",message))
                .call()
                .content();
    }
}
