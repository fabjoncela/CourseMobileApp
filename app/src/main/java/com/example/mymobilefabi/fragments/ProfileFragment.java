package com.example.mymobilefabi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.activities.LoginActivity;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.User;
import com.example.mymobilefabi.utils.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * ProfileFragment - User profile and settings
 * Displays user information and provides logout functionality
 */
public class ProfileFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private TextView userNameText, userEmailText, studentIdText, userInfoText;
    private Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        userNameText = view.findViewById(R.id.userNameText);
        userEmailText = view.findViewById(R.id.userEmailText);
        studentIdText = view.findViewById(R.id.studentIdText);
        userInfoText = view.findViewById(R.id.userInfoText);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Load user information
        loadUserInfo();

        // Set logout button listener
        logoutButton.setOnClickListener(v -> showLogoutConfirmation());
    }

    /**
     * Load user information from database and display
     */
    private void loadUserInfo() {
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            User user = database.userDao().getUserById(userId);

            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    userNameText.setText("Name: " + user.getName());
                    userEmailText.setText("Email: " + user.getEmail());
                    studentIdText.setText("Student ID: " + user.getStudentId());
                    userInfoText.setText("Account created: " + formatDate(user.getCreatedAt()));
                });
            }
        }).start();
    }

    /**
     * Show logout confirmation dialog
     */
    private void showLogoutConfirmation() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout", (dialog, which) -> {
                logout();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    /**
     * Handle logout - clear session and navigate to login
     */
    private void logout() {
        // Clear session data
        sessionManager.logout();

        // Navigate to login activity
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Format date timestamp to readable format
     */
    private String formatDate(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(new java.util.Date(timestamp));
    }
}


