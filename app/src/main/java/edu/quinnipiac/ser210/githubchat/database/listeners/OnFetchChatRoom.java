package edu.quinnipiac.ser210.githubchat.database.listeners;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;

/**
 * @author Thomas Kwashnak
 *
 */
public interface OnFetchChatRoom {
    void onFetchChatRoom(ChatRoom chatRoom, int channel);
}
