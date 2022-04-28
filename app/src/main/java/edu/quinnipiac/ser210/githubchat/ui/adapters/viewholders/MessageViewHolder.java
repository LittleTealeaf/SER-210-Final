package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.ui.adapters.MessageAdapter;

public class MessageViewHolder extends RecyclerView.ViewHolder {


    private final TextView textView;

    public MessageViewHolder(MessageAdapter messageAdapter, @NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.list_message_chat_message);
    }

    public void onBindMessage(Message message) {
        textView.setText(message.getSender() + " " + message.getMessage());
    }
}
