package com.example.application;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothHandler {

    // private static final String TAG = "Jon";
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private BluetoothDevice device = null; // le périphérique (le module bluetooth)
    // private static String blueName;
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

	/*
	 * private InputStream inStream = null; Handler handler = new Handler();
	 * byte delimiter = 10; boolean stopWorker = false; int readBufferPosition =
	 * 0; byte[] readBuffer = new byte[1024];
	 */


    public BluetoothHandler(String blueName) {


        // On récupère la liste des périphériques associés
        Set<BluetoothDevice> setpairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        BluetoothDevice[] pairedDevices = (BluetoothDevice[]) setpairedDevices.toArray(new BluetoothDevice[setpairedDevices.size()]);

        // On parcours la liste pour trouver notre module bluetooth
        for(int i=0;i<pairedDevices.length;i++)
        {
            // On teste si ce périphérique contient le nom du module bluetooth connecté au microcontrôleur
            if(pairedDevices[i].getName().contains(blueName)) {

                device = pairedDevices[i];
                try {
                    // On récupère le socket de notre périphérique
                    btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                    // outStream = btsocket.getInputStream();// Canal de réception (valide uniquement après la connexion)
                    outStream = btSocket.getOutputStream();// Canal d'émission (valide uniquement après la connexion)

                } catch (Exception e) {
                }
                break;
            }
        }
    }

    public void Connect() {
            //new Thread() {
               // @Override
               // public void run() {
                    try {
                        btSocket.connect();// Tentative de connexion
                        // Connexion réussie
                    } catch (Exception e) {
                        // Echec de la connexion
                        //e.printStackTrace();
                    }
           // }.start();
       // }
        //catch(Exception e) {}
    }


    public boolean isConnected ()  {
        try {
            if (btSocket.isConnected()) {
                return true;
            } else return false;
        }
        catch(Exception e){return false;}
     }

    public void writeData(int data) {

        try {
            // On écrit les données dans le buffer d'envoi
            outStream.write(data);

            // On s'assure qu'elles soient bien envoyés
            outStream.flush();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void close() {

        try {
            outStream.close();
            btSocket.close();
        } catch (Exception e) {

        }
    }

}


