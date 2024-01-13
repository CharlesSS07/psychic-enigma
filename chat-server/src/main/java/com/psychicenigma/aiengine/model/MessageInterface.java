package com.psychicenigma.aiengine.model;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.Objects;

public abstract class MessageInterface extends TimestampedUUID {

    public abstract String getContent();

    public abstract String getSender();

    public abstract String getRole();

    @Override
    public String toString() {
        return getSender() + ": " + getContent();
    }

    public static ChatMessage toChatMessage(MessageInterface message, String role) {
        return new ChatMessage(role, message.getContent(), message.getSender());
    }
}
