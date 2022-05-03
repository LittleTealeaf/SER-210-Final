package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.ChatRoom;
import edu.quinnipiac.ser210.githubchat.ui.adapters.ChatRoomAdapter;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarAction;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;

/**
 * @author Thomas Kwashnak
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ChatRoomAdapter.OnChatRoomSelected, ToolbarAction.Info, ToolbarAction.Share {

    private ChatRoomAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.frag_home_recycler_rooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter = new ChatRoomAdapter(requireContext(), this));
        view.findViewById(R.id.frag_home_fab_create).setOnClickListener(this);
        adapter.setFetchChatRoomChannel(DatabaseWrapper.from(requireContext()).startGetChatRoomList(adapter));

        FragmentChangedListener.notifyContext(requireContext(),this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frag_home_fab_create) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_createChatFragment);
        }
    }

    @Override
    public void onChatRoomSelected(ChatRoom chatRoom) {
        Bundle bundle = new Bundle();
        bundle.putString(DatabaseWrapper.KEY_REPO_NAME, chatRoom.getRepoName());
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_chatFragment, bundle);
    }

    @Override
    public void onShare() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Look at this cool app! https://github.com/LittleTealeaf/SER-210-Final");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, null));
    }

    @Override
    public void onInfo() {
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_settingsFragment);
    }
}