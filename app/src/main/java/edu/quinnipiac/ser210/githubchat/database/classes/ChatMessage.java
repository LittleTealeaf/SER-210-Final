package edu.quinnipiac.ser210.githubchat.database.classes;

public class ChatMessage {
    private String name;
    private String message;

    public ChatMessage() {

    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }
}
