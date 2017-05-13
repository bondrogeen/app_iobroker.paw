package de.fun2code.android.pawserver;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

import static de.fun2code.android.pawserver.ServerActivity.*;


public class Device extends PawServerActivity {
    public boolean useWakeLock = true;
    public boolean hideNotificationIcon = false;
    public boolean execAutostartScripts = false;
    public boolean showUrlInNotification = false;
    public boolean restartIpChanged = false;
    public Context contex;

    String pawHome = INSTALL_DIR + "/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device);
        Log.i(TAG, "Start SETTINGS "+INSTALL_DIR);
        Log.i(TAG, "pawHome "+pawHome);
        //readPreferences();
        saveSharedPreferences();

    }

    private void saveSharedPreferences()
    {
        // создадим для примера несколько строчек с настройками. Вы можете пропустить этот код
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = prefs.edit();

        prefEdit.putBoolean("HideNotificationIcon", false);
        prefEdit.putBoolean("ExecAutostartScripts", false);
        prefEdit.putBoolean("UseWakeLock", true);
        prefEdit.putBoolean("ShowUrlInNotification", false);
        prefEdit.putString("PawHome", "/");
        prefEdit.putBoolean("RestartIpChanged", false);
        //prefEdit.clear().commit();
        prefEdit.commit();

        // Теперь сам пример
        File myPath = new File(INSTALL_DIR);
        File myFile = new File(myPath, "MySharedPreferences");
        Log.i(TAG, "save "+INSTALL_DIR);
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
            // what a terrible failure...
            Log.i(TAG, e.toString());
        }
    }


    public void readPreferences() {


        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        hideNotificationIcon = preferences.getBoolean("HideNotificationIcon", false);
        execAutostartScripts = preferences.getBoolean("ExecAutostartScripts", false);
        useWakeLock = preferences.getBoolean("UseWakeLock", true);
        showUrlInNotification = preferences.getBoolean("ShowUrlInNotification", false);
        pawHome = preferences.getString("PawHome", pawHome);
        restartIpChanged = preferences.getBoolean("RestartIpChanged", true);

        Log.i(TAG, "hideNotificationIcon "+hideNotificationIcon);
    }



    /*
    this.context = context;
            SharedPreferences settings = context.getSharedPreferences("PawServer", 0);
            this.pawHome = settings.getString("PawHome", (String)null);
            File preferencesFile = new File(this.pawHome, "etc/preferences");
            if(preferencesFile.isFile() && preferencesFile.exists()) {
            try {
            ServerConfigUtil.injectPreferences(context, preferencesFile);
            } catch (FileNotFoundException var6) {
            Log.e("PawServerReceiver", "Preference file does not exist!");
            } catch (IOException var7) {
            Log.e("PawServerReceiver", "Preference file I/O error!");
            }
            }

    */

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}