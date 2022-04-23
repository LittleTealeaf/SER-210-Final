package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubLinkablesTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.Linkable;

public class ChatFragment extends Fragment implements FetchGithubLinkablesTask.Listener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onFetchLinkables(List<Linkable> linkables) {

    }
}