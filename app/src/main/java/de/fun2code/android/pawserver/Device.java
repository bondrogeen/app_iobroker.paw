package de.fun2code.android.pawserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

public class Device extends PawServerActivity implements CompoundButton.OnCheckedChangeListener {
    public Context contex;
    String pawHome = ServerActivity.INSTALL_DIR + "/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean hideNotificationIcon = preferences.getBoolean("hideNotificationIcon", false);
        boolean execAutostartScripts = preferences.getBoolean("execAutostartScripts", false);
        boolean useWakeLock = preferences.getBoolean("useWakeLock", true);
        boolean showUrlInNotification = preferences.getBoolean("showUrlInNotification", false);
        boolean sendCall = preferences.getBoolean("sendCall", false);
        pawHome = preferences.getString("PawHome", pawHome);
        boolean restartIpChanged = preferences.getBoolean("restartIpChanged", true);
        boolean startedOnBoot = preferences.getBoolean("startedOnBoot", true);

        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setChecked(startedOnBoot);
        Switch switch2 = (Switch) findViewById(R.id.switch2);
        switch2.setChecked(useWakeLock);
        Switch switch3 = (Switch) findViewById(R.id.switch3);
        switch3.setChecked(restartIpChanged);
        Switch switch4 = (Switch) findViewById(R.id.switch4);
        switch4.setChecked(execAutostartScripts);
        Switch switch5 = (Switch) findViewById(R.id.switch5);
        switch5.setChecked(sendCall);
        Switch switch6 = (Switch) findViewById(R.id.switch6);
        switch6.setChecked(hideNotificationIcon);

        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);
        switch4.setOnCheckedChangeListener(this);
        switch5.setOnCheckedChangeListener(this);
        switch6.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = prefs.edit();
        switch (buttonView.getId()){
            case R.id.switch1:
                prefEdit.putBoolean("startedOnBoot", isChecked);
                break;
            case R.id.switch2:
                prefEdit.putBoolean("useWakeLock", isChecked);
                break;
            case R.id.switch3:
                prefEdit.putBoolean("restartIpChanged", isChecked);
                break;
            case R.id.switch4:
                prefEdit.putBoolean("execAutostartScripts", isChecked);
                break;
            case R.id.switch5:
                prefEdit.putBoolean("sendCall", isChecked);
                break;
            case R.id.switch6:
                prefEdit.putBoolean("hideNotificationIcon", isChecked);
                break;
        }

        prefEdit.commit();

        File myPath = new File(INSTALL_DIR);
        File myFile = new File(myPath, "Preferences");
        Log.i(TAG, "Setting save "+myFile);
        try
        {
            FileWriter fw = new FileWriter(myFile);
            PrintWriter pw = new PrintWriter(fw);
            Map<String,?> prefsMap = prefs.getAll();
            for(Map.Entry<String,?> entry : prefsMap.entrySet())
            {
                pw.println(entry.getKey() + ": " + entry.getValue().toString());
            }
            pw.close();
            fw.close();
        }
        catch (Exception e)
        {
            Log.i(TAG, e.toString());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}