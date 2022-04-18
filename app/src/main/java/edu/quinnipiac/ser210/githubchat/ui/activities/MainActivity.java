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

public class MainActivity extends AppCompatActivity {

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);


//        FirebaseAuth.getInstance().useEmulator("10.0.2.2",9099);
        FirebaseAuth.getInstance().useAppLanguage();
//        FirebaseAuth.getInstance().useEmulator("10.0.2.2",9099);


//        mAuth.useEmulator("10.0.2.2",9099);
    }
}