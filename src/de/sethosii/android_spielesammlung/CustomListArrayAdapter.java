package de.sethosii.android_spielesammlung;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.sethosii.android_spielesammlung.persistence.MinesPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.PersistenceHandler;
import de.sethosii.android_spielesammlung.persistence.SpacesliderPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.SudokuPersistentGameData;

/**
 * defines the content for each list row with help of "listviewitem_main" layout
 */
public class CustomListArrayAdapter extends ArrayAdapter<String> {
	/**
	 * tag for logging
	 */
	private static final String Tag = "CustomListArrayAdapter";
	/**
	 * the activity with should be custom
	 */
	private Activity activity;
	/**
	 * to get the layout
	 */
	private static LayoutInflater inflater = null;
	/**
	 * value parameters
	 */
	private ArrayList<String> names;

	/**
	 * Holder for each row View
	 */
	static class ViewHolder {
		public TextView tt;
		public TextView bt;
		public ImageView image;
	}

	/**
	 * Constructor with the Activity and the List of String Arrays
	 * 
	 * @param Activity a the activity to use
	 * @param ArrayList<String> values the given value names
	 */
	public CustomListArrayAdapter(Activity a, ArrayList<String> values) {
		super(a, R.layout.listviewitem_main, values);
		this.activity = a;
		this.names = values;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * defines the View Content for each Row with help of positions
	 * 
	 * @param int position current row position
	 * @param View convertView the default View
	 * @param ViewGroup parent all List View rows who exist at this time
	 * @return View vi the layout and content for the ListView row at current position
	 */
	//
	public View getView(int position, View convertView, ViewGroup parent) {
		//define the default View
		View vi = convertView;
		//holder for each Row View
		ViewHolder holder;

		//define or set the current holder and view layout
		if (convertView == null) {
			Log.w(Tag, "ist null");
			//Set view with LayoutInflater
			vi = inflater.inflate(R.layout.listviewitem_main, null);
			holder = new ViewHolder();
			holder.tt = (TextView) vi.findViewById(R.id.top_Line);
			holder.bt = (TextView) vi.findViewById(R.id.bottom_Line);
			holder.image = (ImageView) vi.findViewById(R.id.game_icon);

			//Set view with holder
			vi.setTag(holder);
		} else
			//Set holder with "saved" LayoutInflater 
			holder = (ViewHolder) vi.getTag();

		Log.w(Tag, "startrow");

		
		if (names.size() <= 0) {
			holder.tt.setText(R.string.Top_Text);

		} else {
			//get the current string for the row with help of position
			String s = names.get(position);
			Log.w(Tag, s + position);
			//set content for the mines row
			if (s.startsWith("Mines")) {
				Log.w(Tag, "mine");
				holder.tt.setText(R.string.mines);
				// load and set mines highscore
				MinesPersistentGameData mpgd = PersistenceHandler
						.getMinesPersistentGameData(activity);
				if (mpgd != null) {
					// time < 10 s add 0 at beginning
					String seconds;
					long tmp = (mpgd.scoring[0].score / 1000) % 60;
					if (tmp < 10) {
						seconds = "0" + Long.toString(tmp);
					} else {
						seconds = Long.toString(tmp);
					}
					String minutes = Long
							.toString((mpgd.scoring[0].score / (1000 * 60)) % 60);
					holder.bt.setText("Highscore: " + minutes + ":"
							+ seconds);
				} else {
					holder.bt.setText("Highscore: ");
				}
				holder.image.setImageResource(R.drawable.minesweeper_icon);
			} else {
				//set content for the sudoku row
				if (s.startsWith("Sudoku")) {
					Log.w(Tag, "sudo");
					holder.tt.setText(R.string.sudoku);
					// loads and set sudoku highscore
					SudokuPersistentGameData spgd = PersistenceHandler
							.getSudokuPersistentGameData(activity);
					if (spgd != null) {
						// time < 10 s add 0 at beginning
						String seconds;
						long tmp = (spgd.scoring[0].score / 1000) % 60;
						if (tmp < 10) {
							seconds = "0" + Long.toString(tmp);
						} else {
							seconds = Long.toString(tmp);
						}
						String minutes = Long
								.toString((spgd.scoring[0].score / (1000 * 60)) % 60);
						holder.bt.setText("Highscore: " + minutes + ":"
								+ seconds);
					} else {
						holder.bt.setText("Highscore: ");
					}
					holder.image.setImageResource(R.drawable.sudoku_icon);
				} else {
					//set content for the spaceslider row
					Log.w(Tag, "space");
					holder.tt.setText(R.string.spaceslider);
					// loads and set spaceslider highscore
					SpacesliderPersistentGameData sspgd = PersistenceHandler
							.getSpacesliderPersistentGameData(activity);
					if (sspgd != null) {
						// time < 10 s add 0 at beginning
						String seconds;
						long tmp = (sspgd.scoring[0].score / 1000) % 60;
						if (tmp < 10) {
							seconds = "0" + Long.toString(tmp);
						} else {
							seconds = Long.toString(tmp);
						}
						String minutes = Long.toString((sspgd.scoring[0].score / (1000 * 60)) % 60);
						holder.bt.setText("Highscore: " + minutes + ":" + seconds);
					} else {
						holder.bt.setText("Highscore: ");
					}
					holder.image.setImageResource(R.drawable.spaceslider_icon);
				}
			}
		}

		return vi;

	}
}
