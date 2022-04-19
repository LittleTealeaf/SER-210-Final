package edu.quinnipiac.ser210.githubchat.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import edu.quinnipiac.ser210.githubchat.R;

public class LoginFragment extends Fragment implements OnSuccessListener<AuthResult>, OnFailureListener, View.OnClickListener {

    public static String TAG = "LoginFragment";

    private OAuthProvider.Builder builder;
    private Task<AuthResult> pendingResultTask;

    private Listener listener;

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
        if (pendingResultTask != null) {
            pendingResultTask.addOnFailureListener(this).addOnSuccessListener(this);
        }

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button_login_github).setOnClickListener(this);

        Activity activity = requireActivity();
        if(activity instanceof Listener) {
            listener = (Listener) activity;
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e(TAG,e.getMessage());
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        if(listener != null) {
            listener.onLogin(authResult);
        }
    }

    @Override
    public void onClick(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        pendingResultTask = auth.startActivityForSignInWithProvider(getActivity(), builder.build());
        pendingResultTask.addOnSuccessListener(this);
        pendingResultTask.addOnFailureListener(this);
    }

    public interface Listener {

        void onLogin(AuthResult authResult);
    }
}