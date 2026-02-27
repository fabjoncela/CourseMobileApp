package com.example.mymobilefabi.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Assignment entity represents assignments and exams.
 * Tracks title, description, due date, priority, and completion status.
 */
@Entity(
    tableName = "assignments",
    foreignKeys = @ForeignKey(
        entity = Course.class,
        parentColumns = "id",
        childColumns = "course_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Assignment {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int course_id;
    private String title;
    private String description;
    private long dueDate;
    private String priority; // high, medium, low
    private String status; // pending, completed
    private long createdAt;

    // Constructor
    public Assignment(int course_id, String title, String description, long dueDate, String priority, String status) {
        this.course_id = course_id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

