package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubUserTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;

public class HomeFragment extends Fragment implements FetchGithubUserTask.Listener, View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ((FloatingActionButton) view.findViewById(R.id.frag_home_fab_create)).setOnClickListener(this);


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFetchGithubUser(GithubUser user) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_home_fab_create) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_createChatFragment);
        }
    }
}