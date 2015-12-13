package info.androidhive.fitnessreward.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        WakeReminderIntentService.acquireStaticLock(context);
        // Run DeviceService (service responsible for gettting data from BT device)
        Intent service = new Intent(context, DeviceService.class);
        context.startService(service);
    }
}
