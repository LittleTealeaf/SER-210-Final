package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.GithubRepoAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnGithubRepoSelected;

public class GithubRepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final GithubRepoAdapter adapter;
    private GithubRepo githubRepo;
    private TextView textView;

    public GithubRepoViewHolder(GithubRepoAdapter adapter, @NonNull View itemView) {
        super(itemView);
        this.adapter = adapter;
        this.textView = itemView.findViewById(R.id.list_github_repo_title);
    }

    public void bindToGithubRepo(GithubRepo githubRepo) {
        this.githubRepo = githubRepo;
        textView.setText(githubRepo.getFullName());
    }

    @Override
    public void onClick(View view) {
        adapter.onGithubRepoSelected(githubRepo);
    }
}
