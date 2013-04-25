/*
 * project		Twittest
 * package		TweetBuilder
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package TweetBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.widget.TextView;

public class AddNReadTweets {
	public static JSONObject NewTweet(String name, String date, String text) {
		JSONObject newTweet = new JSONObject();
		try {
			newTweet.put("name", name);
			newTweet.put("date", date);
			newTweet.put("text", text);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newTweet;
	};
	
	
	public static String ReadNewTweet(JSONObject object) {
		String post = new String();
		try {
			String name, date, text;
		    name = object.getString("name");
		    date = object.getString("date");
		    text = object.getString("text");
			post = "\r\n" + name + " - " + date + "\r\n" + text;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return post;
	}
	
	//This is used to generate a new TextView for each Tweet. It takes a String, makes it the text of a TextView, and returns the TextView.
	public static TextView NewTweetView(String post, Context context) {
		TextView tweetView = new TextView(context);
		tweetView.setText(post);
		return tweetView;
	}
}
