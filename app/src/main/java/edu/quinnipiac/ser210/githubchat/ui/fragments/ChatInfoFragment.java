package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Map;

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
public class ChatInfoFragment extends Fragment
        implements GithubWrapper.OnFetchGithubRepo, View.OnClickListener, DatabaseWrapper.OnRemoveChatRoom, ToolbarAction.Github, GithubWrapper.OnFetchGithubLanguages {

    private int channelGithubRepo, channelRemoveChatRoom, channelGithubLanguages;

    private GithubRepo githubRepo;
    private TextView languagesView;
    private TextView titleView;
    private TextView descriptionView;
    private Button websiteButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        channelGithubRepo = GithubWrapper.from(context).startFetchGithubRepo(requireArguments().getString(Keys.REPO_NAME), this);
        channelGithubLanguages = GithubWrapper.from(context).startFetchGithubLanguages(requireArguments().getString(Keys.REPO_NAME), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentChangedListener.notifyContext(requireContext(), this);
        view.findViewById(R.id.frag_chat_info_button_leave).setOnClickListener(this);
        view.findViewById(R.id.frag_chat_info_button_website).setOnClickListener(this);
        view.findViewById(R.id.frag_chat_info_button_github).setOnClickListener(this);

        titleView = view.findViewById(R.id.frag_chat_info_text_name);
        descriptionView = view.findViewById(R.id.frag_chat_info_text_description);
        languagesView = view.findViewById(R.id.frag_chat_info_text_languages);
        websiteButton = view.findViewById(R.id.frag_chat_info_button_website);
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
        channelGithubLanguages = channelRemoveChatRoom = channelGithubRepo = ThreadManager.NULL_CHANNEL;
    }

    @Override
    public void onFetchGithubRepo(GithubRepo repo, int channel) {
        if (channel == channelGithubRepo) {
            this.githubRepo = repo;

            ToolbarHolder.from(requireContext()).setTitle("About " + repo.getName());

            titleView.setText(repo.getFullName());
            descriptionView.setText(repo.getDescription());

            if (repo.getWebsite() != null) {
                websiteButton.setVisibility(View.VISIBLE);
            } else {
                websiteButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.frag_chat_info_button_leave) {
            new AlertDialog.Builder(requireContext()).setTitle("Leave Room").setMessage("Are you sure you want to leave this chat room?").setIcon(
                    R.drawable.ic_material_logout_48).setCancelable(true).setPositiveButton("Leave", (dialog, id) -> channelRemoveChatRoom = DatabaseWrapper.from(
                    requireContext()).startRemoveChatRoom(githubRepo.getFullName(), this)).setNegativeButton("Cancel", (dialog, id) -> {}).create().show();
        } else if (v.getId() == R.id.frag_chat_info_button_github) {
            onGithub();
        } else if (v.getId() == R.id.frag_chat_info_button_website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(githubRepo.getWebsite())));
        }
    }

    @Override
    public void onGithub() {
        if (githubRepo != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubRepo.getUrl()));
            startActivity(intent);
        }
    }

    @Override
    public void onRemoveChatRoom(String repoName, int channel) {
        if (channel == channelRemoveChatRoom) {
            Navigation.findNavController(requireView()).popBackStack(R.id.homeFragment, false);
        }
    }

    @Override
    public void onFetchGithubLanguages(Map<String, Integer> languages, int channel) {
        if (channel == channelGithubLanguages) {
            StringBuilder builder = new StringBuilder("Written in ");
            for (String key : languages.keySet()) {
                builder.append(key).append(", ");
            }

            languagesView.setText(builder.append("\n").toString().replace(", \n", ""));
        }
    }
}