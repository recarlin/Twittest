/*
 * project		Twittest
 * package		TweetBuilder
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package TweetBuilder;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.widget.TextView;

public class AddNReadTweets {
	//Enum to build the default Tweets from.
	public enum Tweets{
		FIRST("Bill","April 5, 2013", "Hello everyone!"),
		SECOND("Russell","April 6, 2013", "YAY Twitter!"),
		THIRD("Kelly","April 7, 2013", "What's up?");
		
		private final String name;
		private final String date;
		private final String text;
		
		private Tweets(String name, String date, String text){
			this.name = name;
			this.date = date;
			this.text = text;
		}
		
		public String setName(){
			return name;
		}
		public String setDate(){
			return date;
		}
		public String setText(){
			return text;
		}
	};
	
	//Builds the default Tweets JSONObject by looping through the Enum.
	public static JSONObject AddDefault() {
		JSONObject Timeline = new JSONObject();
		try {
			for (Tweets tweet : Tweets.values()) {
				JSONObject currentTweet = new JSONObject();
				currentTweet.put("name", tweet.setName());
				currentTweet.put("date", tweet.setDate());
				currentTweet.put("text", tweet.setText());
				Timeline.put(tweet.name().toString(), currentTweet);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Timeline;
	};
	
	//Builds a new Tweet JSONObject by taking the values set in the different EditText fields.
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
	
	//This reads the default Tweets JSONObject by calling the AddDefault method above.
	//It takes the JSONObject, iterates through each key (Tweet), and generates the the information in the tweet in a String format.
	public static String ReadDefaults() {
		String post = new String();
		try {
			JSONObject defaults = AddDefault();
			@SuppressWarnings("unchecked")
			Iterator<String> iter = defaults.keys();
			while(iter.hasNext()){
			    String name, date, text;
				String key = iter.next();
			    JSONObject object = defaults.getJSONObject(key);
			    name = object.getString("name");
			    date = object.getString("date");
			    text = object.getString("text");
				if (post.isEmpty()) {
					post = name + " - " + date + "\r\n" + text;
				} else {
					post = post + "\r\n\r\n" + name + " - " + date + "\r\n" + text;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return post;
	}
	
	//Reads new Tweets by taking a JSONObject, grabbing the values for each key, and generating a String with all of the information.
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
