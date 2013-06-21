package weatherwidget;

import com.recarlin.wiseweather.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WidgetConfig extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_config_layout);
		
		Button button = (Button) this.findViewById(R.id.finished);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle extras = getIntent().getExtras();
				int widget = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
				String widgetZip = ((EditText) findViewById(R.id.widgetZip)).toString();
			}
		});
	}
}
