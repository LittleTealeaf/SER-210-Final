package edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces;

import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.ui.adapters.ChatRoomAdapter;

/**
 * @author Thomas Kwashnak
 */
@Deprecated
public interface OnChatRoomSelected extends ChatRoomAdapter.OnChatRoomSelected {

    void onChatRoomSelected(ChatRoom chatRoom);
}
