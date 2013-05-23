package com.recarlin.wiseweather;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import forecastBuilder.ConnectionService;
import forecastBuilder.ForecastOperations;
import forecastBuilder.RequestService;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForecastView extends Activity implements ForecastFragment.checker{
	String permZip;
	Boolean doSave = false;
	Boolean loaded = false;
	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			Object path = message.obj;
			if (message.arg1 == RESULT_OK && path != null) {

			} else {

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
//This builds a URL for the request, based on what you pass into it from the EditText, and executes the getTimeline instance.
  	private void getForecastURL(String zipCode) {
  		try {
  			String forecastURL = new String("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + zipCode + ".json");
  			Intent intent = new Intent(this, ConnectionService.class);
  		    Messenger messenger = new Messenger(handler);
  		    intent.putExtra("MESSENGER", messenger);
  		    intent.setData(Uri.parse(forecastURL));
  		    intent.putExtra("urlpath", forecastURL);
  		    startService(intent);
  		} catch(Exception e) {
  			Log.e("GET FORECAST", "Something went wrong!");
  		}
  	}
//Performs the URL request, reads the data, puts it into a TextView, and adds it to the main view.
  	private class getForcast extends AsyncTask<URL, Void, String>{
  		@Override
  		protected String doInBackground(URL...urls) {
  			String response = "";
  			for (URL url: urls) {
//  				response = RequestService.getResponse(url);
  			}
  			return response;
  		}
//This is where the magic happens. This takes JSON results and turns it into how I want it to look.
  		@Override
  		protected void onPostExecute(String result) {
//First, we get to where the information we want is.
  			try {
  				JSONObject resultJSON = new JSONObject(result);
  				JSONArray results = resultJSON.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday");
  				ArrayList<View> resultArray = ForecastOperations.readForcastJSON(results);
  				ArrayList<ImageView> imageArray = new ArrayList<ImageView>();
  				ArrayList<TextView> textArray = new ArrayList<TextView>();
//Next, we take even numbered views, which are ImageViews, and put them into an ArrayList.
//The same is done to odd numbered views, which are placed into their own TextView ArrayList.
  				for (int i = 0; i < resultArray.size(); i++) {
  					if ((i % 2) == 0) {
  						imageArray.add((ImageView)resultArray.get(i));
  					} else {
  						textArray.add((TextView)resultArray.get(i));
  					}
  				}
//Lastly, we take those two ArrayLists, one of ImageViews and one of TextViews, and iterate through them.
//Each one is placed into separate LinearLayouts, based on i, that have already been set up.
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
