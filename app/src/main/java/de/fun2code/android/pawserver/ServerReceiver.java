package de.fun2code.android.pawserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class ServerReceiver extends BroadcastReceiver {

    final String TAG = "ioBroker.paw";
    Context context;
    Intent intent;

    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.app_name), context.MODE_PRIVATE);
        Boolean startedOnBoot = preferences.getBoolean("startedOnBoot", true);

        if (startedOnBoot) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException var3) {
                ;
            }

            this.context = context;
            this.intent = intent;
            Intent i = new Intent(context, ServerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            Log.i(TAG, "Autostart ioBroker");
        }
    }
}

