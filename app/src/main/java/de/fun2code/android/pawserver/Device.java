package de.fun2code.android.pawserver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;

public class Device extends PawServerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device);
        //Log.i(TAG, "Start SETTINGS "+INSTALL_DIR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}