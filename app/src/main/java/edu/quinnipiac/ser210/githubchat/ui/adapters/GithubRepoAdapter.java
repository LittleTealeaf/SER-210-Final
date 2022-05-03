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
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.adapters.viewholders.GithubRepoViewHolder;
import edu.quinnipiac.ser210.githubchat.ui.util.FilterableList;

/**
 * @author Thomas Kwashnak
 */
public class GithubRepoAdapter extends RecyclerView.Adapter<GithubRepoViewHolder>
        implements FilterableList.ChangeListener<GithubRepo> {

    @Deprecated
    private final int CHANNEL_FETCH = 1;


    private final Context context;
    private final LayoutInflater inflater;
    private final OnGithubRepoSelected listener;
    private final FilterableList<GithubRepo> filterableList;

    private final List<GithubRepo> githubRepos = new LinkedList<>();

    private final List<GithubRepoViewHolder> holderList = new ArrayList<>();
    private String filter;
    private GithubRepo selected;

    public GithubRepoAdapter(Context context, OnGithubRepoSelected listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        filterableList = new FilterableList<>(this::filterRepo, this);
//        channelRepoList = GithubWrapper.from(context).startFetchGithubRepoList(null, this);

        final DatabaseWrapper databaseWrapper = DatabaseWrapper.from(context);
        ThreadManager.startThread(() -> GithubWrapper.from(context).fetchGithubRepoList(null)
                .stream()
                .filter((repo) -> databaseWrapper.getChatRoom(repo.getFullName()) == null)
                .collect(Collectors.toList()), (items,index) -> items.forEach(filterableList::addItem));

//        channelChatRoomList = DatabaseWrapper.from(context).startGetChatRoomList(this);
    }

    private boolean filterRepo(GithubRepo repo, String filterString) {

       if(!filterString.equals("")) {
           String[] filters = filterString.toLowerCase(Locale.ROOT).split(" ");

           for (String filter : filters) {
               if (!repo.getFullName().toLowerCase().contains(filter)) {
                   return false;
               }
           }
       }
        return true;
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
            holder.bindToGithubRepo(githubRepos.get(position - 1));
        }
        holder.updateSelectedGithubRepo(selected);
    }

    @Override
    public int getItemCount() {
        return githubRepos.size() + 1;
    }

    public GithubRepo getSelected() {
        return selected;
    }



//    private synchronized void filterOpenedChats() {
//        List<GithubRepo> tmp = githubRepos.stream().filter((repo) -> {
//            for (ChatRoom chatRoom : openRooms) {
//                if (chatRoom.getRepoName().equals(repo.getFullName())) {
//                    return false;
//                }
//            }
//            return true;
//        }).collect(Collectors.toList());
//        githubRepos.clear();
//        githubRepos.addAll(tmp);
//        filterItems("");
//    }

    public void filterItems(String filterText) {
        filterableList.setFilter(filterText);
        this.filter = filterText;
        notifyItemChanged(0);
//        String[] filters = filterText.split(" ");
//        displayRepos = filterText.equals("") ? githubRepos : githubRepos.stream().filter(githubRepo -> {

//        }).collect(Collectors.toList());
//        this.filter = filterText;
//        notifyDataSetChanged();
    }

    public void selectGithubRepo(GithubRepo githubRepo) {
        this.selected = githubRepo;
        listener.onGithubRepoSelected(githubRepo);
        for (GithubRepoViewHolder viewHolder : holderList) {
            viewHolder.updateSelectedGithubRepo(githubRepo);
        }
    }


    @Override
    public void onItemAdded(GithubRepo item) {
        for(int i = 0; i < githubRepos.size(); i++) {
            GithubRepo item2 = githubRepos.get(i);
            if(item2.getFullName().toLowerCase().charAt(0) > item.getFullName().toLowerCase().charAt(0) && item2.getFullName().charAt(0) > item.getFullName().charAt(0)){
                githubRepos.add(i,item);
                notifyItemInserted(i+1);
                return;
            }
        }
        githubRepos.add(item);
        notifyItemInserted(githubRepos.size());
    }

    @Override
    public void onItemRemoved(GithubRepo item) {
        int index = githubRepos.indexOf(item);
        if(index != -1) {
            githubRepos.remove(item);
            notifyItemRemoved(index+1);
        }

    }

    public interface OnGithubRepoSelected {

        void onGithubRepoSelected(GithubRepo githubRepo);
    }
}
