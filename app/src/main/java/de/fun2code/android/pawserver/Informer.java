package de.fun2code.android.pawserver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.pm.ActivityInfo;

/**
 * Created by Roman on 18.05.2017.
 */

public class Informer extends Activity{

    TextView titleText;
    private final String TEXT = "Defaul text";
    private final String COLOR = "#FFFFFF";
    private final String SIZE = "50";
    private final String TEXT_COLOR = "#000000";
    private final String TAG = "ioBroker.paw";
    private LinearLayout bglayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.infotmer);

        bglayout  = (LinearLayout) findViewById(R.id.informer);



        titleText = (TextView) findViewById(R.id.textView6);
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        String textcolor = intent.getStringExtra("textcolor");
        String size = intent.getStringExtra("size");
        String color = intent.getStringExtra("color");

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        int rotate = getWindowManager().getDefaultDisplay().getRotation();

        Log.i(TAG, String.valueOf(rotate));

        if(text == null){
            text = TEXT;
        }
        if(size == null){
            size = SIZE;
        }
        if(color == null){
            color = COLOR;
        }
        if(textcolor == null){
            textcolor = TEXT_COLOR;
        }

        bglayout.setBackgroundColor(Color.parseColor(color));
        titleText.setTextColor(Color.parseColor(textcolor));
        titleText.setTextSize(Integer.parseInt(size));
        titleText.setText(text);

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }




}
