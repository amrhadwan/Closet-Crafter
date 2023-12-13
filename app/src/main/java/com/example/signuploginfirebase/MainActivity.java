package com.example.signuploginfirebase;
import java.lang.String;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;
    NavigationHandler navigationHandler;
    BottomNavigationView bottomNavigationView; // Make sure this is declared

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        logout = findViewById(R.id.logout);
        userName = findViewById(R.id.userName);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Initialize bottomNavigationView

        // Initialize Google Sign In`
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        // Check if user is signed in
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            // User is signed in
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
            showBottomNavigation(); // Show bottom navigation after login
        }

        // Initialize NavigationHandler
        navigationHandler = new NavigationHandler(getSupportFragmentManager(), R.id.fragment_container);

        // Set up bottom navigation
        navigationHandler.attachNavigation(bottomNavigationView);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle logout
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Redirect to login page after logout
                        finish();
                        // startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        });
    }

    // Method to show the bottom navigation
    private void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
