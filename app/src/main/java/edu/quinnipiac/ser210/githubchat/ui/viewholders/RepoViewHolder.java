package edu.quinnipiac.ser210.githubchat.ui.viewholders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.RepoAdapter;

/**
 * @author Thomas Kwashnak
 */
public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RepoAdapter.OnRepoSelectedListener {

    private final RepoAdapter adapter;
    private final View view;
    private final TextView textView;
    private GithubRepo repo;

    public RepoViewHolder(RepoAdapter adapter, @NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.adapter = adapter;

        itemView.setOnClickListener(this);
        textView = view.findViewById(R.id.list_repo_title);
    }

    public void bindTo(GithubRepo repo) {
        this.repo = repo;

        ((TextView) view.findViewById(R.id.list_repo_title)).setText(repo.getFullName());
    }

    @Override
    public void onClick(View view) {
        adapter.onItemSelected(repo);
    }

    @Override
    public void onRepoSelected(GithubRepo repo) {
        if(repo == this.repo) {
            textView.setTypeface(null,Typeface.BOLD);
        } else {
            textView.setTypeface(null,Typeface.NORMAL);
        }
    }
}
