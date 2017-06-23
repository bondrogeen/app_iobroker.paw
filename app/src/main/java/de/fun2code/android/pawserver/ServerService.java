package de.fun2code.android.pawserver;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample "Build your own PAW server" Service.
 *
 *
 */
public class ServerService extends PawServerService {
	SensorManager sensorManager;
	private static String server;
	private static String port;
	private static String device;
	private static String namespace;
	private static String light;
	@Override
	public void onCreate() {
		super.onCreate();
		TAG = getString(R.string.app_name);
        Log.i(TAG, "ServerService Start ");
		SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
		hideNotificationIcon = preferences.getBoolean("hideNotificationIcon", false);
		execAutostartScripts = preferences.getBoolean("execAutostartScripts", false);
		useWakeLock = preferences.getBoolean("useWakeLock", true);
		showUrlInNotification = preferences.getBoolean("showUrlInNotification", false);
		//pawHome = preferences.getString("PawHome", pawHome);
		restartIpChanged = preferences.getBoolean("restartIpChanged", true);
		startedOnBoot = preferences.getBoolean("startedOnBoot", true);
		isRuntime = preferences.getBoolean("isRuntime", false);
		Log.i(TAG, "isRuntime "+isRuntime);
		//TAG = getString(R.string.app_name);
		//startedOnBoot = true;
		//isRuntime = false;
		serverConfig = ServerActivity.INSTALL_DIR + "/conf/server.xml";
		pawHome = ServerActivity.INSTALL_DIR + "/";
		//useWakeLock = true;
		//hideNotificationIcon = false;
		//execAutostartScripts = false;
		//showUrlInNotification = false;
		//restartIpChanged = true;
		notificationTitle = "Start iobroker.paw";
		notificationMessage = "Start server";
		appName = getString(R.string.app_name);
		activityClass = "de.fun2code.android.pawserver.ServerActivity";
		notificationDrawableId = R.drawable.ic_launcher;
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

		List<Sensor> sensors= sensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : sensors) {
			if(sensor.getType()== 5 ){
				Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
				sensorManager.registerListener(workingSensorEventListener, light, SensorManager.SENSOR_DELAY_NORMAL);
				Log.d("Sensors", "" + sensor.getName());
			}
		}

		server = preferences.getString("server", "");
		port = preferences.getString("port", "");
		device = preferences.getString("device", "");
		namespace = preferences.getString("namespace", "");
	}

	private final SensorEventListener workingSensorEventListener = new SensorEventListener() {

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType() == 8) {
				Log.i(TAG, "proximity: "+event.values[0]);
				light = String.valueOf(Math.round(event.values[0]));
				new SendSensor().execute();
			}
		}
	};


	public void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(workingSensorEventListener);
	}

	class SendSensor extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String response = null;
			Log.i(TAG, "Start ");
			String url = "http://" + server + ":" + port;
			try {
				DefaultHttpClient hc = new DefaultHttpClient();
				ResponseHandler<String> res = new BasicResponseHandler();
				HttpPost postMethod = new HttpPost(url);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("send", "proximity"));
				nameValuePairs.add(new BasicNameValuePair("value", light));
				nameValuePairs.add(new BasicNameValuePair("device", device));
				nameValuePairs.add(new BasicNameValuePair("namespace", namespace));
				postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				response = hc.execute(postMethod, res);
			} catch (Exception e) {
				Log.i(TAG, "err " + e);
				return String.valueOf(e);
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "RequestTask " + result);
			if(result.equals("post received")){
				//postStatus = true;
			}else{
				//postStatus = false;
			}
		}

		@Override
		protected void onPreExecute() {

		}
	}

}
