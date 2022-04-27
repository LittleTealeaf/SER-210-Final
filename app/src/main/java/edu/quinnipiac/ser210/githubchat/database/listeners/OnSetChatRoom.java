package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;

public interface OnSetChatRoom {
    void onSetChatRoom(ChatRoom chatRoom, int channel);
}
