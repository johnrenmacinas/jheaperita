package com.peritemacinas.bahi;

import android.content.Intent;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.peritemacinas.bahi.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    private FragmentProfileBinding binding;

    private static final String TAG = "ACCOUNT_TAG";

    private FirebaseAuth firebaseAuth;

    private Context mContext;


    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Use the provided LayoutInflater to initialize the binding
        binding = FragmentProfileBinding.inflate(LayoutInflater.from(mContext), container, false);

        // Return the root view of the binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        //Delete Account
        binding.deleteAccountCv.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), DeleteAccount.class));
            requireActivity().finishAffinity();
        });

        // Change Password
        binding.changePasswordCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(mContext,ChangePasswordActivity.class));
            }
        });

        loadMyInfo();
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String bday = ""+snapshot.child("birthday").getValue();
                        String email = ""+snapshot.child("email").getValue();
                        String name = ""+snapshot.child("fullName").getValue();
                        String gender = ""+snapshot.child("gender").getValue();
                        String mobileNum = ""+snapshot.child("phone").getValue();
                        String profileImage = ""+snapshot.child("profileImageUrl").getValue();
                        String account_Type = ""+snapshot.child("accountType").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();


                        if (timestamp.equals("null")){
                            timestamp = "0";
                        }
                        String formattedDate = Utils.formatTimestampDate(Long.parseLong(timestamp));

                        //set data  to UI
                        binding.showEmail.setText(email);
                        binding.showFullName.setText(name);
                        binding.showBday.setText(bday);
                        binding.showGender.setText(gender);
                        binding.showContact.setText(mobileNum);
                        binding.showMemberSince.setText(formattedDate);


                       if (email.equals("Email")){
                            boolean isVerified = firebaseAuth.getCurrentUser().isEmailVerified();
                            if (isVerified){
                                binding.showVerification.setText("Verified");
                            } else {
                                binding.showVerification.setText("Not Verified");
                            }
                       }
                       else {
                           binding.showVerification.setText("Verified");
                       }

                       try {
                           Glide.with(mContext)
                                   .load(profileImage)
                                   .placeholder(R.drawable.profile_def)
                                   .into(binding.profileDp);
                       }
                       catch (Exception e) {
                           Log.e(TAG, "onDataChange: ", e);
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }

}