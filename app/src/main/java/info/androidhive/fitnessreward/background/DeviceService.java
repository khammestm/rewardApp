package info.androidhive.fitnessreward.background;

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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import info.androidhive.fitnessreward.activity.DataBase;
import info.androidhive.fitnessreward.device.BluetoothLeService;


public class DeviceService extends WakeReminderIntentService {
    private static final String TAG = "DeviceService: ";

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

    public DeviceService(){
        super("DeviceService");
        mDeviceName = DEVICE_NAME;
        mDeviceAddress = DEVICE_MAC;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // Start intent service for device connection and data receiving
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        this.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        // Register Gatt Update receiver with filter(get only steps data)
        this.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        //Reconnect to band device to get new data
        reconnect();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    @Override
    void doReminderWork(Intent intent){
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //context.stopService(gattServiceIntent);
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
                mBluetoothLeService.close();
            }
        }
    };

    /**
     * Perform reconnection to Bluetooth(Band) device
     */
    private void reconnect(){
        if (mConnected) {
            mBluetoothLeService.disconnect();
            //mBluetoothLeService.close();
        }
        Log.d(TAG, "Reconnecting");
        if (mBluetoothLeService != null){
            mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    /**
     * Write data from band device to application database
     * @param data - data received from bluetooth device
     */
    private void writeDataToDB(String data){
        // Open database
        mDbHelper = new DataBase(this);
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
        Log.d(TAG, "Data record " + dateDB);

        // Update today record or create new day record
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

        // Close database
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
        int age = Integer.parseInt(ReadPersonalAge());
        int heigh = Integer.parseInt(ReadPersonalHeiht());
        int weight = Integer.parseInt(ReadPersonalWeight());
        double metob = (age*5 + 6.25*heigh + 10*weight);
        double calories = ((0.007*stepsInt * 0.698)*metob)*weight;
        //double calories = 900000;
        Log.d(TAG, "CALLORIES ");
        Long L = Math.round(calories);
        int caloriesInt = L.intValue();

        return Integer.toString(caloriesInt);
    }

    /**
     * Filter ony for actions from BluetoothLe Service
     * @return intentFilter
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /**
     * Get age of the user from DB
     */
    private String ReadPersonalAge(){
        mDbHelper = new DataBase(this);
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String agePerson = cursor.getString(cursor.getColumnIndex("age"));
            result = agePerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    /**
     * Get weight of the user from DB
     */
    private String ReadPersonalWeight(){
        mDbHelper = new DataBase(this);
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String weightPerson = cursor.getString(cursor.getColumnIndex("weight"));
            result = weightPerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

    /**
     * Get height of the user from DB
     */
    private String ReadPersonalHeiht(){
        mDbHelper = new DataBase(this);
        Cursor cursor = mDbHelper.getAllTodos();
        String result = "";
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            String heihtPerson = cursor.getString(cursor.getColumnIndex("heiht"));
            result = heihtPerson;
            Log.d("Home", "RESULT: " + result);
        }
        mDbHelper.close();
        return result;
    }

}
