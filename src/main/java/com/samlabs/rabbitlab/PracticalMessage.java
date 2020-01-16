package com.samlabs.rabbitlab;

public class PracticalMessage {

    private final String text;
    private final int priority;
    private final boolean secret;

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
