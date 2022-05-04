package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.GithubRepoAdapter;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;
import edu.quinnipiac.ser210.githubchat.ui.util.Keys;

/**
 * @author Thomas Kwashnak
 */
public class CreateChatFragment extends Fragment
        implements GithubRepoAdapter.OnGithubRepoSelected, SearchView.OnQueryTextListener, View.OnClickListener, DatabaseWrapper.OnUpdateChatRoom {

    private GithubRepoAdapter adapter;

    private SearchView searchView;

    private int channelUpdateChatRoom;

    private FloatingActionButton confirmButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.frag_create_search_filter);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Enter Repository");

        RecyclerView recyclerView = view.findViewById(R.id.frag_create_recycler_repos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter = new GithubRepoAdapter(requireContext(), this));

        confirmButton = view.findViewById(R.id.frag_create_fab_confirm);
        confirmButton.setOnClickListener(this);

        FragmentChangedListener.notifyContext(requireContext(),this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onGithubRepoSelected(GithubRepo githubRepo) {
        confirmButton.setEnabled(githubRepo != null || searchView.getQuery().toString().contains("/"));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filterItems(newText);

        if(adapter.getSelected() == null) {
            confirmButton.setEnabled(newText.contains("/"));
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frag_create_fab_confirm) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setFavorite(false);
            chatRoom.setRepoName(adapter.getSelected() != null ? adapter.getSelected().getFullName() : searchView.getQuery().toString());
            channelUpdateChatRoom = DatabaseWrapper.from(requireContext()).startUpdateChatRoom(chatRoom, this);
        }
    }

    @Override
    public void onUpdateChatRoom(ChatRoom chatRoom, int channel) {
        if (channel == channelUpdateChatRoom) {
            Bundle bundle = new Bundle();
            bundle.putString(Keys.REPO_NAME, chatRoom.getRepoName());
            Navigation.findNavController(requireView()).navigate(R.id.action_createChatFragment_to_chatFragment, bundle);
        }
    }
}