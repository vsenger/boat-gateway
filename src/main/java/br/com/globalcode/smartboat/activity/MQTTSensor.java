/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.smartboat.activity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import static org.things.Things.things;
import org.things.gateway.rest.ThingService;

/**
 *
 * @author vsenger
 */
public class MQTTSensor extends TimerTask {

    @Override
    public void run() {
        try { 
            String alcohol = things.execute(ThingService.FTDI, "alcohol", null);
            Thread.sleep(500);
            String temp = things.execute(ThingService.FTDI, "temp_out", null);
            Thread.sleep(500);
            String humidity = things.execute(ThingService.FTDI, "humidity", null);
            Thread.sleep(500);
            String distance = things.execute(ThingService.FTDI, "distance", null);
            Thread.sleep(500);
            String presence = things.execute(ThingService.FTDI, "presence", null);
            Thread.sleep(500);
            String current = things.execute(ThingService.FTDI, "current", null);
            Thread.sleep(500);
            String msg = "gas: " + alcohol + "|temperature: " + temp + "|humidity: " + humidity + 
                    "|distance: " + distance + "|presence: " + presence + "|current: " + current;
            
            MqttClient client; 
            client = new MqttClient("tcp://iot.eclipse.org:1883", "tiziu-smartboat");
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(
                    msg.getBytes());
            client.publish(
                    "things/smartboat/tiziu/sensor", message);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }

        System.out.println("Timer task started at:" + new Date());
        completeTask();
        System.out.println("Timer task finished at:" + new Date());
    }

    private void completeTask() {
        try {
            //assuming it takes 20 secs to complete the task
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
