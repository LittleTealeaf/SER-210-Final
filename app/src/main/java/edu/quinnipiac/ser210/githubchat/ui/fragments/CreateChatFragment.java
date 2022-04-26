package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.RepoAdapter;

public class CreateChatFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener, RepoAdapter.OnRepoSelectedListener {

    private RepoAdapter adapter;
    private Button createManualButton;

    private FloatingActionButton confirmButton;

    private GithubRepo currentSelection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = (SearchView) view.findViewById(R.id.frag_create_search);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Enter Repository");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.frag_create_repo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RepoAdapter(getContext());
        adapter.setOnRepoSelectedListener(this);
        recyclerView.setAdapter(adapter);

        createManualButton = (Button) view.findViewById(R.id.frag_create_button_manual);
        createManualButton.setOnClickListener(this);

        confirmButton = view.findViewById(R.id.frag_create_fab_confirm);
        confirmButton.setOnClickListener(this);


        GithubWrapper.fromObject(requireActivity()).fetchCurrentUserRepos(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filterList(newText);
        createManualButton.setText(String.format("Manually Create: %s", newText));
        createManualButton.setEnabled(!newText.equals(""));
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_create_fab_confirm) {
            Bundle bundle = new Bundle();
            bundle.putLong(DatabaseHelper.BUNDLE_ID,DatabaseHelper.fromObject(requireActivity()).addRepository(currentSelection.toChatRepository()));

            Navigation.findNavController(requireView()).navigate(R.id.action_createChatFragment_to_chatFragment,bundle);
        }
    }

    @Override
    public void onRepoSelected(GithubRepo repo) {
        currentSelection = repo;
        confirmButton.setEnabled(repo != null);
    }
}