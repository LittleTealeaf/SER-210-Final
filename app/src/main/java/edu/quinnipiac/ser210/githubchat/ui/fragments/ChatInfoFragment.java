package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarHolder;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;
import edu.quinnipiac.ser210.githubchat.ui.util.Keys;

/**
 * @author Thomas Kwashnak
 */
public class ChatInfoFragment extends Fragment implements GithubWrapper.OnFetchGithubRepo {

    private int channelGithubRepo;

    private GithubRepo githubRepo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        channelGithubRepo = GithubWrapper.from(context).startFetchGithubRepo(requireArguments().getString(Keys.REPO_NAME), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_info, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(githubRepo != null) {
            ToolbarHolder.from(requireContext()).setTitle("About " + githubRepo.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        channelGithubRepo = ThreadManager.NULL_CHANNEL;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChangedListener.notifyContext(requireContext(), this);
    }

    @Override
    public void onFetchGithubRepo(GithubRepo repo, int channel) {
        if (channel == channelGithubRepo) {
            this.githubRepo = repo;

            ToolbarHolder.from(requireContext()).setTitle("About " + repo.getName());

        }
    }
}