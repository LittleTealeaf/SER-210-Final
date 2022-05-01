package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubAttachable;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.AttachableViewHolder;

public class AttachableAdapter extends RecyclerView.Adapter<AttachableViewHolder> {

    private Context context;
    private LayoutInflater inflater;

    private List<GithubAttachable> attachableList = new LinkedList<>();

    public AttachableAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AttachableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachableViewHolder(this,inflater.inflate(R.layout.list_attachable_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachableViewHolder holder, int position) {
        holder.bindAttachable(attachableList.get(position));
    }

    public void clearItems() {
        int size = attachableList.size();
        attachableList.clear();
        notifyItemRangeRemoved(0,size);
    }

    public void addAttachable(GithubAttachable attachable) {
        attachableList.add(attachable);
        notifyItemInserted(attachableList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return attachableList.size();
    }

    public Context getContext() {
        return context;
    }
}
