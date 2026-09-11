package com.example.Meeting_Notes_Summariser.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.errors.OpenAIException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private static final String GROQ_API_KEY="GROQ_API_KEY";
    private static final String GROQ_BASE_URL="https://api.groq.com/openai/v1/chat/completions";
    private static final String GROQ_MODEL="openai/gpt-oss-120b";
    private static final String CONTENT_TYPE="application/json";
    private static final String SYSTEM_PROMPT="You are a helpful assistant that summarize any given contact "+
            "Ensure the summary is concise, informative, and captures the key points. "+
            "Do not answer anything other than summarization .If the question is about summarization"+
            "respond with 'I can only help with summarization tasks.' ";


    private final ObjectMapper objectMapper;

    public AIService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String chat(String prompt) throws Exception {
        try(CloseableHttpClient httpClient= HttpClients.createDefault()){
            var request=getReques(prompt);
            var response=httpClient.execute(request,resp-> EntityUtils.toString(resp.getEntity()));
            return parseResponse(response);
        }catch (IOException e){
            throw new Exception("Could not call OpenAI API using Java Client");
        }
    }

    private String parseResponse(String response) throws JsonProcessingException {
        Map<String,Object> openAIResponse=objectMapper.readValue(response,Map.class);
        return openAIResponse.get("choices").toString();
    }

    private HttpPost getReques(String prompt) throws JsonProcessingException {
        var request = new HttpPost(GROQ_BASE_URL);
        var openApikey = System.getenv(GROQ_API_KEY);
        request.addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openApikey);

        Map<String, Object> userMessage = Map.of("role", "user", "content", prompt);
        Map<String, Object> systemMessage = Map.of("role", "system", "content", SYSTEM_PROMPT);

        Map<String, Object> body = Map.of("model", GROQ_MODEL, "messages", List.of(userMessage, systemMessage));

        String requestbody = objectMapper.writeValueAsString(body);
        request.setEntity(new StringEntity(requestbody));
        return request;


    }
}
