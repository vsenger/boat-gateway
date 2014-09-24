/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.things.gateway.rest;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
/**
 *
 * @author vsenger
 */
public class TesteMQTT {
    static MqttClient client;
    public static void main(String[] args) {
        doDemo();
    }
    
  public static void doDemo() {
    try {
      client = new MqttClient("tcp://iot.eclipse.org:1883","tiziu-smartboat");
      client.connect();
      MqttMessage message = new MqttMessage();
      message.setPayload("A single message sensor x".getBytes());
      client.publish("things/smartboat/tiziu/test", message);
      client.disconnect();
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }
}
