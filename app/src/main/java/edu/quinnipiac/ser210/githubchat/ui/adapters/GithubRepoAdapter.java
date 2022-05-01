package edu.quinnipiac.ser210.githubchat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnFetchChatRoomList;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubRepoList;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnGithubRepoSelected;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.GithubRepoViewHolder;

/**
 * @author Thomas Kwashnak
 */
public class GithubRepoAdapter extends RecyclerView.Adapter<GithubRepoViewHolder>
        implements OnFetchGithubRepoList, OnGithubRepoSelected, OnFetchChatRoomList {

    @Deprecated
    private final int CHANNEL_FETCH = 1;

    private final int channelRepoList;
    private final int channelChatRoomList;

    private final LayoutInflater inflater;

    private final OnGithubRepoSelected listener;

    private final List<ChatRoom> openRooms = new ArrayList<>();
    private final List<GithubRepo> githubRepos = new ArrayList<>();
    private final List<GithubRepoViewHolder> holderList = new ArrayList<>();
    private List<GithubRepo> displayRepos = new ArrayList<>();
    private String filter;
    private GithubRepo selected;

    public GithubRepoAdapter(Context context, OnGithubRepoSelected listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        channelRepoList = GithubWrapper.from(context).startFetchGithubRepoList(null, this);
        channelChatRoomList = DatabaseWrapper.from(context).startGetChatRoomList(this);
    }

    @NonNull
    @Override
    public GithubRepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GithubRepoViewHolder viewHolder = new GithubRepoViewHolder(this, inflater.inflate(R.layout.list_github_repo_item, parent, false));
        holderList.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GithubRepoViewHolder holder, int position) {
        if (position == 0) {
            holder.bindToCustom(filter);
        } else {
            holder.bindToGithubRepo(displayRepos.get(position - 1));
        }
        holder.onGithubRepoSelected(selected);
    }

    @Override
    public int getItemCount() {
        return displayRepos.size() + 1;
    }

    public GithubRepo getSelected() {
        return selected;
    }

    @Override
    public void onFetchGithubRepoList(List<GithubRepo> githubRepos, int channel) {
        if (channel == channelRepoList) {
            this.githubRepos.addAll(githubRepos);
            filterOpenedChats();
        }
    }

    private synchronized void filterOpenedChats() {
        List<GithubRepo> tmp = githubRepos.stream().filter((repo) -> {
            for (ChatRoom chatRoom : openRooms) {
                if (chatRoom.getRepoName().equals(repo.getFullName())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        githubRepos.clear();
        githubRepos.addAll(tmp);
        filterItems("");
    }

    public void filterItems(String filterText) {
        String[] filters = filterText.split(" ");
        displayRepos = filterText.equals("") ? githubRepos : githubRepos.stream().filter(githubRepo -> {
            for (String filter : filters) {
                if (!githubRepo.getFullName().toLowerCase().contains(filter.toLowerCase(Locale.ROOT))) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        this.filter = filterText;
        notifyDataSetChanged();
    }

    @Override
    public void onGithubRepoSelected(GithubRepo githubRepo) {
        this.selected = githubRepo;
        listener.onGithubRepoSelected(githubRepo);
        for (GithubRepoViewHolder viewHolder : holderList) {
            viewHolder.onGithubRepoSelected(githubRepo);
        }
    }

    @Override
    public void onFetchChatRoomList(List<ChatRoom> chatRooms, int channel) {
        if (channel == channelChatRoomList) {
            openRooms.addAll(chatRooms);
            filterOpenedChats();
        }
    }
}
