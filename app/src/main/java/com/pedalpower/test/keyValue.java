package com.pedalpower.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.FileNotFoundException;
import android.content.SharedPreferences;
import android.content.Context;

import static android.app.PendingIntent.getActivity;

//Key names : boolean INITIALIZED -> if false initialize all tops to 0 and dates to --/--/--
//Key names : TOP_TIME_1,       TOP_TIME_2,         TOP_TIME_3,         TOP_TIME_4,         TOP_TIME_5
//Key names : TOP_TIME_1_DATE,  TOP_TIME_2_DATE,    TOP_TIME_3_DATE,    TOP_TIME_4_DATE,    TOP_TIME_5_DATE
//Key names : TOP_POWER_1,      TOP_POWER_2,        TOP_POWER_3,        TOP_POWER_4,        TOP_POWER_5
//Key names : TOP_POWER_1_DATE, TOP_POWER_2_DATE,   TOP_POWER_3_DATE,   TOP_POWER_4_DATE,   TOP_POWER_5_DATE

public class keyValue {
    private static SharedPreferences.Editor EDITOR;
    private static final int MODE_PRIVATE = 0;
    private SharedPreferences sharedPreferences;
    private static String PREFS = "PREFS";

    //USED IN TUTORIAL, BUT DOESN'T WORK?
    public keyValue() {
        // Blank
    }
    public void initialize(Context context){

        setTime(context, 1, "0");
        setTime(context, 2, "0");
        setTime(context, 3, "0");
        setTime(context, 4, "0");
        setTime(context, 5, "0");
        setTimeDate(context, 1, "--/--/--");
        setTimeDate(context, 2, "--/--/--");
        setTimeDate(context, 3, "--/--/--");
        setTimeDate(context, 4, "--/--/--");
        setTimeDate(context, 5, "--/--/--");
        setPower(context, 1, "0");
        setPower(context, 2, "0");
        setPower(context, 3, "0");
        setPower(context, 4, "0");
        setPower(context, 5, "0");
        setPowerDate(context, 1, "--/--/--");
        setPowerDate(context, 2, "--/--/--");
        setPowerDate(context, 3, "--/--/--");
        setPowerDate(context, 4, "--/--/--");
        setPowerDate(context, 5, "--/--/--");

    }
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
    public static String getTime(Context context, int index) {
        if (index==1){ return getPrefs(context).getString("TOP_TIME_1", "0"); }
        if (index==2){ return getPrefs(context).getString("TOP_TIME_2", "0"); }
        if (index==3){ return getPrefs(context).getString("TOP_TIME_3", "0"); }
        if (index==4){ return getPrefs(context).getString("TOP_TIME_4", "0"); }
        if (index==5){ return getPrefs(context).getString("TOP_TIME_5", "0"); }
        return "error";
    }
    public static String getTimeDate(Context context, int index) {
        if (index==1){ return getPrefs(context).getString("TOP_TIME_1_DATE","--/--/--"); }
        if (index==2){ return getPrefs(context).getString("TOP_TIME_2_DATE","--/--/--"); }
        if (index==3){ return getPrefs(context).getString("TOP_TIME_3_DATE","--/--/--"); }
        if (index==4){ return getPrefs(context).getString("TOP_TIME_4_DATE","--/--/--"); }
        if (index==5){ return getPrefs(context).getString("TOP_TIME_5_DATE","--/--/--"); }
        return "error";
    }
   public static void setTime(Context context, int index, String input) {
       SharedPreferences.Editor editor = getPrefs(context).edit();
        if (index==1){ editor.putString("TOP_TIME_1", input); }
        if (index==2){ editor.putString("TOP_TIME_2", input); }
        if (index==3){ editor.putString("TOP_TIME_3", input); }
        if (index==4){ editor.putString("TOP_TIME_4", input); }
        if (index==5){ editor.putString("TOP_TIME_5", input); }
        editor.commit();
    }
    public static void setTimeDate(Context context, int index, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        if (index==1){ editor.putString("TOP_TIME_1_DATE", input); }
        if (index==2){ editor.putString("TOP_TIME_2_DATE", input); }
        if (index==3){ editor.putString("TOP_TIME_3_DATE", input); }
        if (index==4){ editor.putString("TOP_TIME_4_DATE", input); }
        if (index==5){ editor.putString("TOP_TIME_5_DATE", input); }
        editor.commit();
    }
    public static String getPower(Context context, int index) {
        if (index==1){ return getPrefs(context).getString("TOP_POWER_1", "0"); }
        if (index==2){ return getPrefs(context).getString("TOP_POWER_2", "0"); }
        if (index==3){ return getPrefs(context).getString("TOP_POWER_3", "0"); }
        if (index==4){ return getPrefs(context).getString("TOP_POWER_4", "0"); }
        if (index==5){ return getPrefs(context).getString("TOP_POWER_5", "0"); }
        return "error";
    }
    public static String getPowerDate(Context context, int index) {
        if (index==1){ return getPrefs(context).getString("TOP_POWER_1_DATE","--/--/--"); }
        if (index==2){ return getPrefs(context).getString("TOP_POWER_2_DATE","--/--/--"); }
        if (index==3){ return getPrefs(context).getString("TOP_POWER_3_DATE","--/--/--"); }
        if (index==4){ return getPrefs(context).getString("TOP_POWER_4_DATE","--/--/--"); }
        if (index==5){ return getPrefs(context).getString("TOP_POWER_5_DATE","--/--/--"); }
        return "error";
    }
    public static void setPower(Context context, int index, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        if (index==1){ editor.putString("TOP_POWER_1", input); }
        if (index==2){ editor.putString("TOP_POWER_2", input); }
        if (index==3){ editor.putString("TOP_POWER_3", input); }
        if (index==4){ editor.putString("TOP_POWER_4", input); }
        if (index==5){ editor.putString("TOP_POWER_5", input); }
        editor.commit();
    }
    public static void setPowerDate(Context context, int index, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        if (index==1){ editor.putString("TOP_POWER_1_DATE", input); }
        if (index==2){ editor.putString("TOP_POWER_2_DATE", input); }
        if (index==3){ editor.putString("TOP_POWER_3_DATE", input); }
        if (index==4){ editor.putString("TOP_POWER_4_DATE", input); }
        if (index==5){ editor.putString("TOP_POWER_5_DATE", input); }
        editor.commit();
    }
}



/*
* public class KeyValueDB {
private SharedPreferences sharedPreferences;
private static String PREF_NAME = "prefs";

    public KeyValueDB() {
    // Blank
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getUsername(Context context) {
        return getPrefs(context).getString("username_key", "default_username");
    }

    public static void setUsername(Context context, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
    editor.putString("username_key", input);
    editor.commit();
    }
}
*
* */