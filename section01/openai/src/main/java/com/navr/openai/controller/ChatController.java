package com.navr.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChatController {
    private final ChatClient chatClient; // the default ChatClient instance for general queries
    private final ChatClient hrChatClient; // a separate ChatClient instance for HR-related queries

    /**
     * Inject ChatClient.Builder instance directly into the controller.
     * <li>You can skip bean config if you inject ChatClient.Builder directly (like in this case) into the controller
     * and build the client there.</li>
     * <li>Spring AI auto-configuration provides a ChatClient.Builder when it configures a chat model.
     * The builder is the factory: it knows which model to use and lets you set defaults or other options.
     * So, no separate configuration is needed for the ChatClient Builder.</li>
     * <li>Note that Spring AI auto-configures a ChatClient.Builder, not a ChatClient bean, so the controller
     * currently builds its clients itself. </li>
     * <p>
     * <br/><b>DefaultSystem method: </b>
     * <li>The defaultSystem method allows you to set a default system role message that will be used for all
     * requests made with this client. This avoids the need to set the system message for each request. </li>
     * <li>If you don't set a default system message, the chat client needs to be configured with a system message
     * for each request, which can be repetitive and error-prone.</li>
     *
     * @param chatClientBuilder chatClient builder instance provided by Spring AI auto-configuration
     */
    public ChatController(ChatClient.Builder chatClientBuilder) { // constructor injection of ChatClient.Builder
        this.chatClient = chatClientBuilder.build();
        this.hrChatClient = chatClientBuilder.defaultSystem(
                """
                            You are a helpful assistant to help employees with HR policy related questions.
                            Do not answer questions that are not related to HR policy.
                            If the question is not related to HR policy, politely decline to answer and suggest the user 
                            to contact the appropriate department for assistance.
                        """
        ).build();
    }

    @GetMapping("/chatHR")
    public String chatHR(@RequestParam("message") String messageHR) {
        return hrChatClient.prompt(messageHR).call().content();
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
