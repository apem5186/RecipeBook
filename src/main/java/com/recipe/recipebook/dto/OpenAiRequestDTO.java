package com.recipe.recipebook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OpenAiRequestDTO {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;

    @Getter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}
