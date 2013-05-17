/*
 * project		WiseWeather
 * package		com.recarlin.wiseweather
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package com.recarlin.wiseweather;

import forecastBuilder.FileSystemActions;
import forecastBuilder.SendRequest;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends Activity implements WeatherFragment.checker{
	private static Context _context;
	Boolean connected = false;
//Sets the layout fragment and also sets the _context up for use.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		MainActivity._context = getApplicationContext();
		setContentView(R.layout.weather_fragment);
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
//This is the result of the closing the forecast view and getting the intent data.
//It will save the zip to the filesystem if you pressed to do so, or it will just return to the main screen.
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
//Functionality for the new forecast button. It uses the user inputed zip to do a normal call.
	@Override
	public void onForecastGet() {
		connected = SendRequest.getConnected(MainActivity.this);
		if(connected) {
			String typedZip = ((EditText)findViewById(R.id.zipText)).getText().toString();
			if (typedZip.length() < 6 && typedZip.length() > 4) {
				Intent forecastView = new Intent(_context, ForecastView.class);
				forecastView.putExtra("zip", typedZip);
				startActivityForResult(forecastView, 0);
			} else {
			AlertDialog alert = new AlertDialog.Builder(this).create();
  			alert.setTitle("Error");
  		    alert.setMessage("You must type in an five(5) digit zip code.");
  		    alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
  		        public void onClick(final DialogInterface dialog, final int which) {
  		        }
  		     });
  		     alert.show();
			}
		}
	}
//Functionality for the home forecast button. It pulls the file from the system with the saved zip, and does a normal call.
	@Override
	public void onHomeGet() {
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
}
