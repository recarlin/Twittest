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
	
	public interface checker {
		public void onBack();
		public void onSaveHome();
		public void onViewJSON();
	}
	
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
    	Button saveZip = (Button) view.findViewById(R.id.saveZip);
    	saveZip.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			check.onSaveHome();
    		}
    	});
    	Button JSONSource = (Button) view.findViewById(R.id.JSONSource);
    	JSONSource.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			check.onViewJSON();
    		}
    	});
		return view;
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
}
