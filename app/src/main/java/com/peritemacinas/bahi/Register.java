package com.peritemacinas.bahi;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Register extends AppCompatActivity {

    TextInputEditText fullName, email, phone, birthday, password, confirmPassword;
    TextView textTermsConditions;
    Button registerClick, gotoLogin, uploadImageButton;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    CheckBox asLandlord, asTenant, termsConditions;
    ProgressBar progressBar;
    RadioButton female, male;
    ImageView profilePicPreview;
    Uri selectedImageUri;
    StorageReference storageRef;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeFields();
        setupListeners();
    }

    private void initializeFields() {
        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageRef = FirebaseStorage.getInstance().getReference("ProfileImages");

        fullName = findViewById(R.id.register_Name);
        email = findViewById(R.id.register_Email);
        phone = findViewById(R.id.register_PNumber);
        birthday = findViewById(R.id.register_Bday);
        password = findViewById(R.id.register_Password);
        confirmPassword = findViewById(R.id.register_Confirm_Password);
        registerClick = findViewById(R.id.register_Click);
        gotoLogin = findViewById(R.id.register_LogIn);
        asLandlord = findViewById(R.id.register_Landlord);
        asTenant = findViewById(R.id.register_Tenant);
        female = findViewById(R.id.register_Female);
        male = findViewById(R.id.register_Male);
        termsConditions = findViewById(R.id.check_tac);
        textTermsConditions = findViewById(R.id.tac_click1);
        progressBar = findViewById(R.id.register_ProgressBar);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        profilePicPreview = findViewById(R.id.profilePicPreview);
    }

    private void setupListeners() {
        setupDatePicker();
        setupFieldListeners();

        uploadImageButton.setOnClickListener(view -> selectImage());
        registerClick.setOnClickListener(view -> handleRegister());
        gotoLogin.setOnClickListener(view -> {
            Intent intent = new Intent(Register.this, LogIn.class);
            startActivity(intent);
        });
        textTermsConditions.setOnClickListener(view -> {
            Intent intent = new Intent(Register.this, Terms_Conditions.class);
            startActivity(intent);
        });
    }

    private void setupDatePicker() {
        birthday.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(Register.this, (view1, year1, month1, dayOfMonth) ->
                    birthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
            picker.show();
        });
    }

    private void setupFieldListeners() {
        asTenant.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) asLandlord.setChecked(false);
        });
        asLandlord.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) asTenant.setChecked(false);
        });

        male.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) female.setChecked(false);
        });
        female.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked) male.setChecked(false);
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profilePicPreview.setImageURI(selectedImageUri);
        }
    }

    private void handleRegister() {
        if (!validateAllFields()) return;

        progressBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = fAuth.getCurrentUser();
                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                uploadProfileImage(user.getUid());
                            } else {
                                showError("Verification email failed.");
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> showError("Registration failed: " + e.getMessage()));
    }

    private void uploadProfileImage(String uid) {
        if (selectedImageUri != null) {
            StorageReference fileRef = storageRef.child(uid + ".jpg");
            fileRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        saveUserToDatabase(uid, uri.toString());
                    })).addOnFailureListener(e -> {
                showError("Image upload failed: " + e.getMessage());
            });
        } else {
            saveUserToDatabase(uid, null);
        }
    }

    private void saveUserToDatabase(String uid, String imageUrl) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("fullName", fullName.getText().toString());
        userInfo.put("email", email.getText().toString());
        userInfo.put("phone", phone.getText().toString());
        userInfo.put("birthday", birthday.getText().toString());
        userInfo.put("gender", female.isChecked() ? "female" : "male");
        userInfo.put("accountType", asLandlord.isChecked() ? "landlord" : "tenant");
        userInfo.put("profileImageUrl", imageUrl != null ? imageUrl : "");
        userInfo.put("timestamp", System.currentTimeMillis()); // Add timestamp
        userInfo.put("onlineStatus", "offline"); // Default to offline

        databaseReference.child(uid).setValue(userInfo)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    fAuth.signOut(); // Sign out to prevent immediate access
                    finish(); // Close registration activity
                })
                .addOnFailureListener(e -> showError("Failed to save user data: " + e.getMessage()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("onlineStatus");

            userStatusRef.setValue("online");
            userStatusRef.onDisconnect().setValue("offline"); // Automatically set to offline on disconnect
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userStatusRef = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("onlineStatus");

            userStatusRef.setValue("offline");
        }
    }


    private boolean validateAllFields() {
        if (!checkField(fullName)) return false;
        if (!checkField(email)) return false;
        if (!checkField(phone)) return false;
        if (!checkField(birthday)) return false;
        if (!checkField(password)) return false;
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }
        if (!termsConditions.isChecked()) {
            Toast.makeText(this, "You must agree to the terms and conditions.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkField(TextInputEditText textField) {
        String fullname = fullName.getText().toString().trim();
        String eMail = email.getText().toString().trim();
        String cPhone = phone.getText().toString().trim();
        String bDay = birthday.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (fullname.isEmpty()) {
            fullName.setError("Please enter your full name");
            fullName.requestFocus();
            return false;
        }else if (!isFirstLetterCapitalized(fullname)) {
            fullName.setError("First letter of each word must be capitalized");
            fullName.requestFocus();
            return false;
        } else {
            // Automatically capitalize the first letter of each word
            String formattedName = capitalizeWords(fullname);
            fullName.setText(formattedName);  // Set the formatted name back to the EditText
        }

        if (eMail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return false;
        } else if (!eMail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|net|edu|gov)$")) {
            email.setError("Enter a valid email address (e.g., user@example.com)");
            email.requestFocus();
            return false;
        }

        if (cPhone.isEmpty()) {
            phone.setError("Mobile No. is required");
            phone.requestFocus();
            return false;
        } else if (cPhone.length() != 11) {
            phone.setError("Mobile number must be exactly 11 digits");
            phone.requestFocus();
            return false;
        } else if (!cPhone.matches("^09\\d{9}$")) {
            phone.setError("Please enter a valid Philippine mobile number starting with '09'");
            phone.requestFocus();
            return false;
        } else if (!isValidPhilippinePrefix(cPhone)) {
            phone.setError("Invalid mobile number prefix for the Philippines");
            phone.requestFocus();
            return false;
        }


        if (bDay.isEmpty()) {
            birthday.setError("Date of birth is required");
            birthday.requestFocus();
            return false;
        }else if (!isAtLeast18YearsOld(bDay)) { // Ensure age is at least 18 years
            birthday.setError("You must be at least 18 years old");
            birthday.requestFocus();
            return false;
        }

        if (pass.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return false;
        } else if (pass.length() < 6) {
            password.setError("Password too weak (minimum 6 characters)");
            password.requestFocus();
            return false;
        } else if (!pass.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")) { // Regex for the password policy
            password.setError("Password must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number");
            password.requestFocus();
            return false;
        }if (!termsConditions.isChecked()) {
            Toast.makeText(this, "You must agree to the terms and conditions to proceed", Toast.LENGTH_SHORT).show();
            return false;  // If the checkbox is not checked, return false to prevent account creation
        }

        // If all checks pass, proceed with account creation
        return true;
    }

    private boolean isValidPhilippinePrefix(String cPhone) {
        // List of valid mobile number prefixes in the Philippines
        String[] validPrefixes = {
                "0905", "0906", "0907", "0908", "0909", "0910", "0912", "0915", "0916", "0917",
                "0918", "0919", "0920", "0921", "0922", "0923", "0925", "0926", "0927", "0928",
                "0929", "0930", "0935", "0936", "0937", "0938", "0939", "0940", "0942", "0943",
                "0945", "0946", "0947", "0948", "0949", "0950", "0951", "0955", "0956", "0957",
                "0961", "0963", "0965", "0966", "0967", "0970", "0971", "0973", "0974", "0975",
                "0977", "0978", "0979", "0981", "0989", "0991", "0992", "0993", "0994", "0995",
                "0996", "0997", "0998", "0999"
        };

        // Extract the first 4 digits (prefix) of the mobile number
        String prefix = cPhone.substring(0, 4);

        // Check if the prefix is in the list of valid prefixes
        for (String validPrefix : validPrefixes) {
            if (validPrefix.equals(prefix)) {
                return true;
            }
        }

        return false; // If prefix is not in the list, return false
    }

    private boolean isFirstLetterCapitalized(String fullname) {
        // Split the input into words
        String[] words = fullname.split("\\s+");

        for (String word : words) {
            // Check if the word is not empty and if the first letter is not uppercase
            if (word.length() > 0 && !Character.isUpperCase(word.charAt(0))) {
                return false; // If any word starts with a non-uppercase letter, return false
            }
        }

        return true; // All words start with an uppercase letter
    }

    private String capitalizeWords(String fullname) {
        String[] words = fullname.split("\\s+");  // Split the input string by spaces
        StringBuilder capitalizedName = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedName.append(word.substring(0, 1).toUpperCase());  // Capitalize the first letter
                capitalizedName.append(word.substring(1).toLowerCase());      // Lowercase the remaining letters
                capitalizedName.append(" ");  // Add space between words
            }
        }

        // Remove the last trailing space and return the formatted string
        return capitalizedName.toString().trim();
    }

    // Utility method to ensure the user is at least 18 years old
    private boolean isAtLeast18YearsOld(String bDay) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            sdf.setLenient(false); // Strict parsing to avoid invalid dates
            Date birthDate = sdf.parse(bDay);

            // Calendar instance for the current date
            Calendar today = Calendar.getInstance();

            // Calendar instance for the birth date
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTime(birthDate);

            // Calculate age
            int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);

            // Adjust age if the current date is before the user's birthday this year
            if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age >= 18; // Check if age is at least 18
        } catch (ParseException e) {
            return false; // Invalid date format
        }
    }


    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }
}