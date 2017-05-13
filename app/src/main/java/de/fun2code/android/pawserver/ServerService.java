package de.fun2code.android.pawserver;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Sample "Build your own PAW server" Service.
 *
 *
 */
public class ServerService extends PawServerService {

	@Override
	public void onCreate() {
		super.onCreate();

		TAG = getString(R.string.app_name);
		SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
		hideNotificationIcon = preferences.getBoolean("HideNotificationIcon", false);
		execAutostartScripts = preferences.getBoolean("ExecAutostartScripts", false);
		useWakeLock = preferences.getBoolean("UseWakeLock", true);
		showUrlInNotification = preferences.getBoolean("ShowUrlInNotification", false);
		//pawHome = preferences.getString("PawHome", pawHome);
		restartIpChanged = preferences.getBoolean("RestartIpChanged", true);
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

	}

}
