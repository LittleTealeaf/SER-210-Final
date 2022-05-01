package edu.quinnipiac.ser210.githubchat.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

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
public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, PreferencesHolder, GithubHolder, DatabaseHolder, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private PreferencesWrapper preferencesWrapper;
    private DatabaseWrapper databaseWrapper;
    private GithubWrapper githubWrapper;

    private ActivityResultLauncher<Intent> loginLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        drawerLayout = findViewById(R.id.activity_main_layout);
//        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
//        navigationView = findViewById(R.id.navigation_view);
//        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
//        NavigationUI.setupWithNavController(navigationView,navController);
//        navigationView.setNavigationItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        drawerLayout = findViewById(R.id.activity_main_layout);
        navigationView = findViewById(R.id.navigation_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);


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

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,drawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
