package edu.quinnipiac.ser210.githubchat.database.listeners;

public interface OnRemoveChatRoom {

    void onRemoveChatRoom(String repoName, int channel);
}
