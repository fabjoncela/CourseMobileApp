package com.example.mymobilefabi.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User entity represents a student in the database.
 * Contains authentication credentials and basic student information.
 */
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String email;
    private String password; // In production, use hashed passwords
    private String name;
    private String studentId;
    private String profilePicPath;
    private double gpa;
    private long createdAt;

    // Constructor
    public User(String email, String password, String name, String studentId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
        this.gpa = 0.0;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

