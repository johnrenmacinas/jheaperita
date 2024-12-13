package com.peritemacinas.bahi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.peritemacinas.bahi.databinding.ActivityDeleteAccountBinding;
import com.peritemacinas.bahi.databinding.ProgressDialogBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteAccount extends AppCompatActivity {

    private ActivityDeleteAccountBinding binding;
    private static final String TAG = "DELETE_ACCOUNT_TAG";

    private AlertDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the binding properly
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and User
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        // Handle delete button click
        binding.confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ProgressDialogBinding dialogBinding = ProgressDialogBinding.inflate(LayoutInflater.from(this));
            builder.setView(dialogBinding.getRoot());
            progressDialog = builder.create();
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        ProgressDialogBinding dialogBinding = ProgressDialogBinding.bind(progressDialog.findViewById(R.id.progress_layout));
        dialogBinding.progressText.setText(message);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void deleteAccount() {
        Log.d(TAG, "deleteAccount: ");
        showProgressDialog("Deleting User Account...");

        firebaseUser.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Account deleted");
                        deleteUserAds();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        dismissProgressDialog();
                        Utils.toast(DeleteAccount.this, "Failed to delete account due to " + e.getMessage());
                    }
                });
    }

    private void deleteUserAds() {
        showProgressDialog("Deleting User Ads...");
        DatabaseReference refUserAds = FirebaseDatabase.getInstance().getReference("Ads");
        refUserAds.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        deleteUserData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "onCancelled: Failed to delete ads", error.toException());
                        dismissProgressDialog();
                    }
                });
    }

    private void deleteUserData() {
        showProgressDialog("Deleting User Data...");
        DatabaseReference refUsers = FirebaseDatabase.getInstance().getReference("Users");
        refUsers.child(firebaseAuth.getUid())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: User data deleted...");
                        dismissProgressDialog();
                        startLogIn();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        dismissProgressDialog();
                        Utils.toast(DeleteAccount.this, "Failed to delete user data due to " + e.getMessage());
                    }
                });
    }

    private void startLogIn() {
        Log.d(TAG, "startLogIn: ");
        startActivity(new Intent(this, LogIn.class));
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LogIn.class));
        finishAffinity();
    }
}
