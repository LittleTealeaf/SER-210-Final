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
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRoomList;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnUpdateChatRoom;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnChatRoomSelected;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.ChatRoomViewHolder;

/**
 * @author Thomas Kwashnak
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomViewHolder>
        implements DatabaseWrapper.OnFetchChatRoomList, DatabaseWrapper.OnUpdateChatRoom {

    private final Context context;
    private final OnChatRoomSelected listener;
    private final LayoutInflater inflater;
    private final List<ChatRoom> favorites = new LinkedList<>();
    private final List<ChatRoom> rooms = new LinkedList<>();
    @Deprecated
    private final List<ChatRoom> chatRooms = new LinkedList<>();
    private int fetchChatRoomChannel;

    public ChatRoomAdapter(Context context, OnChatRoomSelected listener) {
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatRoomViewHolder(context, this, inflater.inflate(R.layout.list_chat_room_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        if (position < favorites.size()) {
            holder.bindToChatRoom(favorites.get(position));
        } else {
            holder.bindToChatRoom(rooms.get(position - favorites.size()));
        }
    }

    @Override
    public int getItemCount() {
        return favorites.size() + rooms.size();
    }

    public void setFetchChatRoomChannel(int channel) {
        this.fetchChatRoomChannel = channel;
    }

    @Override
    public void onFetchChatRoomList(List<ChatRoom> chatRooms, int channel) {
        if (channel == fetchChatRoomChannel) {
            for (ChatRoom chatRoom : chatRooms) {
                if (chatRoom.isFavorite()) {
                    addFavorite(chatRoom);
                } else {
                    addRoom(chatRoom);
                }
            }
        }
    }

    private void addFavorite(ChatRoom chatRoom) {
        if (!favorites.contains(chatRoom)) {
            favorites.add(0, chatRoom);
            notifyItemInserted(0);
        }
    }

    private void addRoom(ChatRoom chatRoom) {
        if (!rooms.contains(chatRoom)) {
            rooms.add(0, chatRoom);
            notifyItemInserted(favorites.size());
        }
    }

    @Override
    public void onUpdateChatRoom(ChatRoom chatRoom, int channel) {
        if (chatRoom.isFavorite()) {
            removeRoom(chatRoom);
            addFavorite(chatRoom);
        } else {
            removeFavorite(chatRoom);
            addRoom(chatRoom);
        }
    }

    private void removeRoom(ChatRoom chatRoom) {
        int index = rooms.indexOf(chatRoom);
        if (index != -1) {
            rooms.remove(index);
            notifyItemRemoved(favorites.size() + index);
        }
    }

    private void removeFavorite(ChatRoom chatRoom) {
        int index = favorites.indexOf(chatRoom);
        if (index != -1) {
            favorites.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void selectChatRoom(ChatRoom chatRoom) {
        listener.onChatRoomSelected(chatRoom);
    }

    public interface OnChatRoomSelected {
        void onChatRoomSelected(ChatRoom chatRoom);
    }
}
