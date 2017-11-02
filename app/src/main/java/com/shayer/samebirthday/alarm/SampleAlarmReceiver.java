package com.shayer.samebirthday.alarm;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by Shreeya Patel on 3/11/2016.
 */
public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    SharedPreferences preferences;
    SharedPreferences.Editor pre_Edit;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SampleSchedulingService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 10000, intent, 0);
        if(preferences.getBoolean("set_Alarm",false)==true) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis()+10000, 60000, alarmIntent);
            // Toast.makeText(context, "Alarm Repeat", Toast.LENGTH_SHORT).show();
            Log.d("ALARM","Alarm Repeat");
        }
        else
        {
            pre_Edit=preferences.edit();
            pre_Edit.putBoolean("set_Alarm",true);
            pre_Edit.commit();
            // Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
            alarmMgr.set(AlarmManager.RTC_WAKEUP, 10000, alarmIntent);
            Log.d("ALARM","Alarm Set");
        }


        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
