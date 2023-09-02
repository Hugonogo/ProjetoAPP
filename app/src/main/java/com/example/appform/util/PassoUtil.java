package com.example.appform.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PassoUtil {

    public static void saveData(int previewCount, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("chave-passos", previewCount);
        editor.apply();
    }

}
