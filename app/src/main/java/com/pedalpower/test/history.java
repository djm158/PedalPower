package com.pedalpower.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class history extends AppCompatActivity {
    private TextView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        temp = (TextView) findViewById(R.id.AverageTimeOutput);
        temp.setText(LongToString(MainActivity.averageTime));
        temp = (TextView) findViewById(R.id.AveragePowerOutput);
        temp.setText(String.format("%4.2f", MainActivity.averagePower));

        temp = (TextView) findViewById(R.id.LastTimeOutput);
        temp.setText(LongToString(MainActivity.lastTime));
        temp = (TextView) findViewById(R.id.LastAveragePowerOutput);
        temp.setText(String.format("%4.2f", MainActivity.lastPower));



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

}
