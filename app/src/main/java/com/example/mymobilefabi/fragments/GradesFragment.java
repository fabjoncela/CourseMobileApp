package com.example.mymobilefabi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.adapters.GradeAdapter;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.database.entities.Grade;
import com.example.mymobilefabi.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    private FloatingActionButton addGradeBtn;
    private List<Grade> gradesList = new ArrayList<>();
    private Thread currentLoadThread;

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
        addGradeBtn = view.findViewById(R.id.addGradeBtn);

        // Setup RecyclerView
        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        gradeAdapter = new GradeAdapter(gradesList, database);
        gradesRecyclerView.setAdapter(gradeAdapter);

        // Load grades
        loadGrades();

        // Add grade button
        addGradeBtn.setOnClickListener(v -> showAddGradeDialog());
    }

    /**
     * Load grades from database with thread safety
     */
    private void loadGrades() {
        // Cancel any previous loading thread
        if (currentLoadThread != null && currentLoadThread.isAlive()) {
            currentLoadThread.interrupt();
        }

        currentLoadThread = new Thread(() -> {
            try {
                int userId = sessionManager.getUserId();

                // Get all assignments for user (both pending and completed)
                List<Assignment> assignments = database.assignmentDao().getPendingAssignments(userId);
                assignments.addAll(database.assignmentDao().getCompletedAssignments(userId));

                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                final List<Grade> newGradesList = new ArrayList<>();
                double totalScore = 0;
                int gradeCount = 0;

                // Collect grades for all assignments
                for (Assignment assignment : assignments) {
                    Grade grade = database.gradeDao().getGradeByAssignment(assignment.getId());
                    if (grade != null) {
                        newGradesList.add(grade);
                        totalScore += grade.getPercentage();
                        gradeCount++;
                    }
                }

                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                double averageGrade = gradeCount > 0 ? totalScore / gradeCount : 0;
                double gpa = calculateGPA(averageGrade);

                requireActivity().runOnUiThread(() -> {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    synchronized (gradesList) {
                        gradesList.clear();
                        gradesList.addAll(newGradesList);
                    }
                    gradeAdapter.notifyDataSetChanged();
                    averageGradeText.setText(String.format("Average: %.2f%%", averageGrade));
                    gpaText.setText(String.format("GPA: %.2f", gpa));
                });
            } catch (Exception e) {
                if (!Thread.currentThread().isInterrupted()) {
                    e.printStackTrace();
                }
            }
        });

        currentLoadThread.start();
    }

    /**
     * Show dialog to add a grade
     */
    private void showAddGradeDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_grade, null);

        Spinner assignmentSpinner = dialogView.findViewById(R.id.gradeAssignmentSpinner);
        EditText scoreEt = dialogView.findViewById(R.id.gradeScoreEt);
        EditText maxScoreEt = dialogView.findViewById(R.id.gradeMaxScoreEt);

        // Load assignments without grades yet
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            List<Assignment> allAssignments = database.assignmentDao().getPendingAssignments(userId);
            allAssignments.addAll(database.assignmentDao().getCompletedAssignments(userId));

            // Filter assignments that don't have grades yet
            List<Assignment> assignmentsWithoutGrades = new ArrayList<>();
            for (Assignment assignment : allAssignments) {
                Grade existingGrade = database.gradeDao().getGradeByAssignment(assignment.getId());
                if (existingGrade == null) {
                    assignmentsWithoutGrades.add(assignment);
                }
            }

            final List<Assignment> finalAssignmentsList = assignmentsWithoutGrades;

            requireActivity().runOnUiThread(() -> {
                if (finalAssignmentsList.isEmpty()) {
                    Toast.makeText(requireContext(), "All assignments already have grades or no assignments exist", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create spinner adapter with assignment names
                List<String> assignmentNames = new ArrayList<>();
                for (Assignment assignment : finalAssignmentsList) {
                    assignmentNames.add(assignment.getTitle());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, assignmentNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                assignmentSpinner.setAdapter(adapter);

                // Show dialog
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add Grade")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String scoreStr = scoreEt.getText().toString().trim();
                        String maxScoreStr = maxScoreEt.getText().toString().trim();
                        int selectedPosition = assignmentSpinner.getSelectedItemPosition();

                        if (scoreStr.isEmpty() || maxScoreStr.isEmpty()) {
                            Toast.makeText(requireContext(), "Score and max score are required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            double score = Double.parseDouble(scoreStr);
                            double maxScore = Double.parseDouble(maxScoreStr);

                            if (score < 0 || maxScore <= 0 || score > maxScore) {
                                Toast.makeText(requireContext(), "Invalid score values", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (selectedPosition < 0 || selectedPosition >= finalAssignmentsList.size()) {
                                Toast.makeText(requireContext(), "Please select an assignment", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Get selected assignment
                            Assignment selectedAssignment = finalAssignmentsList.get(selectedPosition);

                            // Save grade to database
                            new Thread(() -> {
                                Grade newGrade = new Grade(selectedAssignment.getId(), score, maxScore);
                                database.gradeDao().insertGrade(newGrade);
                                requireActivity().runOnUiThread(this::loadGrades);
                            }).start();

                        } catch (NumberFormatException e) {
                            Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
        }).start();
    }

    /**
     * Calculate GPA from average grade (Simple conversion: Average% / 25 = GPA out of 4.0)
     */
    private double calculateGPA(double averagePercentage) {
        return (averagePercentage / 25.0); // Scale 0-100 to 0-4.0
    }
}

