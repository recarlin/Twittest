/*
 * project		WiseWeather
 * package		com.recarlin.wiseweather
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package com.recarlin.wiseweather;

import forcastBuilder.FileSystemActions;
import forcastBuilder.SendRequest;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

public class MainActivity extends Activity {
	
	private static Context _context;
	Boolean connected = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity._context = getApplicationContext();
		setContentView(R.layout.weather_layout);
//Button that will send the request, as long as you are connected to the Internet. If you aren't, you get an error.
		Button getForcast = (Button) findViewById(R.id.forcastButton);
		getForcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				connected = SendRequest.getConnected(MainActivity.this);
				if(connected) {
					String typedZip = ((EditText)findViewById(R.id.zipText)).getText().toString();
					Intent forecastView = new Intent(_context, ForecastView.class);
					forecastView.putExtra("zip", typedZip);
					startActivityForResult(forecastView, 0);
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
							Intent forecastView = new Intent(_context, ForecastView.class);
							forecastView.putExtra("zip", myZip);
							startActivityForResult(forecastView, 0);
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
//Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
//This is used to get the context in other classes that are not extensions of Activity.
	public static Context getAppContext() {
        return MainActivity._context;
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent newData) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			Bundle result = newData.getExtras();
			Boolean saveOrNot = result.getBoolean("doSave");
			if (saveOrNot.equals(true)) {
				String saveMahZip = result.getString("saveThisZip");
				FileSystemActions.storeFile(_context, "zip", saveMahZip, false);
			}
		}
	}
}
