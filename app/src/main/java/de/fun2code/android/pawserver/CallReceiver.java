package de.fun2code.android.pawserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.telephony.TelephonyManager;
import android.view.View;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

public class CallReceiver extends BroadcastReceiver {

    protected static Context cont;
    final String TAG = "ioBroker.paw";
    private static String phoneNumber;
    private static String typeCall;
    private static String statusCall;
    final String url = "http://192.168.1.31:8898";
    private static String server;
    private static String port;
    private static String dev_name;
    private static String namespace;
    private static Boolean start;

    public void setServer(String server){
        this.server = server;
    }
    public void setStartBoolean(Boolean start){
        this.start = start;
    }

    public void setPort(String port){
        this.port = port;
    }
    public void setDev_name(String dev_name){
        this.dev_name = dev_name;
    }
    public void setNamespace(String namespace){
        this.namespace = namespace;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            typeCall = "outcoming";
            statusCall = "ringing";
        }
        else if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.i(TAG, "действие "+phoneState);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                typeCall = "incoming";
                statusCall = "ringing";
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                statusCall = "connection";
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                statusCall = "disconnection";
            }
        }
        Log.i(TAG, "statusCall : "+statusCall+", typeCall : "+typeCall+ ", phoneNumber : "+phoneNumber );
        if(start){
            new RequestTask().execute();
        }

    }


    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = null;

            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpPost postMethod = new HttpPost(url);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                //nameValuePairs.add(new BasicNameValuePair("server", server));
                nameValuePairs.add(new BasicNameValuePair("send", "call"));
                nameValuePairs.add(new BasicNameValuePair("device", dev_name));
                nameValuePairs.add(new BasicNameValuePair("namespace", namespace));
                nameValuePairs.add(new BasicNameValuePair("type", typeCall));
                nameValuePairs.add(new BasicNameValuePair("number", phoneNumber));
                nameValuePairs.add(new BasicNameValuePair("status", statusCall));
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = hc.execute(postMethod, res);
            } catch (Exception e) {
                Log.i(TAG, "err " + e);
                return String.valueOf(e);

            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "RequestTask "+result);
        }

        @Override
        protected void onPreExecute() {

        }
    }


}

