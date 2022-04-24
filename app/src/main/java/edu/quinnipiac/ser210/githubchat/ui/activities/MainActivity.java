package edu.quinnipiac.ser210.githubchat.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseHelper;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesWrapper;

/*
Current plan of action:

- The github token is stored in an object. Upon creation / recreation, it will use the Github Cache / api to pull data
- (Async task to get this data should be awaited at all parts of the journey, either that or like listeners and stuff)
- The user's github username is appended to each message. Then, when pulling data, each "visible" chat message has async tasks that fetches that
data to load (ex: getUserAvatar, getUserName...)
- So first we gotta make the database and everything


 */

public class MainActivity extends AppCompatActivity
        implements DatabaseHelper.Holder, GithubWrapper.Holder, PreferencesWrapper.Holder, FirebaseAuth.AuthStateListener {

    private DatabaseHelper databaseHelper;
    private GithubWrapper githubWrapper;
    private PreferencesWrapper preferencesWrapper;

    private ActivityResultLauncher<Intent> loginLauncher;

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onLoginActivityResult);

        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    private void onLoginActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            assert result.getData() != null;
            getGithubWrapper().setGithubToken(result.getData().getStringExtra(GithubWrapper.TOKEN));
            getPreferencesWrapper().setString(GithubWrapper.TOKEN, getGithubWrapper().getGithubToken());
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(databaseHelper != null) {
            databaseHelper.close();
        }
    }

    @Override
    public DatabaseHelper getDatabaseHelper() {
        if(databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }

    @Override
    public GithubWrapper getGithubWrapper() {
        if (githubWrapper == null) {
            githubWrapper = new GithubWrapper(getDatabaseHelper(), getPreferencesWrapper().getString(GithubWrapper.TOKEN, null));
        }
        return githubWrapper;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            preferencesWrapper.edit().remove(GithubWrapper.TOKEN).apply();
            loginLauncher.launch(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public PreferencesWrapper getPreferencesWrapper() {
        if(preferencesWrapper == null) {
            preferencesWrapper = new PreferencesWrapper(this);
        }
        return preferencesWrapper;
    }


}