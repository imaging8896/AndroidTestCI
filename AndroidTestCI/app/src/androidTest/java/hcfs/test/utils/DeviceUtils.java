package hcfs.test.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class DeviceUtils {
	public static void setWifiEnabled(Context context, boolean enabled) {
		int attempts = 60;
		while (isInternetConnected(context) != enabled && attempts > 0) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(enabled);
			attempts--;
			sleep(1);
		}
	}

	public static boolean isInternetConnected(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork.isConnected());
	}

	private static void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
