package com.pedalpower.test;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by Angie on 11/13/2017.
 */
public class start extends AppCompatActivity {
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    //instantiate powerOutputto screen here to prevent Null pointer exception
    TextView powerOutputToScreen;
    data d;

    int totalPoints = 0;//has good and bad
    int badCounter;
    int errorCount = 0;
    //initialized to 1 to avoid divide by zero
    //high = 25.18 in W/kg from world class max https://www.trainingpeaks.com/blog/power-profiling/
    //25.18 W/kg * 1/2.20462 kg/lb = 11.42 W/lb => 12.00 W/lb :: Rounded up for potential future improvement
    double high = 12.00;    //W/lb
    double lastData = 0;

    public void main() throws FileNotFoundException, InterruptedException {

        Log.d("tag", " Debug:                             In Main in start.java");

        // /Variables-----------------------------------------------------------------------------------
        int lastSecond = 0;       //used to know when to update UI time
        int lastNano = 0;         //used to know when to update UI power :: starting with every 10th of a second
        double totalPower = 0;    //used for average power
        double totalTimeInms = 0;  //used for average power

        //gets the current Power and updates the app
        getData();
        int i;
        for(i=0;i<totalPoints;i++){
            Log.d("ADebugTag", "Line Graph Value: "+ (double)d.myPoints.get(i));
        }
        //updates the graph
        //series.appendData(new DataPoint(totalPoints , d.myPoints.get(totalPoints-1) ), true, 10);


        //printout all data after filtering and cleaning algorithms
        System.out.println("Total Points: " + totalPoints);
        System.out.println("Total errors: " + errorCount);
    }

    void addData(double newPoint) {
        totalPoints++;
        System.out.println("total points: " + totalPoints + "       totalPoints+1: " + (totalPoints + 1));
        Log.d("ADebugTag", "Value: " + Double.toString(newPoint));
        if (verify(newPoint) == true) {
            d.myPoints.add(newPoint);
            powerOutputToScreen.setText("" + newPoint);
            if (d.badData.isEmpty() == false) {
                badPoints bp = (badPoints) d.badData.get(0);
                if (totalPoints + 1 == bp.index + 5) {
                    lowPassFilter();
                }
            }
        } else {
            System.out.println("Data in Error :" + newPoint);
        }
    }

    boolean verify(double newData) { //always returns true:: used for bad data input
        if (newData < 0) {
            System.out.println("---Negative value acquired");
            System.out.println("Fill index" + totalPoints + 1);
            badPoints bp = new badPoints();
            bp.index = totalPoints + 1;
            bp.val = newData;
            d.badData.add(bp);
            errorCount++;
            if (d.badData.get(0) != null) {            //make sure there is bad data
                badPoints nextbp = (badPoints) d.badData.get(0);
                int badIndex = nextbp.index;
                System.out.println("Looking for: " + badIndex + "   At:" + this.totalPoints + 1);

                if (badIndex == this.totalPoints + 1 + 5) {
                    this.lowPassFilter();
                }
                return true;
            }
        }
        if (newData > high) {
            System.out.println("Number too high : " + newData + " > max: " + high);   //output for testing
            errorCount++;                                                       //increment error totalPoints+1
            badPoints bp = new badPoints();                                       //create new bad point
            bp.index = totalPoints + 1;                                                     //save badpoints's index
            bp.val = newData;                                                     //save bad point value
            d.badData.add(bp);                                               //add to end of arraylist(check value of list at 0 for next)
            //first index to look for
            badPoints nextbp = (badPoints) d.badData.get(0);               //check first array location
            int badIndex = nextbp.index;
            System.out.println("Looking for: " + badIndex + "   At:" + this.totalPoints + 1);
            return true; //return true, because we save bad data for low pass filter test
        }
        return true;
    }

    void getData() throws FileNotFoundException, InterruptedException {
        //initialize first bad data point
        //implement all bluetooth to get data UNLESS testing

    /*
    *
Once Bluetooth is hooked up just need to return it from this function.
    *
     */
        //implementation for testing:: getting a random number
        Random r = new Random();
        int whole = r.nextInt(1300);

        double randomDataPoint = (double) whole / 100;
        Log.d("ADebugTag", "Random: " + Double.toString(randomDataPoint));
        this.addData(randomDataPoint);
    }

    void lowPassFilter() {
        System.out.println("In low pass filter");
        //get all relative data points and find avereage
        int i;
        double temp, sumx = 0;
        int goodDataCount = 11;       //5 points before + badpoint+ 5 points after
        for (i = 0; i < 11; i++) {
            try {
                temp = (double) d.myPoints.get(this.totalPoints - i - 5);
                sumx = sumx + temp;
                System.out.println("Sum: " + sumx + "=" + temp + " + " + (sumx - temp));
            } catch (ArrayIndexOutOfBoundsException exception) {
                //there is another hole in data
                goodDataCount--;
            }
        }
        //get average
        temp = sumx / goodDataCount;
        d.myPoints.set(this.totalPoints - 6, temp);//fill with new floating average
        d.badData.remove(0);
    }

    void compress() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //should initialize all variables in on Create
        Log.d("tag", " Debug:                             In onCreate in start.java");

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>(new DataPoint[] {});


        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5.5);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(15.0);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        d=new data();
        int i;
        //initialize graphData to all 0s
        for (i=0;i<10;i++){
            d.graphData.add(i,0);
            series.appendData(new DataPoint(i, (double)d.graphData.get(i)), true,50,false);
        }

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_start);
        powerOutputToScreen= (TextView) findViewById(R.id.currentPowerStartActivity);
    }

    protected void onStart() {
        super.onStart();

        Log.d("tag", " Debug:                             In onStart in start.java");
    }

    protected void onResume() {
        super.onResume();
        Log.d("tag", " Debug:                             In onResume in start.java");
        // powerOutputToScreen=
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    main();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
       /*
       Timer imlplementation - only works one time
       try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {

                }
            };
            main();
            timer.schedule(timerTask, 100, 100);
        } catch (IllegalStateException e){
            android.util.Log.i("Damn", "resume error");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

    }
    //when screen is exitted the timer stops
    public void onPause(){
        super.onPause();

        //timer.cancel();
    }
}