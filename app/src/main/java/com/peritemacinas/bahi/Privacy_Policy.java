package com.peritemacinas.bahi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class Privacy_Policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        // Initialize the TextView
        TextView textView = findViewById(R.id.privacyPolicy);

        try {
            // Open the text file from the assets folder
            InputStream inputStream = getAssets().open("privacy_policy.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Convert the buffer into a String and set it to the TextView
            String longText = new String(buffer, "UTF-8");
            textView.setText(longText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}