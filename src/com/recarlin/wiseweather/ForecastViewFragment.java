package com.recarlin.wiseweather;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ForecastViewFragment extends Fragment {
	
	Boolean connected = false;
	private checker check;
	
	public interface checker {
		public void onForecastGet();
		public void onHomeGet();
		public void onSaveHome();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			check = (checker) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "IMPLEMENT CHECKER");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.forecast_layout, container, false);
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
		return view;
	}
}
