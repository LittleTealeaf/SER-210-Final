package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarAction;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarHolder;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;
import edu.quinnipiac.ser210.githubchat.ui.util.Keys;

/**
 * @author Thomas Kwashnak
 */
public class ChatInfoFragment extends Fragment implements GithubWrapper.OnFetchGithubRepo, View.OnClickListener, DatabaseWrapper.OnRemoveChatRoom, ToolbarAction.Github {

    private int channelGithubRepo, channelRemoveChatRoom;

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
        if (githubRepo != null) {
            ToolbarHolder.from(requireContext()).setTitle("About " + githubRepo.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        channelRemoveChatRoom = channelGithubRepo = ThreadManager.NULL_CHANNEL;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChangedListener.notifyContext(requireContext(), this);
        view.findViewById(R.id.frag_chat_info_button_leave).setOnClickListener(this);
    }

    @Override
    public void onFetchGithubRepo(GithubRepo repo, int channel) {
        if (channel == channelGithubRepo) {
            this.githubRepo = repo;

            ToolbarHolder.from(requireContext()).setTitle("About " + repo.getName());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.frag_chat_info_button_leave) {
            new AlertDialog.Builder(requireContext()).setTitle("Leave Room").setMessage("Are you sure you want to leave this chat room?").setIcon(
                    R.drawable.ic_material_logout_48).setCancelable(true).setPositiveButton("Leave", (dialog, id) -> channelRemoveChatRoom = DatabaseWrapper.from(
                    requireContext()).startRemoveChatRoom(githubRepo.getFullName(), this)).setNegativeButton("Cancel", (dialog, id) -> {}).create().show();
        }
    }

    @Override
    public void onRemoveChatRoom(String repoName, int channel) {
        if(channel == channelRemoveChatRoom) {
            Navigation.findNavController(requireView()).popBackStack(R.id.homeFragment,false);
        }
    }

    @Override
    public void onGithub() {
        if(githubRepo != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubRepo.getUrl()));
            startActivity(intent);
        }
    }
}