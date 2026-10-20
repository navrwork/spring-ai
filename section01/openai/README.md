# Spring AI OpenAI Chat

This application demonstrates using Spring AI's `ChatClient` to interact with an OpenAI chat model.

## Sample endpoints

Start the application, then try these endpoints:

- [http://localhost:8080/api/hello?message=master](http://localhost:8080/api/hello?message=master) returns a simple greeting (`Hello, master!`). This endpoint confirms the application is running and echoes the supplied `message` parameter; it does not call an AI model.
- The chat endpoint sends the supplied question to the configured model and returns its response. You can run this HTTP request from an IDE HTTP client or save it in a `.http` file:

  ```http
  GET http://localhost:8080/api/chat?message=how%20is%20the%20weather%20in%20Chennai%20today
  ```

  The model may not have live weather data, so verify current conditions with a live weather source.

## Spring AI chat interfaces

- `ChatModel` is a low-level interface for interacting with a specific chat model.
- `ChatClient` provides a higher-level API for interacting with chat models without requiring application code to manage each model's implementation details.
- `ChatClient.Builder` is the factory for creating a `ChatClient`. Spring AI auto-configuration provides a builder associated with the configured chat model. Use it to customize client defaults, such as a default system prompt or options, and call `build()` to create the client. In this application, the controller injects the builder and builds a client in its constructor.
- `DefaultChatClient` is Spring AI's default implementation of `ChatClient`, created by calling `ChatClient.Builder.build()`.
- `CallResponseSpec` represents the response from the model after calling `call()`. It provides access to response metadata and other details. To retrieve just the generated text, call `content()`:

  ```java
  chatClient.prompt(message).call().content();
  ```

## OpenAI setup

1. Visit [OpenAI Platform](https://platform.openai.com/home) and create an API key. Save the secret key somewhere safe; it cannot be viewed again after creation.
2. Visit the [Billing overview](https://platform.openai.com/settings/organization/billing/overview) and add a small amount of credit to your account. An amount as low as $5 may be available. Turn off automatic recharge if you are only experimenting.
3. Do not hard-code the API key in a properties file. Committing a key to GitHub could expose it. Instead, configure it as an environment variable in your IDE's run configuration (called **Edit Configurations** in IntelliJ).

For this application, set the environment variable `OPENAI_API_KEY` to your key. The Spring Boot application needs a valid API key for the configured OpenAI provider (or valid credentials for another configured model provider) to start successfully.
