package de.fun2code.android.pawserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServerReceiver extends BroadcastReceiver {
    final String TAG = "ioBroker.paw";
    private Context context;
    private Intent intent;
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException var3) {
            ;
        }
        Log.i(TAG, "Autostart ioBroker");

        Intent i = new Intent(context, ServerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}

