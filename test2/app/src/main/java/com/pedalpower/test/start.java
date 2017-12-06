package com.pedalpower.test;
import android.Manifest;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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
    //graph variables
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    TextView powerOutputToScreen;
    //variables used to run ever 500 ms
    private Runnable myTimer;
    private final Handler myHandler = new Handler();
    //time elapsed variables for each run
    long start;
    long end;
    long totalTimeSpent;
    //for top power
    double powerHigh=0;

    data d;
    double sumPower;
    double numRidesCompleted;

    int totalPoints = 0;//has good and bad
    int errorCount = 0;
    int countTo20 = 0;
    double high = 12.00;    //W/lb
    double lastData = 0;
    int lastSecond = 0;       //used to know when to update UI time
    int lastNano = 0;         //used to know when to update UI power :: starting with every 10th of a second
    double totalPower = 0;    //used for average power
    double totalTimeInms = 0;  //used for average power

    // UUIDs for UAT service and associated characteristics.
    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID TX_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID RX_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    // UUID for the BTLE client characteristic which is necessary for notifications.
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // BTLE state
    private BluetoothAdapter adapter;
    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;

    private TextView messages;

    void addData(double newPoint) {
        countTo20++;
        totalPoints++;
        System.out.println("total points: " + totalPoints + "       totalPoints+1: " + (totalPoints + 1));
        Log.d("ADebugTag", "Value: " + Double.toString(newPoint));
        if (verify(newPoint) == true) {
            System.out.println("=======================================================in  verify ");
            if (newPoint>powerHigh){
                powerHigh=newPoint;
            }
            sumPower=sumPower+newPoint;
            d.myPoints.add(newPoint);
           // powerOutputToScreen.setText("" + newPoint);

            setPower(String.format("%.02f",newPoint));
            DataPoint dp = new DataPoint(countTo20, newPoint);
            series.appendData(dp, true, 20);

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
        Log.d("ADebugTag", "IN get Data!");
        //initialize first bad data point
        //implement all bluetooth to get data UNLESS testing

    /*
    *
Once Bluetooth is hooked up just need to return it from this function.
    *
     */
        //implementation for testing:: getting a random number
        Random r = new Random();
        int whole = r.nextInt(1500);

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
        sumPower = sumPower+temp;
        d.myPoints.set(this.totalPoints - 6, temp);//fill with new floating average
        d.badData.remove(0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // init messages view
        messages = (TextView) findViewById(R.id.messages);

        //get start time
        start = Calendar.getInstance().getTimeInMillis();
        Log.d("tag", " Debug:                             start:"+start);

        //should initialize all variables in on Create
        Log.d("tag", " Debug:                             In onCreate in start.java");
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        d = new data();

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(20);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(15.0);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        powerOutputToScreen = (TextView) findViewById(R.id.currentPowerStartActivity);

        adapter = BluetoothAdapter.getDefaultAdapter();
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }
    }

    protected void onStart() {
        super.onStart();
        powerHigh=0;
        long start = Calendar.getInstance().getTimeInMillis();

        Log.d("tag", " Debug:                             In onStart in start.java");
    }

    protected void onResume() {
        MainActivity.totalNumRides++;
        super.onResume();
        powerHigh=0;
        Log.d("tag", " Debug:                             In onResume before runnable in start.java");

        writeLine("Scanning for devices...");
        adapter.startLeScan(scanCallback);

    }
    private void setPower(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                powerOutputToScreen.setText(text);
            }
        });
    }

    @Override
   protected void onStop() {
       super.onStop();
       long end = Calendar.getInstance().getTimeInMillis();
       /*
       FOr the top times screen=------------------------------------------------------------------------------------------------------------------------
        */
        long totalTimeSpent = end-start;
        //see if longer than the lowest top Time String

        MainActivity.lastPower=sumPower/totalPoints;
        MainActivity.lastTime=totalTimeSpent;
        long temp= (long) (MainActivity.averageTime * (numRidesCompleted));
        temp=temp+totalTimeSpent;
        temp= (long) (temp/(numRidesCompleted+1));
        MainActivity.averageTime=temp;
        MainActivity.averagePower=(MainActivity.averagePower*(numRidesCompleted)+MainActivity.lastPower)/(numRidesCompleted+1);


        if (MainActivity.topTimesStrings[0][0]<totalTimeSpent){//rotate 4 down

            MainActivity.topTimesStrings[4][0]=MainActivity.topTimesStrings[3][0];
            MainActivity.topTimesStrings[4][1]=MainActivity.topTimesStrings[3][1];

            MainActivity.topTimesStrings[3][0]=MainActivity.topTimesStrings[2][0];
            MainActivity.topTimesStrings[3][1]=MainActivity.topTimesStrings[2][1];

            MainActivity.topTimesStrings[2][0]=MainActivity.topTimesStrings[1][0];
            MainActivity.topTimesStrings[2][1]=MainActivity.topTimesStrings[1][1];

            MainActivity.topTimesStrings[1][0]=MainActivity.topTimesStrings[0][0];
            MainActivity.topTimesStrings[1][1]=MainActivity.topTimesStrings[0][1];

            MainActivity.topTimesStrings[0][0]=totalTimeSpent;
            MainActivity.topTimesStrings[0][1]=start;
         }
         else if(MainActivity.topTimesStrings[1][0]<totalTimeSpent){
            MainActivity.topTimesStrings[4][0]=MainActivity.topTimesStrings[3][0];
            MainActivity.topTimesStrings[4][1]=MainActivity.topTimesStrings[3][1];

            MainActivity.topTimesStrings[3][0]=MainActivity.topTimesStrings[2][0];
            MainActivity.topTimesStrings[3][1]=MainActivity.topTimesStrings[2][1];

            MainActivity.topTimesStrings[2][0]=MainActivity.topTimesStrings[1][0];
            MainActivity.topTimesStrings[2][1]=MainActivity.topTimesStrings[1][1];

            MainActivity.topTimesStrings[1][0]=totalTimeSpent;
            MainActivity.topTimesStrings[1][1]=start;
        }
        else if(MainActivity.topTimesStrings[2][0]<totalTimeSpent){
            MainActivity.topTimesStrings[4][0]=MainActivity.topTimesStrings[3][0];
            MainActivity.topTimesStrings[4][1]=MainActivity.topTimesStrings[3][1];

            MainActivity.topTimesStrings[3][0]=MainActivity.topTimesStrings[2][0];
            MainActivity.topTimesStrings[3][1]=MainActivity.topTimesStrings[2][1];

            MainActivity.topTimesStrings[2][0]=totalTimeSpent;
            MainActivity.topTimesStrings[2][1]=start;
        }
        else if(MainActivity.topTimesStrings[3][0]<totalTimeSpent){
            MainActivity.topTimesStrings[4][0]=MainActivity.topTimesStrings[3][0];
            MainActivity.topTimesStrings[4][1]=MainActivity.topTimesStrings[3][1];

            MainActivity.topTimesStrings[3][0]=totalTimeSpent;
            MainActivity.topTimesStrings[3][1]=start;
        }
        else if(MainActivity.topTimesStrings[4][0]<totalTimeSpent){
            MainActivity.topTimesStrings[4][0]=totalTimeSpent;
            MainActivity.topTimesStrings[4][1]=start;
        }
        //FOr the top power screen=------------------------------------------------------------------------------------------------------------------------
        if (MainActivity.topPower[0]<powerHigh){//rotate 4 down

            MainActivity.topPower[4]=MainActivity.topPower[3];
            MainActivity.topPowerStrings[4]=MainActivity.topPowerStrings[3];

            MainActivity.topPower[3]=MainActivity.topPower[2];
            MainActivity.topPowerStrings[3]=MainActivity.topPowerStrings[2];

            MainActivity.topPower[2]=MainActivity.topPower[1];
            MainActivity.topPowerStrings[2]=MainActivity.topPowerStrings[1];

            MainActivity.topPower[1]=MainActivity.topPower[0];
            MainActivity.topPowerStrings[1]=MainActivity.topPowerStrings[0];

            MainActivity.topPower[0]=powerHigh;
            MainActivity.topPowerStrings[0]=start;
        }
        else if(MainActivity.topPower[1]<powerHigh){
            MainActivity.topPower[4]=MainActivity.topPower[3];
            MainActivity.topPowerStrings[4]=MainActivity.topPowerStrings[3];

            MainActivity.topPower[3]=MainActivity.topPower[2];
            MainActivity.topPowerStrings[3]=MainActivity.topPowerStrings[2];

            MainActivity.topPower[2]=MainActivity.topPower[1];
            MainActivity.topPowerStrings[2]=MainActivity.topPowerStrings[1];

            MainActivity.topPower[1]=powerHigh;
            MainActivity.topPowerStrings[1]=start;
        }
        else if(MainActivity.topPower[2]<powerHigh){
            MainActivity.topPower[4]=MainActivity.topPower[3];
            MainActivity.topPowerStrings[4]=MainActivity.topPowerStrings[3];

            MainActivity.topPower[3]=MainActivity.topPower[2];
            MainActivity.topPowerStrings[3]=MainActivity.topPowerStrings[2];

            MainActivity.topPower[2]=powerHigh;
            MainActivity.topPowerStrings[2]=start;
        }
        else if(MainActivity.topPower[3]<powerHigh){
            MainActivity.topPower[4]=MainActivity.topPower[3];
            MainActivity.topPowerStrings[4]=MainActivity.topPowerStrings[3];

            MainActivity.topPower[3]=powerHigh;
            MainActivity.topPowerStrings[3]=start;
        }
        else if(MainActivity.topPower[4]<powerHigh){
            MainActivity.topPower[4]=powerHigh;
            MainActivity.topPowerStrings[4]=start;
        }

        if (gatt != null) {
            // For better reliability be careful to disconnect and close the connection.
            gatt.disconnect();
            gatt.close();
            gatt = null;
            tx = null;
            rx = null;
        }
    }

    // Main BTLE device callback where much of the logic occurs.
    private BluetoothGattCallback callback = new BluetoothGattCallback() {
        // Called whenever the device connection state changes, i.e. from disconnected to connected.
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                writeLine("Connected!");
                // Discover services.
                if (!gatt.discoverServices()) {
                    writeLine("Failed to start discovering services!");
                }
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                writeLine("Disconnected!");
            } else {
                writeLine("Connection state changed.  New state: " + newState);
            }
        }

        // Called when services have been discovered on the remote device.
        // It seems to be necessary to wait for this discovery to occur before
        // manipulating any services or characteristics.
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //writeLine("Service discovery completed!");
            } else {
                writeLine("Service discovery failed with status: " + status);
            }
            // Save reference to each characteristic.
            tx = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);
            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);
            // Setup notifications on RX characteristic changes (i.e. data received).
            // First call setCharacteristicNotification to enable notification.
            if (!gatt.setCharacteristicNotification(rx, true)) {
                writeLine("Couldn't set notifications for RX characteristic!");
            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            if (rx.getDescriptor(CLIENT_UUID) != null) {
                BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (!gatt.writeDescriptor(desc)) {
                    writeLine("Couldn't write RX client descriptor value!");
                }
            } else {
                writeLine("Couldn't get RX client descriptor!");
            }
        }

        // Called when a remote characteristic changes (like the RX characteristic).
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            float f1 = ByteBuffer.wrap(characteristic.getValue()).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            addData((double) f1);
        }
    };

    // BTLE device scanning callback.
    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        // Called when a device is found.
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            //writeLine("Found device: " + bluetoothDevice.getAddress());
            // Check if the device has the UART service.
            if (parseUUIDs(bytes).contains(UART_UUID)) {
                // Found a device, stop the scan.
                adapter.stopLeScan(scanCallback);
                writeLine("Found UART service!");
                // Connect to the device.
                // Control flow will now go to the callback functions when BTLE events occur.
                gatt = bluetoothDevice.connectGatt(getApplicationContext(), false, callback);
            }
        }
    };

    // Write some text to the messages text view.
    // Care is taken to do this on the main UI thread so writeLine can be called
    // from any thread (like the BTLE callback).
    private void writeLine(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.setText(text);
            }
        });
    }

    // Filtering by custom UUID is broken in Android 4.3 and 4.4, see:
    //   http://stackoverflow.com/questions/18019161/startlescan-with-128-bit-uuids-doesnt-work-on-native-android-ble-implementation?noredirect=1#comment27879874_18019161
    // This is a workaround function from the SO thread to manually parse advertisement data.
    private List<UUID> parseUUIDs(final byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<UUID>();

        int offset = 0;
        while (offset < (advertisedData.length - 2)) {
            int len = advertisedData[offset++];
            if (len == 0)
                break;

            int type = advertisedData[offset++];
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (len > 1) {
                        int uuid16 = advertisedData[offset++];
                        uuid16 += (advertisedData[offset++] << 8);
                        len -= 2;
                        uuids.add(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                case 0x06:// Partial list of 128-bit UUIDs
                case 0x07:// Complete list of 128-bit UUIDs
                    // Loop through the advertised 128-bit UUID's.
                    while (len >= 16) {
                        try {
                            // Wrap the advertised bits and order them.
                            ByteBuffer buffer = ByteBuffer.wrap(advertisedData, offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
                            long mostSignificantBit = buffer.getLong();
                            long leastSignificantBit = buffer.getLong();
                            uuids.add(new UUID(leastSignificantBit,
                                    mostSignificantBit));
                        } catch (IndexOutOfBoundsException e) {
                            // Defensive programming.
                            //Log.e(LOG_TAG, e.toString());
                            continue;
                        } finally {
                            // Move the offset to read the next uuid.
                            offset += 15;
                            len -= 16;
                        }
                    }
                    break;
                default:
                    offset += (len - 1);
                    break;
            }
        }
        return uuids;
    }

    // tare the scale
    public void tare(View v) {
        if(tx == null) {
            writeLine("tx null");
            return;
        } else {
            tx.setValue("t".getBytes(Charset.forName("UTF-8")));
            if(gatt.writeCharacteristic(tx)) {
                writeLine("tare succeeded");
            } else {
                writeLine("tare failed");
            }
        }
    }
}
