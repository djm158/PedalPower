package com.pedalpower.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static java.sql.DriverManager.println;

public class startActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        println("In onCreate of startActivity.java ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
}
