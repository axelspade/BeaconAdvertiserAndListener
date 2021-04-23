package com.example.runonboottestapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IBeaconListener extends JobIntentService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, IBeaconListener.class, 535, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        listenForIBeacons();
    }

    private void listenForIBeacons() {
        //Toast.makeText(this, "Listening for iBeacons", Toast.LENGTH_SHORT).show();

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();

        List<ScanFilter> result = new ArrayList<>(1);
        final ScanCallback callback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                //BluetoothDevice device = result.getDevice();
                final byte[] scanRecord = result.getScanRecord().getBytes();

                final byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, 6, uuidBytes, 0, 16);

                //iBeacon's UUID
                ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
                long high = bb.getLong();
                long low = bb.getLong();
                UUID uuid = new UUID(high, low);

                //Major
                final byte[] majorBytes = new byte[2];
                System.arraycopy(scanRecord, 22, majorBytes, 0, 2);
                int major = (majorBytes[0] & 0xFF) << 8 | (majorBytes[1] & 0xff);

                //Minor
                final byte[] minorBytes = new byte[2];
                System.arraycopy(scanRecord, 24, minorBytes, 0, 2);
                int minor = (minorBytes[0] & 0xFF) << 8 | (minorBytes[1] & 0xff);

                if (uuid.compareTo(Constants.uuid) == 0 && major == Constants.major)
                    Log.i("BeaconListener", "User with id:" + minor + " is around");
                //logToDisplay("User with id:" + minor + " is around");
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e("BLeScanFailed", "Bluetooth scan failed:" + errorCode);
            }
        };

        scanner.startScan(callback);
        /**
         * In case the networks are found or user exited defined area stop bluetooth searching
         */
//        if (Constants.lastWifiCheck == true || Constants.lastEvent.equals("EXIT"))
//            scanner.stopScan(callback);
        if (Constants.lastEvent.equals("EXIT"))
            scanner.stopScan(callback);
    }

//    private void logToDisplay(final String line) {
//        EditText editText = (EditText) IBeaconListener.this.findViewById(R.id.rangingText);
//        editText.append(line + "\n");
//    }n


}
