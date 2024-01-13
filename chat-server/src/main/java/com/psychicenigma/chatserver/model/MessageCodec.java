package com.psychicenigma.chatserver.model;

import com.google.gson.Gson;
import com.psychicenigma.aiengine.model.Message;
import com.psychicenigma.aiengine.model.MessageInterface;

public class MessageCodec {

    private static final Gson gson = new Gson();

    private String content;

    private String sender;

    public MessageCodec(MessageInterface msg) {
        content = msg.getContent();
        sender = msg.getSender();
    }

    public String toJSON() {
        return gson.toJson(this);
    }

    public static Message fromJSON(String json) {
        return gson.fromJson(json, Message.class);
    }
}
