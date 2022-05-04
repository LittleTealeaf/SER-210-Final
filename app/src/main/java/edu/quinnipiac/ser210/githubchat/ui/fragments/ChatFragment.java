package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.Date;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.firebase.dataobjects.Message;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubRepo;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.adapters.MessageAdapter;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarAction;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarHolder;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;
import edu.quinnipiac.ser210.githubchat.ui.util.Keys;

/**
 * @author Thomas Kwashnak
 */
public class ChatFragment extends Fragment
        implements View.OnClickListener, DatabaseWrapper.OnFetchChatRoom, GithubWrapper.OnFetchGithubRepo, TextWatcher, ToolbarAction.Info, ToolbarAction.Share,
                   ToolbarAction.Github {

    private int channelChatRoom;
    private int channelGithubRepo;
    private GithubRepo githubRepo;
    private MessageAdapter adapter;
    private DatabaseReference databaseReference;
    private EditText inputText;
    private Button sendButton;
    private RecyclerView recyclerView;
    private GithubWrapper githubWrapper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ThreadManager.schedule(this::runTimer);
        githubWrapper = GithubWrapper.from(context);
        channelChatRoom = DatabaseWrapper.from(context).startGetChatRoom(requireArguments().getString(Keys.REPO_NAME), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String ref = requireArguments().getString(Keys.REPO_NAME);

        for (char c : ".#$[]".toCharArray()) {
            ref = ref.replace(c, '_');
        }

        databaseReference = FirebaseDatabase.getInstance().getReference(ref);

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton scrollBack = view.findViewById(R.id.frag_chat_fab_scroll);
        scrollBack.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.frag_chat_recycler_messages);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) == scrollBack.isShown()) {
                    if (scrollBack.isShown()) {
                        scrollBack.hide();
                    } else {
                        scrollBack.show();
                    }
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter = new MessageAdapter(requireArguments().getString(Keys.REPO_NAME), requireContext(), recyclerView));

        inputText = view.findViewById(R.id.frag_chat_edittext_insert);
        inputText.addTextChangedListener(this);

        sendButton = view.findViewById(R.id.frag_chat_button_send);
        sendButton.setOnClickListener(this);

        FragmentChangedListener.notifyContext(requireContext(), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addChildEventListener(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (githubRepo != null) {
            ToolbarHolder.from(requireContext()).setTitle(githubRepo.getName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(adapter);
        adapter.clearEntries();
    }

    @Override
    public void onStop() {
        super.onStop();
        channelChatRoom = ThreadManager.NULL_CHANNEL;
        channelGithubRepo = ThreadManager.NULL_CHANNEL;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frag_chat_button_send) {

            Message message = new Message();

            message.setMessage(inputText.getText().toString());
            message.setSender(githubWrapper.getGithubUser().getLogin());
            message.setSendTime(Date.from(Instant.now()).getTime());

            inputText.setText("");

            databaseReference.push().setValue(message);
        } else if (view.getId() == R.id.frag_chat_fab_scroll) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void onFetchChatRoom(ChatRoom chatRoom, int channel) {
        if (channel == channelChatRoom) {
            channelGithubRepo = githubWrapper.startFetchGithubRepo(chatRoom.getRepoName(), this);
        }
    }

    @Override
    public void onFetchGithubRepo(GithubRepo repo, int channel) {
        if (channel == channelGithubRepo) {
            this.githubRepo = repo;
            ToolbarHolder.from(requireContext()).setTitle(repo.getName());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        sendButton.setEnabled(!editable.toString().equals(""));
    }

    private void runTimer() {
        if (!isDetached()) {
            if (adapter != null) {
                adapter.updateTimes();
            }
            ThreadManager.scheduleDelayed(this::runTimer, 1000 * 30);
        }
    }

    @Override
    public void onInfo() {
        Navigation.findNavController(requireView()).navigate(R.id.action_chatFragment_to_chatInfoFragment, requireArguments());
    }

    @Override
    public void onShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"https://www.githubchatapp.com/room/" + githubRepo.getFullName());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Share Chat"));
    }

    @Override
    public void onGithub() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubRepo.getUrl()));
        startActivity(intent);
    }
}