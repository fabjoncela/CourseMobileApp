package com.example.mymobilefabi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.utils.SessionManager;

/**
 * DashboardFragment - Main dashboard showing overview and quick stats
 */
public class DashboardFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private TextView welcomeText, coursesCountText, assignmentsCountText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        welcomeText = view.findViewById(R.id.welcomeText);
        coursesCountText = view.findViewById(R.id.coursesCountText);
        assignmentsCountText = view.findViewById(R.id.assignmentsCountText);

        // Load user data
        loadDashboardData();
    }

    /**
     * Load dashboard data from database
     */
    private void loadDashboardData() {
        int userId = sessionManager.getUserId();
        String userName = sessionManager.getUserName();

        // Update welcome text
        welcomeText.setText("Welcome, " + userName + "!");

        // Load stats in background thread
        new Thread(() -> {
            int coursesCount = database.courseDao().getCoursesByUserId(userId).size();
            int assignmentsCount = database.assignmentDao().getPendingAssignments(userId).size();

            requireActivity().runOnUiThread(() -> {
                coursesCountText.setText("Total Courses: " + coursesCount);
                assignmentsCountText.setText("Pending Assignments: " + assignmentsCount);
            });
        }).start();
    }
}

