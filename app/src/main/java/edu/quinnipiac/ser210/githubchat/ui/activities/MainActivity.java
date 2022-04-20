package edu.quinnipiac.ser210.githubchat.ui.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.helpers.Keys;

/*
Current plan of action:

- The github token is stored in an object. Upon creation / recreation, it will use the Github Cache / api to pull data
- (Async task to get this data should be awaited at all parts of the journey, either that or like listeners and stuff)
- The user's github username is appended to each message. Then, when pulling data, each "visible" chat message has async tasks that fetches that
data to load (ex: getUserAvatar, getUserName...)
- So first we gotta make the database and everything


 */

public class MainActivity extends AppCompatActivity implements DatabaseHelper.Holder, GithubWrapper.Holder {

    private DatabaseHelper databaseHelper;
    private GithubWrapper githubWrapper;

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);

        if (savedInstanceState != null) {
            githubWrapper = new GithubWrapper(savedInstanceState.getString(Keys.GITHUB_API_TOKEN));
        } else {
            githubWrapper = new GithubWrapper();
        }

        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Keys.GITHUB_API_TOKEN, githubWrapper.getGithubToken());
        databaseHelper.close();
    }

    @Override
    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    @Override
    public GithubWrapper getGithubWrapper() {
        return githubWrapper;
    }
}