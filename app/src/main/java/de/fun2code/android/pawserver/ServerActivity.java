package de.fun2code.android.pawserver;

import java.io.*;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.*;
import android.net.ConnectivityManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
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
    Intent intent;
    ImageView imageView;
    private static Boolean start;
    SharedPreferences preferences;

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
        imageView = (ImageView) findViewById(R.id.imageView);
        cr.setApp_on(true);

        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        Boolean startedOnBoot = preferences.getBoolean("startedOnBoot", true);

        if(startedOnBoot&&wifi.isConnected()){
            startService();
        }else{

        }
        start = startedOnBoot;

        if (ServerService.isRunning()) {
            imageView.setImageResource(R.drawable.on);
            start = true;
        }else{
            imageView.setImageResource(R.drawable.off);
            start = false;
        }

        imageView.setOnClickListener(onClickListener);
        checkInstallation();
        messageHandler = new MessageHandler(this);
        ServerService.setActivityHandler(messageHandler);
        ServerService.setActivity(this);

    }


    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.imageView:
                    start = !start;
                    if(start){
                        imageView.setImageResource(R.drawable.on);
                        startService();
                    }else{
                        imageView.setImageResource(R.drawable.off);
                        onServiceStart(false);
                        stopService();
                    }

                    break;
            }
        }
    };

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
                intent = new Intent(this, Speech.class);
                startActivity(intent);
                return true;

            case R.id.action_settings:
                intent = new Intent(this, Device.class);
                startActivity(intent);
                return true;

            case R.id.action_informer:
                intent = new Intent(this, Informer.class);
                startActivity(intent);
                return true;

            case R.id.action_exit:
                cr.setApp_on(false);
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
        //startService();


        if (ServerService.isRunning()) {
            imageView.setImageResource(R.drawable.on);
            viewUrl.setText(url_temp);
        }else{
            imageView.setImageResource(R.drawable.off);
        }


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
        Intent serviceIntent = new Intent(this.getApplicationContext(), ServerService.class);
        stopService(serviceIntent);

    }


    @Override
    public void startService() {

        ServerService.registerServiceListener(this);

        if (ServerService.isRunning()) {
            viewUrl.setText(url_temp);
        }else{
            cr.setApp_on(true);
            Intent serviceIntent = new Intent(ServerActivity.this, ServerService.class);
            startService(serviceIntent);
        }
    }

    @Override
    public void onServiceStart(boolean success) {
        start = true;
        if (success) {
            imageView.setImageResource(R.drawable.on);
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
        start = false;
        if (ServerService.isRunning()) {
            imageView.setImageResource(R.drawable.on);
            viewUrl.setText(url_temp);
        }else{
            imageView.setImageResource(R.drawable.off);
            viewUrl.setText(" ");
        }

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

                //SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                SharedPreferences.Editor prefEdit = preferences.edit();
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

                //SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
                boolean sendCall = preferences.getBoolean("sendCall", true);

                cr.setSendCall(sendCall);

            } else {
                cr.setSendCall(false);
                cr.setStartBoolean(false);
                viewhead.setText(R.string.no_setting_server);
            }
        }
    }
}
