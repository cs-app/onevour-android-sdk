package org.cise.sdk.ciseapp.modules.chat;

import com.google.gson.annotations.Expose;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
public class ChatMessage {

    @Expose
    private MessageType type;

    @Expose
    private String content;

    @Expose
    private String sender;

    @Expose
    private String time;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
