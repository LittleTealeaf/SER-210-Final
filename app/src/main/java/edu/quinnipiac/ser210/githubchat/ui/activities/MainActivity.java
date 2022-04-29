package edu.quinnipiac.ser210.githubchat.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.database.DatabaseHolder;
import edu.quinnipiac.ser210.githubchat.database.DatabaseWrapper;
import edu.quinnipiac.ser210.githubchat.github.GithubHolder;
import edu.quinnipiac.ser210.githubchat.github.GithubWrapper;
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesHolder;
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesWrapper;

/**
 * @author Thomas Kwashnak
 */
public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, PreferencesHolder, GithubHolder, DatabaseHolder {

    private FirebaseAuth firebaseAuth;
    private PreferencesWrapper preferencesWrapper;
    private DatabaseWrapper databaseWrapper;
    private GithubWrapper githubWrapper;

    private ActivityResultLauncher<Intent> loginLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onLoginActivityResult);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(this);
    }

    private void onLoginActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            getPreferencesWrapper().setString(GithubWrapper.AUTH_TOKEN, result.getData().getStringExtra(GithubWrapper.AUTH_TOKEN));
        }
    }

    @Override
    public PreferencesWrapper getPreferencesWrapper() {
        if (preferencesWrapper == null) {
            return preferencesWrapper = new PreferencesWrapper(this);
        } else {
            return preferencesWrapper;
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            loginLauncher.launch(new Intent(this, LoginActivity.class));
        } else {
            getGithubWrapper().setToken(getPreferencesWrapper().getString(GithubWrapper.AUTH_TOKEN, null));
        }
    }

    @Override
    public GithubWrapper getGithubWrapper() {
        if (githubWrapper == null) {
            return githubWrapper = new GithubWrapper(this);
        } else {
            return githubWrapper;
        }
    }

    @Override
    public DatabaseWrapper getDatabaseWrapper() {
        if (databaseWrapper == null) {
            return databaseWrapper = new DatabaseWrapper(this);
        } else {
            return databaseWrapper;
        }
    }
}
