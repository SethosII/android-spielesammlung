package de.sethosii.android_spielesammlung;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// GUI nicht über XML-Datei erstellen
		// setContentView(R.layout.activity_main);

		// Layout
		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		GridLayout layout = new GridLayout(this);
		layout.setOrientation(GridLayout.VERTICAL);

		// Textfeld
		TextView txt = new TextView(this);
		txt.setText("Hallo TextView");
		txt.setLayoutParams(params);
		layout.addView(txt);

		// Buttons
		Button[] buttons = new Button[8];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(this);
			buttons[i].setText("B " + i);
			buttons[i].setLayoutParams(params);
			layout.addView(buttons[i]);
		}

		LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.addContentView(layout, layoutparams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
