package com.example.mymobilefabi.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mymobilefabi.database.daos.AssignmentDao;
import com.example.mymobilefabi.database.daos.CourseDao;
import com.example.mymobilefabi.database.daos.GradeDao;
import com.example.mymobilefabi.database.daos.NoteDao;
import com.example.mymobilefabi.database.daos.NotificationDao;
import com.example.mymobilefabi.database.daos.UserDao;
import com.example.mymobilefabi.database.entities.Assignment;
import com.example.mymobilefabi.database.entities.Course;
import com.example.mymobilefabi.database.entities.Grade;
import com.example.mymobilefabi.database.entities.Note;
import com.example.mymobilefabi.database.entities.Notification;
import com.example.mymobilefabi.database.entities.User;

/**
 * Room database for the Student Companion App.
 * Contains all entities and provides access to their DAOs.
 */
@Database(
    entities = {User.class, Course.class, Assignment.class, Grade.class, Note.class, Notification.class},
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    // Abstract methods for each DAO
    public abstract UserDao userDao();
    public abstract CourseDao courseDao();
    public abstract AssignmentDao assignmentDao();
    public abstract GradeDao gradeDao();
    public abstract NoteDao noteDao();
    public abstract NotificationDao notificationDao();

    /**
     * Get singleton instance of the database.
     * Uses double-checked locking for thread safety.
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "student_companion_db"
                        )
                        .fallbackToDestructiveMigration() // For development - recreates DB on schema change
                        .addCallback(new Callback() {
                            @Override
                            public void onOpen(androidx.sqlite.db.SupportSQLiteDatabase db) {
                                super.onOpen(db);
                                // Disable foreign key constraints for development
                                db.execSQL("PRAGMA foreign_keys=OFF;");
                            }
                        })
                        .build();
                }
            }
        }
        return instance;
    }
}

