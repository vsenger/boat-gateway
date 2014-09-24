/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.globalcode.smartboat.activity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import static org.things.Things.things;
import org.things.gateway.rest.ThingService;

/**
 *
 * @author vsenger
 */
public class MQTTSensor extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                String alcohol = things.execute(ThingService.FTDI, "alcohol", null);
                Thread.sleep(500);
                String temp = things.execute(ThingService.FTDI, "temp_out", null);
                Thread.sleep(1000);
                String humidity = things.execute(ThingService.FTDI, "humidity", null);
                Thread.sleep(1000);
                String distance = things.execute(ThingService.FTDI, "distance", null);
                Thread.sleep(1000);
                String presence = things.execute(ThingService.FTDI, "presence", null);
                Thread.sleep(1000);
                String current = things.execute(ThingService.FTDI, "current", null);
                Thread.sleep(3000);
                String msg = "gas: " + alcohol + "|temperature: " + temp + "|humidity: " + humidity
                        + "|distance: " + distance + "|presence: " + presence + "|current: " + current;
                System.out.println("MQTT Message " + msg);
                MqttClient client;
                System.out.println("Getting client MQTT - 1234");
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
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MQTTSensor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
