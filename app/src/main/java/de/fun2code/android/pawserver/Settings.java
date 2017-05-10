package de.fun2code.android.pawserver;


import android.os.Bundle;
import android.util.Log;
import de.fun2code.android.pawserver.*;
import org.json.JSONObject;

import java.io.*;

public class Settings extends PawServerActivity {

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        TAG = "iobroker.paw";
        Log.i(TAG, "Start SETTINGS ");

        readFile();

    }

    void readFile() {

        INSTALL_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/www";
        String onfig = INSTALL_DIR + "/html/setting_test.json";


            // получаем путь к SD
            String sdPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/www/html/";
            // добавляем свой каталог к пути

            // формируем объект File, который содержит путь к файлу
            File sdFile = new File(sdPath, "setting_test.json");
            try {
                // открываем поток для чтения
                BufferedReader br = new BufferedReader(new FileReader(sdFile));
                String str = "";
                // читаем содержимое
                while ((str = br.readLine()) != null) {
                    Log.d(TAG, str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }


}
