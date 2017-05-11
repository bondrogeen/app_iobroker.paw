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

public class Settings extends PawServerActivity {
    final Context context = this;
    TextView server;
    TextView port;
    TextView dev_name;
    TextView namespace;


    JSONObject jsonVar = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        try {
            jsonVar.put("server", "192.168.1.31");
            jsonVar.put("port", "8898");
            jsonVar.put("dev_name", "dev1");
            jsonVar.put("namespace", "paw.0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        server = (TextView) findViewById(R.id.server);
        port = (TextView) findViewById(R.id.port);
        dev_name = (TextView) findViewById(R.id.dev_name);
        namespace = (TextView) findViewById(R.id.namespace);
        server.setOnClickListener(onClickListener);
        port.setOnClickListener(onClickListener);
        dev_name.setOnClickListener(onClickListener);
        namespace.setOnClickListener(onClickListener);

        Log.i(TAG, "Start SETTINGS "+INSTALL_DIR);
        readFile();
    }

    void readFile() {
        String sdPath = INSTALL_DIR + "/html/";
        File sdFile = new File(sdPath, "setting.json");
        Log.i(TAG, sdFile.toString());
        if(sdFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(sdFile));
                String str = "";
                while ((str = br.readLine()) != null) {
                    //Log.i(TAG, str);
                    jsonVar = new JSONObject(str);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                server.setText(jsonVar.get("server").toString());
                port.setText(jsonVar.get("port").toString());
                dev_name.setText(jsonVar.get("dev_name").toString());
                namespace.setText(jsonVar.get("namespace").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            server.setText("");
            port.setText("");
            dev_name.setText("");
            namespace.setText("");
        }
    }

    void writeFile() {
        String sdPath = INSTALL_DIR + "/html/";
        File sdFile = new File(sdPath, "setting.json");
        if(!sdFile.exists()){
            try {
                sdFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write(jsonVar.toString());
            bw.close();
            Log.i(TAG, "Файл записан в: " + sdFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //tv.setText("Server");
            switch (v.getId()){
                case R.id.server:
                    Set_id(1);
                    break;
                case R.id.port:
                    Set_id(2);
                    break;
                case R.id.dev_name:
                    Set_id(3);
                    break;
                case R.id.namespace:
                    Set_id(4);
                    break;
            }
        }
    };


    void Set_id(final int id_alert) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        final TextView tv = (TextView) promptsView.findViewById(R.id.tv);
        switch (id_alert){
            case 1:
                tv.setText(R.string.server);
                userInput.setText(server.getText());
                break;
            case 2:
                tv.setText(R.string.port);
                userInput.setText(port.getText());
                break;
            case 3:
                tv.setText(R.string.dev_name);
                userInput.setText(dev_name.getText());
                break;
            case 4:
                tv.setText(R.string.namespace);
                userInput.setText(namespace.getText());
                break;
        }



        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(id_alert == 1){
                                    server.setText(userInput.getText());
                                }else if(id_alert == 2){
                                    port.setText(userInput.getText());
                                }else if(id_alert == 3){
                                    dev_name.setText(userInput.getText());
                                }else if(id_alert == 4){
                                    namespace.setText(userInput.getText());
                                }

                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String alert = "";
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            jsonVar.put("server", server.getText().toString());
            jsonVar.put("port", port.getText().toString());
            jsonVar.put("dev_name", dev_name.getText().toString());
            jsonVar.put("namespace", namespace.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        writeFile();
    }




}
