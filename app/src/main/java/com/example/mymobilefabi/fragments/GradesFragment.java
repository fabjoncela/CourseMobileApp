package com.example.mymobilefabi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.adapters.GradeAdapter;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.database.entities.Grade;
import com.example.mymobilefabi.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * GradesFragment - Track grades and calculate GPA
 */
public class GradesFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private RecyclerView gradesRecyclerView;
    private GradeAdapter gradeAdapter;
    private TextView averageGradeText, gpaText;
    private List<Grade> gradesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        gradesRecyclerView = view.findViewById(R.id.gradesRecyclerView);
        averageGradeText = view.findViewById(R.id.averageGradeText);
        gpaText = view.findViewById(R.id.gpaText);

        // Setup RecyclerView
        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        gradeAdapter = new GradeAdapter(gradesList, database);
        gradesRecyclerView.setAdapter(gradeAdapter);

        // Load grades
        loadGrades();
    }

    /**
     * Load grades from database
     */
    private void loadGrades() {
        new Thread(() -> {
            int userId = sessionManager.getUserId();

            // Get all assignments for user
            List<Assignment> assignments = database.assignmentDao().getPendingAssignments(userId);
            assignments.addAll(database.assignmentDao().getCompletedAssignments(userId));

            gradesList.clear();
            double totalScore = 0;
            int gradeCount = 0;

            for (Assignment assignment : assignments) {
                Grade grade = database.gradeDao().getGradeByAssignment(assignment.getId());
                if (grade != null) {
                    gradesList.add(grade);
                    totalScore += grade.getPercentage();
                    gradeCount++;
                }
            }

            double averageGrade = gradeCount > 0 ? totalScore / gradeCount : 0;
            double gpa = calculateGPA(averageGrade);

            requireActivity().runOnUiThread(() -> {
                gradeAdapter.notifyDataSetChanged();
                averageGradeText.setText(String.format("Average: %.2f%%", averageGrade));
                gpaText.setText(String.format("GPA: %.2f", gpa));
            });
        }).start();
    }

    /**
     * Calculate GPA from average grade (Simple conversion: Average% / 20 = GPA out of 4.0)
     */
    private double calculateGPA(double averagePercentage) {
        return (averagePercentage / 25.0); // Scale 0-100 to 0-4.0
    }
}

