package com.psychicenigma.model;

import java.util.Objects;

public abstract class MessageInterface extends TimestampedUUID {

    public abstract String getContent();

    public abstract String getSender();

    @Override
    public String toString() {
        return getSender() + ": " + getContent();
    }
}
