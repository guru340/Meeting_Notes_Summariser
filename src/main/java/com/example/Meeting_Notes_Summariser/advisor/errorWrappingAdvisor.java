package com.example.Meeting_Notes_Summariser.advisor;

import com.example.Meeting_Notes_Summariser.dto.SummarizationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class errorWrappingAdvisor implements CallAdvisor, StreamAdvisor {
    private final ObjectMapper objectMapper;

    public errorWrappingAdvisor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        log.info("Request received in ErrorWrappingAdvisior with prompt : {}",
                chatClientRequest.prompt().getUserMessage().getText());

        ChatClientResponse chatClientResponse=callAdvisorChain.nextCall(chatClientRequest);

        String assistMessage=chatClientResponse.chatResponse().getResult().getOutput().getText().trim();

        if(!assistMessage.startsWith("```json") && !assistMessage.startsWith("{") && !assistMessage.matches("(?s)^\\[\\s*\\{.*")){
            try {
                SummarizationResponse summarizationResponse = new SummarizationResponse(null, null, assistMessage);
                chatClientResponse.mutate()
                        .chatResponse(ChatResponse.builder().generations(List.of(new Generation(new AssistantMessage(objectMapper.writeValueAsString(summarizationResponse))))).build())
                        .context(Map.copyOf(chatClientRequest.context())).build();
            } catch (JacksonException e) {
                throw new RuntimeException(e);
            }
        }

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        return null;
    }

    @Override
    public String getName() {
        return "errorWrappingAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
