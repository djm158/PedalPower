package pedalpower;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *Angela Hoeltje
 */
public class data {
    ArrayList myPoints=new ArrayList();
    ArrayList badData=new ArrayList();
    ArrayList badIndexList=new ArrayList(); //append bad index to back-check from front
    
    //testing variables
    boolean lowPassFilterTest=false;
    boolean fit=true;
    
    
    int badCounter;
    int errorCount=0;
    int count=1;                            //initialized to 1 to avoid divide by zero
    double average=0;                       //initialized to 0, because ASSUMPTION: must always begin at stopped position
    //high = 25.18 in W/kg from world class max https://www.trainingpeaks.com/blog/power-profiling/
    //25.18 W/kg * 1/2.20462 kg/lb = 11.42 W/lb => 12.00 W/lb :: Rounded up for potential future improvement
    double high = 12.00;    //W/lb       
    double lastData=0;
    double variationConstant=1000;          //Need to find a constant for this
    void addData(double newPoint){
        
        if (this.verify(newPoint)==true){
            this.myPoints.add(newPoint);
            //check for tests
            if (lowPassFilterTest){
                //next bad point
                if (badData.isEmpty()==false){
                    badPoints bp=(badPoints)this.badData.get(0);
                    if (this.count==bp.index +5){
                        this.lowPassFilter();
                    }
                }
            }
            if (fit){
                if (badData.isEmpty()==false){
                    badPoints bp=(badPoints)this.badData.get(0);
                    if (this.count==bp.index +5){
                        System.out.println("fit");
                        this.fit();
                    }
                }
            }
        }
        else{
            System.out.println("Data in Error :"+ newPoint);
        }
        
    }
    boolean verify(double newData){
        if(newData<0){
            System.out.println("---Negative value acquired");
            System.out.println("Fill index"+this.count);
            badPoints bp=new badPoints();
            bp.index=count;
            bp.val=newData;
            this.badData.add(bp);
            errorCount++;
            this.badIndexList.add(this.count);
            
            if (lowPassFilterTest){//first index to look for
                if (this.badData.get(0)!=null){            //make sure there is bad data
                    badPoints nextbp= (badPoints)this.badData.get(0);       
                    int badIndex=nextbp.index;
                    System.out.println("Looking for: "+badIndex+ "   At:"+this.count);
                    if (badIndex==this.count+5){
                        this.lowPassFilter();
                        this.badIndexList.remove(0);
                    }
                    return true;
                }
            }
            if (fit){ //first index to look for
                if (this.badData.get(0)!=null){             //make sure there is bad data
                    badPoints nextbp= (badPoints)this.badData.get(0);       
                    int badIndex=nextbp.index;
                    System.out.println("fit: Looking for: "+badIndex+ "   At:"+this.count);
                    if (badIndex==this.count+5){
                        this.fit();
                        this.badIndexList.remove(0);
                    }
                    return true;
                }
            }
            return false;
        }
        if(newData>high){
            System.out.println("Number too high : "+newData+" > max: "+high);
            errorCount++;
            badPoints bp=new badPoints();
            bp.index=count;
            bp.val=newData;
            this.badData.add(bp);
            errorCount++;
            this.badIndexList.add(this.count);
            if (lowPassFilterTest){
                //first index to look for
                badPoints nextbp= (badPoints)this.badData.get(0);
                int badIndex=nextbp.index;
                System.out.println("Looking for: "+badIndex+ "   At:"+this.count);
                
                return true; //return true, because we save bad data for low pass filter test
            }
            if (fit){
                 //first index to look for
                badPoints nextbp= (badPoints)this.badData.get(0);
                int badIndex=nextbp.index;
                System.out.println("Looking for: "+badIndex+ "   At:"+this.count);
                return true; //return true, because we save bad data for low pass filter test
            }
                
            
             
        }   
       
    if (abs(newData-lastData)>=variationConstant){
            //NEED TO ADD ONCE VARIATION IS DECIDED
        }
        return true;
    }
    void getData () throws FileNotFoundException{
        //initialize first bad data point
    
        int i=-1;//just for testing/printing purposes
        double newPoint=0;
        //implement all bluetooth to get data UNLESS testing
        if(PedalPower.testing==true){
            //get value from text file to test
            try{
                File temp=new File("data.txt"); 
                Scanner inFile = new Scanner(temp);
                //System.out.println("At testing scanner");
                while (inFile.hasNext()){
                    this.count++;//remove for better testing
                    //System.out.println("Reading "+i);
                    this.addData(inFile.nextDouble());         
                }

                inFile.close();
            }
             catch(FileNotFoundException exception){
                 System.out.println("COuld not find file: ");
             }
       
            
        }
        
        
    }
        void lowPassFilter(){
            System.out.println ("In low pass filter");
            //get all relative data points and find avereage
            int i;
            double temp=0;
            
            //5 points before and after
            
            for(i=0;i<10;i++){
                temp=temp+(double)this.myPoints.get(count-i-5);
            }
            //get average
            temp=temp/11;
            this.myPoints.add(count-5, temp);//fill with new floating average
            this.badData.remove(0);
        }
        
        void fit(){
            System.out.println("In fit");
            int n = 10;
            double[] x = new double[10];
            double sumx=0;
            double sumy=0;
            int i=0;
            for (i=0;i<10;i++){
                double temp=(double)this.myPoints.get(this.count-i-5);

                x[i]=temp;
                sumx=sumx+temp;
            }


            double[] y = new double[]{1,1,1  ,1,1,1  ,1,1,1  ,1};
            sumy=10;

            double xbar = sumx / n;
            double ybar = sumy / n;

            // second pass: compute summary statistics
            double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
            for ( i = 0; i < n; i++) {
                xxbar += (x[i] - xbar) * (x[i] - xbar);
                yybar += (y[i] - ybar) * (y[i] - ybar);
                xybar += (x[i] - xbar) * (y[i] - ybar);
            }
            double beta1 = xybar / xxbar;
            double beta0 = ybar - beta1 * xbar;

            // print results
            //System.out.println("y   = " + beta1 + " * x + " + beta0);

            //my missing point
            double point=0;
            this.myPoints.add(this.count-5,point);
            
            //remove the point that has been filled
            this.badData.remove(0);

    }
    
    
}


        /*
        FROM: http://www.oracle.com/technetwork/articles/javame/index-156193.html
        You use the DiscoveryAgent's device – discovery methods to initiate and cancel device discovery:

        retrieveDevices() retrieves already discovered or known devices that are nearby.
        startInquiry() initiates discovery of nearby devices, also called inquiry.
        cancelInquiry() cancels any inquiry presently in progress.
        The Bluetooth Discovery Agent invokes the DiscoveryListener's device – discovery callback methods at different points in the inquiry phase:

        deviceDiscovered()indicates whether a device has been discovered.
        inquiryCompleted()indicates whether the inquiry has succeeded, triggered an error, orbeen canceled.
        The state diagram in Figure 6 illustrates the device-discovery states reached as a result of DiscoveryListener callbacks:
        
        You use the DiscoveryAgent's service-discovery methods to initiate and cancel service discovery:

        selectService() initiates service discovery.
        searchServices() initiates service discovery.
        cancelServiceSearch() cancels any service discovery operation presently in progress.
        The Bluetooth Discovery Agent invokes DiscoveryListener service-discovery callback methods at different points in the service-discovery phase:

        servicesDiscovered() indicates whether services have been discovered.
        serviceSearchCompleted() indicates that service discovery has completed.
        Figure 7 illustrates the service discovery states reached as a result of DiscoveryListener callbacks:
        */
        
        
        
        
    



