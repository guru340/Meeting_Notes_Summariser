package com.example.Meeting_Notes_Summariser.advisor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class systemPromptAdvisor implements CallAdvisor, StreamAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        chatClientRequest=updateSystemMessage(chatClientRequest);

        return callAdvisorChain.nextCall(chatClientRequest);
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        chatClientRequest=updateSystemMessage(chatClientRequest);

        return streamAdvisorChain.nextStream(chatClientRequest);
    }

    private ChatClientRequest updateSystemMessage(ChatClientRequest chatClientRequest) {
        List<Message> existingMessage=new ArrayList<>( chatClientRequest.prompt().getInstructions());
        SystemMessage systemMessage=chatClientRequest.prompt().getSystemMessage();

        existingMessage.add(new SystemMessage(""
                      + systemMessage.getText()));

        chatClientRequest=chatClientRequest.mutate().prompt(Prompt.builder().messages(existingMessage).build()).build();

        String message=chatClientRequest.prompt().getSystemMessage().getText();
        log.info("System Message is updated as : {}",message);
        return chatClientRequest;
    }

    @Override
    public String getName() {
        return "systemPromptAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
