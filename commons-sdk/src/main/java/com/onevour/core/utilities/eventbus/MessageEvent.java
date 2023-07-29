package com.onevour.core.utilities.eventbus;


public class MessageEvent {

    protected String event;

    protected String value;

    public MessageEvent() {
    }

    public MessageEvent(String event) {
        this.event = event;
    }

    public MessageEvent(String event, String value) {
        this.event = event;
        this.value = value;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
