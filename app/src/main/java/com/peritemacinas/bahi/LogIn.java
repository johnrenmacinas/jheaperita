package com.peritemacinas.bahi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LogIn extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button loginClick;
    private TextView gotoRegister, gotoRecoverAcc;
    private FirebaseAuth fAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize Firebase and Database reference
        fAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        email = findViewById(R.id.logInEmailInput);
        password = findViewById(R.id.logInPasswordInput);
        loginClick = findViewById(R.id.logIn_Click);
        gotoRegister = findViewById(R.id.gotoRegister);
        gotoRecoverAcc = findViewById(R.id.gotoRecover);

        loginClick.setOnClickListener(view -> {
            String loginEmail = email.getText().toString().trim();
            String loginPassword = password.getText().toString().trim();

            // Validate email and password fields
            if (!validateInputs(loginEmail, loginPassword)) return;

            // Check user existence in database
            dbRef.orderByChild("email").equalTo(loginEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String userId = userSnapshot.getKey();
                            checkUserAccessLevel(userId);
                        }
                    } else {
                        Toast.makeText(LogIn.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Database error: " + error.getMessage());
                    Toast.makeText(LogIn.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                }
            });
        });

        gotoRegister.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Register.class)));
        gotoRecoverAcc.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Recover_Acc.class)));
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            this.email.setError("Please enter your email");
            this.email.requestFocus();
            return false;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            this.email.setError("Enter a valid email address");
            this.email.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            this.password.setError("Please enter your password");
            this.password.requestFocus();
            return false;
        }

        return true;
    }

    private void checkUserAccessLevel(String uid) {
        if (dbRef == null) {
            Log.e("FirebaseError", "DatabaseReference is null");
            Toast.makeText(this, "Internal error: Database not initialized.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child(uid).child("accountType")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        String accountType = dataSnapshot.getValue(String.class);
                        Log.d("FirebaseLogin", "Account type: " + accountType);

                        if ("landlord".equalsIgnoreCase(accountType)) {
                            String accType = "Landlord";
                            String tid = uid;
                            Intent intent = new Intent(getApplicationContext(), Admin.class);
                            intent.putExtra("accountType", accType);
                            intent.putExtra("uid", tid);
                            startActivity(intent);
                            finish();
                        } else if ("tenant".equalsIgnoreCase(accountType)) {
                            String accType = "Tenant";
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("accountType", accType);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e("FirebaseLogin", "User data not found for UID: " + uid);
                        Toast.makeText(LogIn.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseLogin", "Failed to fetch user data: " + e.getMessage());
                    Toast.makeText(LogIn.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
                });
    }
}
