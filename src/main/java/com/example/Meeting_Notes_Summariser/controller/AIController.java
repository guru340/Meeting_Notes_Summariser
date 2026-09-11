package com.example.Meeting_Notes_Summariser.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/openai/chat")
public class AIController {

    private String SYSTEM_PROPMT="You are a helpful assistant that summarize any given contact "+
            "Ensure the summary is concise, informative, and captures the key points. "+
            "Do not answer anything other than summarization .If the question is about summarization"+
            "respond with 'I can only help with summarization tasks.' ";
    private  final ChatClient chatClient;

    public AIController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/summarizer")
    public String summarsize(@RequestBody String message){
        return chatClient.prompt().
                user(message).system(SYSTEM_PROPMT)
                .call().
                content();
    }
}
