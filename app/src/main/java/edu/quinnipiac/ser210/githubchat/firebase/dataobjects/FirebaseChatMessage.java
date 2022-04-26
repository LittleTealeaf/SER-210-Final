package edu.quinnipiac.ser210.githubchat.firebase.dataobjects;

/**
 * @author Thomas Kwashnak
 * Represents a Chat Message retrieved from the Firebase Realtime Database
 */
public class FirebaseChatMessage {

    private String message;
    private long time;
    private String senderUID;


    public FirebaseChatMessage() {}

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
