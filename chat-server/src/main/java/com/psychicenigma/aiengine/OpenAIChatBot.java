package com.psychicenigma.aiengine;

import com.psychicenigma.aiengine.model.*;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.HashMap;

public abstract class OpenAIChatBot {

    public String name;

    public OpenAIChatBot(String name) {
        this.name = name;
    }

    public OpenAIChatMessageWrapper respond() {

        ConversationInterface<OpenAIChatMessageWrapper> conv = generateConversation();

        System.out.println(conv.transcriptString());

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(conv.getMessages().stream().map(OpenAIChatMessageWrapper::getChatMessage).toList())
                .n(1)
                .maxTokens(1000)
                .logitBias(new HashMap<>())
                .build();

        ChatMessage responseMessage = Settings.OPEN_AI_SERVICE.createChatCompletion(chatCompletionRequest)
                .getChoices().getFirst().getMessage();
        responseMessage.setName(name);

        return new OpenAIChatMessageWrapper(responseMessage);
    }

    public abstract ConversationInterface<OpenAIChatMessageWrapper> generateConversation();
}
