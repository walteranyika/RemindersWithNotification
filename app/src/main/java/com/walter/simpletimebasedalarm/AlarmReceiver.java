package com.walter.simpletimebasedalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG="ALARM_APP";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Alarm received");
        NotificationScheduler.showNotification(context, MainActivity.class,
                "You have pending update to complete", "Complete It Now?");

    }
}
