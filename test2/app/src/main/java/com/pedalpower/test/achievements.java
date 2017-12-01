package com.pedalpower.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class achievements extends AppCompatActivity {
    private TextView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        temp = (TextView) findViewById(R.id.topPower1);
        temp.setText(MainActivity.topTimesStrings[0][0]);
        temp = (TextView) findViewById(R.id.topPower2);
        temp.setText(MainActivity.topTimesStrings[1][0]);
        temp = (TextView) findViewById(R.id.topPower3);
        temp.setText(MainActivity.topTimesStrings[2][0]);
        temp = (TextView) findViewById(R.id.topPower4);
        temp.setText(MainActivity.topTimesStrings[3][0]);
        temp = (TextView) findViewById(R.id.topPower5);
        temp.setText(MainActivity.topTimesStrings[4][0]);



    }
}
