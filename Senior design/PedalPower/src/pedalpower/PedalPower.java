package pedalpower;
import java.io.FileNotFoundException;
/**
 * @author Angie Hoeltje
 */
public class PedalPower {
    public static boolean testing=true;
    public static boolean testingPrintToScreen;
    public static boolean testinPrintToFile;

    public static void main(String[] args) throws FileNotFoundException {
        //main variables
         
//Variables-----------------------------------------------------------------------------------
        data d=new data();
        time startTime=new time();
        time currentTime=new time();
        time timeDifference=new time();
        int lastSecond=0;       //used to know when to update UI time
        int lastNano=0;         //used to know when to update UI power :: starting with every 10th of a second
        double totalPower=0;    //used for average power
        double totalTimeInms=0;  //used for average power
        
 //initialize-----------------------------------------------------------------------------------
           
        startTime.getTime();
        startTime.printTime();
        currentTime.getTime();
        //timeTest();                     //---testing function for time class
       
       
        //get information via blluetooth
        if (!testing){
            d.getData();
        }
        //newData.getData.myListener();
        if (testing){
            d.getData();
        }
        
        
        //printout all data after filtering and cleaning algorithms
        System.out.println("Total Points: "+d.count);
        System.out.println("Total errors: "+d.errorCount);
        System.out.println("New Data: ");
        int i;
        for (i=0;i<d.totalPoints;i++){
            System.out.println(" "+d.myPoints.get(i) );
        }
        
        
        
        
    }
    public static void initialize(){
        
    }
    public static void timeTest(){
                time startTime=new time();
        time currentTime=new time();
        time timeDifference=new time();
        int lastSecond=0;       //used to know when to update UI time
        int lastNano=0;         //used to know when to update UI power :: starting with every 10th of a second
        double totalPower=0;    //used for average power
        double totalTimeInms=0;  //used for average power
        
 //initialize-----------------------------------------------------------------------------------
           
        startTime.getTime();
        startTime.printTime();
        currentTime.getTime();
        
        //test output--- update every second
        int i=0;
        while (i<10000){
            System.out.println("Start time:"+startTime.str);
            System.out.println("current time:"+currentTime.str);
            lastSecond=startTime.sec;
            int wait=1;            
            while (wait==1){
                currentTime.getTime();
                //System.out.println ("Current: "+currentTime.sec+"    lastSecond: "+lastSecond);        
                if (lastSecond<50){
                    if (lastSecond==currentTime.sec-10){
                        lastSecond=currentTime.sec;
                        wait=0;
                        System.out.print("\n"+i+". Start: "+startTime.str+"  Current: ");
                        currentTime.printTime();
                        System.out.print("  Elapsed: ");
                        timeDifference.elapsedTime(startTime, currentTime);
                        
                    }
               }
                else{
                    if (lastSecond==currentTime.sec+50){
                        wait=0;
                    }
                }
            }
            
            i++;
        }
    }
    
}
