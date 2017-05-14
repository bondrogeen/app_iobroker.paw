package de.fun2code.android.pawserver;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Settings extends PawServerActivity {
    final Context context = this;
    TextView server;
    TextView port;
    TextView dev_name;
    TextView namespace;
    TextView resultTest;
    Button testButton;
    private String url;
    private String postServer;
    private String postPort;

    JSONObject jsonVar = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        server = (TextView) findViewById(R.id.server);
        port = (TextView) findViewById(R.id.port);
        dev_name = (TextView) findViewById(R.id.dev_name);
        namespace = (TextView) findViewById(R.id.namespace);
        resultTest = (TextView) findViewById(R.id.resultTest);
        testButton = (Button) findViewById(R.id.testButton);

        server.setOnClickListener(onClickListener);
        port.setOnClickListener(onClickListener);
        dev_name.setOnClickListener(onClickListener);
        namespace.setOnClickListener(onClickListener);
        testButton.setOnClickListener(onClickListener);

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
                dev_name.setText(jsonVar.get("device").toString());
                namespace.setText(jsonVar.get("namespace").toString());
                postServer = jsonVar.get("server").toString();
                postPort = jsonVar.get("port").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            server.setText("");
            port.setText("");
            dev_name.setText("");
            namespace.setText("");
        }

        url = "http://"+postServer+":"+postPort;

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
                case R.id.testButton:
                    resultTest.setText("");
                    new Settings.TestSetToServer().execute();
                    //Set_id(4);
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
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(id_alert == 1){
                                    server.setText(userInput.getText());
                                    postServer = userInput.getText().toString();
                                }else if(id_alert == 2){
                                    port.setText(userInput.getText());
                                    postPort = userInput.getText().toString();
                                }else if(id_alert == 3){
                                    dev_name.setText(userInput.getText());
                                }else if(id_alert == 4){
                                    namespace.setText(userInput.getText());
                                }
                                url = "http://"+postServer+":"+postPort;
                            }
                        })

                .setNegativeButton(android.R.string.cancel,
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

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit)
                .setMessage(R.string.save_setting)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();

                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            jsonVar.put("server", server.getText().toString());
                            jsonVar.put("port", port.getText().toString());
                            jsonVar.put("device", dev_name.getText().toString());
                            jsonVar.put("namespace", namespace.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        writeFile();

                        finish();
                    }
                }).create().show();
    }


    class TestSetToServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpPost postMethod = new HttpPost(url);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                //nameValuePairs.add(new BasicNameValuePair("typeCall", typeCall));
                //nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumber));
                //nameValuePairs.add(new BasicNameValuePair("statusCall", statusCall));
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = hc.execute(postMethod, res);
            } catch (Exception e) {
                //Log.i(TAG, "err " + e);
                return String.valueOf(e);

            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.i(TAG, "RequestTask "+result);
            if(result.equals("post received")){
                Log.i(TAG, "RequestTask "+result);
                resultTest.setTextColor(Color.GREEN);
                resultTest.setText("Connection");

            }else {
                resultTest.setTextColor(Color.RED);
                resultTest.setText("No connection");
                Log.i(TAG, "RequestTask "+result);
            }
        }

        @Override
        protected void onPreExecute() { Log.i(TAG, "RequestTask ");

        }
    }



}
