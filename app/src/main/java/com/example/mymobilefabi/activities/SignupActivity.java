package com.example.mymobilefabi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymobilefabi.MainActivity;
import com.example.mymobilefabi.R;
import com.example.mymobilefabi.database.AppDatabase;
import com.example.mymobilefabi.database.entities.User;
import com.example.mymobilefabi.utils.SessionManager;

/**
 * SignupActivity - User registration screen
 * Creates new user account and saves to database
 */
public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, studentIdEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;
    private TextView loginLink;
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize database and session manager
        database = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginLink);

        // Set click listeners
        signupButton.setOnClickListener(v -> handleSignup());
        loginLink.setOnClickListener(v -> navigateToLogin());
    }

    /**
     * Handle signup process
     */
    private void handleSignup() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String studentId = studentIdEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(studentId)) {
            studentIdEditText.setError("Student ID is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Database operation in background thread
        new Thread(() -> {
            // Check if email already exists
            if (database.userDao().emailExists(email) > 0) {
                runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Email already registered", Toast.LENGTH_SHORT).show());
                return;
            }

            // Create new user
            User newUser = new User(email, password, name, studentId);
            long userId = database.userDao().insertUser(newUser);

            runOnUiThread(() -> {
                Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                // Set user as logged in
                sessionManager.setLoginSession((int) userId, email, name);

                // Navigate to dashboard
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            });
        }).start();
    }

    /**
     * Navigate back to login
     */
    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

