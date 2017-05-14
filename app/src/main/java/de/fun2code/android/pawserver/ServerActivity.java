package de.fun2code.android.pawserver;

import java.io.*;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
        TAG = "ioBroker.paw";
		INSTALL_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/www";
		String onfig = INSTALL_DIR + "/conf/ser.xml";
		Log.i(TAG, "INSTALL_DIR "+INSTALL_DIR);
		calledFromRuntime = true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		handler = new Handler();
		viewUrl = (TextView) findViewById(R.id.url);
		viewhead = (TextView) findViewById(R.id.head);
		ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//viewtvOut.setText("on");
					startService();
				} else {
					//viewtvOut.setText("off");
					onServiceStart(false);
					stopService();
				}
			}
		});

		checkInstallation();
		messageHandler = new MessageHandler(this);
		ServerService.setActivityHandler(messageHandler);
		ServerService.setActivity(this);

		checkServerSettings();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		//TextView TextView = (TextView) findViewById(R.id.url);
		switch (id) {
			case R.id.action_settings_server:
				Intent intent = new Intent(this, Settings.class);
				startActivity(intent);
				return true;

            case R.id.action_settings:
                Intent i = new Intent(this, Device.class);
                startActivity(i);
                return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		ServerService.registerServiceListener(this);
		startService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//stopService();

		ServerService.unregisterServiceListener(this);

	}

	@Override
	public void stopService() {
		Intent serviceIntent = new Intent(this.getApplicationContext(),
				ServerService.class);
		stopService(serviceIntent);
	}


	@Override
	public void startService() {
		if (ServerService.isRunning()) {
			return;
		}

		Intent serviceIntent = new Intent(ServerActivity.this,
		ServerService.class);
		startService(serviceIntent);
	}

	/**
	 * Called when the service has been started
	 * 
	 * @param success <code>true</code> if service was started successfully, 
	 * 					otherwise <code>false</code>
	 */
	@Override
	public void onServiceStart(boolean success) {
		if (success) {
			// Display URL
			PawServerService service = ServerService.getService();

			final String url = service.getPawServer().server.protocol
					+ "://" + Utils.getLocalIpAddress() + ":"
					+ service.getPawServer().serverPort;
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					viewUrl.setText(url);
				}
			});

		}
		else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					viewUrl.setText("");
				}
			});
		}

	}
	
	/**
	 * Called when the service has been stopped
	 * 
	 * @param success <code>true</code> if service was started successfully, 
	 * 					otherwise <code>false</code>
	 */
	@Override
	public void onServiceStop(boolean success) {
		
	}
	
	/**
	 * Checks the installation and extracts the content.zip file
	 * to INSTALL_DIR if needed
	 */
	private void checkInstallation() {
		if(!new File(INSTALL_DIR).exists()) {
			// Create directories
			new File(INSTALL_DIR).mkdirs();
			
			// Files not to overwrite
			HashMap<String, Integer> keepFiles = new HashMap<String, Integer>();
			
			// Extract ZIP file form assets
			try {
				extractZip(getAssets().open("content.zip"),
						INSTALL_DIR, keepFiles);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			
		}
	}

	private void checkServerSettings() {

		JSONObject jsonVar =  new JSONObject();
		if(new File(INSTALL_DIR).exists()) {
			String sdPath = INSTALL_DIR + "/html/";
			File sdFile = new File(sdPath, "setting.json");
			if(sdFile.exists()) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(sdFile));
					String str = "";
					while ((str = br.readLine()) != null) {
						//Log.i(TAG, str);
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

					SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
					SharedPreferences.Editor prefEdit = prefs.edit();
					prefEdit.putString("server", jsonVar.get("server").toString());
					prefEdit.putString("port", jsonVar.get("port").toString());
					prefEdit.putString("device", jsonVar.get("device").toString());
					prefEdit.putString("namespace", jsonVar.get("namespace").toString());
					prefEdit.clear().commit();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				viewhead.setText(R.string.no_setting_server);
			}
		}
	}
}
