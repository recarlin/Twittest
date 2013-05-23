/*
 * project		WiswWeather
 * package		forecastBuilder
 * author		Russell Carlin
 * date			May 1, 2013
 */
package forecastBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class RequestService extends IntentService{

	private int result = Activity.RESULT_CANCELED;
	
	public RequestService() {
		super("SendRequest");
	}
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
//This bit of code, from the site in the lesson, turned out to be MUCH better than what I had slopped together.
//I was using the file writer in my FileSystemActions class to create the file.
//It was way too big and bulky. So, I just grabbed this code to make it cleaner.
	@Override
	protected void onHandleIntent(Intent intent) {
		Uri data = intent.getData();
		String urlPath = intent.getStringExtra("urlpath");
		String fileName = data.getLastPathSegment();
		File output = new File(Environment.getExternalStorageDirectory(), fileName);
		if (output.exists()) {
			output.delete();
		}
		InputStream stream = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(urlPath);
			stream = url.openConnection().getInputStream();
			InputStreamReader reader = new InputStreamReader(stream);
			fos = new FileOutputStream(output.getPath());
			int next = -1;
			while ((next = reader.read()) != -1) {
				fos.write(next);
			}
			result = Activity.RESULT_OK;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.arg1 = result;
			msg.obj = output.getAbsolutePath();
			try {
				messenger.send(msg);
			} catch (android.os.RemoteException e1) {
				Log.w(getClass().getName(), "Exception sending message", e1);
			}
		}
	}
}
