package com.example.runonboottestapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverAdvertiser extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        IBeaconAdvertiserJobIntentService.enqueueWork(context,intent);
    }
}
