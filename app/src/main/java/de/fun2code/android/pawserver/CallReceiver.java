package de.fun2code.android.pawserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.telephony.TelephonyManager;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;
import de.fun2code.android.pawserver.PawServerActivity;


public class CallReceiver extends BroadcastReceiver {
    //final static String TAG = PawServerActivity.TAG;
    private static boolean incomingCall = false;
    public String Number;
    @Override
    public void onReceive(Context context, Intent intent) {
        String TAG = "iobroker.paw";
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Log.i(TAG, "действие "+phoneState);
            if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //Трубка не поднята, телефон звонит
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Number = phoneNumber;
                incomingCall = true;
                new RequestTask().execute("http://192.168.1.31:8898","incoming",phoneNumber);
                Log.i(TAG, "звонок"+ phoneNumber);
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //Телефон находится в режиме звонка (набор номера при исходящем звонке / разговор)
                new RequestTask().execute("http://192.168.1.31:8898","connection",Number);
                Log.i(TAG, "разговор");
            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //Телефон находится в ждущем режиме - это событие наступает по окончанию разговора
                //или в ситуации "отказался поднимать трубку и сбросил звонок".
                new RequestTask().execute("http://192.168.1.31:8898","disconnection",Number);
                Log.i(TAG, "сбросил звонок");
            }
        }
    }


    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String TAG = "ioBroker.paw";
                Log.i(TAG, " "+params.length );
            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpPost postMethod = new HttpPost(params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("call", params[1]));
                nameValuePairs.add(new BasicNameValuePair("number",params[2]));
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                String response = hc.execute(postMethod, res);
                } catch (Exception e) {
                Log.i(TAG, "err "+e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }
    }


}

