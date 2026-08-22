/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class OpenAiCompatibleGateway {
    private final String provider;
    private final String model;
    private final String apiKey;
    private final RestClient client;

    public OpenAiCompatibleGateway(
        @Value("${zhuatech.ai.provider:local}") String provider,
        @Value("${zhuatech.ai.base-url:https://api.deepseek.com}") String baseUrl,
        @Value("${zhuatech.ai.model:deepseek-chat}") String model,
        @Value("${zhuatech.ai.api-key:}") String apiKey
    ) {
        this.provider = provider;
        this.model = model;
        this.apiKey = apiKey;
        this.client = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Metadata metadata() {
        return new Metadata(provider, model, configured());
    }

    public Optional<String> complete(String systemPrompt, String businessContext) {
        if (!configured()) return Optional.empty();
        try {
            Map<?, ?> response = client.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                    "model", model,
                    "temperature", 0.2,
                    "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", businessContext)
                    )
                ))
                .retrieve()
                .body(Map.class);
            return extractContent(response);
        } catch (RuntimeException ignored) {
            return Optional.empty();
        }
    }

    private boolean configured() {
        return !apiKey.isBlank() && !"local".equalsIgnoreCase(provider);
    }

    private Optional<String> extractContent(Map<?, ?> response) {
        if (response == null || !(response.get("choices") instanceof List<?> choices) || choices.isEmpty()) {
            return Optional.empty();
        }
        if (!(choices.getFirst() instanceof Map<?, ?> choice)
            || !(choice.get("message") instanceof Map<?, ?> message)
            || !(message.get("content") instanceof String content)
            || content.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(content.trim());
    }

    public record Metadata(String provider, String model, boolean configured) {}
}

