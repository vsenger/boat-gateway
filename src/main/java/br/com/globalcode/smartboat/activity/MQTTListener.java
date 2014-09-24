/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.globalcode.smartboat.activity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author vsenger
 */
public class MQTTListener implements MqttCallback {

    @Override
    public void connectionLost(Throwable thrwbl) {
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        if(mm.toString().equals("picture")) {
            
        }
        else if(mm.toString().equals("update")) {
            
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
    }
    
}
