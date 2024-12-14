package com.peritemacinas.bahi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.peritemacinas.bahi.databinding.ActivitySettingsBinding;


public class Settings extends AppCompatActivity {

    private ActivitySettingsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_settings);

        TextView editProfileText = findViewById(R.id.edit_click);

        editProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ProfileEdit.class);
                startActivity(intent);
            }
        });

        TextView deleteText = findViewById(R.id.delete_click);
        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, DeleteAccount.class);
                startActivity(intent);
            }
        });
        //change password
        TextView changePasswordText = findViewById(R.id.password_click);
        changePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
