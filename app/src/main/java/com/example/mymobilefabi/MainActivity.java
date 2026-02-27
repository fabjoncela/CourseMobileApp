package com.example.mymobilefabi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mymobilefabi.activities.LoginActivity;
import com.example.mymobilefabi.fragments.DashboardFragment;
import com.example.mymobilefabi.fragments.CoursesFragment;
import com.example.mymobilefabi.fragments.AssignmentsFragment;
import com.example.mymobilefabi.fragments.GradesFragment;
import com.example.mymobilefabi.fragments.NotesFragment;
import com.example.mymobilefabi.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity - Main dashboard with bottom navigation
 * Hosts fragments for different modules of the app
 */
public class MainActivity extends AppCompatActivity {

    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNav;
    private SessionManager sessionManager;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        // Initialize views
        fragmentContainer = findViewById(R.id.fragmentContainer);
        bottomNav = findViewById(R.id.bottomNavigation);

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }

        // Set bottom navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_courses) {
                selectedFragment = new CoursesFragment();
            } else if (itemId == R.id.nav_assignments) {
                selectedFragment = new AssignmentsFragment();
            } else if (itemId == R.id.nav_grades) {
                selectedFragment = new GradesFragment();
            } else if (itemId == R.id.nav_notes) {
                selectedFragment = new NotesFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    /**
     * Load a fragment into the container
     */
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            currentFragment = fragment;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainer, fragment);
            ft.commit();
        }
    }

    /**
     * Navigate back to login
     */
    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * Handle logout
     */
    public void logout() {
        sessionManager.logout();
        navigateToLogin();
    }
}

