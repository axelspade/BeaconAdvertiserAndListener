package com.example.runonboottestapp;

//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.le.AdvertiseCallback;
//import android.bluetooth.le.AdvertiseData;
//import android.bluetooth.le.AdvertiseSettings;
//import android.bluetooth.le.BluetoothLeAdvertiser;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import org.altbeacon.beacon.BeaconManager;
//
//import java.nio.ByteBuffer;
//import java.util.Random;
//import java.util.UUID;
//
//public class BeaconActivity extends Activity {
//
//    protected static final String TAG = "MonitoringActivity";
//    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
//    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_monitoring);
//        verifyBluetooth();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setTitle("This app needs background location access");
//                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
//                            builder.setPositiveButton(android.R.string.ok, null);
//                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                                @TargetApi(23)
//                                @Override
//                                public void onDismiss(DialogInterface dialog) {
//                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
//                                }
//
//                            });
//                            builder.show();
//                        }
//                        else {
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setTitle("Functionality limited");
//                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
//                            builder.setPositiveButton(android.R.string.ok, null);
//                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                                @Override
//                                public void onDismiss(DialogInterface dialog) {
//                                }
//
//                            });
//                            builder.show();
//                        }
//                    }
//                }
//            } else {
//                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//                            PERMISSION_REQUEST_FINE_LOCATION);
//                }
//                else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Functionality limited");
//                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
//                    builder.setPositiveButton(android.R.string.ok, null);
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                        }
//
//                    });
//                    builder.show();
//                }
//
//            }
//        }
//
//        onAdvertise();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        onAdvertise();
//    }
//
//    private void verifyBluetooth() {
//
//        try {
//            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Bluetooth not enabled");
//                builder.setMessage("Please enable bluetooth in settings and restart this application.");
//                builder.setPositiveButton(android.R.string.ok, null);
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        //finish();
//                        //System.exit(0);
//                    }
//                });
//                builder.show();
//            }
//        }
//        catch (RuntimeException e) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Bluetooth LE not available");
//            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
//            builder.setPositiveButton(android.R.string.ok, null);
//            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    //finish();
//                    //System.exit(0);
//                }
//
//            });
//            builder.show();
//
//        }
//
//    }
//
//    public void onListeningClicked(View view) {
//        Intent myIntent = new Intent(this, IBeaconListener.class);
//        this.startActivity(myIntent);
//    }
//
//    @SuppressLint("ShowToast")
//    public void onAdvertise() {
//        Toast.makeText(this, "Advertising started", Toast.LENGTH_SHORT).show();
//
//        final BluetoothLeAdvertiser advertiser =
//                BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
//
//        final AdvertiseCallback callback = new AdvertiseCallback(){};
//
//        AdvertiseSettings advertiserSettings = setAdvertiserSettings();
//        AdvertiseData advertiserData = setAdvertiserData();
//
//        advertiser.startAdvertising(advertiserSettings, advertiserData, callback);
//
//        //advertiser.stopAdvertising(callback);
//    }
//
//    public static byte[] getBytesFromUUID(UUID uuid) {
//        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
//        bb.putLong(uuid.getMostSignificantBits());
//        bb.putLong(uuid.getLeastSignificantBits());
//        return bb.array();
//    }
//
//    private AdvertiseData setAdvertiserData() {
//
//        int major = Constants.major;
//
//        Random rand = new Random();
//        //int minor = rand.nextInt(100);
//        int minor = 14;
//
//        UUID uuid = Constants.uuid;
//        byte[] uuidBytes = getBytesFromUUID(uuid);
//
//        byte[] manufacturerData = new byte[] {
//                (byte) 0x02,
//                (byte) 0x15,
//
//                // ProximityUUID, 16 bytes
//                uuidBytes[0], uuidBytes[1], uuidBytes[2], uuidBytes[3],
//                uuidBytes[4], uuidBytes[5], uuidBytes[6], uuidBytes[7],
//                uuidBytes[8], uuidBytes[9], uuidBytes[10], uuidBytes[11],
//                uuidBytes[12], uuidBytes[13], uuidBytes[14], uuidBytes[15],
//
//                // Major
//                (byte) ((major >> 8) & 0xFF), (byte) (major & 0xff),
//
//                // Minor
//                (byte) ((minor >> 8) & 0xFF), (byte) (minor & 0xff),
//
//                // Measured Power
//                (byte) 0xC8
//        };
//
//        AdvertiseData.Builder advertiseDataBuilder = new AdvertiseData.Builder()
//                .setIncludeTxPowerLevel(false)
//                .setIncludeDeviceName(false)
//                .addManufacturerData(0x004C, manufacturerData);
//
//        return advertiseDataBuilder.build();
//    }
//
//    private AdvertiseSettings setAdvertiserSettings() {
//
//        AdvertiseSettings.Builder advertisingSettingBuilder = new AdvertiseSettings.Builder()
//                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
//                .setConnectable(false)
//                .setTimeout(0)
//                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW);
//
//        return advertisingSettingBuilder.build();
//    }
//
//}
