package com.psychicenigma.model;

import com.theokanning.openai.completion.chat.ChatMessage;

public class ChatMessageWrapper extends MessageInterface {

    private ChatMessage chatMessage;

    public ChatMessageWrapper(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public String getContent() {
        return this.chatMessage.getContent();
    }

    @Override
    public String getSender() {
        return this.chatMessage.getName();
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
