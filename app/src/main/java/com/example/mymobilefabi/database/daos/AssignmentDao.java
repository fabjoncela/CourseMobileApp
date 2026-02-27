package com.example.mymobilefabi.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymobilefabi.database.entities.Assignment;

import java.util.List;

/**
 * DAO for Assignment entity. Handles all database operations for assignments.
 */
@Dao
public interface AssignmentDao {

    /**
     * Insert a new assignment
     */
    @Insert
    long insertAssignment(Assignment assignment);

    /**
     * Update an existing assignment
     */
    @Update
    void updateAssignment(Assignment assignment);

    /**
     * Delete an assignment
     */
    @Delete
    void deleteAssignment(Assignment assignment);

    /**
     * Get assignment by ID
     */
    @Query("SELECT * FROM assignments WHERE id = :id")
    Assignment getAssignmentById(int id);

    /**
     * Get all assignments for a course
     */
    @Query("SELECT * FROM assignments WHERE course_id = :courseId ORDER BY dueDate ASC")
    List<Assignment> getAssignmentsByCourse(int courseId);

    /**
     * Get all pending assignments for a user (across all courses)
     */
    @Query("SELECT a.* FROM assignments a JOIN courses c ON a.course_id = c.id " +
           "WHERE c.user_id = :userId AND a.status = 'pending' ORDER BY a.dueDate ASC")
    List<Assignment> getPendingAssignments(int userId);

    /**
     * Get all completed assignments for a user
     */
    @Query("SELECT a.* FROM assignments a JOIN courses c ON a.course_id = c.id " +
           "WHERE c.user_id = :userId AND a.status = 'completed' ORDER BY a.dueDate DESC")
    List<Assignment> getCompletedAssignments(int userId);

    /**
     * Get all deleted assignments for a user
     */
    @Query("SELECT a.* FROM assignments a JOIN courses c ON a.course_id = c.id " +
           "WHERE c.user_id = :userId AND a.status = 'deleted' ORDER BY a.dueDate DESC")
    List<Assignment> getDeletedAssignments(int userId);

    /**
     * Get all assignments by status
     */
    @Query("SELECT a.* FROM assignments a JOIN courses c ON a.course_id = c.id " +
           "WHERE c.user_id = :userId AND a.status = :status ORDER BY a.dueDate ASC")
    List<Assignment> getAssignmentsByStatus(int userId, String status);

    /**
     * Get assignments by priority
     */
    @Query("SELECT a.* FROM assignments a JOIN courses c ON a.course_id = c.id " +
           "WHERE c.user_id = :userId AND a.priority = :priority ORDER BY a.dueDate ASC")
    List<Assignment> getAssignmentsByPriority(int userId, String priority);

    /**
     * Search assignments by title
     */
    @Query("SELECT a.* FROM assignments a JOIN courses c ON a.course_id = c.id " +
           "WHERE c.user_id = :userId AND a.status = :status AND a.title LIKE '%' || :searchQuery || '%' ORDER BY a.dueDate ASC")
    List<Assignment> searchAssignments(int userId, String status, String searchQuery);
}

