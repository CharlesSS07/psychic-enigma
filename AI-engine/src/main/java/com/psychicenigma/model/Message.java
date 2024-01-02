package com.psychicenigma.model;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Message extends MessageInterface {

    private String content;

    private String sender;

    public static Message fromChatMessage(ChatMessage chatMessage) {
        Message message = new Message();
        message.content = chatMessage.getContent();
        message.sender = chatMessage.getName();
        return message;
    }

    public static ChatMessage toChatMessage(MessageInterface message, String role) {
        return new ChatMessage(role, message.getContent(), message.getSender());
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getSender() {
        return sender;
    }

}
