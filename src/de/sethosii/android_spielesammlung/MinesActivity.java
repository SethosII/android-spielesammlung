package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class MinesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_mines);
		((Button)findViewById(R.id.b11)).setText("1");
/*
		// Hauptlayout
		RelativeLayout layout = new RelativeLayout(this);

		// LinearLayout für Spielfeld und Aktionsbuttons 
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(GridLayout.VERTICAL);
		mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		// Layout für unteres Aktionsmenü
		LinearLayout actionsLayout = new LinearLayout(this);
		actionsLayout.setOrientation(LinearLayout.HORIZONTAL);
		actionsLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		actionsLayout.setPadding(1, 1, 1, 1);

		// Button für neues Spiel
		ImageButton newGame = new ImageButton(this);
		newGame.setImageResource(R.drawable.ic_action_new);
		newGame.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		actionsLayout.addView(newGame);

		// Button zum Umschalten zwischen Markieren und normalem Aufdecken
		ImageButton mark = new ImageButton(this);
		mark.setImageResource(R.drawable.ic_action_cancel);
		mark.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		actionsLayout.addView(mark);

		// Button zum öffnen des Menüs
		ImageButton menu = new ImageButton(this);
		menu.setImageResource(R.drawable.ic_action_view_as_grid);
		menu.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		actionsLayout.addView(menu);

		mainLayout.addView(actionsLayout);

		layout.addView(mainLayout);

		this.addContentView(layout, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
