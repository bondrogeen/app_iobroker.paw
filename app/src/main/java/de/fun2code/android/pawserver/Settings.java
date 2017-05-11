package de.fun2code.android.pawserver;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.fun2code.android.pawserver.*;
import org.json.JSONObject;

import java.io.*;

public class Settings extends PawServerActivity {

    private TextView ;
    private TextView viewtvOut;
    private ToggleButton toogleButton;

    //final String  INSTALL_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/www";
    //String onfig = INSTALL_DIR + "/html/setting_test.json";

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        //TAG = "iobroker.paw";
        Log.i(TAG, "Start SETTINGS "+INSTALL_DIR);
        readFile();
        writeFile();
    }

    void readFile() {

        String sdPath = INSTALL_DIR + "/html/";
        File sdFile = new File(sdPath, "setting_test.json");
        Log.i(TAG, sdFile.toString());
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            while ((str = br.readLine()) != null) {
                Log.i(TAG, str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile() {

        String sdPath = INSTALL_DIR + "/html/";
        File sdFile = new File(sdPath, "setting_test.json");
        //sdFile.mkdirs();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write("Содержимое файла на SD");
            bw.close();
            Log.i(TAG, "Файл записан в: " + sdFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
