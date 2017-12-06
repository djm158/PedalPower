/*

PROJECT RUNS CORRECTLY

 */

package com.pedalpower.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import android.content.SharedPreferences;
import android.content.Context;

import static android.app.PendingIntent.getActivity;
import static java.sql.DriverManager.println;


public class MainActivity extends AppCompatActivity {
    TextView powerOutputToScreen;
    //all of the following will be saved as prefs//
    static long[][] topTimesStrings= new long[5][2]; //first index for top 5 in order(in seconds), second for date(int MMDDYYYY)
    static long[]topPowerStrings= new long[5]; //first index for top 5 in order, second for date
    static double[] topPower=new double[5];
    static long averageTime;
    static double averagePower;
    static long lastTime;
    static double lastPower;
    static int totalNumRides;

//  SharedPreferences sharedPref= getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//  SharedPreferences.Editor editor = sharedPref.edit();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public void main() throws FileNotFoundException, InterruptedException {
        //Variables to be saved to keys in phone
        println("In main of MainActivity.java ");
        getSharedPreferences();
        //all the shared prefrences things happen here --- information saved between the apps DIFFERENT runs
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        powerOutputToScreen= (TextView)findViewById(R.id.currentPowerLabel);
        Log.d("tag", " Debug:                             In onCreate in MainActivity.java");
        sharedPref= getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        //run our main function
        super.onStart();
        try {
            this.main();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void onStart() {

        super.onStart();
        setContentView(R.layout.activity_main);
    }

    protected void onDestroy() {
        super.onDestroy();
        saveSharedPreferences();
    }

    public void historyDisplay(View view) {
        Intent intent = new Intent(MainActivity.this, history.class);
        startActivity(intent);
    }

    public void achievementsDisplay(View view) {
        Intent intent = new Intent(MainActivity.this, achievements.class);
        startActivity(intent);
    }

    public void startDisplay(View view) {
        Intent intent = new Intent(MainActivity.this, start.class);
        startActivity(intent);
    }







    //----------------------------------------------------------------------------
    //-------------         SHARED PREFERENCES METHODS      ---------------------------
    //----------------------------------------------------------------------------

    /*
    static long[][] topTimesStrings= new long[5][2]; //first index for top 5 in order(in seconds), second for date(int MMDDYYYY)
    static long[]topPowerStrings= new long[5]; //first index for top 5 in order, second for date
    static double[] topPower=new double[5];
    long averageTime;
    double averagePower;
    long lastTime;
    double lastPower;
     */
    private void saveSharedPreferences() {
        //top times and associated dates
        editor.putLong("prefTopTimeString1",topTimesStrings[0][0]).commit();
        editor.putLong("prefTopTimeString1date",topTimesStrings[0][1]).commit();
        editor.putLong("prefTopTimeString2",topTimesStrings[1][0]).commit();
        editor.putLong("prefTopTimeString2date",topTimesStrings[1][1]).commit();
        editor.putLong("prefTopTimeString3",topTimesStrings[2][0]).commit();
        editor.putLong("prefTopTimeString3date",topTimesStrings[2][1]).commit();
        editor.putLong("prefTopTimeString4",topTimesStrings[3][0]).commit();
        editor.putLong("prefTopTimeString4date",topTimesStrings[3][1]).commit();
        editor.putLong("prefTopTimeString5",topTimesStrings[4][0]).commit();
        editor.putLong("prefTopTimeString5date",topTimesStrings[4][1]).commit();
        //top powers and associated dates
        editor.putLong("topPowerDate1", topPowerStrings[0]).commit();
        editor.putFloat("topPower1", (float)topPower[0]).commit();
        editor.putLong("topPowerDate2", topPowerStrings[1]).commit();
        editor.putFloat("topPower2", (float)topPower[1]).commit();
        editor.putLong("topPowerDate3", topPowerStrings[2]).commit();
        editor.putFloat("topPower3", (float)topPower[2]).commit();
        editor.putLong("topPowerDate4", topPowerStrings[3]).commit();
        editor.putFloat("topPower4", (float)topPower[3]).commit();
        editor.putLong("topPowerDate5", topPowerStrings[4]).commit();
        editor.putFloat("topPower5", (float)topPower[4]).commit();
        //Other shared preferences variables
        editor.putFloat("averagePower",(float)averagePower).commit();
        editor.putLong("averageTime", averageTime).commit();
        editor.putLong("lastTime", lastTime).commit();
        editor.putFloat("lastPower", (float)lastPower).commit();
        editor.putInt("totalNumRides", totalNumRides).commit();
    }
    private void getSharedPreferences() {
        if (sharedPref!=null) {
            //poit 1 [0]
            if (sharedPref.getLong("prefTopTimeString1", 0) == 0) {
                editor.putLong("prefTopTimeString1", 0).commit();
                editor.putLong("prefTopTimeString1date", 0).commit();
            }
            else {
                topTimesStrings[0][0] = sharedPref.getLong("prefTopTimeString1", 0);
                topTimesStrings[0][1] = sharedPref.getLong("prefTopTimeString1date", 0);
            }
            //point 2 [1]
            if (sharedPref.getLong("prefTopTimeString2", 0) == 0) {
                editor.putLong("prefTopTimeString2", 0).commit();
                editor.putLong("prefTopTimeString2date", 0).commit();
            }
            else {
                topTimesStrings[1][0] = sharedPref.getLong("prefTopTimeString2", 0);
                topTimesStrings[1][1] = sharedPref.getLong("prefTopTimeString2date", 0);
            }
            //point 3 [2]
            if (sharedPref.getLong("prefTopTimeString3", 0) == 0) {
                editor.putLong("prefTopTimeString3", 0).commit();
                editor.putLong("prefTopTimeString3date", 0).commit();
            } else {
                topTimesStrings[2][0] = sharedPref.getLong("prefTopTimeString3", 0);
                topTimesStrings[2][1] = sharedPref.getLong("prefTopTimeString3date", 0);
            }
            //point 4 [3]
            if (sharedPref.getLong("prefTopTimeString4", 0) == 0) {
                editor.putLong("prefTopTimeString4", 0).commit();
                editor.putLong("prefTopTimeString4date", 0).commit();
            } else {
                topTimesStrings[3][0] = sharedPref.getLong("prefTopTimeString4", 0);
                topTimesStrings[3][1] = sharedPref.getLong("prefTopTimeString4date", 0);
            }
            //point 5 [4]
            if (sharedPref.getLong("prefTopTimeString5", 0) == 0) {
                editor.putLong("prefTopTimeString5", 0).commit();
                editor.putLong("prefTopTimeString5date", 0).commit();
            } else {
                topTimesStrings[4][0] = sharedPref.getLong("prefTopTimeString5", 0);
                topTimesStrings[4][1] = sharedPref.getLong("prefTopTimeString5date", 0);
            }
//end times -> start powers
            //point 1 [0]
            if (sharedPref.getFloat("topPower1", 0) == 0) {
                editor.putFloat("topPower1", 0).commit();
                editor.putLong("topPowerDate1", 0).commit();
            }
            else {
                topPower[0]= sharedPref.getFloat("topPower1", 0);
                topPowerStrings[0]	= sharedPref.getLong("topPowerDate1", 0);
            }
            //point 2 [1]
            if (sharedPref.getFloat("topPower2", 0) == 0) {
                editor.putFloat("topPower2", 0).commit();
                editor.putLong("topPowerDate2", 0).commit();
            }
            else {
                topPower[1]= sharedPref.getFloat("topPower2", 0);
                topPowerStrings[1]	= sharedPref.getLong("topPowerDate2", 0);
            }
            //point 3 [2]
            if (sharedPref.getFloat("topPower3", 0) == 0) {
                editor.putFloat("topPower3", 0).commit();
                editor.putLong("topPowerDate3", 0).commit();
            }
            else {
                topPower[2]= sharedPref.getFloat("topPower3", 0);
                topPowerStrings[2]	= sharedPref.getLong("topPowerDate3", 0);
            }
            //point 4 [3]
            if (sharedPref.getFloat("topPower4", 0) == 0) {
                editor.putFloat("topPower4", 0).commit();
                editor.putLong("topPowerDate4", 0).commit();
            }
            else {
                topPower[3]= sharedPref.getFloat("topPower4", 0);
                topPowerStrings[3]	= sharedPref.getLong("topPowerDate4", 0);
            }
            //point 5 [4]
            if (sharedPref.getFloat("topPower5", 0) == 0) {
                editor.putFloat("topPower5", 0).commit();
                editor.putLong("topPowerDate5", 0).commit();
            }
            else {
                topPower[4]= sharedPref.getFloat("topPower5", 0);
                topPowerStrings[4]	= sharedPref.getLong("topPowerDate5", 0);
            }



        }
        averagePower=sharedPref.getFloat("averagePower", 0);
        lastPower=sharedPref.getFloat("lastPower", 0);

        averageTime=sharedPref.getLong("averageTime", 0);
        lastTime=sharedPref.getLong("lastTime", 0);

        totalNumRides=sharedPref.getInt("totalNumRides", 0);
    }

}
