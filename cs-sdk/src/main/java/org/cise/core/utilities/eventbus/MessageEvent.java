package org.cise.core.utilities.eventbus;


public class MessageEvent {

    String event;

    public MessageEvent() {
    }

    public MessageEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
