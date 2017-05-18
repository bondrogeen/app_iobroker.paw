package de.fun2code.android.pawserver;

import java.io.*;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.*;
import android.net.ConnectivityManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.fun2code.android.pawserver.listener.ServiceListener;
import de.fun2code.android.pawserver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerActivity extends PawServerActivity implements ServiceListener {
    @SuppressWarnings("unused")
    private Handler handler;
    private TextView viewUrl;
    private TextView viewhead;
    private ToggleButton toogleButton;
    private static String url_temp;
    public String server;
    public String port;
    public String device;
    public String namespace;
    Receiver cr = new Receiver();
    private ProgressDialog mDialog;
    private int mTotalTime = 70;
    ToggleButton toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = "ioBroker.paw";
        INSTALL_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/iobroker/paw";
        Log.i(TAG, "INSTALL_DIR " + INSTALL_DIR);
        calledFromRuntime = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        handler = new Handler();
        viewUrl = (TextView) findViewById(R.id.url);
        viewhead = (TextView) findViewById(R.id.head);
        cr.setApp_on(true);
        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!ServerService.isRunning()) {
                        startService();
                    }
                } else {
                    onServiceStart(false);
                    stopService();
                }
            }
        });

        checkInstallation();
        messageHandler = new MessageHandler(this);
        ServerService.setActivityHandler(messageHandler);
        ServerService.setActivity(this);

        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "main BroadcastReceiver ");
                final ConnectivityManager connMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                final android.net.NetworkInfo wifi = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                final android.net.NetworkInfo mobile = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifi.isConnected()) {
                    toggle.setChecked(true);
                    cr.setStartBoolean(true);
                }else{
                    toggle.setChecked(false);
                    cr.setStartBoolean(false);
                }
            }
        };

        IntentFilter intFilt = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(br, intFilt);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings_server:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;

            case R.id.action_speech:
                Intent ii = new Intent(this, speech.class);
                startActivity(ii);
                return true;

            case R.id.action_settings:
                Intent i = new Intent(this, Device.class);
                startActivity(i);
                return true;
            case R.id.action_exit:
                onServiceStart(false);
                stopService();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ServerService.registerServiceListener(this);
        startService();


        if (cr.getPostStatus()!=null){
            if(cr.getPostStatus()){
                viewhead.setText("");
            }else{
                viewhead.setText(R.string.no_ping);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopService();

        ServerService.unregisterServiceListener(this);

    }

    @Override
    public void stopService() {

        Receiver cr = new Receiver();
        cr.setApp_on(false);

        Intent serviceIntent = new Intent(this.getApplicationContext(),
                ServerService.class);
        stopService(serviceIntent);


    }


    @Override
    public void startService() {



        if (ServerService.isRunning()) {
            viewUrl.setText(url_temp);
        }else{
            cr.setApp_on(true);
            Intent serviceIntent = new Intent(ServerActivity.this,
                    ServerService.class);
            startService(serviceIntent);
        }


    }

    @Override
    public void onServiceStart(boolean success) {
        if (success) {
            PawServerService service = ServerService.getService();
            final String url = service.getPawServer().server.protocol
                    + "://" + Utils.getLocalIpAddress() + ":"
                    + service.getPawServer().serverPort;
            this.url_temp = url;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewUrl.setText(url_temp);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewUrl.setText("");
                }
            });
        }
    }

    @Override
    public void onServiceStop(boolean success) {

    }


    private void checkInstallation() {
        if (!new File(INSTALL_DIR).exists()) {
            new File(INSTALL_DIR).mkdirs();
            HashMap<String, Integer> keepFiles = new HashMap<String, Integer>();
            try {
                extractZip(getAssets().open("content.zip"),
                        INSTALL_DIR, keepFiles);
                checkServerSettings();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }else{
            checkServerSettings();
        }
    }

    private void checkServerSettings() {

        JSONObject jsonVar = new JSONObject();
        if (new File(INSTALL_DIR).exists()) {
            String sdPath = INSTALL_DIR + "/html/";
            File sdFile = new File(sdPath, "setting.json");
            if (sdFile.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(sdFile));
                    String str = "";
                    while ((str = br.readLine()) != null) {
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
                    server = jsonVar.get("server").toString();
                    port = jsonVar.get("port").toString();
                    device = jsonVar.get("device").toString();
                    namespace = jsonVar.get("namespace").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.putString("server", server);
                prefEdit.putString("port", port);
                prefEdit.putString("device", device);
                prefEdit.putString("namespace", namespace);
                prefEdit.commit();

                cr.setServer(server);
                cr.setPort(port);
                cr.setDev_name(device);
                cr.setApp_on(true);
                cr.setNamespace(namespace);

                SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                boolean sendCall = preferences.getBoolean("sendCall", true);

                cr.setSendCall(sendCall);

            } else {
                cr.setStartBoolean(false);
                viewhead.setText(R.string.no_setting_server);
            }
        }
    }
}
