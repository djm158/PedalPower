package pedalpower;

import java.time.Clock;  
import java.time.LocalTime;
/**
 *  Used for time stamps and display
 * @author Angie
 */
public class time {
    int startCounter=0;
    int hour;
    int min;
    int sec;
    int nano;
    String str; //string of time for output
    
    void getTime(){
        LocalTime l = LocalTime.now(Clock.systemUTC());
        this.hour=l.getHour();
        this.min=l.getMinute();
        this.sec=l.getSecond();
        this.nano=l.getNano();
        
    }
    
    void elapsedTime(time t0, time t1){
        this.hour=    t1.hour -t0.hour;
        this.min=     t1.min  -t0.min;
        this.sec=     t1.sec  -t0.sec;
        this.nano=    t1.nano -t0.nano;
    }
    
    String printTime(){
        this.str=this.strTime();
        return this.str;
    }
    
    
    String strTime(){
        String strTime="strTime";
        /*
        
        if(this.hour<10){      strTime=strTime+"0"+this.hour+":";}
            if (this.hour>12){
                
            }
            else {          strTime=strTime+""+this.hour+":";}
        if(this.min<10){      strTime=strTime+"0"+this.min+":";}
            else {          strTime=strTime+""+this.min+":";}
        if(this.sec<10){      strTime=strTime+"0"+this.sec+":";}
            else {          strTime=strTime+""+this.sec+":";}
        this.str=(""+strTime);
        
        */
        return strTime;
    }
    
    
    
    
}
