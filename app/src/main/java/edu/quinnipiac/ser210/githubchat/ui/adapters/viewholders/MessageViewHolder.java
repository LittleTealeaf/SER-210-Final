package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubUser;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.adapters.AttachmentAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.MessageAdapter;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;
import edu.quinnipiac.ser210.githubchat.ui.util.OnImageLoaded;

public class MessageViewHolder extends RecyclerView.ViewHolder implements GithubWrapper.OnFetchGithubUser, ImageLoader.OnLoadBitmap, View.OnClickListener {

    private final MessageAdapter adapter;
    private final TextView userView;
    private final TextView messageView;
    private final TextView timeView;
    private final ImageView avatarView;
    private final AttachmentAdapter attachmentAdapter;
    private final ListView attachmentListView;

    private int channelFetchUser, channelLoadImage;

    private Message message;
    private GithubUser githubUser;

    public MessageViewHolder(MessageAdapter messageAdapter, @NonNull View itemView) {
        super(itemView);
        adapter = messageAdapter;
        userView = itemView.findViewById(R.id.list_message_text_user);
        messageView = itemView.findViewById(R.id.list_message_text_message);
        avatarView = itemView.findViewById(R.id.list_message_imageview_avatar);
        timeView = itemView.findViewById(R.id.list_message_text_time);
        attachmentListView = itemView.findViewById(R.id.list_message_listview_attachments);
        attachmentAdapter = new AttachmentAdapter(messageAdapter.getContext());
        attachmentListView.setAdapter(attachmentAdapter);
        avatarView.setOnClickListener(this);
    }

    public void onBindMessage(Message message) {
        this.message = message;
        this.githubUser = null;
        userView.setText(message.getSender());
        messageView.setText(message.getMessage());
        avatarView.setVisibility(View.INVISIBLE);
        attachmentListView.setVisibility(View.GONE);

        channelFetchUser = GithubWrapper.from(adapter.getContext()).startFetchGithubUser(message.getSender(), this);

        int channel = ThreadManager.registerChannel();

        attachmentAdapter.resetListView(channel);

        updateTime();

        Matcher matcher = Pattern.compile("(#[0-9]*)\\w+").matcher(message.getMessage());
        GithubWrapper githubWrapper = GithubWrapper.from(adapter.getContext());
        while (matcher.find()) {
            attachmentListView.setVisibility(View.VISIBLE);
            int number = Integer.parseInt(matcher.group().substring(1));
            githubWrapper.startFetchGithubAttachable(adapter.getRepoName(), number, attachmentAdapter, channel);
        }
    }

    public void updateTime() {
         /*
        Source: https://stackoverflow.com/a/69348167/12206859
         */
        timeView.setText(DateUtils.getRelativeTimeSpanString(message.getSendTime()));
    }

    @Override
    public void onFetchGithubUser(GithubUser githubUser, int channel) {
        if (channel == channelFetchUser) {
            this.githubUser = githubUser;
            channelLoadImage = ImageLoader.loadImage(githubUser.getAvatarUrl(), this);
            if (githubUser.getName() != null) {
                userView.setText(githubUser.getName());
            }
        }
    }

    @Override
    public void onLoadBitmap(Bitmap bitmap, int channel) {
        if (channel == channelLoadImage) {
            avatarView.setImageBitmap(bitmap);
            avatarView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.list_message_imageview_avatar && githubUser != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUser.getUrl()));
            adapter.getContext().startActivity(intent);
        }
    }
}
