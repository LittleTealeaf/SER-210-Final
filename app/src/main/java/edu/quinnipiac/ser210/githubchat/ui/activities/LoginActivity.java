package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import java.util.Objects;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;

/**
 * @author Thomas Kwashnak
 */
public class LoginActivity extends AppCompatActivity implements OnSuccessListener<AuthResult>, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.activity_login_button_login).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Task<AuthResult> pendingTask = FirebaseAuth.getInstance().getPendingAuthResult();
        if(pendingTask != null) {
            pendingTask.addOnSuccessListener(this);
        }
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
        }
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        String token = ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken();
        Intent intent = new Intent();
        intent.putExtra(GithubWrapper.AUTH_TOKEN,token);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().startActivityForSignInWithProvider(this, OAuthProvider.newBuilder(GithubAuthProvider.PROVIDER_ID).build()).addOnSuccessListener(this);
    }
}