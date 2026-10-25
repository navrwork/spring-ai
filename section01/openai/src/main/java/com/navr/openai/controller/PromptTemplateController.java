package com.navr.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PromptTemplateController {

    @Autowired
    private ChatClient custSupportChatClient;

    private static final String PROMPT_TEMPLATE = """
            A customer named {customerName} has sent the following message: "{customerMessage}".
            Please draft a professional and empathetic email response to address the customer's concerns.
            The response should be concise, polite, and provide a clear solution or next steps.
            Please ensure that the response is tailored to the customer's specific message and maintains a positive tone.
            Respond with email body. No need for salutation or signature. Do not include any additional information or disclaimers.
            """;

    /**
     * Endpoint to generate an email response based on template content and provided customer name and message values.
     *
     * <li>
     * The method makes use of prompt template with placeholders for dynamic content. The placeholders
     * are replaced with the actual values provided in the request parameters.
     * </li>
     *
     * @param customerName    The name of the customer.
     * @param customerMessage The message sent by the customer.
     * @return A drafted email response addressing the customer's concerns.
     */
    @RequestMapping("/emailResponseV1")
    public String generateEmailResponse(@RequestParam("customerName") String customerName,
                                        @RequestParam("customerMessage") String customerMessage) {
        return custSupportChatClient
                .prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(PROMPT_TEMPLATE)
                                .param("customerName", customerName)
                                .param("customerMessage", customerMessage)
                )
                .call()
                .content();
    }

    @Value("classpath:/templates/prompt-template-email.st")
    Resource userPromptTemplate;

    @GetMapping("/emailResponseV2")
    public String generateEmailResponseV2(@RequestParam("customerName") String customerName,
                                          @RequestParam("customerMessage") String customerMessage) {
        return custSupportChatClient
                .prompt()
                .user(promptUserSpec ->
                        promptUserSpec.text(userPromptTemplate)
                                .param("customerName", customerName)
                                .param("customerMessage", customerMessage)
                )
                .call()
                .content();
    }
}
