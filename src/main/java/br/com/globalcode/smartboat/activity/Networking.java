package br.com.globalcode.smartboat.activity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Vinicius Senger
 */
public class Networking extends Thread {

    static final long INTERVAL = 1000;
    static int picNumber = 0;
    public static boolean running1;

    public void setRunning(boolean r) {
        running1 = r;
    }

    public void run() {
        while (true) {
            try {
                /*if (!running1) {
                 Thread.sleep(INTERVAL);
                 }*/
                System.out.println("Picture number " + SmartBoat.increment + " Upload " + picNumber);
                if (picNumber == SmartBoat.increment) {
                    Thread.sleep(INTERVAL);
                } else {
                    //System.out.println("Converting...");
                    //Process pr2 = rt.exec("convert " + PiPicture.DIR + PiPicture.PICTUREFILENAME + PiPicture.increment + ".jpg -resize 800x600 "
                    //        + PiPicture.DIR + "/low/" + PiPicture.PICTUREFILENAME + PiPicture.increment + ".jpg");
                    //pr2.waitFor();
                    System.out.println("Upload");

                    int r = upload(SmartBoat.PICTUREFILENAME + (picNumber + 1) + ".jpg");
                    if (r > 0) {
                        picNumber++;
                        SmartBoat.speak("upload sucess", "en");

                    }
                    Thread.sleep(INTERVAL);
                }
            } catch (Exception ex) {
                System.out.println("ERRO!");
                Logger.getLogger(Networking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static int upload(String file) {

        String server = "ftp.meiadoisa.com.br";
        int port = 21;
        String user = "meiadoisa";
        String pass = "8262mda";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            //File firstLocalFile = new File(PiPicture.DIR + "/low/" + file);
            File firstLocalFile = new File(SmartBoat.DIR + file);

            String firstRemoteFile = "/www/eduardo/assets/2014-08-09/" + file;
            InputStream inputStream = new FileInputStream(firstLocalFile);

            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println(file + " uploaded!");
            }
            return 1;

        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
            }
        }
    }
}
