package com.example.signuploginfirebase;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
   private NavigationHandler navigationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // Initialize NavigationHandler with the ID of the fragment container
        navigationHandler = new NavigationHandler(getSupportFragmentManager(), R.id.fragmentContainer);
        navigationHandler.attachNavigation(bottomNavigationView);

        // Connect the navigation bar to the fragments
    }
}
