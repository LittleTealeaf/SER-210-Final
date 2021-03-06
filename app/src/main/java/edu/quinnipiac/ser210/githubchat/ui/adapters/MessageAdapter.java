package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.MessageViewHolder;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> implements ChildEventListener {

    private final Context context;
    private final String repoName;
    private final LayoutInflater inflater;
    private final RecyclerView recyclerView;
    private final List<Message> messages;
    private final List<MessageViewHolder> holders;

    public MessageAdapter(String repoName, Context context, RecyclerView recyclerView) {
        this.repoName = repoName;
        this.context = context;
        messages = new ArrayList<>();
        holders = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageViewHolder holder = new MessageViewHolder(this, inflater.inflate(R.layout.list_message_item, parent, false));
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.onBindMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MessageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public void clearEntries() {
        int count = messages.size();
        messages.clear();
        notifyItemRangeRemoved(0, count);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Message message = snapshot.getValue(Message.class);
        assert message != null;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSendTime() > message.getSendTime()) {
                messages.add(i, message);
                notifyItemInserted(i);
                recyclerView.scrollToPosition(messages.size() - 1);
                return;
            }
        }
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
        if (!recyclerView.canScrollVertically(1)) {
            recyclerView.scrollToPosition(messages.size() - 1);
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Message message = snapshot.getValue(Message.class);
        if (message != null) {
            int index = messages.indexOf(message);
            messages.set(index, message);
            notifyItemChanged(index);
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        int index = messages.indexOf(snapshot.getValue(Message.class));
        messages.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        //Do nothing here
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(getContext(), "Connection Error: " + error, Toast.LENGTH_SHORT).show();
    }

    public Context getContext() {
        return context;
    }

    private void setData(DataSnapshot snapshot) {
        messages.clear();
        for (DataSnapshot child : snapshot.getChildren()) {
            messages.add(child.getValue(Message.class));
        }
    }

    public void updateTimes() {
        for (MessageViewHolder holder : holders) {
            holder.updateTime();
        }
    }

    public String getRepoName() {
        return repoName;
    }
}
