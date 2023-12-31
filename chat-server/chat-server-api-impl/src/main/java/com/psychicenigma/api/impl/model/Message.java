package com.psychicenigma.api.impl.model;

import java.util.Objects;

public class Message {
    private String from;
    private String to;
    private String content;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass()!=Message.class) {
            return false;
        }
        Message other = (Message) obj;
        return (
                Objects.equals(other.content, this.content) &&
                Objects.equals(other.from, this.from) &&
                Objects.equals(other.to, this.to)
        );
    }
}
