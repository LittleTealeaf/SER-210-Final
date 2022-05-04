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

import com.google.firebase.auth.FirebaseAuth;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;

/**
 * @author Thomas Kwashnak
 */
public class AppInfoFragment extends Fragment implements View.OnClickListener {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FragmentChangedListener.notifyContext(context, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.frag_app_info_button_github).setOnClickListener(this);
        view.findViewById(R.id.frag_app_info_button_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.frag_app_info_button_github) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/LittleTealeaf/SER-210-Final")));
        } else if(v.getId() == R.id.frag_app_info_button_logout) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out of Github App?")
                    .setIcon(R.drawable.ic_material_logout_48)
                    .setCancelable(true)
                    .setPositiveButton("Logout", (dialog, id) -> FirebaseAuth.getInstance().signOut())
                    .setNegativeButton("Cancel", (dialog, id) -> {})
                    .create()
                    .show();
        }
    }
}