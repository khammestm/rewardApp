package info.androidhive.materialdesign.activity;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.materialdesign.device.BluetoothLeService;

/**
 * Created by rshir on 24.11.2015.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
    private static final String TAG = "DeviceBootReceiver: ";

    private static final long REPEAT_TIME = 1000*30;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private Context mContext;
    private String mDeviceName;
    private String mDeviceAddress;
    private boolean mConnected = false;
    private String mData;
    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;


    public static final String DEVICE_MAC = "88:0F:10:95:88:12";
    public static final String DEVICE_NAME = "MI";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mDeviceName = DEVICE_NAME;
        mDeviceAddress = DEVICE_MAC;

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent receiverIntent = new Intent(context, DeviceBootReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);

            // Set the alarm to start at 8:30 a.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 30);

            // setRepeating() lets you specify a precise custom interval--in this case,
            // 20 minutes.
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * 2, alarmIntent);

            boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                    new Intent(context, StepsFragment.class),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp) {
                Log.d("DebugAlarmManager", "Alarm is already active");
            }
        }

        // Start intent service for device connection and data receiving
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                mData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                writeDataToDB(mData);
            }
        }
    };

    private void reconnect(){
        if (mConnected)
            mBluetoothLeService.disconnect();
        Log.d(TAG, "Reconnecting");
        if (mBluetoothLeService != null){
            mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    private void writeDataToDB(String data){
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDbHelper.getLastDataRecord();
        // Check if cursor is empty
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0 ){
            //cursor is empty
            Log.d(TAG, "Data record is empty. Writng the first row");
            mDbHelper.createNewDataRecord(data,convertToDistance(data), convertToCalories(data));
        }
        // Get last row data record
        cursor = mDbHelper.getLastDataRecord();
        String dateDB = cursor.getString(cursor.getColumnIndex("date"));
        long id = cursor.getLong(cursor.getColumnIndex("_id"));
        Log.d(TAG, "Data record "+dateDB);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateDBform = format.parse(dateDB);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date today = Calendar.getInstance().getTime();
            if (fmt.format(dateDBform).equals(fmt.format(today))){
                Log.d(TAG, "Data record updated");
                mDbHelper.updateDataRecord(id, data, convertToDistance(data), convertToCalories(data));
            } else {
                mDbHelper.createNewDataRecord(data,convertToDistance(data), convertToCalories(data));
            }
        } catch (ParseException e) {
            Log.d(TAG, "Something wrong with data comparing");
            e.printStackTrace();
        }

        mDbHelper.close();
    }

    /**
     * Convert steps measurements to distance
     * @param steps - fitness band measurement of steps
     * @return distance - measurement of the distance in meters
     */
    private String convertToDistance(String steps){
        double stepsInt = Double.parseDouble(steps);
        double distance = stepsInt * 0.698;
        Long L = Math.round(distance);
        int distanceInt = L.intValue();

        return Integer.toString(distanceInt);
    }

    /**
     * Convert steps measurements to calories
     * @param steps - fitness band measurement of steps
     * @return caloeris - measurement of calories in kcal
     */
    private String convertToCalories(String steps){
        double stepsInt = Double.parseDouble(steps);
        double calories = stepsInt * 0.048;
        Long L = Math.round(calories);
        int caloriesInt = L.intValue();

        return Integer.toString(caloriesInt);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}