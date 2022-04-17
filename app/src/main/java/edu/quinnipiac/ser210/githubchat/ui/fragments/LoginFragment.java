package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import edu.quinnipiac.ser210.githubchat.R;


public class LoginFragment extends Fragment implements OnSuccessListener<AuthResult>, OnFailureListener, View.OnClickListener {


    private OAuthProvider.Builder builder;
    private Task<AuthResult> pendingResultTask;

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        builder = OAuthProvider.newBuilder("github.com");

        pendingResultTask = FirebaseAuth.getInstance().getPendingAuthResult();
        if(pendingResultTask != null) {
            pendingResultTask.addOnFailureListener(this).addOnSuccessListener(this);
        }

        return inflater.inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_login_github).setOnClickListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    @Override
    public void onSuccess(AuthResult authResult) {

    }

    @Override
    public void onClick(View view) {

    }
}