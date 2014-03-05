package de.sethosii.android_spielesammlung;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import de.sethosii.android_spielesammlung.persistence.PersistenceHandler;
import de.sethosii.android_spielesammlung.persistence.SudokuPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.SudokuPersistentSnapshot;

public class SudokuActivity extends Activity {

	// the difficulty of the game
	int diff;
	// all fields
	Integer[][] fields;
	// all inputs that were made
	ArrayList<Integer> inputs;
	// the focused field
	Button focused;
	// the last focused field
	Button focused_old;
	// the generated sudoku
	Integer[][] sudoku;
	// the button showing the win message
	Button win;
	// the menu
	LinearLayout optionsmenu;
	// the chronometer
	Chronometer chron;
	// the confirmation on startup
	LinearLayout confirm;
	// mediaplayer
	MediaPlayer player;
	// save for pausing chronometer
	long stop;
	// all disabeld fields
	ArrayList<Integer> disabled;
	// determine if startup
	boolean startup;
	// determine if menu is shown
	boolean menushown;
	// tag for loggings
	private final static String Tag = "SudokuActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku);

		// initialize every needed objects
		win = (Button) findViewById(R.id.winbutton);
		optionsmenu = (LinearLayout) findViewById(R.id.menu);
		chron = (Chronometer) findViewById(R.id.chronometer);
		confirm = (LinearLayout) findViewById(R.id.confirm);

		// hide win message and optionsmenu
		win.setVisibility(View.GONE);
		optionsmenu.setVisibility(View.GONE);

		// if difficulty is higher than 50 an endless loop is generated
		diff = 45;

		// initialize needed lists and arrays
		inputs = new ArrayList<Integer>();
		disabled = new ArrayList<Integer>();
		fields = new Integer[9][9];

		// set flags on creation
		startup = true;
		menushown = false;

		// check if there is a state to load
		if (PersistenceHandler.getSudokuPersistentSnapshot(this, 0) == null) {
			((Button) findViewById(R.id.confirmload)).setEnabled(false);
		}

		// order all fields
		getallFields();

		// disable all fields on startup
		disable(startup);
	}

	// start music when app is maximized again
	@Override
	protected void onResume() {
		if (getString(R.string.soundoff).equals(
				((Button) findViewById(R.id.music)).getText())) {
			playMusic();
		}

		super.onResume();

	}

	// stop music when app is minimized, show menu (except on startup)
	@Override
	protected void onPause() {
		player.stop();

		if (!startup) {
			ImageButton menubutton = (ImageButton) findViewById(R.id.menubutton);
			if (optionsmenu.getVisibility() == View.GONE) {
				showMenu(menubutton);
			}
		}
		super.onPause();
	}

	/** stop chronometer and music on quit */
	@Override
	public void finish() {
		player.stop();
		chron.stop();
		super.finish();
	}

	/**
	 * disables all buttons
	 * 
	 * @param startup
	 *            the variable if sudoku just has been started
	 */
	public void disable(boolean startup) {
		ArrayList<Integer> allids = new ArrayList<Integer>();

		// add all fields
		for (int i = 0; i < 81; i++) {
			int id = R.id.field11 + i;
			allids.add(id);

			// checks if method is launched at startup
			if (!startup) {
				// add all disabled fields
				Button b = (Button) findViewById(id);
				// saves which fields were disabled for enabling
				if (!b.isEnabled()) {
					disabled.add(id);
				}
			}
		}

		// add all inputs
		for (int j = 0; j < 10; j++) {
			allids.add(R.id.input1 + j);
		}

		// add all iconbuttons
		// on startup all buttons are disabled
		// if menu is shown menubutton must be enabled
		int max = 0;
		if (startup) {
			max = 3;
		} else if (!startup) {
			max = 2;
		}
		for (int k = 0; k < max; k++) {
			allids.add(R.id.newbutton + k);
		}

		// disable all buttons
		int change = 0;
		for (int id : allids) {
			change++;
			// switch between Buttons and ImageButtons
			if (change <= 91) {
				Button b = (Button) findViewById(id);
				b.setEnabled(false);
			}
			if (change > 91) {
				ImageButton b = (ImageButton) findViewById(id);
				b.setEnabled(false);
			}
		}
	}

	/**
	 * enables all buttons
	 * 
	 * @param startup
	 *            the variable if sudoku just has been started
	 */
	public void enable(boolean startup) {
		ArrayList<Integer> allids = new ArrayList<Integer>();

		// add all fields
		for (int i = 0; i < 81; i++) {
			int id_now = R.id.field11 + i;
			boolean check = true;

			// checks if function is launched at startup
			// if not function doesn�t enable set fields
			if (startup) {
				allids.add(id_now);
			} else if (!startup) {
				for (int id_disabled : disabled) {
					if (id_now == id_disabled) {
						check = false;
					}
				}
				if (check) {
					allids.add(id_now);
				}
			}
		}
		disabled.clear();

		// add all inputs
		for (int j = 0; j < 10; j++) {
			allids.add(R.id.input1 + j);
		}

		// add all iconbuttons
		for (int k = 0; k < 3; k++) {
			allids.add(R.id.newbutton + k);
		}

		// enable all buttons
		int change = 0;
		for (int id : allids) {
			change++;
			// switch between Buttons and ImageButtons
			if (change <= (allids.size() - 3)) {
				Button b = (Button) findViewById(id);
				b.setEnabled(true);
			}
			if (change > (allids.size() - 3)) {
				ImageButton b = (ImageButton) findViewById(id);
				b.setEnabled(true);
			}

		}
	}

	/**
	 * load saved state
	 * 
	 * @param v
	 *            the view that called method (load buttons)
	 */
	public void load(View v) {

		// load saved Object
		SudokuPersistentSnapshot sps = PersistenceHandler
				.getSudokuPersistentSnapshot(this, 0);
		if (sps != null) {
			startup = false;

			// delete disabled fields
			if (disabled != null) {
				disabled.clear();
			}

			// load disabled fields
			disabled.addAll(sps.disabled);

			// delete previous inputs
			if (inputs != null) {
				inputs.clear();
			}

			// load manual inputs
			inputs.addAll(sps.inputs);

			// load all fields
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					Button b = (Button) findViewById(fields[i][j]);
					b.setText(sps.fieldState[i][j]);

				}
			}

			// load chronometer
			stop = sps.stop;
			chron.setBase(sps.base);
			chron.setText(sps.chrontext);

			// load from startup
			if (confirm.getVisibility() == View.VISIBLE) {
				confirm.setVisibility(View.GONE);
				enable(startup);
				resumeChronometer();
			}

			// delete previous focus
			if (focused != null) {
				focused.setBackgroundColor(Color.WHITE);
				focused = null;
			}

		}
	}

	/**
	 * saves current state
	 * 
	 * @param v
	 *            view that called method (save button)
	 */
	public void save(View v) {

		try {
			SudokuPersistentSnapshot sps = new SudokuPersistentSnapshot();

			// saves chronometer
			sps.stop = stop;
			sps.base = chron.getBase();
			sps.chrontext = chron.getText().toString();

			// save fields, disabled fields and manual inputs
			sps.fieldState = new String[9][9];
			sps.disabled = new ArrayList<Integer>();
			sps.inputs = new ArrayList<Integer>();

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					Button b = (Button) findViewById(fields[i][j]);
					sps.fieldState[i][j] = b.getText().toString();
				}
			}

			sps.disabled.addAll(disabled);
			sps.inputs.addAll(inputs);

			PersistenceHandler.setSudokuPersistentSnapshot(this, 0, sps);

			// enable loading
			if (menushown) {
				((Button) findViewById(R.id.loadgame)).setEnabled(true);
			}

			// show message
			Toast.makeText(this, R.string.succsave, Toast.LENGTH_LONG).show();
		} catch (RuntimeException e) {

			// show mesage
			Toast.makeText(this, R.string.errsave, Toast.LENGTH_LONG).show();
		}

	}

	/** plays music */
	public void playMusic() {
		AssetFileDescriptor afd;
		try {
			// Read the music file from the asset folder
			afd = getAssets().openFd("music.mid");
			// Creation of new media player;
			player = new MediaPlayer();
			// Set the player music source.
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			// Set the looping and play the music.
			player.setLooping(true);
			player.prepare();
			player.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * play music or stop music on button touch
	 * 
	 * @param v
	 *            view that called method (music on off button)
	 */
	public void sound(View v) {
		Button music = (Button) findViewById(R.id.music);

		if ((player != null) && player.isPlaying()) {
			music.setText(R.string.soundon);
			player.stop();
		}

		else if ((player != null) && !player.isPlaying()) {
			music.setText(R.string.soundoff);
			playMusic();

		}
	}

	/** orders the fields to arranged array */
	public void getallFields() {
		int counter = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				fields[i][j] = R.id.field11 + counter;
				counter++;
			}
		}
		for (int k = 0; k < 9; k++) {
			for (int l = 3; l < 6; l++) {
				fields[k][l] = R.id.field11 + counter;
				counter++;
			}
		}
		for (int m = 0; m < 9; m++) {
			for (int n = 6; n < 9; n++) {
				fields[m][n] = R.id.field11 + counter;
				counter++;
			}
		}
	}

	/**
	 * set focused field on selected number
	 * 
	 * @param v
	 *            view that called method (selected number)
	 */
	public void setNumbers(View v) {

		if (focused != null) {
			// adds field to clicked fields
			inputs.add(focused.getId());

			// sets the focused field on clicked number
			switch (v.getId()) {
			case R.id.input1:
				focused.setText(R.string.one);
				break;
			case R.id.input2:
				focused.setText(R.string.two);
				break;
			case R.id.input3:
				focused.setText(R.string.three);
				break;
			case R.id.input4:
				focused.setText(R.string.four);
				break;
			case R.id.input5:
				focused.setText(R.string.five);
				break;
			case R.id.input6:
				focused.setText(R.string.six);
				break;
			case R.id.input7:
				focused.setText(R.string.seven);
				break;
			case R.id.input8:
				focused.setText(R.string.eight);
				break;
			case R.id.input9:
				focused.setText(R.string.nine);
				break;
			case R.id.inputdel:
				focused.setText("");
				break;
			default:
				Log.e(Tag, "setnumbers - switch Failed!!!");
				break;

			}
		}
		checkwin();
	}

	/** checks if player won */
	public void checkwin() {
		if (fillcheck() == true) {
			if (inputcheck() == true) {

				// stop chronometer
				chron.stop();

				// set wining message
				win.setText(R.string.win);
				win.setVisibility(View.VISIBLE);

				// set highscore

				// current highscore in ms
				long highscore = SystemClock.elapsedRealtime()
						- chron.getBase();

				SudokuPersistentGameData spgd = PersistenceHandler
						.getSudokuPersistentGameData(this);

				// checks if current score is higher than saved highscore
				// if so overwrite
				if (spgd != null) {
					if (spgd.scoring.length == 1) {
						if (highscore < spgd.scoring[0].score) {
							spgd.scoring[0].score = highscore;
							PersistenceHandler.setSudokuPersistentGameData(
									this, spgd);
						}
					}
					// if there is no score, save current score
					else if (spgd.scoring == null) {
						spgd = new SudokuPersistentGameData();
						spgd.addHighScore(highscore);
						PersistenceHandler.setSudokuPersistentGameData(this,
								spgd);
					}
				}
				// if there is no object, create object and save score
				else {
					spgd = new SudokuPersistentGameData();
					spgd.addHighScore(highscore);
					PersistenceHandler.setSudokuPersistentGameData(this, spgd);

				}
			}
		}
	}

	/**
	 * gets the selected field
	 * 
	 * @param v
	 *            view that called method (field)
	 */
	public void getField(View v) {
		focused_old = focused;
		focused = (Button) findViewById(v.getId());
		if (focused != focused_old) {
			focused.setBackgroundColor(Color.GRAY);
			if (focused_old != null) {
				focused_old.setBackgroundColor(Color.WHITE);
			}
		}

	}

	/**
	 * deletes all inputs
	 * 
	 * @param v
	 *            view that called method (delete button)
	 */
	public void deleteInput(View v) {
		for (int id : inputs) {
			Button b = (Button) findViewById(id);
			b.setText("");
		}
	}

	/**
	 * generates Sudoku
	 * 
	 * @param v
	 *            view that called method (new game buttons)
	 */
	public void generateSudoku(View v) {
		int pointer = 8;
		int[] run = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] random = new int[9];
		int shift = 0;
		sudoku = new Integer[9][9];
		int hswitch = 0;
		int vswitch = 0;

		// enable all buttons
		enable(startup);

		// hide win message
		win.setVisibility(View.GONE);

		// count time
		startChronometer();

		// hide confirm
		confirm.setVisibility(View.GONE);

		// delete previous focus
		if (focused != null) {
			focused.setBackgroundColor(Color.WHITE);
			focused = null;
		}

		// delete previous inputs
		if (inputs != null) {
			inputs.clear();
		}
		// picks randomly numbers of run without putting back into random
		for (int h = 0; h < 9; h++) {
			int index = (int) (Math.random() * (pointer + 1));
			random[h] = run[index];
			run[index] = run[pointer];
			pointer--;
		}

		// fills and shifts the fieldarray
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudoku[i][j] = random[shift];
				if (shift == 8) {
					shift = 0;
				} else {
					shift++;
				}
			}
			switch (i) {
			case (0):
				shift = 6;
				break;
			case (1):
				shift = 3;
				break;
			case (2):
				shift = 8;
				break;
			case (3):
				shift = 5;
				break;
			case (4):
				shift = 2;
				break;
			case (5):
				shift = 7;
				break;
			case (6):
				shift = 4;
				break;
			case (7):
				shift = 1;
				break;
			default:
				Log.e(Tag, "generatesudoku - switch failed!!!");
				break;

			}
		}

		// shuffles the array

		// shuffles horizontal lines
		for (int k = 0; k < 3; k++) {
			int y1 = (int) (Math.random() * 3) + hswitch;
			int y2 = (int) (Math.random() * 3) + hswitch;

			for (int x = 0; x < 9; x++) {
				int n1 = sudoku[y1][x];
				int n2 = sudoku[y2][x];
				sudoku[y1][x] = n2;
				sudoku[y2][x] = n1;

			}
			hswitch = hswitch + 3;
		}

		// shuffles vertical lines
		for (int l = 0; l < 3; l++) {
			int x1 = (int) (Math.random() * 3) + vswitch;
			int x2 = (int) (Math.random() * 3) + vswitch;

			for (int y = 0; y < 9; y++) {
				int n1 = sudoku[y][x1];
				int n2 = sudoku[y][x2];
				sudoku[y][x1] = n2;
				sudoku[y][x2] = n1;

			}
			vswitch = vswitch + 3;
		}

		// sudoku with gaps to be completed
		Integer[][] gapsudoku = new Integer[9][9];

		for (int m = 0; m < 9; m++) {
			for (int n = 0; n < 9; n++) {
				gapsudoku[m][n] = sudoku[m][n];
			}
		}

		// deletes fields that don�t change the uniqueness
		for (int o = 0; o < diff; o++) {
			int rx = (int) (Math.random() * 9);
			int ry = (int) (Math.random() * 9);
			int save = gapsudoku[rx][ry];

			// if field is empty skip back
			if (gapsudoku[rx][ry] == 0) {
				o--;
			}

			// if field is not empty check if random field can be removed
			else if (gapsudoku[rx][ry] != 0) {
				gapsudoku[rx][ry] = 0;
				if (checkSolutions(gapsudoku) == false) {
					gapsudoku[rx][ry] = save;
					o--;
				}
			}

		}

		// sets the calculated sudoku with gaps on buttons
		for (int p = 0; p < 9; p++) {
			for (int q = 0; q < 9; q++) {
				Button b = (Button) findViewById(fields[p][q]);
				if (gapsudoku[p][q] == 0) {
					b.setText("");
					b.setEnabled(true);
				}

				if (gapsudoku[p][q] != 0) {
					b.setText(gapsudoku[p][q].toString());
					b.setEnabled(false);
				}
			}
		}

		// set startup false
		if (startup) {
			startup = false;
		}

		// if new game is selected through menu disable everything
		if (menushown) {
			disable(startup);
			stopChronometer();
		}

	}

	/**
	 * checks if all fields were filled by player
	 * 
	 * @return returns if all fields are filled
	 */
	public boolean fillcheck() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Button b = (Button) findViewById(fields[i][j]);
				if (b.getText() == "") {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * checks if all fields were filled correctly by player
	 * 
	 * @return returns if all fields were filled correctly by player
	 */
	public boolean inputcheck() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Button b = (Button) findViewById(fields[i][j]);
				int number = Integer.parseInt(b.getText().toString());
				if (sudoku[i][j] != number) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * shows and hides menu
	 * 
	 * @param v
	 *            view that called method (menu button)
	 */
	public void showMenu(View v) {

		ImageButton menu = (ImageButton) findViewById(R.id.menubutton);

		// if menu is gone, show menu, pause chronometer,
		// disable background, color menu button
		if (optionsmenu.getVisibility() == View.GONE) {
			optionsmenu.setVisibility(View.VISIBLE);
			stopChronometer();
			disable(startup);
			menu.setColorFilter(Color.RED);
			menushown = true;

			// disable load button if there is nothing to load
			if (PersistenceHandler.getSudokuPersistentSnapshot(this, 0) == null) {
				((Button) findViewById(R.id.loadgame)).setEnabled(false);
			}

			else {
				((Button) findViewById(R.id.loadgame)).setEnabled(true);
			}
		}

		// if menu is visible, hide menu,
		// resume chronometer, enable background, color menu button
		else if (optionsmenu.getVisibility() == View.VISIBLE) {
			optionsmenu.setVisibility(View.GONE);
			resumeChronometer();
			enable(startup);
			menu.setColorFilter(null);
			menushown = false;
		}
	}

	/** start chronometer and set base */
	public void startChronometer() {
		chron.setBase(SystemClock.elapsedRealtime());
		chron.start();

	}

	/** stop chronometer and save stop */
	public void stopChronometer() {
		stop = SystemClock.elapsedRealtime();
		chron.stop();
	}

	/** resume chronometer and set base to continue */
	public void resumeChronometer() {
		chron.setBase(chron.getBase() + SystemClock.elapsedRealtime() - stop);
		chron.start();
	}

	/**
	 * quits sudoku
	 * 
	 * @param v
	 *            view that called method (quit button)
	 */
	public void quit(View v) {
		chron.stop();
		finish();
	}

	/**
	 * checks if sudoku with gap has a distinct solution
	 * 
	 * @param gapsudoku
	 *            sudoku with gaps to be solved
	 * @return returns if sudoku has a distinct solution
	 */
	public boolean checkSolutions(Integer[][] gapsudoku) {
		Integer[][] copy = new Integer[9][9];
		int[] run = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		// copy gapsudoko, so it can be filled
		for (int g = 0; g < 9; g++) {
			for (int h = 0; h < 9; h++) {
				copy[g][h] = gapsudoku[g][h];
			}
		}

		// is solution completely filled
		boolean filled = false;

		// sets all fields with just one solution
		while (!filled) {
			filled = true;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (copy[i][j] == 0) {
						int solutions = 0;
						int save = 0;
						for (int k = 0; k < 9; k++) {
							if (validate(run[k], i, j, copy) == true) {
								solutions++;
								save = run[k];
							}
						}

						// if a distinct solution for a field was found
						// set solution, repeat everything
						if (solutions == 1) {
							copy[i][j] = save;
							filled = false;
						}
					}
				}
			}
		}

		// checks if there are still fields missing
		for (int m = 0; m < 9; m++) {
			for (int n = 0; n < 9; n++) {
				if (copy[m][n] == 0) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * checks if a number is valid at the position (y,x)
	 * 
	 * @param number
	 *            to be checked
	 * @param x
	 *            position x of number
	 * @param y
	 *            position y of number
	 * @param copy
	 *            the sudoku belonging to the number
	 * @return returns if number is valid at the position
	 */
	public boolean validate(int number, int y, int x, Integer[][] copy) {
		boolean checkx = true;
		boolean checky = true;
		boolean checkxy = true;
		int xu = 0;
		int xd = 0;
		int yu = 0;
		int yd = 0;

		// checks horizontal
		for (int k = 0; k < 9; k++) {
			if (number == copy[y][k]) {
				checkx = false;
			}
		}

		// checks vertical
		for (int l = 0; l < 9; l++) {
			if (number == copy[l][x]) {
				checky = false;
			}
		}

		// gets the box
		if (y >= 0 && y <= 2) {
			yu = 2;
			yd = 0;
		}
		if (y >= 3 && y <= 5) {
			yu = 5;
			yd = 3;
		}
		if (y >= 6 && y <= 8) {
			yu = 8;
			yd = 6;
		}
		if (x >= 0 && x <= 2) {
			xu = 2;
			xd = 0;
		}
		if (x >= 3 && x <= 5) {
			xu = 5;
			xd = 3;
		}
		if (x >= 6 && x <= 8) {
			xu = 8;
			xd = 6;
		}

		// checks in box
		while (yd <= yu) {
			while (xd <= xu) {
				if (number == copy[yd][xd]) {
					checkxy = false;
				}

				xd++;
			}
			yd++;
		}
		return checkx && checky && checkxy;

	}

}
