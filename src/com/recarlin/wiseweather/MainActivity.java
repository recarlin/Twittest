/*
 * project		WiseWeather
 * package		com.recarlin.wiseweather
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package com.recarlin.wiseweather;

import inscription.ChangeLogDialog;

import forecastBuilder.ConnectionCheck;
import forecastBuilder.RequestService;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends FragmentActivity{
	private static Context _context;
	Boolean connected = false;
//Sets the layout fragment and also sets the _context up for use.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu); 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case (R.id.change):
	        	ChangeLogDialog _ChangelogDialog = new ChangeLogDialog(this);
				_ChangelogDialog.show();
				break;
	        default:
	    }
	    return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity._context = getApplicationContext();
		setContentView(R.layout.weather_layout);
		
		WebView wv = (WebView) findViewById(R.id.webview);
		wv.loadUrl("file:///android_asset/WiseWeatherWebView/ForecastZip.html");
		wv.addJavascriptInterface(new WebAppInterface(this), "doIt");
		WebSettings ws = wv.getSettings();
		ws.setJavaScriptEnabled(true);
	}
//This is used to get the context in other classes that are not extensions of Activity.
	public static Context getAppContext() {
        return MainActivity._context;
    }
	
	public class WebAppInterface {
	    Context _Context;
	    WebAppInterface(Context c) {
	        _Context = c;
	    }

	    @JavascriptInterface
	    public void getNew(String zip) {
	        newGet(zip);
	    }
	    @JavascriptInterface
	    public void getHome() {
	        homeGet();
	    }
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
				RequestService.storeFile(_context, "zip", saveMahZip, false);
			}
		}
	}
//Functionality for the new forecast button. It uses the user inputed zip to do a normal call.
	public void newGet(String zip) {
		connected = ConnectionCheck.getConnected(this);
		if(connected) {
			if (zip.length() < 6 && zip.length() > 4) {
				Intent forecastView = new Intent(_context, ForecastView.class);
				forecastView.putExtra("zip", zip);
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
		} else {
			AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setTitle("Error");
			alert.setMessage("You are not connected!");
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
				}
			});
			alert.show();
		}
	}
//Functionality for the home forecast button. It pulls the file from the system with the saved zip, and does a normal call.
	public void homeGet() {
		connected = ConnectionCheck.getConnected(this);
		if(connected) {
			String myZip = RequestService.readFile(MainActivity.this, "zip", false);
			if (myZip != null) {
				Intent forecastView = new Intent(_context, ForecastView.class);
				forecastView.putExtra("zip", myZip);
				startActivityForResult(forecastView, 0);
			} else {
				AlertDialog alert = new AlertDialog.Builder(this).create();
				alert.setTitle("Error");
				alert.setMessage("There is no Home Zip saved!");
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int which) {
					}
				});
  		     alert.show();
			}
		} else {
			AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setTitle("Error");
			alert.setMessage("You are not connected!");
			alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
				}
			});
			alert.show();
		}
	}
}