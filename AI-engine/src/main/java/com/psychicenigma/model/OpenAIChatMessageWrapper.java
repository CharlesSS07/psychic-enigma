package com.psychicenigma.model;

import com.theokanning.openai.completion.chat.ChatMessage;

public class OpenAIChatMessageWrapper extends MessageInterface {

    private ChatMessage chatMessage;

    public OpenAIChatMessageWrapper(ChatMessage chatMessage) {
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

    @Override
    public String getRole() {
        return this.chatMessage.getRole();
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }
}
