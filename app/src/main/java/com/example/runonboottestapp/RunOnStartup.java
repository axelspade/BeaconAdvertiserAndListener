package com.example.runonboottestapp;//package com.google.android.gms.location.sample.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RunOnStartup extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            Intent intentq = new Intent(context, IBeaconAdvertiserJobIntentService.class);
            context.startForegroundService(intentq);
            IBeaconAdvertiserJobIntentService.enqueueWork(context,intentq);
        }
    }
}
