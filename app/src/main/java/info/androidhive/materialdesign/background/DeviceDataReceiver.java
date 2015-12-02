package info.androidhive.materialdesign.background;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import info.androidhive.materialdesign.device.BluetoothLeService;

public class DeviceDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        WakeReminderIntentService.acquireStaticLock(context);
        // For our recurring task, we'll just display a message
        Intent service = new Intent(context, DeviceService.class);
        context.startService(service);
    }
}
