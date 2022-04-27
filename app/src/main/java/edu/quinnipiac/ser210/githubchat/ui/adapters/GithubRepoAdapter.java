package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubRepoList;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnGithubRepoSelected;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.GithubRepoViewHolder;

public class GithubRepoAdapter extends RecyclerView.Adapter<GithubRepoViewHolder> implements OnFetchGithubRepoList, OnGithubRepoSelected {

    private final int CHANNEL_FETCH = 1;

    private final LayoutInflater inflater;

    private final OnGithubRepoSelected listener;

    private final List<GithubRepo> githubRepos = new ArrayList<>();
    private List<GithubRepo> displayRepos = new ArrayList<>();

    private final List<GithubRepoViewHolder> holderList = new ArrayList<>();

    public GithubRepoAdapter(Context context, OnGithubRepoSelected listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        GithubWrapper.from(context).startFetchGithubRepoList(null,this,CHANNEL_FETCH);
    }

    @NonNull
    @Override
    public GithubRepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GithubRepoViewHolder viewHolder = new GithubRepoViewHolder(this,inflater.inflate(R.layout.list_github_repo_item, parent, false));
        holderList.add(viewHolder);
        return viewHolder;
    }

    public void filterItems(String filterText) {
        String[] filters = filterText.split(" ");
        displayRepos = filterText.equals("") ? githubRepos : githubRepos.stream().filter(githubRepo -> {
            for(String filter : filters) {
                if(!githubRepo.getFullName().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull GithubRepoViewHolder holder, int position) {
        holder.bindToGithubRepo(displayRepos.get(position));
    }

    @Override
    public int getItemCount() {
        return displayRepos.size();
    }

    @Override
    public void onFetchGithubRepoList(List<GithubRepo> githubRepos, int channel) {
        if(channel == CHANNEL_FETCH) {
            this.githubRepos.addAll(githubRepos);
            filterItems("");
            notifyItemRangeInserted(this.githubRepos.size() - githubRepos.size(),githubRepos.size());
        }
    }

    @Override
    public void onGithubRepoSelected(GithubRepo githubRepo) {
        listener.onGithubRepoSelected(githubRepo);
    }
}
