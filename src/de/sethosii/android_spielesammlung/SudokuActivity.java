package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SudokuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		GridLayout layout = new GridLayout(this);
		layout.setOrientation(GridLayout.VERTICAL);

		// Textfeld
		TextView txt = new TextView(this);
		txt.setText("Sudoku");
		txt.setLayoutParams(params);
		layout.addView(txt);

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
