package de.fun2code.android.pawserver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.telephony.TelephonyManager;
import com.github.kevinsawicki.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class CallReceiver extends BroadcastReceiver {
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
            String TAG = "iobroker.paw";

                Log.i(TAG, " "+params.length );

            try {
                //создаем запрос на сервер
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                //он у нас будет посылать post запрос
                HttpPost postMethod = new HttpPost(params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                //передаем параметры из наших текстбоксов
                //лоигн
                nameValuePairs.add(new BasicNameValuePair("call", params[1]));
                //пароль
                nameValuePairs.add(new BasicNameValuePair("number",params[2]));
                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
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

