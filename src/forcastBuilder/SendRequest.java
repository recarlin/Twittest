/*
 * project		WiswWeather
 * package		TweetBuilder
 * author		Russell Carlin
 * date			May 1, 2013
 */
package forcastBuilder;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class SendRequest {

	static Boolean connected = false;
//Runs the check for Internet connectivity.
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
//Gets the response from the site, building a string that is JSON data.
	public static String getResponse(URL url) {
		String response = "";
		try{
			URLConnection con = url.openConnection();
			BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
			byte[] content = new byte[1024];
			int read = 0;
			StringBuffer buff = new StringBuffer();
			while((read = bis.read(content)) != -1) {
				response = new String(content,0,read);
				buff.append(response);
			}
			return buff.toString();
		} catch(Exception e) {
			Log.e("URL IS MESSED UP!", "getResponse");
		}
		return response;
	}
}
