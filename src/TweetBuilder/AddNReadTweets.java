/*
 * project		Twittest
 * package		TweetBuilder
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package TweetBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class AddNReadTweets {
	//This reads the response from the site, goes through the JSONArray, picks out what info I want, and builds a String.
	public static String ReadDefaults(JSONArray results) {
		String post = new String();
		try {
			for(int i = 0; i < results.length(); i++){
				String title, forcast, precipitation;
				JSONObject currentPeriod = results.getJSONObject(i);
				title = currentPeriod.getString("title");
			    forcast = currentPeriod.getString("fcttext");
			    precipitation = currentPeriod.getString("pop");
				post = post + title + " - " + precipitation + "% Chance of Precipitation\r\n" + forcast + "\r\n\r\n";
			}
		} catch (JSONException e) {
			Log.e("READJSON", "Error Reading JSON!");
		}
		return post;
	}
	//This generates a new TextView for a String.
	public static TextView NewTweetView(String post, Context context) {
		TextView tweetView = new TextView(context);
		tweetView.setText(post);
		return tweetView;
	}
}
