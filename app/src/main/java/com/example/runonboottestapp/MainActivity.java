package com.example.runonboottestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, IBeaconAdvertiserJobIntentService.class);
        //IBeaconAdvertiserJobIntentService.enqueueWork(this,intent);
        this.startService(intent);
        IBeaconAdvertiserJobIntentService.enqueueWork(this,intent);
    }
}