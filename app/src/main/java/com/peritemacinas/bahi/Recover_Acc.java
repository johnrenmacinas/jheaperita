package com.peritemacinas.bahi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.peritemacinas.bahi.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Recover_Acc extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private Button resetPasswordButton;
    private TextView statusTextView;

    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_acc);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        statusTextView = findViewById(R.id.statusTextView);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    statusTextView.setText("Please enter a valid email address.");
                    statusTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Send password reset email
                sendPasswordResetEmail(email);
            }
        });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        statusTextView.setText("Password reset email sent! Please check your inbox.");
                        statusTextView.setVisibility(View.VISIBLE);
                    } else {
                        statusTextView.setText("Error: " + task.getException().getMessage());
                        statusTextView.setVisibility(View.VISIBLE);
                    }
                });
    }
}
