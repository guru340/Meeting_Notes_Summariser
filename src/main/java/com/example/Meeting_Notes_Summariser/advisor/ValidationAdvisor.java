package com.example.Meeting_Notes_Summariser.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class ValidationAdvisor implements CallAdvisor, StreamAdvisor {
    private static final int MAX_INPUT_TOKEN = 5000;

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        validateinput(chatClientRequest);
        ChatClientResponse chatClientResponse=callAdvisorChain.nextCall(chatClientRequest);
        validateOutput(chatClientResponse);
        return chatClientResponse;
    }

    private void validateOutput(ChatClientResponse chatClientResponse) {
        ChatResponse chatResponse= chatClientResponse.chatResponse();

        if(chatResponse==null){
            throw new IllegalArgumentException("LLM returned a null result");
        }
        String output=chatResponse.getResult().getOutput().getText();

        if(output==null || output.trim().isEmpty()){
            throw new IllegalArgumentException("LLM returned a empty response");
        }
    }

    private void validateinput(ChatClientRequest chatClientRequest) {
        String input=chatClientRequest.prompt().getContents();
        if (input.length()>MAX_INPUT_TOKEN){
            throw new IllegalArgumentException("Prompt too long: exceeds "+MAX_INPUT_TOKEN+"character");
        }
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    @Override
    public String getName() {
        return "ValidationAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
