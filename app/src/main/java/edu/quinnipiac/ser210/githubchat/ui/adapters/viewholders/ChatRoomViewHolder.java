package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnUpdateChatRoom;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnChatRoomSelected;

/**
 * @author Thomas Kwashnak
 */
public class ChatRoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final Context context;
    private final IListener listener;
    private final TextView textView;
    private final Button favoriteButton;
    private ChatRoom chatRoom;

    public ChatRoomViewHolder(Context context, IListener listener, @NonNull View itemView) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        itemView.setOnClickListener(this);

        textView = itemView.findViewById(R.id.list_chat_room_text);
        favoriteButton = itemView.findViewById(R.id.list_chat_room_button_favorite);
        favoriteButton.setOnClickListener(this);
    }

    public void bindToChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        if (chatRoom != null) {
            textView.setText(chatRoom.getRepoName());
            if(chatRoom.isFavorite()) {
                favoriteButton.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_star_24,context.getTheme()));
            } else {
                favoriteButton.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_star_border_24,context.getTheme()));
            }
        } else {
            favoriteButton.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_star_border_24,context.getTheme()));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.list_chat_room_button_favorite) {
            chatRoom.setFavorite(!chatRoom.isFavorite());
            DatabaseWrapper.from(context).startUpdateChatRoom(chatRoom,listener);
        } else {
            listener.onChatRoomSelected(chatRoom);
        }
    }

    public interface IListener extends OnChatRoomSelected, OnUpdateChatRoom {}
}
