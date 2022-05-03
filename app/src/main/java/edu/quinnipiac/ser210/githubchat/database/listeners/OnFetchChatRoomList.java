package edu.quinnipiac.ser210.githubchat.database.listeners;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;

@Deprecated
public interface OnFetchChatRoomList extends DatabaseWrapper.OnFetchChatRoomList {

    void onFetchChatRoomList(List<ChatRoom> chatRooms, int channel);
}
