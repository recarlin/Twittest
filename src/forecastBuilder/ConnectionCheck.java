package forecastBuilder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionCheck {

	static Boolean connected = false;
	
	public static Boolean getConnected(Context context) {
		connectionInfo(context);
		return connected;
	}
//Checks to see if you are connected to the Internet.
	public static void connectionInfo(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manager.getActiveNetworkInfo();
		if(netInfo != null) {
			if(netInfo.isConnected()) {
				connected = true;
			}
		}
	}
}
