package de.sethosii.android_spielesammlung;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	/**
	 * tag for logging
	 */
	private static final String Tag = "MainActivity";

	/**
	 * define custom content for List
	 */
	CustomListArrayAdapter adapter;

	
	/**
	 * Start Method for the programm 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// GUI nicht über XML-Datei erstellen
		// setContentView(R.layout.activity_main);
		Log.e(Tag, "startlist");

		//major values for the list, for positioning and define the right content
		ArrayList<String> values = new ArrayList<String>();
		values.add(getString(R.string.mines));
		values.add(getString(R.string.sudoku));
		values.add(getString(R.string.spaceslider));

		// use the custom list content
		adapter = new CustomListArrayAdapter(this, values);
		setListAdapter(adapter);
	}

	
	/**
	 * starts the selected game activity
	 * 
	 * @param ListView l the List Element
	 * @param View v the current View
	 * @param int position position of the chosen row
	 * @param long id the id of the row
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		if (item.startsWith(getString(R.string.mines))) {
			Intent mines = new Intent(this, MinesActivity.class);
			this.startActivity(mines);
		} else {
			if (item.startsWith(getString(R.string.sudoku))) {
				Intent sudoku = new Intent(this, SudokuActivity.class);
				this.startActivity(sudoku);
			} else {
				Intent spaceslider = new Intent(this, SpacesliderActivity.class);
				this.startActivity(spaceslider);
			}

		}
	}
	/**
	 * rebuild/refresh(Highscores) the list content 
	 */
	@Override
	protected void onResume() {
		super.onResume();

		Log.e(Tag, "onresume");
		//major values for the list, for positioning and define the right content
		ArrayList<String> values = new ArrayList<String>();
		values.add(getString(R.string.mines));
		values.add(getString(R.string.sudoku));
		values.add(getString(R.string.spaceslider));

		// use the custom list content
		adapter = new CustomListArrayAdapter(this, values);
		setListAdapter(adapter);
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present. 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
