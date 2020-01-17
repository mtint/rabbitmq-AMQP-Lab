package com.samlabs.rabbitlab;

import java.io.Serializable;

public class PracticalMessage {

    private String text;
    private int priority;
    private boolean secret;

    public PracticalMessage() {
    }

    public PracticalMessage(String text, int priority, boolean secret) {
        this.text = text;
        this.priority = priority;
        this.secret = secret;
    }

    public String getText() {
        return text;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "PracticalMessage{" +
                "text='" + text + '\'' +
                ", priority=" + priority +
                ", secret=" + secret +
                '}';
    }
}
