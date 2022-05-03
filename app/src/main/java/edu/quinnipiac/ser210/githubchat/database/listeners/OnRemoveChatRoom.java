package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;

@Deprecated
public interface OnRemoveChatRoom extends DatabaseWrapper.OnRemoveChatRoom {

    void onRemoveChatRoom(String repoName, int channel);
}
