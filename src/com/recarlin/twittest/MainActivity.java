/*
 * project		Twittest
 * package		com.recarlin.twittest
 * author		Russell Carlin
 * date			Apr 22, 2013
 */
package com.recarlin.twittest;


import java.util.Calendar;
import org.json.JSONObject;
import TweetBuilder.AddNReadTweets;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
		//Sets up a EditText and TextView for users to type their name.
		LinearLayout name = new LinearLayout(this);
		name.setOrientation(LinearLayout.HORIZONTAL);
		name.setLayoutParams(par2);
		nameEntry = new EditText(this);
		nameEntry.setHint("Type your name");
		TextView nameLabel = new TextView(this);
		nameLabel.setText("Name");
		nameLabel.setTextSize(25);
		name.addView(nameLabel);
		name.addView(nameEntry);
		
		
		//Sets up a EditText and TextView for users to type their Tweet.
		LinearLayout tweet = new LinearLayout(this);
		tweet.setOrientation(LinearLayout.HORIZONTAL);
		tweet.setLayoutParams(par2);
		tweetEntry = new EditText(this);
		tweetEntry.setHint("Type your Tweet");
		TextView tweetLabel = new TextView(this);
		tweetLabel.setText("Tweet");
		tweetLabel.setTextSize(25);
		tweet.addView(tweetLabel);
		tweet.addView(tweetEntry);
		
		//Sets up a button, with click listener.
		Button saveTweet = new Button(this);
		saveTweet.setText("Save Tweet");
		//This listener will generate a new Tweet based on what is in the EditText fields.
		//At the end, it adds the new Tweet to the main view.
		saveTweet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
				JSONObject tweetAsJSON = AddNReadTweets.NewTweet(nameEntry.getText().toString(), date, tweetEntry.getText().toString());
				String tweetAsString = AddNReadTweets.ReadNewTweet(tweetAsJSON);
				TextView tweetAsView = AddNReadTweets.NewTweetView(tweetAsString, MainActivity.this);
				lay.addView(tweetAsView);
			}
		});
		
		//This starts the generation of default Tweets. We end up with a string with all three Tweets.
		String defaultStrings = AddNReadTweets.ReadDefaults();
		//Then this call turns that default Tweet string into a TextView
		TextView defaultPosts = AddNReadTweets.NewTweetView(defaultStrings, this);
		//Here we add all of the views to the main view. This includes the default Tweets.
		lay.addView(title);
		lay.addView(name);
		lay.addView(tweet);
		lay.addView(saveTweet);
		lay.addView(defaultPosts);
		setContentView(lay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
