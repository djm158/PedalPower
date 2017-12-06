package com.pedalpower.test;
import java.util.ArrayList;
/**
 *Angela Hoeltje
 * data()
 *      void addData(double newPoint)
 *      boolean verify(double newData)
 *      void getData ()
 *      void lowPassFilter()
 *
 */
public class data extends start {
    ArrayList myPoints = new ArrayList();                 //when myPOints==max -> compress -> zeroout
    ArrayList badData = new ArrayList();
    ArrayList<Double>  graphData = new ArrayList();
    //initialized to 1 to avoid divide by zero
    //high = 25.18 in W/kg from world class max https://www.trainingpeaks.com/blog/power-profiling/
    //25.18 W/kg * 1/2.20462 kg/lb = 11.42 W/lb => 12.00 W/lb :: Rounded up for potential future improvement

}