package com.flatcode.beautytouch.Unit;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.flatcode.beautytouch.R;

public class THEME {

    public static void setThemeOfApp(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            context.setTheme(R.style.OneTheme);
        } else if (sharedPreferences.getString("color_option", "TWO").equals("TWO")) {
            context.setTheme(R.style.TwoTheme);
        } else if (sharedPreferences.getString("color_option", "THREE").equals("THREE")) {
            context.setTheme(R.style.ThreeTheme);
        } else if (sharedPreferences.getString("color_option", "FOUR").equals("FOUR")) {
            context.setTheme(R.style.FourTheme);
        } else if (sharedPreferences.getString("color_option", "FIVE").equals("FIVE")) {
            context.setTheme(R.style.FiveTheme);
        } else if (sharedPreferences.getString("color_option", "SIX").equals("SIX")) {
            context.setTheme(R.style.SixTheme);
        } else if (sharedPreferences.getString("color_option", "SEVEN").equals("SEVEN")) {
            context.setTheme(R.style.SevenTheme);
        } else if (sharedPreferences.getString("color_option", "EIGHT").equals("EIGHT")) {
            context.setTheme(R.style.EightTheme);
        } else if (sharedPreferences.getString("color_option", "NINE").equals("NINE")) {
            context.setTheme(R.style.NineTheme);
        } else if (sharedPreferences.getString("color_option", "TEEN").equals("TEEN")) {
            context.setTheme(R.style.TeenTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            context.setTheme(R.style.OneNightTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_TWO").equals("NIGHT_TWO")) {
            context.setTheme(R.style.TwoNightTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_THREE").equals("NIGHT_THREE")) {
            context.setTheme(R.style.ThreeNightTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_FOUR").equals("NIGHT_FOUR")) {
            context.setTheme(R.style.FourNightTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_FIVE").equals("NIGHT_FIVE")) {
            context.setTheme(R.style.FiveNightTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_SIX").equals("NIGHT_SIX")) {
            context.setTheme(R.style.SixNightTheme);
        } else if (sharedPreferences.getString("color_option", "NIGHT_SEVEN").equals("NIGHT_SEVEN")) {
            context.setTheme(R.style.SevenNightTheme);
        }
    }
}