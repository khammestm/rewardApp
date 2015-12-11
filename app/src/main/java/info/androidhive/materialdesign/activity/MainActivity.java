package info.androidhive.materialdesign.activity;
/**
 * Created by Daria, Roma, Alper
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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
   /* int i=1;*/
    /*private static final int uniqueID=45612;
    NotificationCompat.Builder notification;*/
/*private BroadcastReceiver br;*/

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent receiverIntent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 123456789, receiverIntent, 0);


        SharedPreferences sharedPref = this.getSharedPreferences("FITNESS_PREFERENCES", Context.MODE_PRIVATE);
        if(!sharedPref.contains("keep_working_timestamp")){
            Log.d("MAIN", "This is the first time. Creating timestamp KEY and setting it to 0");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("keep_working_timestamp", 0);
            editor.commit();
        }

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),60000, sender);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        // display the first navigation drawer view on app launch
        displayView(0);
    }



    /*protected void populateListView(){

        mDbHelper = new DataBase(this);
        Cursor cursor = mDbHelper.getAllGoals();
        String[] fromFieldNames= new String[] {DataBase.KEY_ID,DataBase.GOAL_DATE};
       *//* int toViewIDs= *//*
        SimpleCursorAdapter myCursorAdapter=new SimpleCursorAdapter(getBaseContext(),R.layout.fragment_display_goals, cursor,fromFieldNames,new int[]{R.id.list_item},0);
        ListView myList= (ListView) findViewbyId(R.id.list_item);
        myList.setAdapter(myCursorAdapter);

    }*/




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
                fragment = new DisplayGoalsFragment();
                title = "MY GOALS";
                break;
            case 4:
                fragment = new MessagesFragment();
                title = getString(R.string.title_personal_data);
                break;
            case 5:
                fragment = new StatsFragment();
                title = getString(R.string.title_statistics);
                break;
            case 6:
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