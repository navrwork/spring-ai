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
4. Sample env variable configuration for IntelliJ:

   - as 'OPENAI_API_KEY=sk-...' in the **Environment variables** field.

Note: The Spring Boot application needs a valid API key for the configured OpenAI provider (or valid credentials for another configured model provider) to start successfully.

## Ollama setup

Ollama runs models locally and does not require an OpenAI API key. To use it with this application:

1. Install [Ollama](https://ollama.com/download) and make sure it is running.
2. Download a model, for example:

   ```shell
   ollama pull llama3.2
   ```

3. In `build.gradle`, replace the OpenAI model starter with the Ollama model starter:

   ```groovy
   implementation 'org.springframework.ai:spring-ai-starter-model-ollama'
   ```

4. Add the properties below to `src/main/resources/application.properties`.

Remove the OpenAI API key configuration when switching providers. Restart the application and use the same `/api/chat` endpoint; Spring AI will connect to Ollama at `http://localhost:11434`.

## General Spring AI properties

These retry properties are shared across Spring AI model integrations:

| Property                                   | Sample value | Description                                                                    |
|--------------------------------------------|--------------|--------------------------------------------------------------------------------|
| `spring.ai.model.chat`                     | `ollama`     | Selects the chat model provider.                                               |
| `spring.ai.retry.max-attempts`             | `3`          | Maximum number of attempts for a model request, including the initial attempt. |
| `spring.ai.retry.backoff.initial-interval` | `2s`         | Delay before the first retry.                                                  |
| `spring.ai.retry.backoff.multiplier`       | `2`          | Factor by which the delay increases after each retry.                          |
| `spring.ai.retry.backoff.max-interval`     | `10s`        | Maximum delay between retries.                                                 |

## Model-specific Spring AI properties

### Ollama

| Property                              | Sample value             | Description                               |
|---------------------------------------|--------------------------|-------------------------------------------|
| `spring.ai.ollama.base-url`           | `http://localhost:11434` | URL of the Ollama server.                 |
| `spring.ai.ollama.chat.options.model` | `llama3.2`               | Name of the Ollama model to use for chat. |

### OpenAI

OpenAI-specific properties used by this application:

| Property                                    | Sample value        | Description                                                                |
|---------------------------------------------|---------------------|----------------------------------------------------------------------------|
| `spring.ai.open.ai.apiKey`                  | `${OPENAI_API_KEY}` | OpenAI API key, read from the `OPENAI_API_KEY` environment variable.       |
| `spring.ai.openai.chat.options.model`       | `gpt-4o-mini`       | OpenAI model to use for chat.                                              |
| `spring.ai.openai.chat.options.temperature` | `0.2`               | Controls response randomness; lower values produce more consistent output. |
| `spring.ai.openai.chat.options.max-tokens`  | `300`               | Maximum number of tokens to generate in the response.                      |

### AWS Bedrock

AWS Bedrock-specific properties configured in this project:

| Property                                        | Sample value          | Description                                                          |
|-------------------------------------------------|-----------------------|----------------------------------------------------------------------|
| `spring.ai.model.chat`                          | `bedrock-converse`    | Selects the Bedrock Converse provider for chat.                      |
| `spring.ai.bedrock.aws.region`                  | `us-east-1`           | AWS region containing the Bedrock service.                           |
| `spring.ai.bedrock.aws.access-key`              | `${AWS_ACCESS_KEY}`   | AWS access key, read from the `AWS_ACCESS_KEY` environment variable. |
| `spring.ai.bedrock.aws.secret-key`              | `${AWS_SECRET_KEY}`   | AWS secret key, read from the `AWS_SECRET_KEY` environment variable. |
| `spring.ai.bedrock.chat.options.model`          | `anthropic.claude-v2` | Bedrock chat model identifier.                                       |
| `spring.ai.bedrock.converse.chat.options.model` | `anthropic.claude-v2` | Bedrock Converse chat model identifier.                              |
