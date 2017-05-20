package de.fun2code.android.pawserver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Roman on 18.05.2017.
 */

public class Informer extends Activity{

    TextView titleText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.infotmer);

        titleText = (TextView) findViewById(R.id.textView6);
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        String size = intent.getStringExtra("size");
        if(size == null){
            text = "Это проверочный текст !!! Diese Validierung Text !!! This is the verification text !!! This is another test text !!!";
        }
        titleText.setTextSize(100);
        titleText.setText(text);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
