package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;

@Deprecated
public interface OnUpdateChatRoom {

    void onUpdateChatRoom(ChatRoom chatRoom, int channel);
}
