package com.example.Meeting_Notes_Summariser.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/huggingface/chat")
public class huggingfaceController {

    private final static String SYSTEM_PROPMT="You are a senior engineer. Generate code based on the given description. "+
            "Ensure the code is idiomatic, efficient, and follows best practices.";

    private final ChatClient chatClient;

    public huggingfaceController(@Qualifier("huggingfaceChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/generate-code")
    public ChatClientResponse generatecode(@RequestBody String message){
        ChatOptions chatOptions=ChatOptions.builder()
                .model("ai/mistral:7b")
                .maxTokens(50)
                .build();

        return chatClient.prompt()
                .system(SYSTEM_PROPMT).
                user(message)
                .call().chatClientResponse();
    }
}
