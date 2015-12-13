package info.androidhive.materialdesign.background;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.materialdesign.activity.DataBase;
import info.androidhive.materialdesign.device.BluetoothLeService;
import info.androidhive.materialdesign.device.GattAttributes;

/**
 * Created by rshir on 12.12.2015.
 */
public class UpdateData {
    private static final String TAG = "Bluetooth Device: ";

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private TextView mDataField;
    private BluetoothLeService mBluetoothLeService;
    private Button mGetDataButton;
    private String mDeviceName;
    private String mDeviceAddress;
    private boolean mConnected = false;
    private Cursor cursor;
    private DataBase mDbHelper;
    private SQLiteDatabase mDb;
    private String mData;
    private AlarmManager mAlarmManager;
    private Context mContext;
    private PendingIntent pendingIntent;
    public Activity activity;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String DEVICE_MAC = "88:0F:10:95:88:12";
    public static final String DEVICE_NAME = "MI";

    public UpdateData(Activity activity) {
        this.activity = activity;
    }

    // Connect to bluetooth device and update the database information
    public void receiveAndUpdateData() {
        // Setup Bluetooth device
        mDeviceName = DEVICE_NAME;
        mDeviceAddress = DEVICE_MAC;

        // Start intent service for device connection and data receiving
        Intent gattServiceIntent = new Intent(activity, BluetoothLeService.class);
        activity.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        activity.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        // Reconnect to be sure that we will receive the data
        reconnect();
        if(mData != null){
            writeDataToDB(mData);
        }
        if (mBluetoothLeService != null){
            mBluetoothLeService.close();
            activity.unregisterReceiver(mGattUpdateReceiver);
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //getActivity().finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Reconnect for Bluetooth device
    private void reconnect(){
        if (mConnected)
            mBluetoothLeService.close();
//            mBluetoothLeService.close();
        Log.d(TAG, "Reconnecting");
        if (mBluetoothLeService != null){
            mBluetoothLeService.close();
            mBluetoothLeService.connect(mDeviceAddress);
        }
    }

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
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                mData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                writeDataToDB(mData);
                mBluetoothLeService.close();
            }
        }
    };

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            if (GattAttributes.lookup(uuid, "unknown").equals("Band")) {
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
                // Loops through available characteristics
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    uuid = gattCharacteristic.getUuid().toString();
                    // Find step measurement
                    if (GattAttributes.lookup(uuid, "unknown").equals("Step Measurement")) {
                        mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                        mBluetoothLeService.readCharacteristic(gattCharacteristic);
                    }
                }
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
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
        //double calories = 10000;
        Long L = Math.round(calories);
        int caloriesInt = L.intValue();

        return Integer.toString(caloriesInt);
    }

    private void writeDataToDB(String data){
        mDbHelper = new DataBase(activity);
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
}

