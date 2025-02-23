package com.nativelocalstorage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.util.Log;

public class ProtcoBleManager {

    private static ProtcoBleManager protcoBleManager = null;
    private BluetoothLeAdvertiser advertiser;
    private BluetoothAdapter adapter = null;
    private AdvertisingSetCallback advertisingCallback;
    private AdvertisingSet currentAdvertisingSet;
    private static final String TAG2 = "Protco Broadcaster";

    private ProtcoBleManager(){
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isLeExtendedAdvertisingSupported()) {
            adapter = null;
        }
        if(adapter!=null){
            getAdvertiser();
        }
    }
    public static ProtcoBleManager getProtcoBleManager() {
        if(protcoBleManager == null){
            protcoBleManager = new ProtcoBleManager();
        }
        return protcoBleManager;
    }

    public BluetoothLeAdvertiser getAdvertiser() {
        if(adapter == null){
            throw new IllegalStateException("Bluetooth adapter not initialized");
        }
        if(advertiser == null){
            advertiser = adapter.getBluetoothLeAdvertiser();
        }
        return advertiser;
    }

    public AdvertisingSetCallback getAdvertisingCallback() {
        if(advertisingCallback == null){
            advertisingCallback = new AdvertisingSetCallback() {
                @Override
                public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
                    Log.i(TAG2, "Advertising started: txPower=" + txPower + ", status=" + status);
                    if (status == AdvertisingSetCallback.ADVERTISE_SUCCESS) {
                        currentAdvertisingSet = advertisingSet;
                        Log.i(TAG2, "Manufacture Data passed succesfuuly");
                    } else {
                        Log.e(TAG2, "Advertising start failed: " + status);
                    }
                }

                @Override
                public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
                    Log.i(TAG2, "Advertising stopped");
                    currentAdvertisingSet = null;
                }

            };
        }
        return advertisingCallback;
    }

    public AdvertisingSet getCurrentAdvertisingSet() {
        return currentAdvertisingSet;
    }

    public void startAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback){
        if (advertiser != null) {
            advertiser.startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, callback);

        }
    }

    public  void stopAdvertisingSet(){
        if (advertiser != null) {
            advertiser.stopAdvertisingSet(advertisingCallback);
        }
    }
}
