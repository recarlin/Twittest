package com.recarlin.wiseweather;

import forecastBuilder.RequestService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class ForecastView extends Activity implements ForecastFragment.checker{
	String permZip;
	Boolean doSave = false;
	Boolean loaded = false;
	private Handler handler = new Handler() {
//This receives the message from the request service and checks if it was successful for not.
//If so, we then get the cursor from a query call on the content provider.
//After getting the cursor, we iterate through it, placing the data in the scrollview to display.
		public void handleMessage(Message message) {
			Object path = message.obj;
			if (message.arg1 == RESULT_OK && path != null) {
				Uri uri = Uri.parse("content://com.recarlin.wiseweather.forecastprovider/periods");
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				while (cursor.moveToNext()) {
					TextView first = new TextView(MainActivity.getAppContext());
					TextView second = new TextView(MainActivity.getAppContext());
  					LinearLayout col1 = ((LinearLayout)findViewById(getResources().getIdentifier("_0", "id", getPackageName())));
  					
  					first.setText(cursor.getString(1));
					second.setText(cursor.getString(2) + "\n");
					
					first.setTextAppearance(getApplicationContext(), R.style.Title);
					first.setTextSize(24);
					second.setTextAppearance(getApplicationContext(), R.style.Forecast);
					
  					col1.addView(first);
  					col1.addView(second);
				}
			} else {
				AlertDialog alert = new AlertDialog.Builder(MainActivity.getAppContext()).create();
	  			alert.setTitle("Error");
	  		    alert.setMessage("Something went terribly wrong. You should have brought your towel.");
	  		    alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
	  		    	public void onClick(final DialogInterface dialog, final int which) {
	  		        }
	  		     });
	  		     alert.show();
			}
		};
	};
//Here we set the ContentView to the fragment.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forecase_fragment);
    }
//Checks if information is already loaded onto the screen. If it isn't, it will go ahead and do so.
    @Override
    protected void onStart() {
        super.onStart();
        if (loaded.equals(false)) {
        	Bundle data = getIntent().getExtras();
	        if(data != null){
	          permZip = data.getString("zip");
	          getForecastURL(permZip);
	        }
	        loaded = true;
        }
    }
//This builds a URL for the request, based on what you pass into it from the EditText. It then starts the service to retrieve the data.
  	private void getForecastURL(String zipCode) {
  		try {
  			String forecastURL = new String("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + zipCode + ".json");
  			Intent intent = new Intent(this, RequestService.class);
  		    Messenger messenger = new Messenger(handler);
  		    intent.putExtra("MESSENGER", messenger);
  		    intent.setData(Uri.parse(forecastURL));
  		    intent.putExtra("urlpath", forecastURL);
  		    startService(intent);
  		} catch(Exception e) {
  			Log.e("GET FORECAST", "Something went wrong!");
  		}
  	}
//This makes an intent to send back to the main activity. It is used to save the zip, if selected.
  	@Override
  	public void finish() {
  	    Intent data = new Intent();
  	    data.putExtra("saveThisZip", permZip);
  	    data.putExtra("doSave", doSave);
  	    setResult(RESULT_OK, data);
  	    super.finish();
  	}
//Closes the activity when the Back button is clicked.
	@Override
	public void onBack() {
		finish();
	}
//Marks the zip to be saved as the new home zip and closes the activity when the Save button is clicked.
	@Override
	public void onSaveHome() {
		doSave = true;
		finish();
	}
//Launches an implicit intent to open the raw JSON data in the browser.
	@Override
	public void onViewJSON() {
		Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + permZip + ".json"));
		startActivity(internetIntent);
	}
}
