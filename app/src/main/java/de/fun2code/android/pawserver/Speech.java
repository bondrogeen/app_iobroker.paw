package de.fun2code.android.pawserver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Speech extends Activity {

    String TAG = "ioBroker.paw";
    TextView speech_text;
    Button botton_speech;
    Button botton;
    Bitmap bitmam;
    Intent intent;
    Context context;
    NotificationManager nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Start Speech");
        setContentView(R.layout.speech);
        botton_speech = (Button) findViewById(R.id.speech);
        //botton = (Button) findViewById(R.id.button3);

        //botton.setOnClickListener(onClickListener);
        botton_speech.setOnClickListener(onClickListener);

        //speech_text.setText("");
        //setListAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listSensorType));
        //getListView().setTextFilterEnabled(true);


    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case  R.id.speech:
                    Intent intt = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    // добаляем дополнительные параметры:
                    intt.putExtra(RecognizerIntent.EXTRA_PROMPT, "Голосовой поиск");  // текстовая подсказка пользователю
                    intt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);  // модель распознавания
                    intt.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);  // количество резальтатов, которое мы хотим получить
                    intt.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                    startActivityForResult(intt,1);
                    break;
            }
        }
    };


    public static String speech (String voice ){
        voice = "test";
        return voice;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        speech_text = (TextView) findViewById(R.id.speech_text);
        Log.i(TAG, "locale "+Locale.getDefault().toString());
        Log.i(TAG, "start ");
        if (data != null) {
            ArrayList respone = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (respone.size() > 0) {
                speech_text.setText("" + respone.get(0));
            }
            Log.i(TAG, "size "+respone.size());
        }
    }




    protected void onStop() {
        super.onStop();
        finish();

    }

}
