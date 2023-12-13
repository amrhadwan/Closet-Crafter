package com.example.signuploginfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHandler extends AppCompatActivity{
    private FragmentManager activity;
    private int containerId;

    public NavigationHandler(FragmentManager activity, int containerId) {
        this.activity = activity;
        this.containerId = containerId;
    }

    public void attachNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    Fragment fragment = null;

                    if (item.getItemId() == R.id.navigation_home) {
                        fragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.navigation_add_item) {
                        fragment = new AddItemFragment();
                    } else if (item.getItemId() == R.id.navigation_view_outfits) {
                        fragment = new ViewOutfitsFragment();
                    }

                    if (fragment != null) {
                        loadFragment(fragment);
                        return true;
                    }

                    return false;
                }
        );
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = activity.getFragment(null,null).getChildFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}

