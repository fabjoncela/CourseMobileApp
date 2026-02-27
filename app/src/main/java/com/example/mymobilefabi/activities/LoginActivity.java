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
 * LoginActivity - User login screen
 * Validates email and password, then navigates to main dashboard
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signupLink;
    private AppDatabase database;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize database and session manager
        database = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToDashboard();
            return; // Exit onCreate to prevent further initialization
        }

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);

        // Set click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        signupLink.setOnClickListener(v -> navigateToSignup());
    }

    /**
     * Handle login process
     */
    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            return;
        }

        // Database operation in background thread
        new Thread(() -> {
            User user = database.userDao().getUserByEmail(email);

            runOnUiThread(() -> {
                if (user != null && user.getPassword().equals(password)) {
                    // Login successful
                    sessionManager.setLoginSession(user.getId(), user.getEmail(), user.getName());
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard();
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    /**
     * Navigate to signup screen
     */
    private void navigateToSignup() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    /**
     * Navigate to main dashboard
     */
    private void navigateToDashboard() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

