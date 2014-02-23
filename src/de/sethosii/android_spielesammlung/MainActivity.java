package de.sethosii.android_spielesammlung;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;



public class MainActivity extends ListActivity {

	private static final String Tag = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// GUI nicht über XML-Datei erstellen
		// setContentView(R.layout.activity_main);
		Log.e(Tag, "startlist");
		String[] values = new String[] { "Mines", "Sudoku", "Spaceslider" };
		// use your custom layout
	    CustomListArrayAdapter adapter = new CustomListArrayAdapter(this, values);
	    setListAdapter(adapter);
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		//		R.layout.listviewitem_main, R.id.top_Line, values);
		//setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		if (item.startsWith("Mines")) {
			Intent mines = new Intent(this, MinesActivity.class);
			this.startActivity(mines);
			System.out.println("Mines");
		} else {
			if (item.startsWith("Sudoku")) {
				Intent sudoku = new Intent(this, SudokuActivity.class);
				this.startActivity(sudoku);
				System.out.println("Sudoku");
			} else {
				Intent spaceslider = new Intent(this, SpacesliderActivity.class);
				this.startActivity(spaceslider);
				System.out.println("Spaceslider");
			}
		
		}
	}

	// private class StableArrayAdapter extends ArrayAdapter<String> {
	//
	// HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	//
	// public StableArrayAdapter(Context context, int textViewResourceId,
	// List<String> objects) {
	// super(context, textViewResourceId, objects);
	// for (int i = 0; i < objects.size(); ++i) {
	// mIdMap.put(objects.get(i), i);
	// }
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// String item = getItem(position);
	// return mIdMap.get(item);
	// }
	//
	// @Override
	// public boolean hasStableIds() {
	// return true;
	// }
	//
	// }

	// LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	// LinearLayout.LayoutParams match = new LinearLayout.LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	// LinearLayout.LayoutParams match_wrap = new LinearLayout.LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	// LinearLayout.LayoutParams wrap_match = new LinearLayout.LayoutParams(
	// LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
	//
	// // Layout
	// LinearLayout layout = new LinearLayout(this);
	// layout.setOrientation(LinearLayout.VERTICAL);
	//
	// // Menüeinträge
	// int game_count = 3;
	// LinearLayout[] entry_layout = new LinearLayout[game_count];
	// ImageView[] icons = new ImageView[game_count];
	// LinearLayout[] text_layout = new LinearLayout[game_count];
	// TextView[] games = new TextView[game_count];
	// TextView[] highscores = new TextView[game_count];
	// Button[] buttons = new Button[game_count];
	// MainActivityListener buttonlistener = new MainActivityListener(this);
	//
	// for (int i = 0; i < entry_layout.length; i++) {
	// entry_layout[i] = new LinearLayout(this);
	// entry_layout[i].setOrientation(LinearLayout.HORIZONTAL);
	// entry_layout[i].setPadding(0, 2, 0, 2);
	//
	// icons[i] = new ImageView(this);
	// icons[i].setImageResource(R.drawable.minesweeper_icon);
	// icons[i].setLayoutParams(wrap_match);
	//
	// entry_layout[i].addView(icons[i]);
	//
	// text_layout[i] = new LinearLayout(this);
	// text_layout[i].setOrientation(LinearLayout.VERTICAL);
	// text_layout[i].setLayoutParams(wrap_match);
	//
	// games[i] = new TextView(this);
	// games[i].setText("Spiel " + i + "                       ");
	// games[i].setTextSize(18);
	// games[i].setTypeface(Typeface.DEFAULT_BOLD);
	// games[i].setLayoutParams(match_wrap);
	// text_layout[i].addView(games[i]);
	//
	// highscores[i] = new TextView(this);
	// highscores[i].setText("Highscore " + i);
	// highscores[i].setLayoutParams(match_wrap);
	// text_layout[i].addView(highscores[i]);
	//
	// entry_layout[i].addView(text_layout[i]);
	//
	// buttons[i] = new Button(this);
	// buttons[i].setText("+");
	// buttons[i].setId(i); // ID zum Auslesen im Listener
	// buttons[i].setOnClickListener(buttonlistener);
	// buttons[i].setLayoutParams(wrap);
	// entry_layout[i].addView(buttons[i]);
	//
	// layout.addView(entry_layout[i]);
	// }
	//
	// this.addContentView(layout, match);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}


