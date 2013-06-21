package weatherwidget;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.recarlin.wiseweather.R;

import forecastBuilder.RequestService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	public String theStuff = new String();
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		String zip = RequestService.readFile(context, "zip", false);
		if (zip != null) {
			try{
				URL url = new URL("http://api.wunderground.com/api/137996d2b3a91dcf/forecast/q/" + zip + ".json");
				getTimeline gtl = new getTimeline();
				gtl.execute(url);
				
				Log.e("STUFF", theStuff);
				RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
				rv.setTextViewText(R.id.temp, theStuff);
				appWidgetManager.updateAppWidget(appWidgetIds, rv);
			} catch(Exception e) {
				Log.e("UPDATE", "Error updating!");
			}
		} else {
			Log.i("ERROR", "There was an issue updating.");
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	private class getTimeline extends AsyncTask<URL, Void, String>{
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
				theStuff = forecast;
			} catch(Exception e) {
				Log.e("JSON ERROR", "Your JSON is incorrect!");
			}
		}
	}
}
