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
import androidx.fragment.app.Fragment;
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
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesHolder;
import edu.quinnipiac.ser210.githubchat.preferences.PreferencesWrapper;
import edu.quinnipiac.ser210.githubchat.threads.ThreadManager;
import edu.quinnipiac.ser210.githubchat.ui.fragments.ChatFragment;
import edu.quinnipiac.ser210.githubchat.ui.fragments.ChatInfoFragment;
import edu.quinnipiac.ser210.githubchat.ui.fragments.CreateChatFragment;
import edu.quinnipiac.ser210.githubchat.ui.fragments.HomeFragment;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarAction;
import edu.quinnipiac.ser210.githubchat.ui.toolbar.ToolbarHolder;
import edu.quinnipiac.ser210.githubchat.ui.util.FragmentChangedListener;
import edu.quinnipiac.ser210.githubchat.ui.util.ImageLoader;

/**
 * @author Thomas Kwashnak
 */
public class MainActivity extends AppCompatActivity
        implements FirebaseAuth.AuthStateListener, PreferencesHolder, GithubHolder, DatabaseHolder, NavigationView.OnNavigationItemSelectedListener, ToolbarHolder,
                   GithubWrapper.OnFetchGithubUser, ImageLoader.OnLoadBitmap, FragmentChangedListener {

    private int channelGithubUser, channelUserAvatar;

    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private volatile Toolbar toolbar;

    private GithubUser githubUser;

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
            navController.popBackStack(R.id.homeFragment,false);
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
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Fragment fragment = getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments().get(0);

        if (item.getItemId() == R.id.menu_toolbar_share) {
            ((ToolbarAction.Share) fragment).onShare();
        } else if (item.getItemId() == R.id.menu_toolbar_info) {
            ((ToolbarAction.Info) fragment).onInfo();
        } else if (item.getItemId() == R.id.menu_toolbar_github) {
            ((ToolbarAction.Github) fragment).onGithub();
        }

        return super.onOptionsItemSelected(item);
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
        if (item.getItemId() == R.id.menu_drawer_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out of Github App?")
                    .setIcon(R.drawable.ic_material_logout_48)
                    .setCancelable(true)
                    .setPositiveButton("Logout", (dialog, id) -> FirebaseAuth.getInstance().signOut())
                    .setNegativeButton("Cancel", (dialog, id) -> {})
                    .create()
                    .show();
        } else if (item.getItemId() == R.id.menu_drawer_view_profile) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubUser.getUrl()));
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_drawer_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Look at this cool app! https://github.com/LittleTealeaf/SER-210-Final");
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, null));
        } else if (item.getItemId() == R.id.menu_drawer_info) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments().get(0);
            if(fragment instanceof HomeFragment) {
                navController.navigate(R.id.action_homeFragment_to_settingsFragment);
            } else if(fragment instanceof ChatFragment) {
                navController.navigate(R.id.action_chatFragment_to_settingsFragment);
            } else if(fragment instanceof ChatInfoFragment) {
                navController.navigate(R.id.action_chatInfoFragment_to_settingsFragment);
            } else if(fragment instanceof CreateChatFragment) {
                navController.navigate(R.id.action_createChatFragment_to_settingsFragment);
            }
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
           if(githubUser != null) {
               this.githubUser = githubUser;
               View header = navigationView.getHeaderView(navigationView.getHeaderCount() - 1);
               if (githubUser.getName() != null) {
                   ((TextView) header.findViewById(R.id.drawer_header_text_name)).setText(githubUser.getName());
                   ((TextView) header.findViewById(R.id.drawer_header_text_username)).setText(githubUser.getLogin());
               } else {
                   ((TextView) header.findViewById(R.id.drawer_header_text_name)).setText(githubUser.getLogin());
               }

               channelUserAvatar = ImageLoader.loadImage(githubUser.getAvatarUrl(), this);
           }
        }
    }

    @Override
    public void onLoadBitmap(Bitmap bitmap, int channel) {
        if (channel == channelUserAvatar) {
            (
                    (ImageView) navigationView.getHeaderView(navigationView.getHeaderCount() - 1).findViewById(R.id.drawer_header_imageview_account)
            ).setImageBitmap(bitmap);
        }
    }

    @Override
    public void onFragmentLoaded(Fragment fragment) {
        try {
            Menu menu = toolbar.getMenu();
            menu.findItem(R.id.menu_toolbar_share).setVisible(fragment instanceof ToolbarAction.Share);
            menu.findItem(R.id.menu_toolbar_info).setVisible(fragment instanceof ToolbarAction.Info);
            menu.findItem(R.id.menu_toolbar_github).setVisible(fragment instanceof ToolbarAction.Github);
        } catch (Exception e) {
            ThreadManager.run(() -> {
                while (toolbar == null) ;
                ThreadManager.schedule(() -> onFragmentLoaded(fragment));
            });
        }
    }
}
