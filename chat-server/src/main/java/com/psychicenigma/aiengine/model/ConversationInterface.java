package com.psychicenigma.aiengine.model;

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

    public String toMessageStringList() {
        List<T> msgs =  getMessages();
        String[] msgJSONs = new String[msgs.size()];
        int i = 0;
        for (MessageInterface msg : msgs) {
            StringBuilder msgJSON = new StringBuilder("[");
            msgJSON.append('{');
            msgJSON.append(String.join(
                    ",",
                    String.format("'content': '%s'", msg.getContent()),
                    String.format("'name': '%s'", msg.getSender()),
                    String.format("'role': '%s'", msg.getRole())));
            msgJSON.append('}');
            msgJSONs[i] = msgJSON.toString();
        }
        return '['+String.join(",", msgJSONs)+']';
    }

    public abstract void clear();

    public abstract void extend(ConversationInterface<T> conversation);

}