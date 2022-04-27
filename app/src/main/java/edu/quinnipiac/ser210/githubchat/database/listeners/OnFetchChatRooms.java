package edu.quinnipiac.ser210.githubchat.database.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;

public interface OnFetchChatRooms {
    void onFetchChatRooms(List<ChatRoom> chatRooms, int channel);
}
