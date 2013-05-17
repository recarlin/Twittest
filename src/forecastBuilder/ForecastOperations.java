/*
 * project		WiseWeather
 * package		forecastBuilder
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package forecastBuilder;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.recarlin.wiseweather.MainActivity;
import com.recarlin.wiseweather.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForecastOperations {
	public static Context context = MainActivity.getAppContext();
//This reads the response from the site, goes through the JSONArray, picks out what info I want,
//builds views for the images and text, then puts them into an array.
	public static ArrayList<View> readForcastJSON(JSONArray results) {
		ArrayList<View> post = new ArrayList<View>();
		try {
			for(int i = 0; i < results.length(); i++){
				String title, forcast, precipitation, icon;
//Picks out all the information from the JSON results that we need.
				JSONObject currentPeriod = results.getJSONObject(i);
				title = currentPeriod.getString("title");
			    forcast = currentPeriod.getString("fcttext");
			    precipitation = currentPeriod.getString("pop");
			    icon = currentPeriod.getString("icon");
//Uses the information gathered to create views ready to add to the display.
			    ImageView imageV = new ImageView(context);
		    	String myIcon = getIcon(icon);
		    	int idInt = context.getResources().getIdentifier(myIcon, "drawable", context.getPackageName());
		    	imageV.setImageResource(idInt);
			    TextView textV = newTextView((title + " - " + precipitation + "% Precipitation\r\n" + forcast + "\r\n"), context);
//Sets some properties to the views.
		    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    	params.gravity=Gravity.CENTER;
		    	imageV.setMinimumWidth(150);
		    	imageV.setMinimumHeight(150);
		    	imageV.setLayoutParams(params);
			    textV.setTextAppearance(context, R.style.Forecast);
//Adds the views to the ArrayList that will be returned.
			    post.add(imageV);
				post.add(textV);
			}
		} catch (JSONException e) {
			Log.e("READJSON", "Error Reading JSON!");
		}
		return post;
	}
//This builds a new ImageView for the bitmaps.
	public static ImageView newImageView(Bitmap post, Context context) {
		ImageView imageView = new ImageView(context);
		imageView.setImageBitmap(post);
		return imageView;
	}
//This builds a new TextView for the strings.
	public static TextView newTextView(String post, Context context) {
		TextView textView = new TextView(context);
		textView.setText(post);
		return textView;
	}
//This selects the proper image based on what icon the JSON says to use.
	public static String getIcon(String icon) {
		String myGIF = new String();
		if (icon.equals("chanceflurries") || icon.equals("chancesnow") || icon.equals("flurries") || icon.equals("snow") || icon.equals("nt_chanceflurries") || icon.equals("nt_chancesnow") || icon.equals("nt_flurries") || icon.equals("nt_snow")) {
			myGIF = "flurries";
		} else if (icon.equals("chancerain") || icon.equals("rain") || icon.equals("nt_chancerain") || icon.equals("nt_rain")) {
			myGIF = "rain";
		} else if (icon.equals("chancesleet") || icon.equals("sleet") || icon.equals("nt_chancesleet") || icon.equals("nt_sleet")) {
			myGIF = "sleet";
		} else if (icon.equals("chancetstorms") || icon.equals("tstorms") || icon.equals("nt_chancetstorms") || icon.equals("nt_tstorms")) {
			myGIF = "storms";
		} else if (icon.equals("clear") || icon.equals("sunny")) {
			myGIF = "clear";
		} else if (icon.equals("cloudy") || icon.equals("nt_cloudy")) {
			myGIF = "cloudy";
		} else if (icon.equals("fog") || icon.equals("hazy") || icon.equals("nt_fog") || icon.equals("nt_hazy")) {
			myGIF = "fog";
		} else if (icon.equals("mostlycloudy") || icon.equals("mostlysunny") || icon.equals("partlycloudy") || icon.equals("partlysunny")) {
			myGIF = "mostlycloudy";
		} else if (icon.equals("nt_clear") || icon.equals("nt_sunny")) {
			myGIF = "nt_clear";
		} else if (icon.equals("nt_mostlycloudy") || icon.equals("nt_mostlysunny") || icon.equals("nt_partlycloudy") || icon.equals("nt_partlysunny")) {
			myGIF = "nt_partlycloudy";
		}
		return myGIF;
	}
}
