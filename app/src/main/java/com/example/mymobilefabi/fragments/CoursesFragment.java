package com.example.mymobilefabi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymobilefabi.R;
import com.example.mymobilefabi.adapters.CourseAdapter;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Course;
import com.example.mymobilefabi.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * CoursesFragment - Manage courses with CRUD operations
 */
public class CoursesFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private RecyclerView coursesRecyclerView;
    private CourseAdapter courseAdapter;
    private FloatingActionButton addCourseBtn;
    private EditText searchEditText;
    private List<Course> coursesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        addCourseBtn = view.findViewById(R.id.addCourseBtn);
        searchEditText = view.findViewById(R.id.searchEditText);

        // Setup RecyclerView
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        courseAdapter = new CourseAdapter(coursesList, this);
        coursesRecyclerView.setAdapter(courseAdapter);

        // Load courses
        loadCourses();

        // Add course button click
        addCourseBtn.setOnClickListener(v -> showAddCourseDialog());

        // Search functionality
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCourses(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    /**
     * Load all courses from database
     */
    private void loadCourses() {
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            coursesList.clear();
            coursesList.addAll(database.courseDao().getCoursesByUserId(userId));

            requireActivity().runOnUiThread(() -> courseAdapter.notifyDataSetChanged());
        }).start();
    }

    /**
     * Search courses by name
     */
    private void searchCourses(String query) {
        if (query.isEmpty()) {
            loadCourses();
            return;
        }

        new Thread(() -> {
            int userId = sessionManager.getUserId();
            List<Course> results = database.courseDao().searchCourses(userId, query);

            requireActivity().runOnUiThread(() -> {
                coursesList.clear();
                coursesList.addAll(results);
                courseAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    /**
     * Show dialog to add new course
     */
    private void showAddCourseDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_course, null);

        EditText nameEt = dialogView.findViewById(R.id.courseNameEt);
        EditText codeEt = dialogView.findViewById(R.id.courseCodeEt);
        EditText teacherEt = dialogView.findViewById(R.id.courseTeacherEt);
        EditText semesterEt = dialogView.findViewById(R.id.courseSemesterEt);
        EditText creditsEt = dialogView.findViewById(R.id.courseCreditsEt);

        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Course")
            .setView(dialogView)
            .setPositiveButton("Add", (dialog, which) -> {
                String name = nameEt.getText().toString().trim();
                String code = codeEt.getText().toString().trim();
                String teacher = teacherEt.getText().toString().trim();
                String semester = semesterEt.getText().toString().trim();
                String creditsStr = creditsEt.getText().toString().trim();

                if (name.isEmpty() || code.isEmpty() || teacher.isEmpty() || semester.isEmpty() || creditsStr.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                int credits = Integer.parseInt(creditsStr);
                int userId = sessionManager.getUserId();

                // Validate user ID
                if (userId <= 0) {
                    Toast.makeText(requireContext(), "User not logged in. Please log in again.", Toast.LENGTH_LONG).show();
                    return;
                }

                new Thread(() -> {
                    try {
                        Course newCourse = new Course(userId, name, code, teacher, semester, credits);
                        database.courseDao().insertCourse(newCourse);
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Course added successfully", Toast.LENGTH_SHORT).show();
                            loadCourses();
                        });
                    } catch (Exception e) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Error adding course: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                }).start();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    /**
     * Delete a course
     */
    public void deleteCourse(Course course) {
        new Thread(() -> {
            database.courseDao().deleteCourse(course);
            requireActivity().runOnUiThread(this::loadCourses);
        }).start();
    }
}

