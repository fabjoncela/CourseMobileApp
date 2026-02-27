package com.example.mymobilefabi.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Grade entity stores grades for assignments.
 * Tracks score, max score, and calculates percentages.
 */
@Entity(
    tableName = "grades",
    foreignKeys = @ForeignKey(
        entity = Assignment.class,
        parentColumns = "id",
        childColumns = "assignment_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class Grade {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int assignment_id;
    private double score;
    private double maxScore;
    private long createdAt;

    // Constructor
    public Grade(int assignment_id, double score, double maxScore) {
        this.assignment_id = assignment_id;
        this.score = score;
        this.maxScore = maxScore;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Calculate percentage score
     */
    public double getPercentage() {
        if (maxScore == 0) return 0;
        return (score / maxScore) * 100;
    }
}

