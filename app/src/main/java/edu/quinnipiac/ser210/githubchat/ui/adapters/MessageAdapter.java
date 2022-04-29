package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.MessageViewHolder;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> implements ChildEventListener {


    private final LayoutInflater inflater;

    private final RecyclerView recyclerView;

    private final List<Message> messages;

    public MessageAdapter(Context context, RecyclerView recyclerView) {
        messages = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(this,inflater.inflate(R.layout.list_message_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.onBindMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();

    }

    public void setInitialData(DataSnapshot snapshot) {
        int count = messages.size();
        messages.clear();
        notifyItemRangeRemoved(0,count);
        for(DataSnapshot child : snapshot.getChildren()) {
            messages.add(child.getValue(Message.class));
            notifyItemInserted(messages.size() - 1);
        }
    }

   private void setData(DataSnapshot snapshot) {
        messages.clear();
        for(DataSnapshot child : snapshot.getChildren()) {
            messages.add(child.getValue(Message.class));
        }
   }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        messages.add(snapshot.getValue(Message.class));
        notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        setData(snapshot);
        System.out.println(previousChildName);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        int index = messages.indexOf(snapshot.getValue(Message.class));
        messages.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        setData(snapshot);
        notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}