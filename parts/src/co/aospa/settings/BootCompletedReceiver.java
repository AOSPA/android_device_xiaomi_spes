package co.aospa.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import co.aospa.settings.utils.FileUtils;
import co.aospa.settings.refreshrate.RefreshUtils;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final boolean DEBUG = false;
    private static final String TAG = "RefreshRateParts";
    
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "Received intent: " + intent.getAction());
        if (!intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
            return;
        }

        Log.i(TAG, "Boot completed, starting services");
        RefreshUtils.initialize(context);
    }        
}