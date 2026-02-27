package com.example.mymobilefabi.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymobilefabi.database.entities.Notification;

import java.util.List;

/**
 * DAO for Notification entity. Handles all database operations for notifications/reminders.
 */
@Dao
public interface NotificationDao {

    /**
     * Insert a new notification
     */
    @Insert
    long insertNotification(Notification notification);

    /**
     * Update an existing notification
     */
    @Update
    void updateNotification(Notification notification);

    /**
     * Delete a notification
     */
    @Delete
    void deleteNotification(Notification notification);

    /**
     * Get notification by ID
     */
    @Query("SELECT * FROM notifications WHERE id = :id")
    Notification getNotificationById(int id);

    /**
     * Get all pending notifications (not yet notified)
     */
    @Query("SELECT * FROM notifications WHERE isNotified = 0 ORDER BY reminderTime ASC")
    List<Notification> getPendingNotifications();

    /**
     * Get all notifications for a specific assignment
     */
    @Query("SELECT * FROM notifications WHERE assignment_id = :assignmentId ORDER BY reminderTime DESC")
    List<Notification> getNotificationsByAssignment(int assignmentId);

    /**
     * Get notifications due soon (within 24 hours)
     */
    @Query("SELECT * FROM notifications WHERE isNotified = 0 AND reminderTime <= :currentTime + 86400000 AND reminderTime > :currentTime")
    List<Notification> getUpcomingNotifications(long currentTime);
}

