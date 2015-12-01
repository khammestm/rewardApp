package info.androidhive.materialdesign.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by rshir on 24.11.2015.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    private static final long REPEAT_TIME = 1000*30;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent receiverIntent = new Intent(context, StepsFragment.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);

            // Set the alarm to start at 8:30 a.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            calendar.set(Calendar.MINUTE, 03);

            // setRepeating() lets you specify a precise custom interval--in this case,
            // 20 minutes.
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * 1, alarmIntent);

            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    new Intent(context, StepsFragment.class),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp) {
                Log.d("DebugAlarmManager", "Alarm is already active");
            }
        }
    }
}