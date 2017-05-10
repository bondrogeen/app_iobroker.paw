package de.fun2code.android.pawserver;


import android.os.Bundle;
import android.util.Log;
import de.fun2code.android.pawserver.*;

public class Settings extends PawServerActivity {

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.settings);
        TAG = "iobroker.paw";
        Log.i(TAG, "Start SETTINGS ");

    }
}
