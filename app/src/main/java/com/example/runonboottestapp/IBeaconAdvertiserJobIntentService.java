package com.example.runonboottestapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;


import java.nio.ByteBuffer;
import java.util.UUID;

public class IBeaconAdvertiserJobIntentService extends JobIntentService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId){
//        Toast.makeText(this, "START STICKY", Toast.LENGTH_LONG).show();
//        return START_STICKY;
//    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, IBeaconAdvertiserJobIntentService.class, 533, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        //Toast.makeText(this, "START STICKY", Toast.LENGTH_LONG).show();
        System.out.println("---------------------START ADVERTISING");
        final BluetoothLeAdvertiser advertiser =
                BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        final AdvertiseCallback callback = new AdvertiseCallback(){};

        AdvertiseSettings advertiserSettings = setAdvertiserSettings();
        AdvertiseData advertiserData = setAdvertiserData();

        advertiser.startAdvertising(advertiserSettings, advertiserData, callback);
    }

    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    private AdvertiseData setAdvertiserData() {

        int major = Constants.major;

        int minor = 14;

        UUID uuid = Constants.uuid;
        byte[] uuidBytes = getBytesFromUUID(uuid);

        byte[] manufacturerData = new byte[] {
                (byte) 0x02,
                (byte) 0x15,

                // ProximityUUID, 16 bytes
                uuidBytes[0], uuidBytes[1], uuidBytes[2], uuidBytes[3],
                uuidBytes[4], uuidBytes[5], uuidBytes[6], uuidBytes[7],
                uuidBytes[8], uuidBytes[9], uuidBytes[10], uuidBytes[11],
                uuidBytes[12], uuidBytes[13], uuidBytes[14], uuidBytes[15],

                // Major
                (byte) ((major >> 8) & 0xFF), (byte) (major & 0xff),

                // Minor
                (byte) ((minor >> 8) & 0xFF), (byte) (minor & 0xff),

                // Measured Power
                (byte) 0xC8
        };

        AdvertiseData.Builder advertiseDataBuilder = new AdvertiseData.Builder()
                .setIncludeTxPowerLevel(false)
                .setIncludeDeviceName(false)
                .addManufacturerData(0x004C, manufacturerData);

        return advertiseDataBuilder.build();
    }

    private AdvertiseSettings setAdvertiserSettings() {

        AdvertiseSettings.Builder advertisingSettingBuilder = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
                .setConnectable(false)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW);

        return advertisingSettingBuilder.build();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "TaskRemoved", Toast.LENGTH_LONG).show();
        super.onTaskRemoved(rootIntent);

        Intent intent = new Intent(this, IBeaconAdvertiserJobIntentService.class);
        this.startForegroundService(intent);
        IBeaconAdvertiserJobIntentService.enqueueWork(this,intent);


        this.stopSelf();
    }

}
