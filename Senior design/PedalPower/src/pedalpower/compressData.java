/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pedalpower;

import java.util.ArrayList;

/**
 *
 * @author Angie
 * 
 * Used with myData to compress all points by half when Data gets too large. 
 * 
 * Must write algorithm to cut in half and save to display data. 
 * Need to determine max points to output to the screen and a refresh rate.
 * Need to determine max points to save- compressed and not compressed.
 * logarithmic function? Grows too fast. 
 * Points per second? 10? Immediate compress vs total compress?
 * 
 * 
 * link to android display information:
 * https://developer.android.com/guide/practices/screens_support.html
 * 
 * note: Android S4 is approx 391 dp
 */
public class compressData {
    int maxPoints;
    int compressionRatio;       //number of raw data points/ new points in array
                                //i.e. 8 points raw to one point displayed=8
                                //must then average next 8 points to add point to array
                                //when array fills compress every 2 points to 1, compression ratio=compressionratio*2
                                //numDisplayPoints=numDisplayPOints/2
    int sizeCompressed;
    int sizeNew;
    int numDisplayPoints;       //SHould be statically defined when agreed upon
    ArrayList outputPoints=new ArrayList();
    ArrayList Compressedpoints=new ArrayList();
    void compress(){
        
        
    }
    
    
}
