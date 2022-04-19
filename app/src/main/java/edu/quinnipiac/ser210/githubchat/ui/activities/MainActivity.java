package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.OAuthCredential;

import java.util.Objects;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.helpers.interfaces.GithubTokenHolder;
import edu.quinnipiac.ser210.githubchat.ui.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, GithubTokenHolder {

    private String githubToken;

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
    }

    @Override
    public void onLogin(AuthResult authResult) {
        githubToken = ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken();
        navController.navigate(R.id.action_loginFragment_to_debugChatFragment);
    }

    @Override
    public String getGithubToken() {
        return githubToken;
    }
}