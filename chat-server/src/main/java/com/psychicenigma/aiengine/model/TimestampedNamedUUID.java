package com.psychicenigma.aiengine.model;

public class TimestampedNamedUUID extends TimestampedUUID {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
