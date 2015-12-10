package info.androidhive.materialdesign.activity;
/**
 * Created by Daria, Roma, Alper
 */
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import info.androidhive.materialdesign.R;

public class RingAlarm extends FragmentActivity {

    MediaPlayer mp=null ;
    ImageView image;
    private final String TAG = "RA-1";
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


        Button stopAlarm = (Button) findViewById(R.id.button);

        mp = MediaPlayer.create(getBaseContext(),R.raw.alarm_1);


        Long remaining_time = getIntent().getExtras().getLong("remaining_goal_time");
        int remaining_steps = getIntent().getExtras().getInt("remaining_steps");

        Log.d(TAG, "Ring Alarm 1 triggered with Remaining Time: " + remaining_time + ", steps: " + remaining_steps);

        TextView goal_text = (TextView) findViewById(R.id.text_goal);
        goal_text.setText("You need " + remaining_steps + " more steps in " + convertSecondsToHMmSs(remaining_time));


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

    public static String convertSecondsToHMmSs(long seconds) {
        seconds = seconds/1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h,m,s);
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

