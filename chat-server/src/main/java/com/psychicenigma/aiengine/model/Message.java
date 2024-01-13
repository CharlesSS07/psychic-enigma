package com.psychicenigma.aiengine.model;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Message extends MessageInterface {

    private String content;

    private String sender;



    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getRole() {
        return null;
    }

}
