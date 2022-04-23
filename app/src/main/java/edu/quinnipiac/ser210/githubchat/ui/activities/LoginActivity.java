package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import java.util.Objects;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

public class LoginActivity extends AppCompatActivity implements OnSuccessListener<AuthResult>, OnFailureListener, View.OnClickListener {


    private OAuthProvider.Builder builder;
    private Task<AuthResult> pendingTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder = OAuthProvider.newBuilder("github.com");

        findViewById(R.id.button_login_github).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        pendingTask = auth.startActivityForSignInWithProvider(this,builder.build());

        pendingTask.addOnSuccessListener(this);
        pendingTask.addOnFailureListener(this);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e("LoginActivity",e.getMessage());
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        String token = ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(GithubWrapper.TOKEN,token);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}