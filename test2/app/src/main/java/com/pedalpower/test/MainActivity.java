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
    static boolean testing=true;
    static String[][] topTimesStrings= new String[5][2]; //first index for top 5 in order, second for date
    static String[][] topPowerStrings= new String[5][2]; //first index for top 5 in order, second for date

public void main() throws FileNotFoundException, InterruptedException {
    //Variables to be saved to keys in phone
    println("In main of MainActivity.java ");

    SharedPreferences PREFS = getSharedPreferences("PREFS",0);
    if(!PREFS.contains("INITIALIZED")){
      //  keyValue.initialize();
        topTimesStrings[0][0]="2000";
        topTimesStrings[1][0]="1500";
        topTimesStrings[2][0]="1000";
        topTimesStrings[3][0]="100";
        topTimesStrings[4][0]="10";
    }

}



protected void onCreate(Bundle savedInstanceState) {
    powerOutputToScreen= (TextView)findViewById(R.id.currentPower);
    Log.d("tag", " Debug:                             In onCreate in MainActivity.java");
    super.onCreate(savedInstanceState);
    //run our main function
    try {this.main(); }
    catch (FileNotFoundException e) { e.printStackTrace(); } catch (InterruptedException e) {
        e.printStackTrace();
    }
    setContentView(R.layout.activity_main);

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

}
