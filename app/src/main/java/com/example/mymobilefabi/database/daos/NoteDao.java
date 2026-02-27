package com.example.mymobilefabi.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymobilefabi.database.entities.Note;

import java.util.List;

/**
 * DAO for Note entity. Handles all database operations for notes.
 */
@Dao
public interface NoteDao {

    /**
     * Insert a new note
     */
    @Insert
    long insertNote(Note note);

    /**
     * Update an existing note
     */
    @Update
    void updateNote(Note note);

    /**
     * Delete a note
     */
    @Delete
    void deleteNote(Note note);

    /**
     * Get note by ID
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);

    /**
     * Get all notes for a user
     */
    @Query("SELECT * FROM notes WHERE user_id = :userId ORDER BY updatedAt DESC")
    List<Note> getNotesByUserId(int userId);

    /**
     * Get all notes for a specific course
     */
    @Query("SELECT * FROM notes WHERE user_id = :userId AND course_id = :courseId ORDER BY updatedAt DESC")
    List<Note> getNotesByCourse(int userId, int courseId);

    /**
     * Search notes by title or content
     */
    @Query("SELECT * FROM notes WHERE user_id = :userId AND (title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%') ORDER BY updatedAt DESC")
    List<Note> searchNotes(int userId, String searchQuery);
}

