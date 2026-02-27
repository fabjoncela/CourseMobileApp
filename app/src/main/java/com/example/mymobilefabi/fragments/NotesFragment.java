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
import com.example.mymobilefabi.adapters.NoteAdapter;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.Course;
import com.example.mymobilefabi.database.entities.Note;
import com.example.mymobilefabi.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * NotesFragment - Manage study notes with search functionality
 */
public class NotesFragment extends Fragment {

    private AppDatabase database;
    private SessionManager sessionManager;
    private RecyclerView notesRecyclerView;
    private NoteAdapter noteAdapter;
    private FloatingActionButton addNoteBtn;
    private EditText searchEditText;
    private List<Note> notesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView);
        addNoteBtn = view.findViewById(R.id.addNoteBtn);
        searchEditText = view.findViewById(R.id.searchEditText);

        // Setup RecyclerView
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter = new NoteAdapter(notesList, this);
        notesRecyclerView.setAdapter(noteAdapter);

        // Load notes
        loadNotes();

        // Add note button
        addNoteBtn.setOnClickListener(v -> showAddNoteDialog());

        // Search functionality
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNotes(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    /**
     * Load all notes for user
     */
    private void loadNotes() {
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            notesList.clear();
            notesList.addAll(database.noteDao().getNotesByUserId(userId));

            requireActivity().runOnUiThread(() -> noteAdapter.notifyDataSetChanged());
        }).start();
    }

    /**
     * Search notes
     */
    private void searchNotes(String query) {
        if (query.isEmpty()) {
            loadNotes();
            return;
        }

        new Thread(() -> {
            int userId = sessionManager.getUserId();
            List<Note> results = database.noteDao().searchNotes(userId, query);

            requireActivity().runOnUiThread(() -> {
                notesList.clear();
                notesList.addAll(results);
                noteAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    /**
     * Show add note dialog
     */
    private void showAddNoteDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_note, null);

        EditText titleEt = dialogView.findViewById(R.id.noteTitleEt);
        EditText contentEt = dialogView.findViewById(R.id.noteContentEt);
        Spinner courseSpinner = dialogView.findViewById(R.id.noteCourseSpinner);

        // Load courses for spinner in background
        new Thread(() -> {
            int userId = sessionManager.getUserId();
            List<Course> courses = database.courseDao().getCoursesByUserId(userId);

            requireActivity().runOnUiThread(() -> {
                // Create display list with "None" option first, then course names
                List<String> courseNames = new ArrayList<>();
                courseNames.add("None (General Note)");

                for (Course course : courses) {
                    courseNames.add(course.getName() + " (" + course.getCode() + ")");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, courseNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setAdapter(adapter);

                // Show the dialog after courses are loaded
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add Note")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, whichButton) -> {
                        String title = titleEt.getText().toString().trim();
                        String content = contentEt.getText().toString().trim();
                        int selectedPosition = courseSpinner.getSelectedItemPosition();

                        if (title.isEmpty() || content.isEmpty()) {
                            Toast.makeText(requireContext(), "Title and content required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // If position 0 is selected (None), use courseId = 0
                        // Otherwise, get the course ID from the selected position (subtract 1 for "None" offset)
                        int courseId = 0;
                        if (selectedPosition > 0 && selectedPosition <= courses.size()) {
                            courseId = courses.get(selectedPosition - 1).getId();
                        }

                        int finalCourseId = courseId;
                        new Thread(() -> {
                            Note newNote = new Note(userId, finalCourseId, title, content);
                            database.noteDao().insertNote(newNote);
                            requireActivity().runOnUiThread(this::loadNotes);
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            });
        }).start();
    }

    /**
     * Delete note
     */
    public void deleteNote(Note note) {
        new Thread(() -> {
            database.noteDao().deleteNote(note);
            requireActivity().runOnUiThread(this::loadNotes);
        }).start();
    }
}

