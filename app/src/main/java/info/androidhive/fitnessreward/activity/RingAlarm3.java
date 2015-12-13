package info.androidhive.fitnessreward.activity;
/**
 * Created by Daria, Roma, Alper
 */
import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import info.androidhive.fitnessreward.R;

public class RingAlarm3 extends FragmentActivity {

    MediaPlayer mp=null ;
    ImageView image;
    private final String TAG = "RA-3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_alarm3);
        image = (ImageView) findViewById(R.id.image_3);
        image.setImageResource(R.drawable.fail_photo);


        Button stopAlarm = (Button) findViewById(R.id.button);

        mp = MediaPlayer.create(getBaseContext(),R.raw.alarm_3);


        int goal_steps3 = getIntent().getExtras().getInt("goal_steps3");

        /*Log.d(TAG, "Ring Alarm 2 triggered with Remaining Time: " + remaining_time + ", steps: " + remaining_steps);*/

        TextView goal_text3 = (TextView) findViewById(R.id.text_goal3);
        goal_text3.setText("You did not complete " + goal_steps3+ " steps in given time");


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

