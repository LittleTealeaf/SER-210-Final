package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRooms;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnChatRoomSelected;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.ChatRoomViewHolder;

/**
 * @author Thomas Kwashnak
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomViewHolder> implements OnFetchChatRooms {


    private final OnChatRoomSelected listener;

    private final LayoutInflater inflater;

    private final List<ChatRoom> chatRooms = new LinkedList<>();

    public ChatRoomAdapter(Context context, OnChatRoomSelected listener) {
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomViewHolder(listener,inflater.inflate(R.layout.list_chat_room_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        holder.bindToChatRoom(chatRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    @Override
    public void onFetchChatRooms(List<ChatRoom> chatRooms, int channel) {
        int size = this.chatRooms.size();
        this.chatRooms.clear();
        notifyItemRangeRemoved(0,size);
        this.chatRooms.addAll(chatRooms);
        notifyItemRangeInserted(0,chatRooms.size());
    }
}
