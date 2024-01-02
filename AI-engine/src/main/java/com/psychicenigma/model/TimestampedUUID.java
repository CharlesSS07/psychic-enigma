package com.psychicenigma.model;

import java.util.Date;
import java.util.UUID;

public abstract class TimestampedUUID {

    UUID uuid;
    Date timestamp;

    public TimestampedUUID() {
        uuid = UUID.randomUUID();
        timestamp = new Date();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
