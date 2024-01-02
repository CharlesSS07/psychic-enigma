package com.psychicenigma.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ConversationInterface<T extends MessageInterface> extends TimestampedUUID {

    // Add a message to the conversation
    public abstract void addMessage(T message);

    // Get all messages in the conversation
    public abstract List<T> getMessages();

    public String transcriptString() {
        String transcript = "Conversation:\n";
        for (T t : getMessages()) {
            transcript += t.getSender() + ": " + t.getContent() + "\n";
        }
        return transcript;
    }

}