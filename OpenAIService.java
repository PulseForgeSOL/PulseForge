package com.example.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class OpenAIService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "your_openai_api_key";

    public String getResponse(String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        String requestBody = """
        {
            "model": "gpt-4",
            "messages": [
                {"role": "system", "content": "You are an interactive AI assistant."},
                {"role": "user", "content": "%s"}
            ]
        }
        """.formatted(message);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            OPENAI_API_URL, HttpMethod.POST, request, Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("choices")) {
            Map<String, Object> choice = ((Map<String, Object>) ((List<?>) responseBody.get("choices")).get(0));
            return (String) ((Map<String, Object>) choice.get("message")).get("content");
        }

        return "Sorry, I couldn't process your request.";
    }
}
