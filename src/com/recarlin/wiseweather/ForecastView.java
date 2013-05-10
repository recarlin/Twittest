package com.recarlin.wiseweather;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import forcastBuilder.ForecastOperations;
import forcastBuilder.SendRequest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForecastView extends Activity {
	
	String permZip;
	Boolean doSave = false;
	Boolean loaded = false;
//Sets the layout up and the button listeners. Clicking back will finish the activity, save will finish and send an intent for the zip,
//and JSON will send an intent to open the browser to view JSON.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_layout);
        Button back = (Button) findViewById(R.id.back);
    	back.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			finish();
    		}
    	});
    	Button saveZip = (Button) findViewById(R.id.saveZip);
    	saveZip.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			doSave = true;
    			finish();
    		}
    	});
    	Button JSONSource = (Button) findViewById(R.id.JSONSource);
    	JSONSource.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + permZip + ".json"));
    			startActivity(internetIntent);
    		}
    	});
    }
//Fixes the bug with going from the browser back to the forecast view. Usually it will add a bunch of junk in there but this checks if it is already loaded.
//If it is loaded, then it does nothing. If it isn't it will load the forecast and set the boolean to true.
    @Override
    protected void onStart() {
        super.onStart();
        if (loaded.equals(false)) {
        	Bundle data = getIntent().getExtras();
	        if(data != null){
	          permZip = data.getString("zip");
	          getForcastURL(permZip);
	        }
	        loaded = true;
        }
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
//This is where the magic happens. This takes JSON results, breaks it down the views into an imageview array and a textview array.
//From there it will add the views to the appropriate linearlayouts already placed in the layout xml.
  		@Override
  		protected void onPostExecute(String result) {
  			try {
  				JSONObject resultJSON = new JSONObject(result);
  				JSONArray results = resultJSON.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday");
  				ArrayList<View> resultArray = ForecastOperations.readForcastJSON(results);
  				ArrayList<ImageView> imageArray = new ArrayList<ImageView>();
  				ArrayList<TextView> textArray = new ArrayList<TextView>();
  				for (int i = 0; i < resultArray.size(); i++) {
  					if ((i % 2) == 0) {
  						imageArray.add((ImageView)resultArray.get(i));
  					} else {
  						textArray.add((TextView)resultArray.get(i));
  					}
  				}
  				for (int i = 0; i < imageArray.size(); i++) {
  					String idString = "_" + i;
  					int idInt = getResources().getIdentifier(idString, "id", getPackageName());
  					LinearLayout layout = ((LinearLayout)findViewById(idInt));
  					layout.addView(imageArray.get(i));
  				}
  				for (int i = 0; i < textArray.size(); i++) {
  					String idString = "_" + i;
  					int idInt = getResources().getIdentifier(idString, "id", getPackageName());
  					LinearLayout layout = ((LinearLayout)findViewById(idInt));
  					layout.addView(textArray.get(i));
  				}
  			} catch(JSONException e) {
  				Log.e("JSON ERROR", "Your JSON is incorrect!");
  			}
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
}
