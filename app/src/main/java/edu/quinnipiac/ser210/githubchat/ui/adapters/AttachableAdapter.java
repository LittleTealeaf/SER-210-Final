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

@Deprecated
public class AttachableAdapter extends RecyclerView.Adapter<AttachableViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;

    private final List<GithubAttachable> attachableList = new LinkedList<>();

    public AttachableAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AttachableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachableViewHolder(this, inflater.inflate(R.layout.list_attachable_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachableViewHolder holder, int position) {
        holder.bindAttachable(attachableList.get(position));
    }

    @Override
    public int getItemCount() {
        return attachableList.size();
    }

    public void clearItems() {
        int size = attachableList.size();
        attachableList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAttachable(GithubAttachable attachable) {
        for (int i = 0; i < attachableList.size(); i++) {
            if (attachable.getNumber() < attachableList.get(i).getNumber()) {
                attachableList.add(i, attachable);
                notifyItemInserted(i);
                return;
            }
        }
        attachableList.add(attachable);
        notifyItemInserted(attachableList.size() - 1);
    }

    public Context getContext() {
        return context;
    }
}
