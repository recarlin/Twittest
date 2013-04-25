/*
 * project		Twittest
 * package		com.recarlin.twittest
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package com.recarlin.twittest;

import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import TweetBuilder.AddNReadTweets;
import TweetBuilder.SendRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText nameEntry;
	EditText tweetEntry;
	LinearLayout lay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Sets the linear layout up.
		lay = new LinearLayout(this);
		lay.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		lay.setLayoutParams(par);
		
		//Sets up a title text view.
		TextView title = new TextView(this);
		title.setText("Twitter Feed");
		title.setTextSize(40);
		title.setGravity(Gravity.CENTER);
		
		LinearLayout.LayoutParams par2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		//Sets up a button, with click listener.
		Button saveTweet = new Button(this);
		saveTweet.setText("Get Timeline");
		//This listener will generate a new Tweet based on what is in the EditText fields.
		//At the end, it adds the new Tweet to the main view.
		saveTweet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = tweetEntry.getText().toString();
				getTimelineURL(username);
			}
		});
		
		//Sets up a EditText and TextView for users to type their Tweet.
		LinearLayout tweet = new LinearLayout(this);
		tweet.setOrientation(LinearLayout.HORIZONTAL);
		tweet.setLayoutParams(par2);
		par2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
		tweetEntry = new EditText(this);
		tweetEntry.setHint("Twitter Username");
		tweetEntry.setLayoutParams(par2);
		tweet.addView(tweetEntry);
		tweet.addView(saveTweet);
		
		//Here we add all of the views to the main view.
		lay.addView(title);
		lay.addView(tweet);
		setContentView(lay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void getTimelineURL(String username) {
		try {
			URL userTimelineURL = new URL("https://api.twitter.com/1.1/statuses/user_timeline.json?" + username + "=twitterapi&count=20");
			getTimeline gtl = new getTimeline();
			gtl.execute(userTimelineURL);
		} catch(MalformedURLException e) {
			Log.e("MALFORMED URL", "URL is incorrect!");
		}
	}
	
	private class getTimeline extends AsyncTask<URL, Void, String>{
		@Override
		protected String doInBackground(URL...urls) {
			String response = "";
			for (URL url: urls) {
				response = SendRequest.getResponse(url);
			}
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.i("URL RESPONSE", result);
		}
	}
}
