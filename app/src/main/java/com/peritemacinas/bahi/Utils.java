package com.peritemacinas.bahi;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    // Static method to show a toast message
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static long getTimeStamp(){
        return System.currentTimeMillis();
    }

    public static String formatTimestampDate(Long timestamp){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);

        String date = DateFormat.format("dd/MM/yyyy", calendar).toString();

        return date;
    }

    public static byte[] getBytes(InputStream inputStream) {

        return new byte[0];
    }
}
