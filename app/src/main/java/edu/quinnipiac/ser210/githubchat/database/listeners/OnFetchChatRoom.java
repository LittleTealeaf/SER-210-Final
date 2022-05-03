package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;

@Deprecated
public interface OnFetchChatRoom extends DatabaseWrapper.OnFetchChatRoom {

    void onFetchChatRoom(ChatRoom chatRoom, int channel);
}
