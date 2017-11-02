package com.shayer.samebirthday.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Shreeya Patel on 3/11/2016.
 */
public class SampleBootReceiver extends BroadcastReceiver {

    SampleAlarmReceiver alarm = new SampleAlarmReceiver();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            System.out.println("alarm::::::::");
            alarm.setAlarm(context);
        }
    }
}
