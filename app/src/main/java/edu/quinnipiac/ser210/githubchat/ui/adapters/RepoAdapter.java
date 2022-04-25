package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubRepoListTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.viewholders.RepoViewHolder;

public class RepoAdapter extends RecyclerView.Adapter<RepoViewHolder> implements FetchGithubRepoListTask.Listener {

    private final List<GithubRepo> repos = new LinkedList<>();

    private final LayoutInflater inflater;

    public RepoAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepoViewHolder(this, inflater.inflate(R.layout.list_repo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.bindTo(repos.get(position));
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    @Override
    public void onFetchGithubRepoList(List<GithubRepo> repoList) {
        repos.addAll(repoList);
        notifyItemRangeInserted(0, repoList.size());
    }
}
