package de.fun2code.android.pawserver;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.fun2code.android.pawserver.*;
import org.json.JSONObject;

import java.io.*;

public class Settings extends PawServerActivity {

    TextView server;
    TextView port;
    TextView dev_name;
    TextView namespace;

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        server = (TextView) findViewById(R.id.server);
        port = (TextView) findViewById(R.id.port);
        dev_name = (TextView) findViewById(R.id.dev_name);
        namespace = (TextView) findViewById(R.id.namespace);

        server.setOnClickListener(onClickListener);
        port.setOnClickListener(onClickListener);
        dev_name.setOnClickListener(onClickListener);
        namespace.setOnClickListener(onClickListener);
        //TAG = "iobroker.paw";
        Log.i(TAG, "Start SETTINGS "+INSTALL_DIR);
        //readFile();
        //writeFile();
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

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.server:
                    server.setText("123");
                    break;
                case R.id.port:
                    port.setText("123");
                    break;
                case R.id.dev_name:
                    dev_name.setText("123");
                    break;
                case R.id.namespace:
                    namespace.setText("123");
                    break;

            }

        }
    };




}
