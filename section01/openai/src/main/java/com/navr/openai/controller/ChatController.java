package com.navr.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatClient chatClient;

    /**
     * Inject ChatClient.Builder instance directly into the controller. <br/>
     * Note: <br/>
     * <li>You can skip that config if you inject ChatClient.Builder directly into the controller and build the client there.</li>
     * <li>Spring AI auto-configuration provides a ChatClient.Builder when it configures a chat model. The builder is the factory: it knows which model to use and lets you set defaults or other options. So, no separate configuration is needed for the ChatClient Builder.</li>
     *
     * @param chatClientBuilder
     */
    public ChatController(ChatClient.Builder chatClientBuilder) { // constructor injection
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt(message).call().content();
    }

    /**
     * A simple endpoint to test the controller.
     *
     * @param message The message to be echoed back.
     * @return A greeting message.
     */
    @GetMapping("/hello")
    public String sayHello(@RequestParam("message") String message) {
        return "Hello, " + message + "!";
    }
}
