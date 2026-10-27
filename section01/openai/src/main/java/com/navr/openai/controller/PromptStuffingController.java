package com.navr.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Prompt stuffing (also called contextual prompting) in Spring AI is the practice of injecting external data or custom
 * context directly into the prompt sent to a Large Language Model (LLM).
 * Because LLMs have knowledge cut-off dates and no inherent access to your private database or files,
 * "stuffing the prompt" bridges the gap. It acts as a manual, foundational approach to Retrieval-Augmented Generation (RAG)
 * by providing all necessary context within the model's token context window so it can answer accurately.
 */
@RestController
@RequestMapping("/api/v1")
public class PromptStuffingController {

    private final ChatClient chatClient; // the default ChatClient instance for general queries

    public PromptStuffingController(ChatClient.Builder clientBuilder) {
        this.chatClient = clientBuilder.build();
    }

    @Value("classpath:/promptTemplates/systemPromptTemplateStuffing.st")
    Resource systemPromptTemplate; // Load the prompt template from a resource file

    @GetMapping("/promptStuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        return this.chatClient.prompt()
                .system(systemPromptTemplate)
                .user(message) // pass the user message as is
                .call()
                .content();
    }
}
