package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.OAuthCredential;

import java.util.Objects;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.database.dataobjects.GithubCache;
import edu.quinnipiac.ser210.githubchat.helpers.Keys;
import edu.quinnipiac.ser210.githubchat.interfaces.GithubTokenHolder;
import edu.quinnipiac.ser210.githubchat.ui.fragments.LoginFragment;

/*
Current plan of action:

- The github token is stored in an object. Upon creation / recreation, it will use the Github Cache / api to pull data
- (Async task to get this data should be awaited at all parts of the journey, either that or like listeners and stuff)
- The user's github username is appended to each message. Then, when pulling data, each "visible" chat message has async tasks that fetches that
data to load (ex: getUserAvatar, getUserName...)
- So first we gotta make the database and everything


 */

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, GithubTokenHolder {

    private String githubToken;

    DatabaseHelper helper;

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            githubToken = savedInstanceState.getString(Keys.GITHUB_API_TOKEN);
        }

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        helper = new DatabaseHelper(this);
        GithubCache cache = new GithubCache();
        cache.setUrl("https://www.google.com");
        cache.setContent("I AM A BIRD");
        cache.setFetchTime(1000024);
        helper.addGithubCache(cache);
        System.out.println(helper.getGithubCache("https://www.google.com").getContent());
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Keys.GITHUB_API_TOKEN,githubToken);
    }
}