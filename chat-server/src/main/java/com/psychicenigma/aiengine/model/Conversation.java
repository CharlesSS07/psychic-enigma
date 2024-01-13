package com.psychicenigma.aiengine.model;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Conversation<T extends MessageInterface> extends ConversationInterface<T> {
    private List<T> messages;

    public Conversation() {
        this.messages = new ArrayList<T>();
    }

    // Add a message to the conversation
    @Override
    public void addMessage(T message) {
        this.messages.add(message);
    }

    // Get all messages in the conversation
    @Override
    public List<T> getMessages() {
        return this.messages;
    }

    @Override
    public void clear() {
        this.messages.clear();
    }

    @Override
    public void extend(ConversationInterface<T> conversation) {
        this.messages.addAll(conversation.getMessages());
    }
}