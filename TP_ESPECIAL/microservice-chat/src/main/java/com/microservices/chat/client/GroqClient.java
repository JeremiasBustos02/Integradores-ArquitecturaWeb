package com.microservices.chat.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Component
public class GroqClient {
    private static final Logger log = LoggerFactory.getLogger(GroqClient.class);
    private final RestTemplate rest;
    private final String baseUrl;
    private final String apiKey;
    private final String model;

    public GroqClient(
            @Value("${groq.base-url:https://api.groq.com/openai}") String baseUrl,
            @Value("${groq.api-key}") String apiKey,
            @Value("${groq.model:llama-3.3-70b-versatile}") String model
    ) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.model = model;
        
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GROQ API key no configurada. Revisá groq.api-key.");
        }
        if (!apiKey.startsWith("gsk_")) {
            throw new IllegalStateException("GROQ API key con formato inválido. Debe comenzar con gsk_.");
        }
        
        log.info("Groq Client inicializado. baseUrl={}, model={}", baseUrl, model);

        var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(60_000);

        this.rest = new RestTemplate(factory);
        this.rest.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().isError()) {
                    String body = new String(response.getBody().readAllBytes());
                    throw new HttpClientErrorException(
                            response.getStatusCode(),
                            "Groq error: " + body
                    );
                }
            }
        });
    }

    /**
     * Envía un mensaje al LLM con tools (function calling) disponibles.
     * 
     * @param messages Historial de mensajes de la conversación
     * @param tools Lista de tools disponibles para el LLM
     * @return Respuesta del LLM (puede incluir tool calls)
     */
    public Map<String, Object> chatWithTools(List<Map<String, Object>> messages, List<Map<String, Object>> tools) {
        String url = baseUrl + "/v1/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("temperature", 0.1);
        body.put("messages", messages);
        
        if (tools != null && !tools.isEmpty()) {
            body.put("tools", tools);
            body.put("tool_choice", "auto");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        log.debug("Enviando request a Groq: {}", body);
        var resp = rest.exchange(url, HttpMethod.POST, req, Map.class);
        
        log.debug("Respuesta de Groq: {}", resp.getBody());
        return resp.getBody();
    }

    public Map<String, Object> extractAssistantMessage(Map<String, Object> response) {
        List<?> choices = (List<?>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("No se recibieron choices en la respuesta de Groq");
        }
        
        Map<?, ?> choice0 = (Map<?, ?>) choices.get(0);
        return (Map<String, Object>) choice0.get("message");
    }

    public boolean hasToolCalls(Map<String, Object> assistantMessage) {
        return assistantMessage.get("tool_calls") != null;
    }

    public List<Map<String, Object>> extractToolCalls(Map<String, Object> assistantMessage) {
        return (List<Map<String, Object>>) assistantMessage.get("tool_calls");
    }

    public String preguntar(String prompt) {
        String url = baseUrl + "/v1/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("temperature", 0.3);
        body.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        var resp = rest.exchange(url, HttpMethod.POST, req, Map.class);
        
        List<?> choices = (List<?>) ((Map<?, ?>) resp.getBody()).get("choices");
        Map<?, ?> choice0 = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) choice0.get("message");
        return (String) message.get("content");
    }
}

