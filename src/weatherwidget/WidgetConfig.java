package weatherwidget;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.recarlin.wiseweather.R;

import forecastBuilder.RequestService;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class WidgetConfig extends Activity{

	public String theStuff = "DEFAULT";
	public int id;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_layout);
	
		ImageView imb = (ImageView) findViewById(R.id.widgetButton);
		imb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Bundle extras = getIntent().getExtras();
				int widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				id = widgetID;
				try {
					String zip = ((EditText)findViewById(R.id.widgetZip)).getText().toString();
					URL url = new URL("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + zip + ".json");
					getTimeline gtl = new getTimeline();
					gtl.execute(url);
				} catch (MalformedURLException e) {
					Log.e("ERROR", "Maleformed URL");
				}
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
				setResult(RESULT_OK, resultValue);
				finish();
			}
			class getTimeline extends AsyncTask<URL, Void, String>{
				@Override
				protected String doInBackground(URL...urls) {
					String response = "";
					for (URL url: urls) {
						response = RequestService.getResponse(url);
					}
					return response;
				}

				@Override
				protected void onPostExecute(String result) {
					try {
						JSONObject resultJSON = new JSONObject(result);
						JSONArray results = resultJSON.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday");
						JSONObject first = results.getJSONObject(0);
						String forecast = first.getString("fcttext");
						RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout);
						rv.setTextViewText(R.id.temp, forecast);
						AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(id, rv);
					} catch(Exception e) {
						Log.e("JSON ERROR", "Your JSON is incorrect!");
					}
				}
			}
		});
	}
}

