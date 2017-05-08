package de.fun2code.android.pawserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.widget.CompoundButton;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.fun2code.android.pawserver.listener.ServiceListener;
import de.fun2code.android.pawserver.util.Utils;


/**
 * Sample "Build your own PAW server" Activity.
 * 
 *
 */


public class ServerActivity extends PawServerActivity implements ServiceListener {
	@SuppressWarnings("unused")
	private Handler handler;
	
	// View that displays the server URL
	private TextView viewUrl;
	private TextView viewtvOut;
	private ToggleButton toogleButton;

	/** Called when the activity is first created. */
	@Override



	public void onCreate(Bundle savedInstanceState) {
		TAG = "iobroker.paw";
		INSTALL_DIR = getFilesDir().getAbsolutePath() + "/www";

		calledFromRuntime = true;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		handler = new Handler();

		viewUrl = (TextView) findViewById(R.id.url);
        //viewtvOut = (TextView) findViewById(R.id.tvOut);

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

		/* Check installation and extract ZIP if necessary */
		checkInstallation();

        //viewtvOut.setText("Server running on: ");
		/*
		 * Register handler This is needed in order to get dialogs etc. to work.
		 */
		messageHandler = new MessageHandler(this);
		ServerService.setActivityHandler(messageHandler);

		/*
		 * Register activity with service.
		 */
		ServerService.setActivity(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		 *  Registers the listener that calls onServiceStart() and
		 *  onServiceStop().
		 */
		//PawServerService service = ServerService.getService();
		//viewUrl = (TextView) findViewById(R.id.url);

		//viewtvOut.setText(url);



		ServerService.registerServiceListener(this);
		startService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//stopService();
		
		/*
		 * Unregisters the listener
		 */
		ServerService.unregisterServiceListener(this);

	}

	/**
	 * Stops the service
	 */
	@Override
	public void stopService() {
		Intent serviceIntent = new Intent(this.getApplicationContext(),
				ServerService.class);
		stopService(serviceIntent);
	}

	/**
	 * Starts the service
	 */
	@Override
	public void startService() {
		/*
		 * Do nothing, if service is already running.
		 */
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

}