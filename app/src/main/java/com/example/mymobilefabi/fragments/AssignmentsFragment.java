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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * AssignmentsFragment - Manage assignments with tabs for Pending, Completed, and Deleted
 */
public class AssignmentsFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private RecyclerView assignmentsRecyclerView;
    private AssignmentAdapter assignmentAdapter;
    private FloatingActionButton addAssignmentBtn;
    private EditText searchEditText;
    private TabLayout tabLayout;
    private List<Assignment> assignmentsList = new ArrayList<>();
    private String currentStatus = "pending"; // Default tab
    private Thread currentLoadThread; // Track current loading thread to cancel duplicates

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
        tabLayout = view.findViewById(R.id.assignmentTabLayout);
        assignmentsRecyclerView = view.findViewById(R.id.assignmentsRecyclerView);
        addAssignmentBtn = view.findViewById(R.id.addAssignmentBtn);
        searchEditText = view.findViewById(R.id.searchEditText);

        // Setup RecyclerView
        assignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        assignmentAdapter = new AssignmentAdapter(assignmentsList, this, database);
        assignmentsRecyclerView.setAdapter(assignmentAdapter);

        // Setup Tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0: // Pending
                        currentStatus = "pending";
                        break;
                    case 1: // Completed
                        currentStatus = "completed";
                        break;
                    case 2: // Deleted
                        currentStatus = "deleted";
                        break;
                }
                searchEditText.setText(""); // Clear search
                loadAssignmentsByStatus();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Load initial assignments
        loadAssignmentsByStatus();

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
     * Load assignments based on current status/tab
     * Properly handles thread synchronization to prevent duplicates when switching tabs quickly
     */
    private void loadAssignmentsByStatus() {
        // Cancel any previous loading thread
        if (currentLoadThread != null && currentLoadThread.isAlive()) {
            currentLoadThread.interrupt();
        }

        currentLoadThread = new Thread(() -> {
            try {
                int userId = sessionManager.getUserId();
                final List<Assignment> newList = database.assignmentDao().getAssignmentsByStatus(userId, currentStatus);

                if (Thread.currentThread().isInterrupted()) {
                    return; // Cancel if thread was interrupted
                }

                requireActivity().runOnUiThread(() -> {
                    // Verify we're still on the same status (status might have changed again)
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    synchronized (assignmentsList) {
                        assignmentsList.clear();
                        assignmentsList.addAll(newList);
                    }
                    assignmentAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                // Handle interruption or other errors gracefully
                if (!Thread.currentThread().isInterrupted()) {
                    e.printStackTrace();
                }
            }
        });

        currentLoadThread.start();
    }

    /**
     * Search assignments by title within current status
     */
    private void searchAssignments(String query) {
        if (query.isEmpty()) {
            loadAssignmentsByStatus();
            return;
        }

        // Cancel any previous loading thread
        if (currentLoadThread != null && currentLoadThread.isAlive()) {
            currentLoadThread.interrupt();
        }

        currentLoadThread = new Thread(() -> {
            try {
                int userId = sessionManager.getUserId();
                final List<Assignment> results = database.assignmentDao().searchAssignments(userId, currentStatus, query);

                if (Thread.currentThread().isInterrupted()) {
                    return; // Cancel if thread was interrupted
                }

                requireActivity().runOnUiThread(() -> {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }

                    synchronized (assignmentsList) {
                        assignmentsList.clear();
                        assignmentsList.addAll(results);
                    }
                    assignmentAdapter.notifyDataSetChanged();
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
     * Show dialog to add assignment
     */
    private void showAddAssignmentDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_assignment, null);

        EditText titleEt = dialogView.findViewById(R.id.assignmentTitleEt);
        EditText descEt = dialogView.findViewById(R.id.assignmentDescEt);
        Spinner courseSpinner = dialogView.findViewById(R.id.assignmentCourseSpinner);
        Spinner prioritySpinner = dialogView.findViewById(R.id.assignmentPrioritySpinner);

        // Setup priority spinner with fixed options
        List<String> priorityOptions = new ArrayList<>();
        priorityOptions.add("High");
        priorityOptions.add("Medium");
        priorityOptions.add("Low");

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, priorityOptions);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

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
                        String priority = prioritySpinner.getSelectedItem().toString().toLowerCase(); // Get from spinner
                        int selectedCoursePosition = courseSpinner.getSelectedItemPosition();

                        if (title.isEmpty()) {
                            Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (selectedCoursePosition < 0 || selectedCoursePosition >= courses.size()) {
                            Toast.makeText(requireContext(), "Please select a course", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get the actual course ID from the selected position
                        int courseId = courses.get(selectedCoursePosition).getId();
                        long dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000); // 7 days from now

                        new Thread(() -> {
                            Assignment newAssignment = new Assignment(courseId, title, desc, dueDate, priority, "pending");
                            database.assignmentDao().insertAssignment(newAssignment);
                            requireActivity().runOnUiThread(this::loadAssignmentsByStatus);
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
        }).start();
    }

    // ...existing code...

    /**
     * Soft delete assignment (mark as deleted)
     */
    public void deleteAssignment(Assignment assignment) {
        new Thread(() -> {
            assignment.setStatus("deleted");
            database.assignmentDao().updateAssignment(assignment);
            requireActivity().runOnUiThread(this::loadAssignmentsByStatus);
        }).start();
    }

    /**
     * Restore deleted assignment
     */
    public void restoreAssignment(Assignment assignment) {
        new Thread(() -> {
            assignment.setStatus("pending");
            database.assignmentDao().updateAssignment(assignment);
            requireActivity().runOnUiThread(this::loadAssignmentsByStatus);
        }).start();
    }

    /**
     * Permanently delete assignment
     */
    public void permanentlyDeleteAssignment(Assignment assignment) {
        new Thread(() -> {
            database.assignmentDao().deleteAssignment(assignment);
            requireActivity().runOnUiThread(this::loadAssignmentsByStatus);
        }).start();
    }
}

