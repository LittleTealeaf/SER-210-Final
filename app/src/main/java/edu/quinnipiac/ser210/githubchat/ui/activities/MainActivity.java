package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.ui.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener {

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);



//        mAuth.useEmulator("10.0.2.2",9099);
    }

    @Override
    public void onLogin() {

    }
}