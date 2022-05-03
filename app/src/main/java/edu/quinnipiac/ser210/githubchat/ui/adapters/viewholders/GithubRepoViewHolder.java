package edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.GithubRepoAdapter;

/**
 * @author Thomas Kwashnak
 */
public class GithubRepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final GithubRepoAdapter adapter;
    private final TextView textView;
    private GithubRepo githubRepo;

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
        if (githubRepo != null) {
            textView.setText(githubRepo.getFullName());
        }
    }

    @Override
    public void onClick(View view) {
        adapter.selectGithubRepo(githubRepo);
    }

    public void updateSelectedGithubRepo(GithubRepo githubRepo) {
        textView.setTypeface(null, githubRepo == this.githubRepo ? Typeface.BOLD : Typeface.NORMAL);
    }

    @Deprecated
    public interface oldOnGithubRepoSelected extends GithubRepoAdapter.OnGithubRepoSelected {

        void onGithubRepoSelected(GithubRepo githubRepo);
    }
}
