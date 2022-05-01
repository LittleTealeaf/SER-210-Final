package edu.quinnipiac.ser210.githubchat.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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
import edu.quinnipiac.ser210.githubchat.github.dataobjects.GithubUser;
import edu.quinnipiac.ser210.githubchat.github.listeners.OnFetchGithubUser;
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesHolder;
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesWrapper;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.adapters.interfaces.ToolbarHolder;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;
import edu.quinnipiac.ser210.githubchat.ui.util.OnImageLoaded;

/**
 * @author Thomas Kwashnak
 */
public class MainActivity extends AppCompatActivity
        implements FirebaseAuth.AuthStateListener, PreferencesHolder, GithubHolder, DatabaseHolder, NavigationView.OnNavigationItemSelectedListener, ToolbarHolder, OnFetchGithubUser, OnImageLoaded {

    private int channelGithubUser, channelUserAvatar;

    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private GithubUser githubUser;

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

//        toolbar.setTitle("Github App");

        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onLoginActivityResult);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(this);
        channelGithubUser = ThreadManager.NULL_CHANNEL;
        channelUserAvatar = ThreadManager.NULL_CHANNEL;
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
            channelGithubUser = getGithubWrapper().startFetchGithubUser(null, this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out of Github App?")
                    .setIcon(R.drawable.ic_material_logout_48)
                    .setCancelable(true)
                    .setPositiveButton("Logout", (dialog, id) -> FirebaseAuth.getInstance().signOut())
                    .setNegativeButton("Cancel", (dialog, id) -> {})
                    .create()
                    .show();
        } else if(item.getItemId() == R.id.menu_item_view_profile) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUser.getUrl()));
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onFetchGithubUser(GithubUser githubUser, int channel) {
        if (channel == channelGithubUser) {
            this.githubUser = githubUser;
            View header = navigationView.getHeaderView(navigationView.getHeaderCount() - 1);
            if (!githubUser.getName().equals("null")) {
                ((TextView) header.findViewById(R.id.drawer_header_text_name)).setText(githubUser.getName());
                ((TextView) header.findViewById(R.id.drawer_header_text_username)).setText(githubUser.getLogin());
            } else {
                ((TextView) header.findViewById(R.id.drawer_header_text_name)).setText(githubUser.getLogin());
            }

            channelUserAvatar = ImageLoader.loadImage(githubUser.getAvatarUrl(), this);
        }
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, int channel) {
        if (channel == channelUserAvatar) {
            ((ImageView) navigationView.getHeaderView(navigationView.getHeaderCount() - 1).findViewById(R.id.drawer_header_imageview_account)).setImageBitmap(bitmap);
        }
    }
}
