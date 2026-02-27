package com.example.mymobilefabi.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Notification/Reminder entity for tracking assignment reminders.
 * Stores details about reminders set for assignments.
 */
@Entity(
    tableName = "notifications",
    foreignKeys = @ForeignKey(
        entity = Assignment.class,
        parentColumns = "id",
        childColumns = "assignment_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int assignment_id;
    private String title;
    private String message;
    private long reminderTime;
    private boolean isNotified;
    private long createdAt;

    // Constructor
    public Notification(int assignment_id, String title, String message, long reminderTime) {
        this.assignment_id = assignment_id;
        this.title = title;
        this.message = message;
        this.reminderTime = reminderTime;
        this.isNotified = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

