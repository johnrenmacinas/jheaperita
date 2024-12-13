package com.peritemacinas.bahi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Menu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        TextView notifText = view.findViewById(R.id.notification_click);

        notifText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notifications.class);
                startActivity(intent);
            }
        });

        TextView faqText = view.findViewById(R.id.faq_click);

        faqText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FaQ.class);
                startActivity(intent);
            }
        });

        TextView tacText = view.findViewById(R.id.tac_click);

        tacText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms_Conditions.class);
                startActivity(intent);
            }
        });

        TextView privacyText = view.findViewById(R.id.privacy_click);

        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Privacy_Policy.class);
                startActivity(intent);
            }
        });

        TextView settingsText = view.findViewById(R.id.settings_click);

        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class);
                startActivity(intent);
            }
        });

        TextView logoutText = view.findViewById(R.id.logout_click);

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogIn.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

