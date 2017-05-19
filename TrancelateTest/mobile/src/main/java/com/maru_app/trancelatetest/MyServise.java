package com.maru_app.trancelatetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by maru on 2017/05/19.
 */

public class MyServise extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("MyService", "onMessageReceived");
        Log.d("MyService", messageEvent.getPath());
        Log.d("MyService", new String(messageEvent.getData()));
    }
}