package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubAttachable;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubUser;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.adapters.AttachableAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.MessageAdapter;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;
import edu.quinnipiac.ser210.githubchat.ui.util.OnImageLoaded;

public class MessageViewHolder extends RecyclerView.ViewHolder
        implements OnFetchGithubUser, OnImageLoaded, OnFetchGithubAttachable, View.OnClickListener {


    private final MessageAdapter adapter;
    private final TextView userView;
    private final TextView messageView;
    private final ImageView avatarView;
    private final AttachableAdapter attachableAdapter;
    private final RecyclerView recyclerView;

    private int channelFetchUser, channelLoadImage, channelFetchAttachable;

    private GithubUser githubUser;

    public MessageViewHolder(MessageAdapter messageAdapter, @NonNull View itemView) {
        super(itemView);
        adapter = messageAdapter;
        userView = itemView.findViewById(R.id.list_message_text_user);
        messageView = itemView.findViewById(R.id.list_message_text_message);
        avatarView = itemView.findViewById(R.id.list_message_imageview_avatar);
        attachableAdapter = new AttachableAdapter(messageAdapter.getContext());

        avatarView.setOnClickListener(this);

        recyclerView =((RecyclerView) itemView.findViewById(R.id.list_message_recyclerview_attachable));
        recyclerView.setLayoutManager(new LinearLayoutManager(adapter.getContext()));
        recyclerView.setAdapter(attachableAdapter);
    }

    public void onBindMessage(Message message) {
        this.githubUser = null;
        userView.setText(message.getSender());
        messageView.setText(message.getMessage());
        avatarView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.GONE);
        channelFetchAttachable = ThreadManager.registerChannel();
        attachableAdapter.clearItems();
        channelFetchUser = GithubWrapper.from(adapter.getContext()).startFetchGithubUser(message.getSender(),this);

        Matcher matcher = Pattern.compile("(#[0-9]*)\\w+").matcher(message.getMessage());
        GithubWrapper githubWrapper = GithubWrapper.from(adapter.getContext());
        while(matcher.find()) {
            int number = Integer.parseInt(matcher.group().substring(1));
            githubWrapper.startFetchGithubAttachable(adapter.getRepoName(),number,this,channelFetchAttachable);
        }
    }



    @Override
    public void onFetchGithubUser(GithubUser githubUser, int channel) {
        if(channel == channelFetchUser) {
            this.githubUser = githubUser;
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

    @Override
    public void onFetchGithubAttachable(GithubAttachable attachable, int channel) {
        if(channel == channelFetchAttachable && attachable != null) {
            attachableAdapter.addAttachable(attachable);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.list_message_imageview_avatar && githubUser != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUser.getUrl()));
            adapter.getContext().startActivity(intent);
        }
    }
}
