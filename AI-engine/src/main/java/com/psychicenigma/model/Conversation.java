package com.psychicenigma.model;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Conversation<T extends MessageInterface> extends ConversationInterface<T> {
    private List<T> messages;

    public Conversation() {
        this.messages = new ArrayList<>();
    }

    // Add a message to the conversation
    public void addMessage(T message) {
        this.messages.add(message);
    }

    // Get all messages in the conversation
    public List<T> getMessages() {
        return this.messages;
    }

}