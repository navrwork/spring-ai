package com.navr.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptTemplateBeanConfig {

    @Bean
    public ChatClient getPromptTemplateChatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem(
                        """
                                You are a helpful customer service assistant to help employees with drafting
                                email responses to customer inquiries to improve customer satisfaction.
                                Do not answer questions that are not related to customer service email responses.
                                If the question is not related to customer service email responses, politely decline to answer
                                and suggest the user to contact the appropriate department for assistance.
                                """
                )
                .build();
    }
}
