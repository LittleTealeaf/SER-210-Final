package edu.quinnipiac.ser210.githubchat.ui.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.RepoAdapter;

public class RepoViewHolder extends RecyclerView.ViewHolder {

    private final RepoAdapter adapter;
    private final View view;

    public RepoViewHolder(RepoAdapter adapter, @NonNull View itemView) {
        super(itemView);
        this.adapter = adapter;
        this.view = itemView;
    }

    public void bindTo(GithubRepo repo) {
        ((TextView) view.findViewById(R.id.list_repo_title)).setText(repo.getFullName());
    }
}
