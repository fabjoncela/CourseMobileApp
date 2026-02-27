package com.example.mymobilefabi.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mymobilefabi.MainActivity;
import com.example.mymobilefabi.R;

/**
 * NotificationManager - Handles push notifications and reminders
 */
public class NotificationHelper {

    private static final String CHANNEL_ID = "assignment_reminders";
    private static final String CHANNEL_NAME = "Assignment Reminders";
    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    /**
     * Create notification channel for Android 8+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for upcoming assignments and deadlines");
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Show assignment reminder notification
     */
    public void showAssignmentReminder(String assignmentTitle, String description, int notificationId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dashboard)
            .setContentTitle(assignmentTitle)
            .setContentText(description)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, builder.build());
    }

    /**
     * Cancel notification
     */
    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }
}

