package de.fun2code.android.pawserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ServerReceiver extends BroadcastReceiver {
    final String TAG = "ioBroker.paw";
    private Context context;
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(TAG, "onReceive " + intent.getAction());
        //context.startService(new Intent(context, ServerActivity.class));
        Log.i(TAG, "Start iobroker");
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException var3) {
            ;
        }
        Log.i(TAG, "Start + 10сек iobroker");

        this.context.startService(new Intent(this.context, ServerActivity.class));
    }
}

