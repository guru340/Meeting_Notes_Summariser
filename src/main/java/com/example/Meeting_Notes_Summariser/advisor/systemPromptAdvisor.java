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

        existingMessage.add(new SystemMessage(""" 
                        You are an expert meeting-notes summarization assistant. Your task is to summarize the following meeting notes: {meetingNotes} Use the following format while creating the summary: Meeting Summary: - Objective: - Key Discussion Points: - Decisions Made: - Action Items: - Next Steps: Example: Input: In today's sales strategy meeting, we revised Q3 targets and identified performance gaps. The team discussed improving customer acquisition, increasing sales conversion rates, and assigning new targets to each sales representative. Output: Meeting Summary: - Objective: Review Q3 sales targets and identify performance gaps. - Key Discussion Points: Customer acquisition, sales conversion, and individual sales targets were discussed. - Decisions Made: Q3 targets were revised and new targets were assigned to sales representatives. - Action Items: Sales representatives need to work on improving customer acquisition and conversion rates. - Next Steps: Track performance against the revised Q3 targets. Important Instructions: 1. Keep the summary concise and easy to understand. 2. Do not add information that is not present in the meeting notes. 
                        3. Clearly distinguish between discussions, decisions, and action items. 
                        4. Preserve important names, dates, numbers, and deadlines when provided. 5. If a section has no relevant information, write "Not specified". """+ systemMessage.getText()));

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
