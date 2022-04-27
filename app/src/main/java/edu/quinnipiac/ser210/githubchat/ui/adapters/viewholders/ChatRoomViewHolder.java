package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.ui.adapters.ChatRoomAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnChatRoomSelected;

public class ChatRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final OnChatRoomSelected listener;
    private final TextView textView;
    private ChatRoom chatRoom;

    public ChatRoomViewHolder(OnChatRoomSelected listener, @NonNull View itemView) {
        super(itemView);
        this.listener = listener;

        itemView.setOnClickListener(this);

        textView = itemView.findViewById(R.id.list_chat_room_text);
    }

    public void bindToChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        if(chatRoom != null) {
            textView.setText(chatRoom.getRepoName());
        }
    }

    @Override
    public void onClick(View view) {
        listener.onChatRoomSelected(chatRoom);
    }
}
