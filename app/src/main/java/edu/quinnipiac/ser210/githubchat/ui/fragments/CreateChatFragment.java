package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.database.listeners.OnSetChatRoom;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.GithubRepoAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnGithubRepoSelected;

public class CreateChatFragment extends Fragment implements OnGithubRepoSelected, SearchView.OnQueryTextListener, View.OnClickListener,
                                                            OnSetChatRoom {

    private static final int CHANNEL_CREATE = 1;
    private GithubRepoAdapter adapter;

    private FloatingActionButton confirmButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = view.findViewById(R.id.frag_create_search_filter);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Enter Repository");

        RecyclerView recyclerView = view.findViewById(R.id.frag_create_recycler_repos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter = new GithubRepoAdapter(requireContext(),this));

        confirmButton = view.findViewById(R.id.frag_create_fab_confirm);
        confirmButton.setOnClickListener(this);

    }

    @Override
    public void onGithubRepoSelected(GithubRepo githubRepo) {
        confirmButton.setEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filterItems(newText);
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_create_fab_confirm) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setFavorite(false);
            chatRoom.setRepoName(adapter.getSelected().getFullName());
            DatabaseWrapper.from(requireContext()).startSetChatRoom(chatRoom,this,CHANNEL_CREATE);
        }
    }

    @Override
    public void onSetChatRoom(ChatRoom chatRoom, int channel) {
        if(channel == CHANNEL_CREATE) {
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseWrapper.KEY_REPO_NAME,chatRoom.getRepoName());
            Navigation.findNavController(requireView()).navigate(R.id.action_createChatFragment_to_chatFragment,bundle);
        }
    }
}