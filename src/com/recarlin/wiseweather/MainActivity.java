/*
 * project		WiseWeather
 * package		com.recarlin.wiseweather
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package com.recarlin.wiseweather;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import forcastBuilder.FileSystemActions;
import forcastBuilder.ForecastOperations;
import forcastBuilder.SendRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

public class MainActivity extends Activity {
	
	private static Context context;
	Boolean connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.context = getApplicationContext();
//		try{
//			String myZip = FileSystemActions.readFile(MainActivity.this, "zip", false);
//			if (myZip != null) {
//				getForcastURL(myZip);
//			}
//		} catch(Exception e) {
//			Log.e("STORED ZIP", "There is no stored zip!");
//		}
		setContentView(R.layout.weather_layout);
//Button that will send the request, as long as you are connected to the Internet. If you aren't, you get an error.
		Button getForcast = (Button) findViewById(R.id.forcastButton);
		getForcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((GridLayout)findViewById(R.id.forecastGrid)).removeAllViews();
				connected = SendRequest.getConnected(MainActivity.this);
				if(connected) {
					String zipCode = ((EditText) findViewById(R.id.zipText)).getText().toString();
					getForcastURL(zipCode);
					FileSystemActions.storeFile(MainActivity.this, "zip", zipCode, false);
				} else {
					Log.i("CONNECTION", "You are not connected to the Internet!");
				}
			}
		});
//Checks to see if there is a stored zip code on the file system. If so, it loads the info for that zip code.
		Button getSaved = (Button) findViewById(R.id.savedButton);
		getSaved.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((GridLayout)findViewById(R.id.forecastGrid)).removeAllViews();
				connected = SendRequest.getConnected(MainActivity.this);
				if(connected) {
					try{
						String myZip = FileSystemActions.readFile(MainActivity.this, "zip", false);
						if (myZip != null) {
							getForcastURL(myZip);
						}
					} catch(Exception e) {
						Log.e("STORED ZIP", "There is no stored zip!");
					}
				} else {
					Log.i("CONNECTION", "You are not connected to the Internet!");
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
//This builds a URL for the request, based on what you pass into it from the EditText, and executes the getTimeline instance.
	private void getForcastURL(String zipCode) {
		try {
			URL forcastURL = new URL("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + zipCode + ".json");
			getForcast gfc = new getForcast();
			gfc.execute(forcastURL);
		} catch(MalformedURLException e) {
			Log.e("MALFORMED URL", "URL is incorrect!");
		}
	}
//Performs the URL request, reads the data, puts it into a TextView, and adds it to the main view.
	private class getForcast extends AsyncTask<URL, Void, String>{
		@Override
		protected String doInBackground(URL...urls) {
			String response = "";
			for (URL url: urls) {
				response = SendRequest.getResponse(url);
			}
			return response;
		}
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject resultJSON = new JSONObject(result);
				JSONArray results = resultJSON.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday");
				ArrayList<View> resultArray = ForecastOperations.readForcastJSON(results);
				for (int i = 0; i < resultArray.size(); i++) {
					((GridLayout)findViewById(R.id.forecastGrid)).addView(resultArray.get(i));
				}
			} catch(JSONException e) {
				Log.e("JSON ERROR", "Your JSON is incorrect!");
			}
		}
	}
//This is used to get the context in other classes that are not extentions of Activity.
	public static Context getAppContext() {
        return MainActivity.context;
    }
}
