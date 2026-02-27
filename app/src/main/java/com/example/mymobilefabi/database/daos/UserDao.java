package com.example.mymobilefabi.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymobilefabi.database.entities.User;

/**
 * DAO for User entity. Handles all database operations for users.
 */
@Dao
public interface UserDao {

    /**
     * Insert a new user into the database
     */
    @Insert
    long insertUser(User user);

    /**
     * Update an existing user
     */
    @Update
    void updateUser(User user);

    /**
     * Delete a user
     */
    @Delete
    void deleteUser(User user);

    /**
     * Get user by ID
     */
    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);

    /**
     * Get user by email
     */
    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    /**
     * Check if email exists
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int emailExists(String email);
}

