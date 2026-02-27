package com.example.mymobilefabi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.adapters.AssignmentAdapter;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * AssignmentsFragment - Manage assignments and exams with CRUD operations
 */
public class AssignmentsFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private RecyclerView assignmentsRecyclerView;
    private AssignmentAdapter assignmentAdapter;
    private FloatingActionButton addAssignmentBtn;
    private EditText searchEditText;
    private List<Assignment> assignmentsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assignments, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        assignmentsRecyclerView = view.findViewById(R.id.assignmentsRecyclerView);
        addAssignmentBtn = view.findViewById(R.id.addAssignmentBtn);
        searchEditText = view.findViewById(R.id.searchEditText);

        // Setup RecyclerView
        assignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        assignmentAdapter = new AssignmentAdapter(assignmentsList, this, database);
        assignmentsRecyclerView.setAdapter(assignmentAdapter);

        // Load assignments
        loadAssignments();

        // Add assignment button
        addAssignmentBtn.setOnClickListener(v -> showAddAssignmentDialog());

        // Search functionality
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAssignments(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    /**
     * Load pending assignments
     */
    private void loadAssignments() {
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            assignmentsList.clear();
            assignmentsList.addAll(database.assignmentDao().getPendingAssignments(userId));

            requireActivity().runOnUiThread(() -> assignmentAdapter.notifyDataSetChanged());
        }).start();
    }

    /**
     * Search assignments by title
     */
    private void searchAssignments(String query) {
        if (query.isEmpty()) {
            loadAssignments();
            return;
        }

        new Thread(() -> {
            int userId = sessionManager.getUserId();
            List<Assignment> results = database.assignmentDao().searchAssignments(userId, query);

            requireActivity().runOnUiThread(() -> {
                assignmentsList.clear();
                assignmentsList.addAll(results);
                assignmentAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    /**
     * Show dialog to add assignment
     */
    private void showAddAssignmentDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_assignment, null);

        EditText titleEt = dialogView.findViewById(R.id.assignmentTitleEt);
        EditText descEt = dialogView.findViewById(R.id.assignmentDescEt);
        EditText courseIdEt = dialogView.findViewById(R.id.assignmentCourseIdEt);
        EditText priorityEt = dialogView.findViewById(R.id.assignmentPriorityEt);

        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Assignment")
            .setView(dialogView)
            .setPositiveButton("Add", (dialog, which) -> {
                String title = titleEt.getText().toString().trim();
                String desc = descEt.getText().toString().trim();
                String courseIdStr = courseIdEt.getText().toString().trim();
                String priority = priorityEt.getText().toString().trim();

                if (title.isEmpty() || courseIdStr.isEmpty() || priority.isEmpty()) {
                    Toast.makeText(requireContext(), "Required fields missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                int courseId = Integer.parseInt(courseIdStr);
                long dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // 7 days from now

                new Thread(() -> {
                    Assignment newAssignment = new Assignment(courseId, title, desc, dueDate, priority, "pending");
                    database.assignmentDao().insertAssignment(newAssignment);
                    requireActivity().runOnUiThread(this::loadAssignments);
                }).start();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    /**
     * Delete assignment
     */
    public void deleteAssignment(Assignment assignment) {
        new Thread(() -> {
            database.assignmentDao().deleteAssignment(assignment);
            requireActivity().runOnUiThread(this::loadAssignments);
        }).start();
    }
}

