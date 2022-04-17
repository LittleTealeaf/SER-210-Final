package edu.quinnipiac.ser210.githubchat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import edu.quinnipiac.ser210.githubchat.R;

public class MainActivity extends AppCompatActivity {

    /*
    https://firebase.google.com/docs/auth/android/github-auth
     */

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuth.useEmulator("10.0.2.2",9099);
    }
}