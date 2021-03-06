package edu.quinnipiac.ser210.githubchat.firebase.dataobjects;

import java.util.UUID;

/**
 * @author Thomas Kwashnak
 */
public class Message {

    private String message;
    private String sender;
    private long sendTime;
    private String uuid;

    public Message() {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + (getSender() != null ? getSender().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        return ((Message) o).getUuid().equals(uuid);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
