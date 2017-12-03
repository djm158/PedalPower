package com.pedalpower.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class achievements extends AppCompatActivity {
    private TextView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        //convert ints to Strings for output
        //String topTime1=MainActivity.topTimesStrings[0][0];
        temp = (TextView) findViewById(R.id.topTime1);
        temp.setText(LongToString(MainActivity.topTimesStrings[0][0]));
        temp = (TextView) findViewById(R.id.topTime2);
        temp.setText(LongToString(MainActivity.topTimesStrings[1][0]));
        temp = (TextView) findViewById(R.id.topTime3);
        temp.setText(LongToString(MainActivity.topTimesStrings[2][0]));
        temp = (TextView) findViewById(R.id.topTime4);
        temp.setText(LongToString(MainActivity.topTimesStrings[3][0]));
        temp = (TextView) findViewById(R.id.topTime5);
        temp.setText(LongToString(MainActivity.topTimesStrings[4][0]));

        temp = (TextView) findViewById(R.id.topTime1date);
        temp.setText(getDate(MainActivity.topTimesStrings[0][1]));
        temp = (TextView) findViewById(R.id.topTime2date);
        temp.setText(getDate(MainActivity.topTimesStrings[1][1]));
        temp = (TextView) findViewById(R.id.topTime3date);
        temp.setText(getDate(MainActivity.topTimesStrings[2][1]));
        temp = (TextView) findViewById(R.id.topTime4date);
        temp.setText(getDate(MainActivity.topTimesStrings[3][1]));
        temp = (TextView) findViewById(R.id.topTime5date);
        temp.setText(getDate(MainActivity.topTimesStrings[4][1]));

        //powers

        temp = (TextView) findViewById(R.id.topPower1);
        temp.setText(String.format("%4.2f", MainActivity.topPower[0]));
        temp = (TextView) findViewById(R.id.topPower2);
        temp.setText(String.format("%4.2f", MainActivity.topPower[1]));
        temp = (TextView) findViewById(R.id.topPower3);
        temp.setText(String.format("%4.2f", MainActivity.topPower[2]));
        temp = (TextView) findViewById(R.id.topPower4);
        temp.setText(String.format("%4.2f", MainActivity.topPower[3]));
        temp = (TextView) findViewById(R.id.topPower5);
        temp.setText(String.format("%4.2f", MainActivity.topPower[4]));

        temp = (TextView) findViewById(R.id.topPower1date);
        temp.setText(getDate(MainActivity.topPowerStrings[0]));
        temp = (TextView) findViewById(R.id.topPower2date);
        temp.setText(getDate(MainActivity.topPowerStrings[1]));
        temp = (TextView) findViewById(R.id.topPower3date);
        temp.setText(getDate(MainActivity.topPowerStrings[2]));
        temp = (TextView) findViewById(R.id.topPower4date);
        temp.setText(getDate(MainActivity.topPowerStrings[3]));
        temp = (TextView) findViewById(R.id.topPower5Date);
        temp.setText(getDate(MainActivity.topPowerStrings[4]));


    }
    String LongToString(long time){
        String s,m,h;
        int seconds = (int) (time/ 1000) % 60 ;
        int minutes = (int) ((time/ (1000*60)) % 60);
        int hours   = (int) ((time / (1000*60*60)) % 24);

        if (seconds<10){ s="0"+seconds;       }
        else{  s=""+seconds;  }

        if (minutes<10){ m="0"+minutes;       }
        else{  m=""+minutes;  }

        if (hours<10){ h="0"+hours;       }
        else{  h=""+hours;  }

        return ""+h+":"+m+":"+s;
    }
    public static String getDate(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
