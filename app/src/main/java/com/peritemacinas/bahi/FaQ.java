package com.peritemacinas.bahi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FaQ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_q);

        TextView answer1_Text = findViewById(R.id.answer1);
        TextView answer2_Text = findViewById(R.id.answer2);
        TextView answer3_Text = findViewById(R.id.answer3);
        TextView answer4_Text = findViewById(R.id.answer4);
        TextView answer5_Text = findViewById(R.id.answer5);
        TextView answer6_Text = findViewById(R.id.answer6);
        TextView answer7_Text = findViewById(R.id.answer7);
        TextView answer8_Text = findViewById(R.id.answer8);
        TextView answer9_Text = findViewById(R.id.answer9);
        TextView answer10_Text = findViewById(R.id.answer10);
        TextView answer11_Text = findViewById(R.id.answer11);
        TextView answer12_Text = findViewById(R.id.answer12);
        TextView answer13_Text = findViewById(R.id.answer13);
        TextView answer14_Text = findViewById(R.id.answer14);
        TextView answer15_Text = findViewById(R.id.answer15);
        TextView answer16_Text = findViewById(R.id.answer16);

        ImageView btn1 = findViewById(R.id.btnQestion1);
        ImageView btn2 = findViewById(R.id.btnQestion2);
        ImageView btn3 = findViewById(R.id.btnQestion3);
        ImageView btn4 = findViewById(R.id.btnQestion4);
        ImageView btn5 = findViewById(R.id.btnQestion5);
        ImageView btn6 = findViewById(R.id.btnQestion6);
        ImageView btn7 = findViewById(R.id.btnQestion7);
        ImageView btn8 = findViewById(R.id.btnQestion8);
        ImageView btn9 = findViewById(R.id.btnQestion9);
        ImageView btn10 = findViewById(R.id.btnQestion10);
        ImageView btn11 = findViewById(R.id.btnQestion11);
        ImageView btn12 = findViewById(R.id.btnQestion12);
        ImageView btn13 = findViewById(R.id.btnQestion13);
        ImageView btn14 = findViewById(R.id.btnQestion14);
        ImageView btn15 = findViewById(R.id.btnQestion15);
        ImageView btn16 = findViewById(R.id.btnQestion16);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer1_Text.getVisibility() == View.GONE){
                    answer1_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer1_Text.setVisibility(View.GONE);
                    btn1.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer2_Text.getVisibility() == View.GONE){
                    answer2_Text.setVisibility(View.VISIBLE);
                    btn2.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer2_Text.setVisibility(View.GONE);
                    btn2.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer3_Text.getVisibility() == View.GONE){
                    answer3_Text.setVisibility(View.VISIBLE);
                    btn3.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer3_Text.setVisibility(View.GONE);
                    btn4.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer4_Text.getVisibility() == View.GONE){
                    answer4_Text.setVisibility(View.VISIBLE);
                    btn4.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer4_Text.setVisibility(View.GONE);
                    btn1.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer5_Text.getVisibility() == View.GONE){
                    answer5_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer5_Text.setVisibility(View.GONE);
                    btn5.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer6_Text.getVisibility() == View.GONE){
                    answer6_Text.setVisibility(View.VISIBLE);
                    btn6.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer6_Text.setVisibility(View.GONE);
                    btn6.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer7_Text.getVisibility() == View.GONE){
                    answer7_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer7_Text.setVisibility(View.GONE);
                    btn7.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer8_Text.getVisibility() == View.GONE){
                    answer8_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer8_Text.setVisibility(View.GONE);
                    btn8.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer9_Text.getVisibility() == View.GONE){
                    answer9_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer9_Text.setVisibility(View.GONE);
                    btn9.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer10_Text.getVisibility() == View.GONE){
                    answer10_Text.setVisibility(View.VISIBLE);
                    btn10.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer10_Text.setVisibility(View.GONE);
                    btn10.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer11_Text.getVisibility() == View.GONE){
                    answer11_Text.setVisibility(View.VISIBLE);
                    btn11.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer11_Text.setVisibility(View.GONE);
                    btn11.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer12_Text.getVisibility() == View.GONE){
                    answer12_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer12_Text.setVisibility(View.GONE);
                    btn12.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer13_Text.getVisibility() == View.GONE){
                    answer13_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer13_Text.setVisibility(View.GONE);
                    btn1.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer14_Text.getVisibility() == View.GONE){
                    answer14_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer14_Text.setVisibility(View.GONE);
                    btn14.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer15_Text.getVisibility() == View.GONE){
                    answer15_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer15_Text.setVisibility(View.GONE);
                    btn1.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });

        btn16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answer16_Text.getVisibility() == View.GONE){
                    answer16_Text.setVisibility(View.VISIBLE);
                    btn1.setImageResource(R.drawable.baseline_arrow_up_24);
                } else {
                    answer16_Text.setVisibility(View.GONE);
                    btn16.setImageResource(R.drawable.baseline_arrow_down_24);
                }
            }
        });
    }
}