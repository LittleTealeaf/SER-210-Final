package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.ui.adapters.GithubRepoAdapter;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnGithubRepoSelected;

public class CreateChatFragment extends Fragment implements OnGithubRepoSelected {

    private GithubRepoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.frag_create_recycler_repos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter = new GithubRepoAdapter(requireContext(),this));

    }

    @Override
    public void onGithubRepoSelected(GithubRepo githubRepo) {

    }
}