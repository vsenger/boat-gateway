/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.globalcode.smartboat.activity;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vsenger
 */
public class TimeLapse  extends TimerTask {

 
    @Override
    public void run() {

    }
    public static final String CAMERA_OPTS = " -w 800 -h 600 -q 75  ";
    
    public static final String DIR = "/home/pi/java/apache-tomcat-8.0.9/webapps/boat/picture";
    final static GpioPinDigitalOutput myLed[] = new GpioPinDigitalOutput[3];
    static boolean alternate;
    public static String PICTUREFILENAME = "picamera";
    static Runtime rt = Runtime.getRuntime();
    static int increment;
    static boolean buttonHigh;
    static long buttonTimeCounter;
    static boolean serviceRunning;
    static Process timeLapse;
    static long timeCounter;
    static long lapseTimeout;
    
    public static void checkTimeLapse() {
        if (!serviceRunning) {
            return;
        }
        if (System.currentTimeMillis() - timeCounter < lapseTimeout) {
            return;
        }
        System.out.println("Timelapse Picture");
        takePicture();
        timeCounter = System.currentTimeMillis();
        
    }
    
    public static void takePicture() {
        try {
            myLed[2].setState(true);
            Process pr1 = rt.exec("raspistill " + CAMERA_OPTS + "  -t 1 -n -vs -o " + DIR + PICTUREFILENAME + ++increment + ".jpg");
            pr1.waitFor();  
            speak("foto", "pt");
            
            myLed[2].setState(false);
            
        } //if(event.getState().equals("HIGH"))
        catch (Exception ex) {
            Logger.getLogger(SmartBoat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void speak(String frase, String language) {
        try {
            System.out.println("Vai falar " + frase);
            Process pr1 = rt.exec("espeak -p99 -v" + language + " -k5 -s150 \"" + frase + "\"");
            
        } catch (Exception ex) {
            Logger.getLogger(SmartBoat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    static Networking net;
    
    public static void main(String[] args) {
        try {
            final GpioController gpio = GpioFactory.getInstance();
            final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07,
                    PinPullResistance.PULL_DOWN); //GPIO_00 = 17
            myLed[0] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LED #1", PinState.LOW);//GPIO_07 = 4
            myLed[1] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED #2", PinState.LOW);//GPIO_03 = 22
            myLed[2] = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED #3", PinState.LOW);//GPIO_05 = 24
            System.out.println("Starting camera");
            blink(myLed[0], 3, 200);
            blink(myLed[1], 3, 200);
            blink(myLed[2], 3, 200);
            myLed[0].setState(true);
            net = new Networking();
            net.setRunning(true);
            net.setPriority(Thread.MIN_PRIORITY);
            System.out.println("Starting network service");
            
            net.start();
            myButton.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // display pin state on console
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                    if (event.getState().isHigh() && !buttonHigh) {
                        buttonHigh = true;
                        buttonTimeCounter = System.currentTimeMillis();
                        
                    }
                    if (event.getState().isLow()) {
                        buttonHigh = false;
                        if (System.currentTimeMillis() - buttonTimeCounter > 3000) {
                            if (!serviceRunning) {
                                long interval = System.currentTimeMillis() - buttonTimeCounter;
                                lapseTimeout = interval;
                                System.out.println("Starting time lapse service with " + (interval / 1000) + " seconds interval...");
                                serviceRunning = true;
                                myLed[1].setState(true);
                            } else {
                                System.out.println("Stoping time lapse");
                                serviceRunning = false;
                                try {
                                    Process pr1 = rt.exec("sudo sync");
                                } catch (IOException ex) {
                                    Logger.getLogger(SmartBoat.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                myLed[1].setState(false);
                            }
                        } else {
                            System.out.println("Manual Picture");
                            takePicture();
                        }
                    }
                    
                }
                
            });
            
            for (;;) {
                Thread.sleep(100);
                checkTimeLapse();
            }
        } catch (Exception e) {
            System.out.println("Something was wrong...");
            e.printStackTrace();
        } finally {
            myLed[0].setState(true);
            
        }
    }
    
    static void blink(GpioPinDigitalOutput myLed, int nTimes, int delay) throws Exception {
        for (int x = 0; x < nTimes; x++) {
            myLed.setState(true);
            Thread.sleep(delay);
            myLed.setState(false);
            Thread.sleep(delay);
        }
    }
}
