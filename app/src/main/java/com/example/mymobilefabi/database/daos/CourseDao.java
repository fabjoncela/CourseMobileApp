package com.example.mymobilefabi.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymobilefabi.database.entities.Course;

import java.util.List;

/**
 * DAO for Course entity. Handles all database operations for courses.
 */
@Dao
public interface CourseDao {

    /**
     * Insert a new course
     */
    @Insert
    long insertCourse(Course course);

    /**
     * Update an existing course
     */
    @Update
    void updateCourse(Course course);

    /**
     * Delete a course
     */
    @Delete
    void deleteCourse(Course course);

    /**
     * Get course by ID
     */
    @Query("SELECT * FROM courses WHERE id = :id")
    Course getCourseById(int id);

    /**
     * Get all courses for a user
     */
    @Query("SELECT * FROM courses WHERE user_id = :userId ORDER BY createdAt DESC")
    List<Course> getCoursesByUserId(int userId);

    /**
     * Get courses filtered by semester
     */
    @Query("SELECT * FROM courses WHERE user_id = :userId AND semester = :semester ORDER BY name")
    List<Course> getCoursesBySemester(int userId, String semester);

    /**
     * Get courses filtered by teacher
     */
    @Query("SELECT * FROM courses WHERE user_id = :userId AND teacher = :teacher ORDER BY name")
    List<Course> getCoursesByTeacher(int userId, String teacher);

    /**
     * Search courses by name
     */
    @Query("SELECT * FROM courses WHERE user_id = :userId AND name LIKE '%' || :searchQuery || '%'")
    List<Course> searchCourses(int userId, String searchQuery);
}

