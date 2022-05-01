package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubUser;
import edu.quinnipiac.ser210.githubchat.ui.adapters.MessageAdapter;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;
import edu.quinnipiac.ser210.githubchat.ui.util.OnImageLoaded;

public class MessageViewHolder extends RecyclerView.ViewHolder implements OnFetchGithubUser, OnImageLoaded {

    private final MessageAdapter adapter;
    private final TextView userView;
    private final TextView messageView;
    private final ImageView avatarView;

    private int channelFetchUser, channelLoadImage;

    public MessageViewHolder(MessageAdapter messageAdapter, @NonNull View itemView) {
        super(itemView);
        adapter = messageAdapter;
        userView = itemView.findViewById(R.id.list_message_text_user);
        messageView = itemView.findViewById(R.id.list_message_text_message);
        avatarView = itemView.findViewById(R.id.list_message_imageview_avatar);
    }

    public void onBindMessage(Message message) {
        userView.setText(message.getSender());
        messageView.setText(message.getMessage());
        avatarView.setVisibility(View.INVISIBLE);
        channelFetchUser = GithubWrapper.from(adapter.getContext()).startFetchGithubUser(message.getSender(),this);
    }

    @Override
    public void onFetchGithubUser(GithubUser githubUser, int channel) {
        if(channel == channelFetchUser) {
            channelLoadImage = ImageLoader.loadImage(githubUser.getAvatarUrl(),this);
            if(githubUser.getName() != null) {
                userView.setText(githubUser.getName());
            }
        }
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, int channel) {
        if(channel == channelLoadImage) {
            avatarView.setImageBitmap(bitmap);
            avatarView.setVisibility(View.VISIBLE);
        }
    }
}
