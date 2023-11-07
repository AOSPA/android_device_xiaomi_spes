package co.aospa.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import co.aospa.settings.refreshrate.RefreshUtils;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final boolean DEBUG = false;
    private static final String TAG = "RefreshRateParts";
    
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "Received intent: " + intent.getAction());

        switch (intent.getAction()) {
            case Intent.ACTION_LOCKED_BOOT_COMPLETED:
                onLockedBootCompleted(context);
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                onBootCompleted(context);
                break;
        }
    }

    private static void onLockedBootCompleted(Context context) {
        // Services that don't require reading from data.
    }
    
    private static void onBootCompleted(Context context) {
        // Data is now accessible (user has just unlocked).
        RefreshUtils.initialize(context);
    }        
}