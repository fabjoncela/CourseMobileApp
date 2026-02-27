package com.example.mymobilefabi.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymobilefabi.database.entities.Grade;

import java.util.List;

/**
 * DAO for Grade entity. Handles all database operations for grades.
 */
@Dao
public interface GradeDao {

    /**
     * Insert a new grade
     */
    @Insert
    long insertGrade(Grade grade);

    /**
     * Update an existing grade
     */
    @Update
    void updateGrade(Grade grade);

    /**
     * Delete a grade
     */
    @Delete
    void deleteGrade(Grade grade);

    /**
     * Get grade by ID
     */
    @Query("SELECT * FROM grades WHERE id = :id")
    Grade getGradeById(int id);

    /**
     * Get grade for a specific assignment
     */
    @Query("SELECT * FROM grades WHERE assignment_id = :assignmentId")
    Grade getGradeByAssignment(int assignmentId);

    /**
     * Get all grades for an assignment (in case of multiple grades)
     */
    @Query("SELECT * FROM grades WHERE assignment_id = :assignmentId ORDER BY createdAt DESC")
    List<Grade> getAllGradesByAssignment(int assignmentId);

    /**
     * Get average score for a course
     */
    @Query("SELECT AVG(g.score) FROM grades g JOIN assignments a ON g.assignment_id = a.id WHERE a.course_id = :courseId")
    Double getAverageScoreByCourse(int courseId);
}

