package com.peritemacinas.bahi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.peritemacinas.bahi.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import android.Manifest;


public class ProfileEdit extends AppCompatActivity {

    private ActivityProfileEditBinding binding;

    private static final String TAG = "PROFILE_EDIT_TAG";

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        loadMyInfo();

        binding.addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

        binding.editProfileClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                validateData();
            }

        });
    }

    private String name = "";
    private String bday = "";
    private String mobileNum = "";


    private void validateData() {
        name = binding.editName.getText().toString().trim();
        bday = binding.editBday.getText().toString().trim();
        mobileNum = binding.editMobile.getText().toString().trim();

        if (imageUri == null) {
            updateProfileDb(null); // No image provided
        } else {
            try {
                encodeImageAndSaveToDb();
            } catch (Exception e) {
                Log.e(TAG, "validateData: Failed to encode image", e);
                Utils.toast(ProfileEdit.this, "Failed to process the image.");
            }
        }
    }

    private void encodeImageAndSaveToDb() {
        Log.d(TAG, "encodeImageAndSaveToDb: Encoding image...");
        progressDialog.setMessage("Processing image...");
        progressDialog.show();

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = Utils.getBytes(inputStream); // Utility to convert InputStream to byte[]
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            updateProfileDb(base64Image);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "encodeImageAndSaveToDb: File not found", e);
            Utils.toast(ProfileEdit.this, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "encodeImageAndSaveToDb: Failed to read image", e);
            Utils.toast(ProfileEdit.this, "Failed to read image: " + e.getMessage());
        } finally {
            progressDialog.dismiss();
        }
    }


    private void uploadProfileImageStorage(){
        Log.d(TAG, "uploadProfileImageStroage");

        progressDialog.setMessage("Uploading user profile image...");
        progressDialog.show();

        String filePathAndName = "UserImages/"+"profile_"+firebaseAuth.getUid();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        ref.putFile(imageUri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress =(100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        Log.d(TAG, "onProgress: Progress: "+progress);

                        progressDialog.setMessage("Uploading profile image. Progress: " + (int) progress + "%");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess Uploaded");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (uriTask.isSuccessful());

                        String uploadedImageUrl = uriTask.getResult().toString();

                        updateProfileDb(uploadedImageUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure", e);
                        progressDialog.dismiss();
                        Utils.toast(ProfileEdit.this, "Failed to upload profile image due to "+e.getMessage());
                    }
                });
    }

    private void updateProfileDb(String base64Image) {
        progressDialog.setMessage("Updating user info...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fullName", name);
        hashMap.put("birthday", bday);
        hashMap.put("phone", mobileNum);

        if (base64Image != null) {
            hashMap.put("profileImageBase64", base64Image); // Store Base64 string
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "updateProfileDb: Info updated");
                    progressDialog.dismiss();
                    Utils.toast(ProfileEdit.this, "Profile Updated Successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updateProfileDb: Failed to update info", e);
                    progressDialog.dismiss();
                    Utils.toast(ProfileEdit.this, "Failed to update profile due to " + e.getMessage());
                });
    }


    private void loadMyInfo() {
        Log.d(TAG, "loadMyInfo");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = "" + snapshot.child("fullName").getValue();
                        String bday = "" + snapshot.child("birthday").getValue();
                        String mobileNum = "" + snapshot.child("phone").getValue();
                        String base64Image = "" + snapshot.child("profileImageBase64").getValue();

                        binding.editBday.setText(bday);
                        binding.editName.setText(name);
                        binding.editMobile.setText(mobileNum);

                        if (base64Image != null && !base64Image.isEmpty()) {
                            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                            binding.profileDp.setImageBitmap(bitmap);
                        } else {
                            binding.profileDp.setImageResource(R.drawable.profile_def);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "loadMyInfo: Failed to load user info", error.toException());
                    }
                });
    }

    private void imagePickDialog(){

        PopupMenu popupMenu = new PopupMenu(this, binding.addProfilePic);

        popupMenu.getMenu().add(android.view.Menu.NONE, 1, 1, "Camera");
        popupMenu.getMenu().add(android.view.Menu.NONE, 2, 2, "Gallery");


        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == 1){

                    Log.d(TAG, "onMenuItemClick: Camera Clicked, check if camera permission(s) granted or not");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        requestCameraPermissions.launch(new String[]{Manifest.permission.CAMERA});
                    }
                    else{
                        requestCameraPermissions.launch(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    }
                }
                else if (itemId == 2){

                    Log.d(TAG, "onMenuItemClick: Check if storage permission in granted or not");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

                    } else {
                        requestStoragePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }
                return false;
            }
        });
    }


    private ActivityResultLauncher<String[]> requestCameraPermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    Log.d(TAG, "onActivityResult: "+result.toString());

                    boolean areAllGranted = true;
                    for (Boolean isGranted: result.values()){

                        areAllGranted = areAllGranted && isGranted;
                    }
                    if (areAllGranted) {
                        Log.d(TAG, "onActivityResult: All Granted e.g. Camera, Storage");
                        pickImageCamera();
                    }
                    else {
                        Log.d(TAG, "onActivityResult: All or either one is defined");
                        Utils.toast(ProfileEdit.this, "Camera or Storage or both permission denied...");
                    }
                }
            }
    );
    private ActivityResultLauncher<String> requestStoragePermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    Log.d(TAG, "onActivityResult: isGranted: " + isGranted);

                    if (isGranted) {
                        pickImageGallery();
                    } else {
                        Utils.toast(ProfileEdit.this, "Storage permission denied...");
                    }
                }
            }
    );

    private void checkStoragePermissionAndPickImage() {
        // Check if permission is already granted
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickImageGallery();
        } else {
            // Request the permission
            requestStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }



    private void pickImageCamera(){
        Log.d(TAG, "pickImageCamera");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "TEMP_TITLE");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "TEMP_DESCRIPTION");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK){

                        Log.d(TAG, "onActivityResult: Image Captured: "+imageUri);

                        try {
                            Glide.with(ProfileEdit.this)
                                    .load(imageUri)
                                    .placeholder(R.drawable.profile_def)
                                    .into(binding.profileDp);
                        } catch (Exception e){
                            Log.e(TAG, "onActivityResult: ", e);
                        }
                    }
                    else {
                        Utils.toast(ProfileEdit.this, "Cancelled...");
                    }
                }
            }
    );

    private void pickImageGallery() {
        Log.d(TAG, "pickImageGallery");

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }


    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            Log.d(TAG, "onActivityResult: Image picked: " + imageUri);

                            // Load the selected image into your ImageView
                            Glide.with(ProfileEdit.this)
                                    .load(imageUri)
                                    .placeholder(R.drawable.profile_def)
                                    .into(binding.profileDp);
                        }
                    } else {
                        Utils.toast(ProfileEdit.this, "Cancelled...");
                    }
                }
            }
    );

}