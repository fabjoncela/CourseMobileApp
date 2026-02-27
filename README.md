# Student Companion App - Android Studio Java Project

## Overview
A comprehensive Java Android application for managing student academic activities including courses, assignments, grades, and notes. The app features user authentication, CRUD operations, and real-time data persistence using Room database.

## Features

### 1. **Authentication**
- User registration (signup) with email and password validation
- User login with email/password authentication
- Session management using SharedPreferences
- Secure password storage (for class project - use hashing in production)

### 2. **Dashboard**
- Welcome message with student name
- Quick statistics (total courses, pending assignments)
- Overview of student activities

### 3. **Courses Management**
- Create new courses with details (name, code, teacher, semester, credits)
- View all courses in a list
- Search courses by name
- Delete courses
- Course detail display

### 4. **Assignments/Exams**
- Create assignments with title, description, due date, priority, and status
- View all pending and completed assignments
- Mark assignments as complete
- Search assignments by title
- Delete assignments
- Priority-based coloring (high/medium/low)

### 5. **Grades Tracker**
- Record grades for assignments
- Calculate percentage scores
- Display average grade across all assignments
- Calculate GPA (simplified 4.0 scale)
- Visual grade representation with color coding

### 6. **Notes**
- Create study notes
- Attach notes to courses
- Search notes by title or content
- Delete notes
- Timestamp tracking (created/updated)

### 7. **Notifications (Ready for integration)**
- Notification helper for reminder alerts
- Channel creation for Android 8+
- Ready for WorkManager integration

## Project Structure

```
MyMobileFabi/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/mymobilefabi/
│   │       │   ├── activities/
│   │       │   │   ├── LoginActivity.java
│   │       │   │   └── SignupActivity.java
│   │       │   ├── fragments/
│   │       │   │   ├── DashboardFragment.java
│   │       │   │   ├── CoursesFragment.java
│   │       │   │   ├── AssignmentsFragment.java
│   │       │   │   ├── GradesFragment.java
│   │       │   │   └── NotesFragment.java
│   │       │   ├── adapters/
│   │       │   │   ├── CourseAdapter.java
│   │       │   │   ├── AssignmentAdapter.java
│   │       │   │   ├── GradeAdapter.java
│   │       │   │   └── NoteAdapter.java
│   │       │   ├── database/
│   │       │   │   ├── AppDatabase.java
│   │       │   │   ├── daos/
│   │       │   │   │   ├── UserDao.java
│   │       │   │   │   ├── CourseDao.java
│   │       │   │   │   ├── AssignmentDao.java
│   │       │   │   │   ├── GradeDao.java
│   │       │   │   │   ├── NoteDao.java
│   │       │   │   │   └── NotificationDao.java
│   │       │   │   └── entities/
│   │       │   │       ├── User.java
│   │       │   │       ├── Course.java
│   │       │   │       ├── Assignment.java
│   │       │   │       ├── Grade.java
│   │       │   │       ├── Note.java
│   │       │   │       └── Notification.java
│   │       │   ├── utils/
│   │       │   │   ├── SessionManager.java
│   │       │   │   └── NotificationHelper.java
│   │       │   └── MainActivity.java
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_login.xml
│   │       │   │   ├── activity_signup.xml
│   │       │   │   ├── activity_main.xml
│   │       │   │   ├── fragment_dashboard.xml
│   │       │   │   ├── fragment_courses.xml
│   │       │   │   ├── fragment_assignments.xml
│   │       │   │   ├── fragment_grades.xml
│   │       │   │   ├── fragment_notes.xml
│   │       │   │   ├── item_course.xml
│   │       │   │   ├── item_assignment.xml
│   │       │   │   ├── item_grade.xml
│   │       │   │   ├── item_note.xml
│   │       │   │   ├── dialog_add_course.xml
│   │       │   │   ├── dialog_add_assignment.xml
│   │       │   │   └── dialog_add_note.xml
│   │       │   ├── drawable/
│   │       │   │   ├── ic_dashboard.xml
│   │       │   │   ├── ic_courses.xml
│   │       │   │   ├── ic_assignments.xml
│   │       │   │   ├── ic_grades.xml
│   │       │   │   ├── ic_notes.xml
│   │       │   │   ├── ic_add.xml
│   │       │   │   ├── button_background.xml
│   │       │   │   ├── edit_text_background.xml
│   │       │   │   └── bottom_nav_selector.xml
│   │       │   ├── menu/
│   │       │   │   └── bottom_nav_menu.xml
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       ├── colors.xml
│   │       │       └── themes.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
└── gradle/
    └── libs.versions.toml
```

## Database Schema

### Users Table
```
- id: INT (PrimaryKey, AutoGenerate)
- email: STRING (Unique)
- password: STRING
- name: STRING
- studentId: STRING
- profilePicPath: STRING
- gpa: DOUBLE
- createdAt: LONG
```

### Courses Table
```
- id: INT (PrimaryKey)
- user_id: INT (ForeignKey -> Users)
- name: STRING
- code: STRING
- teacher: STRING
- semester: STRING
- credits: INT
- createdAt: LONG
```

### Assignments Table
```
- id: INT (PrimaryKey)
- course_id: INT (ForeignKey -> Courses)
- title: STRING
- description: STRING
- dueDate: LONG
- priority: STRING (high/medium/low)
- status: STRING (pending/completed)
- createdAt: LONG
```

### Grades Table
```
- id: INT (PrimaryKey)
- assignment_id: INT (ForeignKey -> Assignments)
- score: DOUBLE
- maxScore: DOUBLE
- createdAt: LONG
```

### Notes Table
```
- id: INT (PrimaryKey)
- user_id: INT (ForeignKey -> Users)
- course_id: INT (ForeignKey -> Courses)
- title: STRING
- content: STRING
- createdAt: LONG
- updatedAt: LONG
```

### Notifications Table
```
- id: INT (PrimaryKey)
- assignment_id: INT (ForeignKey -> Assignments)
- title: STRING
- message: STRING
- reminderTime: LONG
- isNotified: BOOLEAN
- createdAt: LONG
```

## Key Libraries Used

- **AndroidX AppCompat** - Backward compatibility
- **Material Design 3** - Modern UI components
- **RecyclerView** - Efficient list rendering
- **CardView** - Card-based layouts
- **Room Database** - Local data persistence
- **Navigation Component** - Fragment navigation
- **WorkManager** - Background task scheduling
- **Hilt** - Dependency injection (ready for integration)
- **MPAndroidChart** - Chart visualization (ready for integration)

## How to Run

1. **Clone/Open Project**
   - Open Android Studio
   - File → Open → Navigate to MyMobileFabi folder

2. **Build Project**
   - Build → Make Project (Ctrl+F9)
   - Gradle will sync dependencies automatically

3. **Run App**
   - Run → Run 'app' (Shift+F10)
   - Select emulator or connected device

4. **First Time Setup**
   - App will open to Login screen
   - Click "Sign Up" to create new account
   - Fill in details: Name, Email, Student ID, Password
   - Click "Create Account"
   - You'll be logged in and redirected to Dashboard

## Navigation Flow

```
LoginActivity
├── → SignupActivity → MainActivity
└── → MainActivity (if logged in)

MainActivity (Dashboard)
├── DashboardFragment (Stats overview)
├── CoursesFragment (Course management)
├── AssignmentsFragment (Task management)
├── GradesFragment (Grade tracking)
└── NotesFragment (Note management)
```

## Testing the Features

### 1. Create a Course
- Navigate to Courses tab
- Tap + button
- Fill course details (Name: "Data Structures", Code: "CS201", etc.)
- Tap "Add"

### 2. Create an Assignment
- Navigate to Assignments tab
- Tap + button
- Fill assignment details (Course ID: 1, Title: "Project 1", etc.)
- Tap "Add"

### 3. Add a Grade
- Assignment grades are stored linked to assignments
- To add: Navigate to Grades tab
- Grades will display with percentage and GPA calculation

### 4. Create a Note
- Navigate to Notes tab
- Tap + button
- Fill note details (optional course ID link)
- Tap "Add"

### 5. Search Features
- Each tab has search bar (except dashboard)
- Type to filter items in real-time

## Important Notes for Development

### Thread Safety
- All database operations run on background threads
- UI updates are posted back to main thread
- Uses `runOnUiThread()` for thread-safe updates

### Data Persistence
- All data is stored in Room database
- Database file: `student_companion_db`
- Survives app kills and device restarts

### Session Management
- SharedPreferences stores user session
- User ID, email, and name cached locally
- Session cleared on logout

### Future Enhancement Opportunities
1. **Backend Integration** - Firebase or REST API
2. **Charts** - MPAndroidChart integration for grade visualization
3. **Notifications** - WorkManager for scheduled reminders
4. **Dark Mode** - Theme switching implementation
5. **Cloud Sync** - Data synchronization
6. **Export** - CSV export functionality
7. **Profile Pictures** - Image selection and storage
8. **GPA Calculation** - Advanced GPA algorithms
9. **Analytics** - Performance tracking over time
10. **Collaborative Features** - Shared assignments/notes

## Troubleshooting

### Build Errors
- Run `Build → Clean Project` then `Build → Make Project`
- Check that all gradle dependencies are downloaded
- Verify Java SDK 11+ is configured

### Runtime Errors
- Check logcat for error messages: View → Tool Windows → Logcat
- Verify database is initialized in AppDatabase
- Check that fragments are properly added to activity

### Database Issues
- Data persists in local database
- To reset: Settings → Apps → MyMobileFabi → Clear Data
- Or use Android Studio Device Manager to wipe emulator

## License
This is a class project for educational purposes.

## Author
Created for Student Companion App - Android Development Course

