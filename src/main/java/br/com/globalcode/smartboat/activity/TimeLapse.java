/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.globalcode.smartboat.activity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author vsenger
 */
public class TimeLapse  extends TimerTask {
    @Override
    public void run() {
        System.out.println("Timer task started at:"+new Date());
        System.out.println("Timer task finished at:"+new Date());
    }
 
}
