package info.androidhive.materialdesign.activity;
/**
 * Created by Daria, Roma, Alper
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import info.androidhive.materialdesign.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private DataBase mDbHelper;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    int i=1;
    /*private static final int uniqueID=45612;
    NotificationCompat.Builder notification;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*notification= new NotificationCompat.Builder(this);*/

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Date date = null;
        try {
            date = df.parse(formattedDate);
            long current_epoch = date.getTime();
            System.out.println(current_epoch);
            mDbHelper = new DataBase(this);
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
                System.out.println(goal_steps);

                String current_step = cursor2.getString(cursor2.getColumnIndex("steps"));
                int current_steps = Integer.parseInt(current_step);
                System.out.println(current_steps);
                long x=(current_epoch%(3600*24*1000));
                long a=x/(3600*1000);
                System.out.println(a);

                System.out.println(current_epoch);
                System.out.println(goal_epoch);

//Create a new PendingIntent and add it to the AlarmManager
               /* Intent intent = new Intent(this, RingAlarm.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);*/
                AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


            if ((current_epoch<goal_epoch) && (current_steps<goal_steps) /*&& (current_epoch+259200000>goal_epoch) && a>8 && a<21*/){
                try {
                    System.out.println("1111111111111111111111111111111111");
                    /*i=i+1;*/
                    /*if (i%6==0) {*/
                        i=0;
                        Intent intent = new Intent(this, RingAlarm.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    /*am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),60000 , pendingIntent);*/
                        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent);
                    /*}*/

                    long remaining_hours=(goal_epoch-current_epoch)/3600000;
                    long remaining_minutes=((goal_epoch-current_epoch)-remaining_hours*3600000)/60000;
                    long remaining_seconds=((goal_epoch-current_epoch)-remaining_hours*3600000-remaining_minutes*60000)/1000;
                    System.out.println(remaining_hours);
                    System.out.println(remaining_minutes);
                    System.out.println(remaining_seconds);
                    /*String r_h = Long.toString(remaining_hours);*/
                    /*Log.d("About",""+r_h);*/
                    /*int r_h=(int) remaining_hours;
                    Log.d(""+ r_h);

                    
                    notification.setAutoCancel(true);

                    notification.setSmallIcon(R.mipmap.ic_launcher);
                    notification.setTicker("This is the ticker");
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("Here is the title");
                    notification.setContentText("I am the body of not");
                    notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

                    Intent intent2=new Intent(this,MainActivity.class);
                    PendingIntent pendingIntent2= PendingIntent.getActivity(this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent2);

                    NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(uniqueID, notification.build());*/




                } catch (Exception e){}


            }

                if (/*(current_epoch<goal_epoch) &&*/ (current_steps>=goal_steps)){
                    try {
                        i=0;
                        System.out.println("2222222222222222222222222");
                        ///*Create a new PendingIntent and add it to the AlarmManager
                        Intent intent2 = new Intent(this, RingAlarm2.class);
                        PendingIntent pendingIntent2 = PendingIntent.getActivity(this,
                                12345, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
                        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),pendingIntent2);
                        mDbHelper.deleteFirstRow();

                    } catch (Exception e){}

                }


                if ((current_epoch>=goal_epoch && (current_steps<goal_steps))){
                    try {
                        i=0;
                        System.out.println("33333333333333333333333333");
                        ///*Create a new PendingIntent and add it to the AlarmManager
                        Intent intent3 = new Intent(this, RingAlarm3.class);
                        PendingIntent pendingIntent3 = PendingIntent.getActivity(this,
                                12345, intent3, PendingIntent.FLAG_CANCEL_CURRENT);
                        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pendingIntent3);
                        mDbHelper.deleteFirstRow();
                    } catch (Exception e){}

                }



            }
                mDbHelper.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }





        /*try {

            //Create a new PendingIntent and add it to the AlarmManager
            Intent intent = new Intent(this, RingAlarm.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am =
                    (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                    1000*10,pendingIntent);

            NotificationCompat.Builder notification;
            final int uniqueID=45612;
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification= new NotificationCompat.Builder(this);
            notification.setAutoCancel(true);

            notification.setSmallIcon(R.mipmap.ic_launcher);
            notification.setTicker("This is the ticker");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("Here is the title");
            notification.setContentText("I am the body of not");
            notification.setSound(alarmSound);
            notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

            Intent intent2=new Intent(this,MainActivity.class);
            PendingIntent pendingIntent2= PendingIntent.getActivity(this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent2);

            NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(uniqueID, notification.build());



        } catch (Exception e) {}*/
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        // display the first navigation drawer view on app launch
        displayView(0);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_today);
                break;
            case 1:
                fragment = new StepsFragment();
                title = getString(R.string.title_daily_activity);
                break;
            case 2:
                fragment = new StepsAddGoalFragment();
                title = getString(R.string.title_set_goal);
                break;
            case 3:
                fragment = new MessagesFragment();
                title = getString(R.string.title_personal_data);
                break;
            case 4:
                fragment = new StatsFragment();
                title = getString(R.string.title_statistics);
                break;
            case 5:
                fragment = new AboutFragment();
                title = getString(R.string.title_about);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}