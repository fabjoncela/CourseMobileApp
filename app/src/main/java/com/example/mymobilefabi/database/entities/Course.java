package com.example.mymobilefabi.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Course entity represents an academic course.
 * Contains course details like code, teacher, semester, and credits.
 */
@Entity(
    tableName = "courses",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int user_id;
    private String name;
    private String code;
    private String teacher;
    private String semester;
    private int credits;
    private long createdAt;

    // Constructor
    public Course(int user_id, String name, String code, String teacher, String semester, int credits) {
        this.user_id = user_id;
        this.name = name;
        this.code = code;
        this.teacher = teacher;
        this.semester = semester;
        this.credits = credits;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

