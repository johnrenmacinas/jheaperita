package com.peritemacinas.bahi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Menu_Landlord extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu__landlord, container, false);

        TextView notifLandlordText = view.findViewById(R.id.notificationLandlord_click);

        notifLandlordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notifications.class);
                startActivity(intent);
            }
        });

        TextView faqText = view.findViewById(R.id.faqLandlord_click);

        faqText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FaQ.class);
                startActivity(intent);
            }
        });

        TextView tacLandlordText = view.findViewById(R.id.tacLandlord_click);

        tacLandlordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms_Conditions.class);
                startActivity(intent);
            }
        });

        TextView privacyLandlordText = view.findViewById(R.id.privacyLandlord_click);

        privacyLandlordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Privacy_Policy.class);
                startActivity(intent);
            }
        });

        TextView settingsLandlordText = view.findViewById(R.id.settingsLandlord_click);

        settingsLandlordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }
        });

        TextView trashLandlordText = view.findViewById(R.id.trashLandlord_click);

        trashLandlordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Trash_Landlord.class);
                startActivity(intent);
            }
        });

        TextView logoutLandlordText = view.findViewById(R.id.logoutLandlord_click);

        logoutLandlordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogIn.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

