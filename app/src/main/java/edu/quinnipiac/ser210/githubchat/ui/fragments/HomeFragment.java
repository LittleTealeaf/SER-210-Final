package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
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
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.OnChatRoomSelected;

/**
 * @author Thomas Kwashnak
 */
public class HomeFragment extends Fragment implements View.OnClickListener, OnChatRoomSelected {

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
        recyclerView.setAdapter(adapter = new ChatRoomAdapter(requireContext(),this));
        view.findViewById(R.id.frag_home_fab_create).setOnClickListener(this);
        DatabaseWrapper.from(requireContext()).oldstartgetchatrooms(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_home_fab_create) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_createChatFragment);
        }
    }

    @Override
    public void onChatRoomSelected(ChatRoom chatRoom) {
        Bundle bundle = new Bundle();
        bundle.putString(DatabaseWrapper.KEY_REPO_NAME,chatRoom.getRepoName());
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_chatFragment,bundle);
    }
}