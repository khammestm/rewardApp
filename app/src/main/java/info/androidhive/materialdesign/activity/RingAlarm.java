package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import info.androidhive.materialdesign.R;

public class RingAlarm extends FragmentActivity {

    MediaPlayer mp=null ;
    ImageView image;
   /* private DataBase mDbHelper;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*mDbHelper = new DataBase(this);*/
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_alarm1);
        image = (ImageView) findViewById(R.id.image_1);
        image.setImageResource(R.drawable.keepworking_photo);

        /*R.layout.fragment_about*/
        /*Toast.makeText(getApplicationContext(), "YAY!", Toast.LENGTH_LONG).show();*/
        Button stopAlarm = (Button) findViewById(R.id.button);

        mp = MediaPlayer.create(getBaseContext(),R.raw.alarm_1);
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 300 milliseconds
        v.vibrate(1000);

        /*NotificationCompat.Builder notification;
        final int uniqueID=45612;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification= new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Here is the title");
        notification.setContentText("I am the body of not");
        *//*notification.setSound(alarmSound);*//*
        notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        Intent intent2=new Intent(this,RingAlarm.class);
        PendingIntent pendingIntent2= PendingIntent.getActivity(this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent2);

        NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());*/


        stopAlarm.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                mp.stop();
                finish();
                return false;
            }
        });

        playSound(this, getAlarmUri());
    }

    private void playSound(final Context context, Uri alert) {


        Thread background = new Thread(new Runnable() {
            public void run() {
                try {

                    mp.start();
                    /*Cursor cursor = mDbHelper.getEarliestGoalRecord();*/
                    /*mDbHelper.deleteFirstRow();*/

                } catch (Throwable t) {
                    Log.i("Animation", "Thread  exception "+t);
                }
            }
        });
        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();

    }               //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {

        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    }

