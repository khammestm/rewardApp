package info.androidhive.materialdesign.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.activity.DataBase;
import info.androidhive.materialdesign.activity.RingAlarm;
import info.androidhive.materialdesign.activity.RingAlarm2;
import info.androidhive.materialdesign.activity.RingAlarm3;


public class MyBroadcastReceiver extends BroadcastReceiver {


    private DataBase mDbHelper;
    private Context mContext;
    private int counter = 0;
    private final String TAG = "BC-RECV";
    private final int NUMBER_OF_DAYS_BEFORE = 3;
    /*int i=1;*/
    public static String convertSecondsToHMmSs(long seconds) {
        seconds = seconds/1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h,m,s);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        Calendar c = Calendar.getInstance();
        Log.d(TAG, "Entered broadcast receiver. Current time = " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Date date = null;
        try {
            date = df.parse(formattedDate);
            long current_epoch = date.getTime();
            Log.d(TAG, "current_epoch " + current_epoch);
            mDbHelper = new DataBase(mContext);
            Cursor cursor = mDbHelper.getEarliestGoalRecord();
            Cursor cursor2 = mDbHelper.getLastDataRecord();
            long goal_epoch = 0;


            if ((cursor != null) && (cursor.getCount() > 0) && (cursor2 != null) && (cursor2.getCount() > 0)) {
                cursor.moveToFirst();
                String date2 = cursor.getString(cursor.getColumnIndex("date"));
                String steps = cursor.getString(cursor.getColumnIndex("distance"));
                Date date_obj = new Date(Long.parseLong(date2));
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                goal_epoch = date_obj.getTime();
                int goal_steps = Integer.parseInt(steps);
                Log.d(TAG, "Found goal steps: " + goal_steps);

                String current_step_str = cursor2.getString(cursor2.getColumnIndex("steps"));
                int current_steps = Integer.parseInt(current_step_str);
                Log.d(TAG, "Found current steps: " + current_steps);

                int hour_right_now = c.get(Calendar.HOUR_OF_DAY);

                Log.d(TAG, "Hour, now: " + hour_right_now);

                Log.d(TAG, "Now: " + current_epoch + "\t" + df.format(new Date(current_epoch)));
                Log.d(TAG, "Goal:" + goal_epoch + "\t" + df.format(new Date(goal_epoch)));



                if ((current_epoch < goal_epoch) && (current_steps < goal_steps) && (current_epoch + (NUMBER_OF_DAYS_BEFORE * 3600 * 24 * 1000) > goal_epoch) && hour_right_now > 8 && hour_right_now < 21) {
                    try {
                        Log.d(TAG, "Now we are supposed to ring an ALARM!");

                        SharedPreferences sharedPref = mContext.getSharedPreferences("FITNESS_PREFERENCES", Context.MODE_PRIVATE);
                        Long last_run_timestamp = sharedPref.getLong("keep_working_timestamp", 0);
                        Log.d(TAG, "Last time we run KEEP_WORKING alert was: " + "\t" + DateUtils.getRelativeTimeSpanString(mContext, last_run_timestamp));
                        if (current_epoch < (last_run_timestamp + 3 * 3600 * 1000)) {
                            Log.d(TAG, "Not yet..");
                            return;
                        }

                        Log.d(TAG, "Updating keep_working_timestamp");
                        resetTimeStamp(false);


                        Intent intent1 = new Intent(mContext, RingAlarm.class);
                        intent1.putExtra("remaining_goal_time", goal_epoch - current_epoch);
                        intent1.putExtra("remaining_steps", goal_steps - current_steps);
                        PendingIntent pendingIntent1 = PendingIntent.getActivity(mContext,
                                12345, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

                        //am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent1);


                        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext);
                        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        notification.setAutoCancel(true);
                        notification.setSmallIcon(R.mipmap.mandela_1);
                        notification.setTicker("Ticker ne aqqqqqqqqqq");
                        notification.setWhen(System.currentTimeMillis());
                        notification.setContentTitle("DO NOT STOP!");
                        notification.setContentText("You need " + (goal_steps - current_steps) + " more steps in " + convertSecondsToHMmSs(goal_epoch - current_epoch));
                        notification.setSound(uri);
                        notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                        notification.setContentIntent(pendingIntent1);

                        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                        long time = new Date().getTime();
                        String tmpStr = String.valueOf(time);
                        String last4Str = tmpStr.substring(tmpStr.length() - 5);
                        int notificationId = Integer.valueOf(last4Str);


                        nm.notify(notificationId, notification.build());


                    } catch (Exception e) {
                    }


                } else if (current_steps >= goal_steps) {
                    try {
                        Log.d(TAG, "GOAL REACHED!!!");
                        resetTimeStamp(true);
                        //Create a new PendingIntent and add it to the AlarmManager
                        Intent intent2 = new Intent(mContext, RingAlarm2.class);
                        intent2.putExtra("goal_steps2", goal_steps);
                        PendingIntent pendingIntent2 = PendingIntent.getActivity(mContext,
                                12345, intent2, PendingIntent.FLAG_CANCEL_CURRENT);

                        /*am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent2);*/

                        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext);
                        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        notification.setAutoCancel(true);
                        notification.setSmallIcon(R.mipmap.winner_1);
                        notification.setTicker("amkkkkkkkk");
                        notification.setWhen(System.currentTimeMillis());
                        notification.setContentTitle("YOU ARE A WINNER!");
                        notification.setContentText("You have completed " + (goal_steps) + " steps");
                        notification.setSound(uri);
                        notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

                        notification.setContentIntent(pendingIntent2);

                        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                        long time = new Date().getTime();
                        String tmpStr = String.valueOf(time);
                        String last4Str = tmpStr.substring(tmpStr.length() - 5);
                        int notificationId = Integer.valueOf(last4Str);


                        nm.notify(notificationId, notification.build());

                        mDbHelper.deleteFirstRow();

                    } catch (Exception e) {
                    }

                } else if ((current_epoch >= goal_epoch && (current_steps < goal_steps))) {
                    try {
                        Log.d(TAG, "GOAL FAILED!!!");
                        //Create a new PendingIntent and add it to the AlarmManager
                        Intent intent3 = new Intent(mContext, RingAlarm3.class);
                        intent3.putExtra("goal_steps3", goal_steps);
                        PendingIntent pendingIntent3 = PendingIntent.getActivity(mContext,
                                12345, intent3, PendingIntent.FLAG_CANCEL_CURRENT);

                        /*am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent3);*/

                        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext);
                        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        notification.setAutoCancel(true);
                        notification.setSmallIcon(R.mipmap.fail_1);
                        notification.setTicker("This is the ticker");
                        notification.setWhen(System.currentTimeMillis());
                        notification.setContentTitle("FAILURE!");
                        notification.setContentText("You did not complete " + (goal_steps) + "steps in given time" );
                        notification.setSound(uri);
                        notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

                        notification.setContentIntent(pendingIntent3);

                        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                        long time = new Date().getTime();
                        String tmpStr = String.valueOf(time);
                        String last4Str = tmpStr.substring(tmpStr.length() - 5);
                        int notificationId = Integer.valueOf(last4Str);


                        nm.notify(notificationId, notification.build());

                        mDbHelper.deleteFirstRow();
                        resetTimeStamp(true);
                    } catch (Exception e) {
                    }

                } else {
                    Log.d(TAG, "Broadcast triggered, nothing happened..");
                }


            } else {
                Log.d(TAG, "No goal found");
            }
            mDbHelper.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    void resetTimeStamp(boolean hard_reset) {
        Calendar c = Calendar.getInstance();
        SharedPreferences sharedPref = mContext.getSharedPreferences("FITNESS_PREFERENCES", Context.MODE_PRIVATE);
        Log.d(TAG, "Updating keep_working_timestamp");
        SharedPreferences.Editor editor = sharedPref.edit();
        if (hard_reset)
            editor.putLong("keep_working_timestamp", 0);
        else
            editor.putLong("keep_working_timestamp", c.getTimeInMillis());
        editor.commit();
    }

}