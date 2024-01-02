package com.psychicenigma;

import com.psychicenigma.model.*;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.HashMap;

public class OpenAIChatBot {

    public ConversationInterface<ChatMessageWrapper> getConversation() {
        return conversation;
    }

    ConversationInterface<ChatMessageWrapper> conversation;

    private OpenAiService service;

    public OpenAIChatBot() {
        String OPENAI_TOKEN = System.getenv("OPENAI_TOKEN");
        service = new OpenAiService(OPENAI_TOKEN);
        resetMemory();
    }

    public void resetMemory() {
        conversation = new Conversation<ChatMessageWrapper>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant who helps a user write a document.", "System");
        conversation.addMessage(
                new ChatMessageWrapper(systemMessage));
    }

    public MessageInterface send(MessageInterface incoming) {

        conversation.addMessage(
                new ChatMessageWrapper(Message.toChatMessage(incoming, ChatMessageRole.USER.value())));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(conversation.getMessages().stream().map(ChatMessageWrapper::getChatMessage).toList())
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .user(incoming.getSender())
                .build();

        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().getFirst().getMessage();
        responseMessage.setName("JJ");
        System.out.println(responseMessage.getContent());
        System.out.println(responseMessage.getName());
        System.out.println(responseMessage.getRole());
        ChatMessageWrapper ret = new ChatMessageWrapper(responseMessage);

        conversation.addMessage(ret);

        return ret;
    }
}
