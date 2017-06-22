package de.fun2code.android.pawserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import de.fun2code.android.pawserver.ServerActivity;

/**
 * Created by Roman on 09.05.2017.
 */



public class test {

    public static String TAG = "iobroker.paw";

     public static void test(){

         String x = ServerActivity.INSTALL_DIR;
         Log.i(TAG, "INSTALL_DIR x___ "+ x);



    }


    //File myPath = new File(Environment.getExternalStorageDirectory().toString());
    //String path = getApplicationInfo().dataDir
    //Log.i("iobroker.paw",myPath.toString() );

    //File myFile = new File(myPath, "MySharedPreferences");

}
