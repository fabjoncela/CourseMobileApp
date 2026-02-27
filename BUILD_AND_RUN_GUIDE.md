STUDENT COMPANION APP - QUICK BUILD & RUN GUIDE
===============================================

🚀 STEP-BY-STEP BUILD INSTRUCTIONS
===================================

STEP 1: OPEN PROJECT IN ANDROID STUDIO
--------------------------------------
1. Launch Android Studio
2. File → Open
3. Navigate to: C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi
4. Click "Open"
5. Wait for indexing to complete (progress bar at bottom)

STEP 2: SYNC GRADLE DEPENDENCIES
---------------------------------
1. When prompted: "Gradle files have changed since the project was last built"
2. Click: "Sync Now" (blue link at top)
3. Wait for sync to complete
   - Downloads all libraries (Room, Material, Navigation, etc.)
   - Should complete in 2-5 minutes
   - Progress shows in bottom "Gradle" window

If No Prompt Appears:
   File → Sync Now
   Or: Tools → Android → Sync Now

STEP 3: VERIFY BUILD CONFIGURATION
-----------------------------------
1. File → Project Structure
2. Check:
   ✓ Project: SDK Version 36
   ✓ App: Compile SDK 36
   ✓ Java Version: 11
   ✓ Gradle Version: 8.0+
3. Click "OK"

STEP 4: CLEAN AND BUILD PROJECT
--------------------------------
1. Build → Clean Project
   - Removes previous build artifacts
   - Progress shows: "Clean completed"

2. Build → Make Project (Keyboard: Ctrl+F9)
   - Compiles all Java classes
   - Processes all XML layouts
   - Generates R.java file
   - Creates APK

Expected Time: 1-3 minutes
Expected Result: "BUILD SUCCESSFUL" message at bottom

If Build Fails:
   → See "BUILD TROUBLESHOOTING" section below

STEP 5: CREATE EMULATOR (If needed)
-----------------------------------
1. Tools → Device Manager
2. Click "Create Device"
3. Select Phone: Pixel 4a
4. Select OS: Android 12 (API 31) or higher
5. Click "Next" → "Finish"
6. Wait for emulator setup

Or Use Existing Emulator:
   - Plug in Android device with USB debugging enabled
   - Device shows in dropdown

STEP 6: RUN THE APPLICATION
----------------------------
1. Run → Run 'app' (Keyboard: Shift+F10)
2. Select Device:
   ✓ Choose emulator or connected device
   ✓ Click "OK"

3. Wait for app to launch
   - First launch: 30-60 seconds
   - App opens to LoginActivity
   - Database creates automatically

STEP 7: TEST INITIAL LAUNCH
---------------------------
You should see:
✓ Blue background
✓ "Student Companion" title
✓ Email input field
✓ Password input field
✓ Login button
✓ "Don't have account? Sign Up" link

If app crashes:
   → Check Logcat (View → Tool Windows → Logcat)
   → See "BUILD TROUBLESHOOTING" section

========================================
📝 FIRST-TIME USER TEST FLOW
========================================

1. SIGN UP (Create Account)
   ─────────────────────────
   Click: "Sign Up" link at bottom
   
   Fill form:
   - Full Name: "John Doe"
   - Email: "john@example.com"
   - Student ID: "STU12345"
   - Password: "password123"
   - Confirm Password: "password123"
   
   Click: "Create Account"
   
   Expected: 
   ✓ Success toast message
   ✓ Redirect to Dashboard automatically
   ✓ Welcome message shows "Welcome, John Doe!"

2. EXPLORE DASHBOARD
   ──────────────────
   You should see:
   ✓ Welcome message with your name
   ✓ Card showing "Total Courses: 0"
   ✓ Card showing "Pending Assignments: 0"
   ✓ Bottom navigation with 5 tabs

3. ADD A COURSE
   ─────────────
   Click: Bottom Navigation → "Courses" tab
   
   Tap: Blue + button (bottom right)
   
   Fill dialog:
   - Course Name: "Data Structures"
   - Course Code: "CS201"
   - Teacher Name: "Dr. Smith"
   - Semester: "Spring 2026"
   - Credits: "3"
   
   Click: "Add"
   
   Expected:
   ✓ Course appears in list
   ✓ Can delete course (red button)
   ✓ Dashboard shows "Total Courses: 1"

4. ADD AN ASSIGNMENT
   ──────────────────
   Click: Bottom Navigation → "Tasks" tab
   
   Tap: Blue + button
   
   Fill dialog:
   - Title: "Project 1"
   - Description: "Build a binary tree"
   - Course ID: "1" (from step 3)
   - Priority: "high"
   
   Click: "Add"
   
   Expected:
   ✓ Assignment appears in red (high priority)
   ✓ Shows due date (7 days from now)
   ✓ Can mark "Complete" or "Delete"
   ✓ Dashboard shows "Pending Assignments: 1"

5. ADD A GRADE
   ────────────
   Grades are linked to assignments
   In GradesFragment (ready for grades display)
   
   Click: Bottom Navigation → "Grades" tab
   
   Expected:
   ✓ See statistics section
   ✓ Average grade and GPA display
   ✓ Can add grades via dialog

6. ADD A NOTE
   ──────────
   Click: Bottom Navigation → "Notes" tab
   
   Tap: Blue + button
   
   Fill dialog:
   - Title: "Chapter 5 Summary"
   - Content: "Binary trees are hierarchical structures..."
   - Course ID: "1" (optional)
   
   Click: "Add"
   
   Expected:
   ✓ Note appears in list
   ✓ Shows creation date
   ✓ Can delete note

7. TEST SEARCH
   ────────────
   In any tab with search:
   
   Type in search bar: "Data"
   
   Expected:
   ✓ Filters courses/assignments/notes in real-time
   ✓ Only matching items show
   ✓ Clear search to see all again

8. LOGOUT & LOGIN TEST
   ───────────────────
   Currently implemented:
   ✓ Logout method available in MainActivity
   ✓ Click logout → redirect to LoginActivity
   
   Login with previous credentials:
   - Email: "john@example.com"
   - Password: "password123"
   
   Expected:
   ✓ Login succeeds
   ✓ All data persists from previous session
   ✓ Dashboard shows same courses/assignments

========================================
🔍 VERIFICATION CHECKLIST
========================================

After Building and Running:

Core Functionality:
  ☐ App opens without crashing
  ☐ Login screen displays correctly
  ☐ Can create account (signup)
  ☐ Can login after signup
  ☐ Dashboard shows after login
  ☐ All 5 bottom tabs accessible

Courses Module:
  ☐ Can add course
  ☐ Course appears in list
  ☐ Can search courses
  ☐ Can delete course
  ☐ Course count updates on dashboard

Assignments Module:
  ☐ Can add assignment
  ☐ Shows in list with due date
  ☐ Priority shows with color (red/orange/green)
  ☐ Can mark complete
  ☐ Can delete assignment
  ☐ Assignment count updates on dashboard

Grades Module:
  ☐ Grades tab displays
  ☐ Shows average grade
  ☐ Shows GPA calculation
  ☐ Color-coded grades visible

Notes Module:
  ☐ Can add note
  ☐ Note appears in list
  ☐ Can search notes
  ☐ Can delete note
  ☐ Timestamp visible

Data Persistence:
  ☐ Close app completely
  ☐ Reopen app
  ☐ All data still there
  ☐ Can login with same credentials

Navigation:
  ☐ Bottom tabs switch fragments smoothly
  ☐ No lag or stuttering
  ☐ Back button works appropriately
  ☐ Dialogs appear/close cleanly

UI/UX:
  ☐ All text readable
  ☐ All buttons clickable
  ☐ Cards display nicely
  ☐ Colors appropriate
  ☐ No overlapping text

========================================
⚠️ BUILD TROUBLESHOOTING
========================================

ISSUE 1: Gradle Sync Fails
──────────────────────────
Error: "Failed to resolve..."

Solutions:
1. Check internet connection
2. File → Invalidate Caches → Invalidate and Restart
3. Delete gradle cache: rm -r .gradle
4. Try: Build → Clean Project → Make Project
5. Update Android Studio: Help → Check for Updates

ISSUE 2: Build Fails with Compilation Errors
─────────────────────────────────────────────
Error: "@Entity cannot be applied to class"
Or similar annotation errors

Solutions:
1. Verify Room dependency in build.gradle
2. Check that all entity classes have @Entity
3. Check that all DAOs have @Dao
4. Run: Build → Clean Project
5. File → Invalidate Caches
6. Try build again

ISSUE 3: R.java Errors (Cannot resolve R)
─────────────────────────────────────────
Error: "Cannot resolve symbol R"

Solutions:
1. Clean project: Build → Clean Project
2. Rebuild: Build → Make Project
3. Check layout XML for errors (syntax)
4. Check drawable files for errors
5. Check strings.xml for errors

ISSUE 4: Database Errors at Runtime
──────────────────────────────────────
Error: "Cannot find database"
Or: "Invalid migration"

Solutions:
1. Uninstall app: adb uninstall com.example.mymobilefabi
2. Clear app data: Settings → Apps → MyMobileFabi → Clear Data
3. Reinstall via Run → Run 'app'
4. Check AppDatabase.java singleton

ISSUE 5: App Crashes on Launch
──────────────────────────────
View Logcat to see error:
View → Tool Windows → Logcat

Common causes:
- Missing R id (check layouts)
- NullPointerException (check initialization)
- Missing manifest entry (check AndroidManifest.xml)

Solution: Read error in Logcat and fix accordingly

ISSUE 6: Emulator Won't Start
────────────────────────────
Solutions:
1. Close all emulators
2. Tools → Device Manager → Start emulator
3. Wait 2-3 minutes for boot
4. Try cold boot: Device Manager → Run → Cold Boot

ISSUE 7: Device Not Recognized
──────────────────────────────
Solutions:
1. Enable USB Debugging on device
2. Check USB connection
3. adb devices (in terminal) to verify
4. Update USB driver if needed

========================================
🔧 GRADLE CONSOLE COMMANDS
========================================

Run from Terminal (in project directory):

# Full build
./gradlew build

# Clean then build
./gradlew clean build

# Run tests
./gradlew test

# Build APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Check gradle version
./gradlew -v

# Sync dependencies
./gradlew sync

========================================
📱 TESTING ON REAL DEVICE
========================================

1. Enable USB Debugging
   Settings → Developer Options → USB Debugging

2. Connect device via USB cable

3. Trust the computer when prompted on device

4. Verify connection
   Terminal: adb devices
   (Should show device in list)

5. Run app
   Run → Run 'app' → Select device → OK

6. App installs and launches on device

========================================
💾 COMMON PATHS & FILES
========================================

Project Root:
C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi\

Source Code:
C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi\app\src\main\java\

Layouts:
C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi\app\src\main\res\layout\

Manifests:
C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi\app\src\main\AndroidManifest.xml

Gradle Files:
C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi\app\build.gradle.kts
C:\Users\sfabj\AndroidStudioProjects\MyMobileFabi\gradle\libs.versions.toml

Database File (Generated at Runtime):
C:\Users\[device]\data\com.example.mymobilefabi\databases\student_companion_db

========================================
🎯 EXPECTED OUTPUT LOGS
========================================

Good Build Output:
─────────────────
BUILD SUCCESSFUL in 45s
123 actionable tasks: 123 executed

Good Run Output:
────────────────
D/AppDatabase: Creating new AppDatabase instance
D/SQLite: Opening database: student_companion_db
I/System.out: Room database initialized successfully

========================================
✅ NEXT STEPS AFTER SUCCESSFUL BUILD
========================================

1. Thoroughly test all features
   - Follow "First-Time User Test Flow" above

2. Document any issues
   - Note crashes, bugs, or unexpected behavior

3. Review code quality
   - Check for warnings in IDE
   - Verify all methods documented

4. Test on multiple devices/emulators
   - Different Android versions
   - Different screen sizes

5. Prepare for submission
   - Ensure all files included
   - Verify documentation complete
   - Create release APK if needed

6. Generate APK for Submission
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - APK located in: app\build\outputs\apk\debug\

========================================
📞 SUPPORT REFERENCES
========================================

Android Developer Documentation:
https://developer.android.com

Room Database:
https://developer.android.com/training/data-storage/room

RecyclerView:
https://developer.android.com/guide/topics/ui/layout/recyclerview

Fragments:
https://developer.android.com/guide/fragments

Material Design:
https://material.io/design

Android Studio Help:
Help → Android Studio Help (in Android Studio)

========================================
✨ YOU'RE READY TO BUILD!
========================================

Follow these steps in order:
1. Open project in Android Studio
2. Sync Gradle
3. Clean & Build Project
4. Create/Select Emulator
5. Run App
6. Test Features
7. Submit Project

Good luck with your Student Companion App! 🎉

========================================

