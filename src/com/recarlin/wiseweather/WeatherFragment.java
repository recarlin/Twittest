package com.recarlin.wiseweather;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WeatherFragment extends Fragment {
	Boolean connected = false;
	private checker check;
//Interface used to perform actions from any activity.
	public interface checker {
		public void onForecastGet();
		public void onHomeGet();
		public void onChangeGet();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.weather_layout, container, false);
//Button that will send the request, as long as you are connected to the Internet.
		Button getForcast = (Button) view.findViewById(R.id.forcastButton);
		getForcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				check.onForecastGet();
			}
		});
//Checks to see if there is a stored zip code on the file system. If so, it loads the info for that zip code.
		Button getSaved = (Button) view.findViewById(R.id.savedButton);
		getSaved.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				check.onHomeGet();
			}
		});
//This handles the click listener on the logo, which opens the change log.
		ImageView getChange = (ImageView) view.findViewById(R.id.changeLogButton);
		getChange.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				check.onChangeGet();
			}
		});
		return view;
	}
//Checks to see if the activity calling the fragment has the correct implementation to use it.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			check = (checker) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "IMPLEMENT CHECKER");
		}
	}
}
