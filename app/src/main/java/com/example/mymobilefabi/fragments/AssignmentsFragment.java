package com.example.mymobilefabi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.adapters.AssignmentAdapter;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.database.entities.Course;
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
        Spinner courseSpinner = dialogView.findViewById(R.id.assignmentCourseSpinner);
        EditText priorityEt = dialogView.findViewById(R.id.assignmentPriorityEt);

        // Load courses for spinner in background
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            List<Course> courses = database.courseDao().getCoursesByUserId(userId);

            requireActivity().runOnUiThread(() -> {
                if (courses.isEmpty()) {
                    Toast.makeText(requireContext(), "Please create a course first", Toast.LENGTH_LONG).show();
                    return;
                }

                // Create display list with course names
                List<String> courseNames = new ArrayList<>();
                for (Course course : courses) {
                    courseNames.add(course.getName() + " (" + course.getCode() + ")");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, courseNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setAdapter(adapter);

                // Show the dialog after courses are loaded
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add Assignment")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, whichButton) -> {
                        String title = titleEt.getText().toString().trim();
                        String desc = descEt.getText().toString().trim();
                        String priority = priorityEt.getText().toString().trim();
                        int selectedPosition = courseSpinner.getSelectedItemPosition();

                        if (title.isEmpty() || priority.isEmpty()) {
                            Toast.makeText(requireContext(), "Title and priority required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (selectedPosition < 0 || selectedPosition >= courses.size()) {
                            Toast.makeText(requireContext(), "Please select a course", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get the actual course ID from the selected position
                        int courseId = courses.get(selectedPosition).getId();
                        long dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // 7 days from now

                        new Thread(() -> {
                            Assignment newAssignment = new Assignment(courseId, title, desc, dueDate, priority, "pending");
                            database.assignmentDao().insertAssignment(newAssignment);
                            requireActivity().runOnUiThread(this::loadAssignments);
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
        }).start();
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

