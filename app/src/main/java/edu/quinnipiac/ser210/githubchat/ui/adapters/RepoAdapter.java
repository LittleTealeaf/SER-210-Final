package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubRepoListTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.viewholders.RepoViewHolder;

/**
 * @author Thomas Kwashnak
 */
public class RepoAdapter extends RecyclerView.Adapter<RepoViewHolder> implements FetchGithubRepoListTask.Listener {

    private final List<GithubRepo> repos = new LinkedList<>();

    private List<GithubRepo> displayRepos = new LinkedList<>();

    private final LayoutInflater inflater;

    private OnRepoSelectedListener listener;

    private List<OnRepoSelectedListener> viewListeners = new LinkedList<>();

    public RepoAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setOnRepoSelectedListener(OnRepoSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RepoViewHolder holder = new RepoViewHolder(this, inflater.inflate(R.layout.list_repo_item, parent, false));
        viewListeners.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.bindTo(displayRepos.get(position));
    }


    @Override
    public int getItemCount() {
        return displayRepos.size();
    }

    @Override
    public void onFetchGithubRepoList(List<GithubRepo> repoList) {
        repos.addAll(repoList);
        displayRepos = repos;
        notifyItemRangeInserted(0, repoList.size());
    }

    public void filterList(String filterText) {
        String[] filters = filterText.split(" ");
        displayRepos = filterText.equals("") ? repos : repos.stream().filter(githubRepo -> {
            for(String filter : filters) {
                if(!githubRepo.getFullName().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        notifyDataSetChanged();

    }



    public void onItemSelected(GithubRepo item) {
        if(listener != null) {
            listener.onRepoSelected(item);

        }
        for(OnRepoSelectedListener listener : viewListeners) {
            listener.onRepoSelected(item);
        }
    }

    public interface OnRepoSelectedListener {
        void onRepoSelected(GithubRepo repo);
    }
}
