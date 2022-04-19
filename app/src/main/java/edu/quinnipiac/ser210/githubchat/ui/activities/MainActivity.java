package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import java.util.Objects;

import edu.quinnipiac.ser210.githubchat.R;
import edu.quinnipiac.ser210.githubchat.ui.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener {

    private String githubAPIToken;

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
    public void onLogin(AuthResult authResult) {
        githubAPIToken = ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken();
        navController.navigate(R.id.action_loginFragment_to_debugChatFragment);
    }
}