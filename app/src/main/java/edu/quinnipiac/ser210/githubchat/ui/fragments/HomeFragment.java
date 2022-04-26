package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.github.async.FetchGithubUserTask;
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.ui.async.LoadImageTask;

/**
 * @author Thomas Kwashnak
 */
public class HomeFragment extends Fragment implements FetchGithubUserTask.Listener, View.OnClickListener, LoadImageTask.Listener {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        GithubWrapper.fromObject(context).fetchCurrentUser(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ((FloatingActionButton) view.findViewById(R.id.frag_home_fab_create)).setOnClickListener(this);



        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFetchGithubUser(GithubUser user) {
        new LoadImageTask(this).execute(user.getAvatarUrl());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frag_home_fab_create) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_createChatFragment);
        }
    }

    @Override
    public void onLoadImage(Bitmap bitmap) {
        ((ImageView) requireView().findViewById(R.id.frag_home_imageview_debug)).setImageBitmap(bitmap);
    }
}