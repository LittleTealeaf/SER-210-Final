package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.GithubRepoAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnGithubRepoSelected;

public class GithubRepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnGithubRepoSelected {

    private final GithubRepoAdapter adapter;
    private GithubRepo githubRepo;
    private TextView textView;

    public GithubRepoViewHolder(GithubRepoAdapter adapter, @NonNull View itemView) {
        super(itemView);
        this.adapter = adapter;
        this.textView = itemView.findViewById(R.id.list_github_repo_title);
        itemView.setOnClickListener(this);
    }

    public void bindToCustom(String name) {
        if (name == null || name.equals("")) {
            textView.setText("");
        } else {
            textView.setText(String.format("Use Repo: %s", name));
        }
        githubRepo = null;
    }

    public void bindToGithubRepo(GithubRepo githubRepo) {
        this.githubRepo = githubRepo;
        if(githubRepo != null) {
            textView.setText(githubRepo.getFullName());
        }
    }

    @Override
    public void onClick(View view) {
        adapter.onGithubRepoSelected(githubRepo);
    }

    @Override
    public void onGithubRepoSelected(GithubRepo githubRepo) {
        textView.setTypeface(null, githubRepo == this.githubRepo ? Typeface.BOLD : Typeface.NORMAL);
    }
}
