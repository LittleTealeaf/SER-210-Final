package edu.quinnipiac.ser210.githubchat.firebase.dataobjects;

public class Message {

    private String message;
    private String sender;

    public Message() {}

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

        Message message1 = (Message) o;

        if (getMessage() != null ? !getMessage().equals(message1.getMessage()) : message1.getMessage() != null) return false;
        return getSender() != null ? getSender().equals(message1.getSender()) : message1.getSender() == null;
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
}
