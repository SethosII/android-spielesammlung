package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class MinesActivity extends Activity {

	private int dimensionX;
	private int dimensionY;
	private int mineCount = 0;
	private Button[][] buttons;
	private TextView tvMineCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acitivity_mines);

		dimensionX = 9;
		dimensionY = 9;
		mineCount = 10;
		buttons = new Button[dimensionX][dimensionY];

		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				buttons[i][j] = ((Button) findViewById(R.id.b11 + i
						* dimensionY + j));
				buttons[i][j].setText("");
			}
		}

		tvMineCount = (TextView) findViewById(R.id.mineCount);
		tvMineCount.setText(getString(R.string.mines_remaining) + ": " + mineCount);

		/*
		 * dimensionX = 9; dimensionY = 9; buttons = new Button[9][9];
		 * 
		 * // Hauptlayout RelativeLayout layout = new RelativeLayout(this);
		 * layout.setPadding(10, 10, 10, 10);
		 * 
		 * // LinearLayout für Spielfeld und Aktionsbuttons LinearLayout
		 * mainLayout = new LinearLayout(this);
		 * mainLayout.setOrientation(LinearLayout.VERTICAL);
		 * mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		 * 
		 * // LinearLayouts für Spielfeldzeilen LinearLayout gameLayout = new
		 * LinearLayout(this); gameLayout.setOrientation(LinearLayout.VERTICAL);
		 * gameLayout.setBackgroundColor(Color.BLACK);
		 * gameLayout.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 20));
		 * 
		 * // einzelne Zeilen LinearLayout[] gameRows = new
		 * LinearLayout[dimensionX]; for (int i = 0; i < gameRows.length; i++) {
		 * gameRows[i] = new LinearLayout(this);
		 * gameRows[i].setOrientation(LinearLayout.HORIZONTAL);
		 * gameRows[i].setPadding(1, 1, 1, 1); gameRows[i].setLayoutParams(new
		 * LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT,
		 * LayoutParams.WRAP_CONTENT, 1)); buttons[i] = new
		 * Button[gameRows.length]; for (int j = 0; j < gameRows.length; j++) {
		 * buttons[i][j] = new Button(this);
		 * buttons[i][j].setBackgroundColor(Color.WHITE);
		 * buttons[i][j].setText("9"); buttons[i][j].setPadding(1, 1, 1, 1);
		 * buttons[i][j] .setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		 * gameRows[i].addView(buttons[i][j]); }
		 * gameLayout.addView(gameRows[i]);
		 * 
		 * }
		 * 
		 * mainLayout.addView(gameLayout);
		 * 
		 * // Layout für unteres Aktionsmenü LinearLayout actionsLayout = new
		 * LinearLayout(this);
		 * actionsLayout.setOrientation(LinearLayout.HORIZONTAL);
		 * actionsLayout.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
		 * 
		 * // Button für neues Spiel ImageButton newGame = new
		 * ImageButton(this);
		 * newGame.setImageResource(R.drawable.ic_action_new);
		 * newGame.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 3));
		 * actionsLayout.addView(newGame);
		 * 
		 * // Button zum Umschalten zwischen Markieren und normalem Aufdecken
		 * ImageButton mark = new ImageButton(this);
		 * mark.setImageResource(R.drawable.ic_action_cancel);
		 * mark.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 3));
		 * actionsLayout.addView(mark);
		 * 
		 * // Button zum öffnen des Menüs ImageButton menu = new
		 * ImageButton(this);
		 * menu.setImageResource(R.drawable.ic_action_view_as_grid);
		 * menu.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 3));
		 * actionsLayout.addView(menu);
		 * 
		 * // Layout für Infoanzeigen LinearLayout info = new
		 * LinearLayout(this); info.setOrientation(LinearLayout.VERTICAL);
		 * info.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		 * 
		 * // Minenanzeige TextView remaining = new TextView(this);
		 * remaining.setText(getString(R.string.mines) + ": " + mineCount);
		 * remaining.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		 * info.addView(remaining);
		 * 
		 * // Zeitanzeige Chronometer time = new Chronometer(this);
		 * time.setFormat("%s"); time.setGravity(Gravity.CENTER);
		 * time.setLayoutParams(new LinearLayout.LayoutParams(
		 * LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		 * info.addView(time);
		 * 
		 * actionsLayout.addView(info);
		 * 
		 * mainLayout.addView(actionsLayout);
		 * 
		 * layout.addView(mainLayout);
		 * 
		 * this.addContentView(layout, new RelativeLayout.LayoutParams(
		 * LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
