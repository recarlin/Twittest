package com.recarlin.wiseweather;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ForecastFragment extends Fragment {
	private checker check;
//Interface used to perform actions from any activity.
	public interface checker {
		public void onBack();
	}
//Sets the listeners on all the buttons.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.forecast_layout, container, false);
		
		Button back = (Button) view.findViewById(R.id.back);
    	back.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			check.onBack();
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
