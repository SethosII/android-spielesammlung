package de.sethosii.android_spielesammlung;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.sethosii.android_spielesammlung.persistence.MinesPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.MinesPersistentSnapshot;
import de.sethosii.android_spielesammlung.persistence.PersistenceHandler;

public class MinesActivity extends Activity {

	/** mark fields or not */
	private boolean mark;
	/** game states */
	private EnumGameState end;
	/** shortcuts for size in vertical direction */
	private int dimensionX;
	/** shortcuts for size in horizontal direction */
	private int dimensionY;
	/** number of placed mines */
	private int maxMineCount;
	/** number of mines remaining */
	private int mineCount;
	/** solution */
	private String[][] solution;
	/** user view */
	private Button[][] view;
	/** mark */
	private ImageButton changeMode;
	/** menu */
	private ImageButton menuButton;
	/** display mine count */
	private TextView tvMineCount;
	/** display time */
	private Chronometer chronometer;
	/** menu */
	private LinearLayout menu;
	/** confirm */
	private LinearLayout confirm;
	/** game end button */
	private Button endButton;
	/** time elapsed */
	private long timeElapsed;
	/** is chronometer stopped */
	private boolean chronometerStopped;
	/** mediaplayer */
	private MediaPlayer musicPlayer;

	/**
	 * initialize all fields
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mines);

		// set values for start
		mark = false;
		end = EnumGameState.NOT_STARTED;
		dimensionX = 9;
		dimensionY = 9;
		maxMineCount = 10;
		mineCount = maxMineCount;
		timeElapsed = 0;
		chronometerStopped = false;

		// get elements and initialize them
		solution = new String[dimensionX][dimensionY];
		view = new Button[dimensionX][dimensionY];
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				view[i][j] = ((Button) findViewById(R.id.b11 + i * dimensionY
						+ j));
				view[i][j].setText("");
				view[i][j].setBackgroundColor(Color.parseColor("#DDDDDD"));
			}
		}
		changeMode = (ImageButton) findViewById(R.id.changeMode);
		menuButton = (ImageButton) findViewById(R.id.showMenu);
		tvMineCount = (TextView) findViewById(R.id.mineCount);
		tvMineCount.setText(getString(R.string.mines_remaining) + ": "
				+ mineCount);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		menu = (LinearLayout) findViewById(R.id.menu);
		menu.setVisibility(View.GONE);
		endButton = (Button) findViewById(R.id.endbutton);
		confirm = (LinearLayout) findViewById(R.id.confirmDialog);
		endButton.setVisibility(View.GONE);
		enableView(false);
		if (PersistenceHandler.getMinesPersistentSnapshot(this, 0) == null) {
			((Button) findViewById(R.id.confirmload)).setEnabled(false);
		}
	}

	/**
	 * stop chronometer and music on quit
	 */
	@Override
	public void finish() {
		musicPlayer.stop();
		chronometer.stop();
		super.finish();
	}

	/**
	 * start music and chronometer when app is maximized
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (getString(R.string.soundoff).equals(
				((Button) findViewById(R.id.music)).getText())) {
			playMusic();
		}
		resumeChronometer();
	}

	/**
	 * stop music and chronometer when app is minimized
	 */
	@Override
	protected void onPause() {
		stopChronometer();
		musicPlayer.stop();
		super.onPause();
	}

	/**
	 * generate new game
	 * 
	 * @param x
	 *            x position of first touched field
	 * @param y
	 *            y position of first touched field
	 */
	private void generateGame(int x, int y) {
		initialize();
		placeMines(x, y);
		setNumbers();
		startChronometer();
	}

	/**
	 * reset all values for a new game
	 */
	private void initialize() {
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				solution[i][j] = "";
				view[i][j].setText("");
				view[i][j].setBackgroundColor(Color.parseColor("#DDDDDD"));
				view[i][j].setTextColor(Color.BLACK);
			}
		}
		enableView(true);
		end = EnumGameState.NOT_FINISHED;
		mineCount = 10;
		tvMineCount.setText(getString(R.string.mines_remaining) + ": "
				+ mineCount);
		menu.setVisibility(View.GONE);
		menuButton.setColorFilter(null);
		confirm.setVisibility(View.GONE);
		endButton.setVisibility(View.GONE);
		enableView(true);

		// reset time
		stopChronometer();
		chronometer.setBase(SystemClock.elapsedRealtime());
		timeElapsed = 0;
		chronometerStopped = true;
	}

	/**
	 * place the mines after first field is touched
	 * 
	 * @param x
	 *            x position of first touched field
	 * @param y
	 *            y position of first touched field
	 */
	private void placeMines(int x, int y) {
		int placed = 0;
		while (placed < mineCount) {
			int posX = (int) (Math.random() * dimensionX);
			int posY = (int) (Math.random() * dimensionY);
			// first field shouldn't contain a mine
			if ((posX != x || posY != y) && !solution[posX][posY].equals("*")) {
				solution[posX][posY] = "*";
				placed++;
			}
		}
	}

	/**
	 * calculate the numbers on the solution
	 */
	private void setNumbers() {
		int number = 0;
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				if (!solution[i][j].equals("*")) {
					number = 0;
					if (i > 0) {
						if (solution[i - 1][j].equals("*")) {
							number++;
						}
						if (j < dimensionY - 1) {
							if (solution[i - 1][j + 1].equals("*")) {
								number++;
							}
						}
						if (j > 0) {
							if (solution[i - 1][j - 1].equals("*")) {
								number++;
							}
						}
					}
					if (j > 0) {
						if (solution[i][j - 1].equals("*")) {
							number++;
						}
						if (i < dimensionX - 1) {
							if (solution[i + 1][j - 1].equals("*")) {
								number++;
							}
						}
					}
					if (i < dimensionX - 1) {
						if (solution[i + 1][j].equals("*")) {
							number++;
						}
						if (j < dimensionY - 1) {
							if (solution[i + 1][j + 1].equals("*")) {
								number++;
							}
						}
					}
					if (j < dimensionY - 1) {
						if (solution[i][j + 1].equals("*")) {
							number++;
						}
					}
					if (number != 0) {
						solution[i][j] = Integer.toString(number);
					}
				}
			}
		}
	}

	/**
	 * create input for touchField method
	 * 
	 * @param v
	 *            touched view
	 */
	public void getField(View v) {
		String name = getResources().getResourceEntryName(v.getId());
		int x = Integer.parseInt(name.substring(1, 2)) - 1;
		int y = Integer.parseInt(name.substring(2, 3)) - 1;
		// create new game on first touch
		if (end.equals(EnumGameState.NOT_STARTED) && !mark) {
			generateGame(x, y);
		}
		touchField(x, y);
	}

	/**
	 * method called when field was touched
	 * 
	 * @param posX
	 *            x position of touched field
	 * @param posY
	 *            y position of touched field
	 */
	private void touchField(int posX, int posY) {
		Button touched = view[posX][posY];
		// set flag or not
		if (mark) {
			// empty: set flag
			if (touched.getText().equals("")) {
				touched.setText("X");
				touched.setTextColor(Color.RED);
				mineCount--;
				tvMineCount.setText(getString(R.string.mines_remaining) + ": "
						+ mineCount);
			} else
			// marked: remove flag
			if (touched.getText().equals("X")) {
				touched.setText("");
				touched.setTextColor(Color.BLACK);
				mineCount++;
				tvMineCount.setText(getString(R.string.mines_remaining) + ": "
						+ mineCount);
			}
		} else {
			// checks if field is covered
			if (touched.getText().equals("")) {
				// set field of solution at the position
				touched.setText(solution[posX][posY]);
				touched.setBackgroundColor(Color.WHITE);
				// mine: game lost
				if (solution[posX][posY].equals("*")) {
					touched.setText("*");
					touched.setBackgroundColor(Color.WHITE);
					end = EnumGameState.LOSE;
				} else
				// empty field: reveal surrounding
				if (solution[posX][posY].equals("")) {
					revealSurrounding(posX, posY);
				}
			}
		}
		checkEnd();
	}

	/**
	 * called when empty field is touched reveal surrounding
	 * 
	 * @param posX
	 *            x position of empty field
	 * @param posY
	 *            y position of empty field
	 */
	private void revealSurrounding(int posX, int posY) {
		// mark field as empty
		view[posX][posY].setText(" ");
		view[posX][posY].setTextColor(Color.BLACK);
		view[posX][posY].setBackgroundColor(Color.WHITE);
		// check surrounding, marked: do nothing
		// empty: mark, else: set number on field
		if (posX > 0) {
			if (!solution[posX - 1][posY].equals("!")) {
				if (solution[posX - 1][posY].equals("")) {
					solution[posX - 1][posY] = " ";
					revealSurrounding(posX - 1, posY);
				} else {
					view[posX - 1][posY].setText(solution[posX - 1][posY]);
					view[posX - 1][posY].setTextColor(Color.BLACK);
					view[posX - 1][posY].setBackgroundColor(Color.WHITE);
				}
			}
			if (posY < dimensionY - 1) {
				if (!solution[posX - 1][posY + 1].equals("!")) {
					if (solution[posX - 1][posY + 1].equals("")) {
						solution[posX - 1][posY + 1] = " ";
						revealSurrounding(posX - 1, posY + 1);
					} else {
						view[posX - 1][posY + 1]
								.setText(solution[posX - 1][posY + 1]);

						view[posX - 1][posY + 1].setTextColor(Color.BLACK);
						view[posX - 1][posY + 1]
								.setBackgroundColor(Color.WHITE);
					}
				}
			}
			if (posY > 0) {
				if (solution[posX - 1][posY - 1].equals("")) {
					solution[posX - 1][posY - 1] = " ";
					revealSurrounding(posX - 1, posY - 1);
				} else {
					view[posX - 1][posY - 1]
							.setText(solution[posX - 1][posY - 1]);

					view[posX - 1][posY - 1].setTextColor(Color.BLACK);
					view[posX - 1][posY - 1].setBackgroundColor(Color.WHITE);
				}
			}
		}
		if (posY > 0) {
			if (solution[posX][posY - 1].equals("")) {
				solution[posX][posY - 1] = " ";
				revealSurrounding(posX, posY - 1);
			} else {
				view[posX][posY - 1].setText(solution[posX][posY - 1]);
				view[posX][posY - 1].setTextColor(Color.BLACK);
				view[posX][posY - 1].setBackgroundColor(Color.WHITE);
			}
			if (posX < dimensionX - 1) {
				if (solution[posX + 1][posY - 1].equals("")) {
					solution[posX + 1][posY - 1] = " ";
					revealSurrounding(posX + 1, posY - 1);
				} else {
					view[posX + 1][posY - 1]
							.setText(solution[posX + 1][posY - 1]);
					view[posX + 1][posY - 1].setTextColor(Color.BLACK);
					view[posX + 1][posY - 1].setBackgroundColor(Color.WHITE);
				}
			}
		}
		if (posX < dimensionX - 1) {
			if (solution[posX + 1][posY].equals("")) {
				solution[posX + 1][posY] = " ";
				revealSurrounding(posX + 1, posY);
			} else {
				view[posX + 1][posY].setText(solution[posX + 1][posY]);
				view[posX + 1][posY].setTextColor(Color.BLACK);
				view[posX + 1][posY].setBackgroundColor(Color.WHITE);
			}
			if (posY < dimensionY - 1) {
				if (solution[posX + 1][posY + 1].equals("")) {
					solution[posX + 1][posY + 1] = " ";
					revealSurrounding(posX + 1, posY + 1);
				} else {
					view[posX + 1][posY + 1]
							.setText(solution[posX + 1][posY + 1]);
					view[posX + 1][posY + 1].setTextColor(Color.BLACK);
					view[posX + 1][posY + 1].setBackgroundColor(Color.WHITE);
				}
			}
		}
		if (posY < dimensionY - 1) {
			if (solution[posX][posY + 1].equals("")) {
				solution[posX][posY + 1] = " ";
				revealSurrounding(posX, posY + 1);
			} else {
				view[posX][posY + 1].setText(solution[posX][posY + 1]);
				view[posX][posY + 1].setTextColor(Color.BLACK);
				view[posX][posY + 1].setBackgroundColor(Color.WHITE);
			}
		}
	}

	/**
	 * check game state
	 */
	private void checkEnd() {
		if (end.equals(EnumGameState.LOSE)) {
			chronometer.stop();
			timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
			endButton.setVisibility(View.VISIBLE);
			endButton.setText(R.string.lose);
			enableView(false);
		} else {
			int remainingFields = 0;
			for (int i = 0; i < dimensionX; i++) {
				for (int j = 0; j < dimensionY; j++) {
					if (view[i][j].getText().equals("")
							|| view[i][j].getText().equals("X")) {
						remainingFields++;
					}
				}
			}
			if (remainingFields <= maxMineCount) {
				end = EnumGameState.WIN;
			}
			if (end.equals(EnumGameState.WIN)) {
				chronometer.stop();
				// score
				timeElapsed = SystemClock.elapsedRealtime()
						- chronometer.getBase();
				MinesPersistentGameData mpgd = PersistenceHandler
						.getMinesPersistentGameData(this);
				// score > highscorer: save
				if (mpgd != null) {
					if (mpgd.scoring.length == 1) {
						if (timeElapsed < mpgd.scoring[0].score) {
							mpgd.scoring[0].score = timeElapsed;
							PersistenceHandler.setMinesPersistentGameData(this,
									mpgd);
						}
					}
					// no highscore: save
					else if (mpgd.scoring == null) {
						mpgd = new MinesPersistentGameData();
						mpgd.addHighScore(timeElapsed);
						PersistenceHandler.setMinesPersistentGameData(this,
								mpgd);
					}
				} else {
					mpgd = new MinesPersistentGameData();
					mpgd.addHighScore(timeElapsed);
					PersistenceHandler.setMinesPersistentGameData(this, mpgd);

				}

				endButton.setVisibility(View.VISIBLE);
				endButton.setText(R.string.win);
				enableView(false);
			}
		}
	}

	/**
	 * enable or diable all fields
	 * 
	 * @param enable
	 *            true or false
	 */
	private void enableView(boolean enable) {
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				view[i][j].setEnabled(enable);
			}
		}
	}

	/**
	 * method called when cross button is touched change mark mode
	 * 
	 * @param v
	 *            touched view
	 */
	public void changeMode(View v) {
		mark = !mark;
		if (mark) {
			changeMode.setColorFilter(Color.RED);
		} else {
			changeMode.setColorFilter(null);
		}
	}

	/**
	 * method called when new game button is touched
	 * 
	 * @param v
	 *            touched view
	 */
	public void clear(View v) {
		initialize();
		end = EnumGameState.NOT_STARTED;
	}

	/**
	 * method called when menu button is touched open/close menu
	 * 
	 * @param v
	 *            touched view
	 */
	public void menu(View v) {
		if (menu.getVisibility() == View.VISIBLE) {
			menu.setVisibility(View.GONE);
			enableView(true);
			menuButton.setColorFilter(null);
			if (end == EnumGameState.NOT_FINISHED) {
				resumeChronometer();
			}
		} else {
			confirm.setVisibility(View.GONE);
			menu.setVisibility(View.VISIBLE);
			enableView(false);
			if (PersistenceHandler.getMinesPersistentSnapshot(this, 0) == null) {
				((Button) findViewById(R.id.loadgame)).setEnabled(false);
			} else {
				((Button) findViewById(R.id.loadgame)).setEnabled(true);
			}
			menuButton.setColorFilter(Color.RED);
			if (end == EnumGameState.NOT_FINISHED) {
				stopChronometer();
			}
		}
	}

	/**
	 * start chronometer
	 */
	private void startChronometer() {
		if (chronometerStopped) {
			chronometer.start();
			chronometerStopped = false;
		}
	}

	/**
	 * stop chronometer
	 */
	private void stopChronometer() {
		if (!chronometerStopped) {
			chronometer.stop();
			timeElapsed = SystemClock.elapsedRealtime();
			chronometerStopped = true;
		}
	}

	/**
	 * resume chronometer
	 */
	private void resumeChronometer() {
		if (chronometerStopped && (menu.getVisibility() != View.VISIBLE)
				&& (end == EnumGameState.NOT_FINISHED)) {
			System.out.println("!!chronoresume!!");
			chronometer.setBase(chronometer.getBase()
					+ SystemClock.elapsedRealtime() - timeElapsed);
			startChronometer();
		}
	}

	/**
	 * method called when load button is touched restore all required fields
	 * 
	 * @param v
	 */
	public void load(View v) {
		try {
			MinesPersistentSnapshot mps = PersistenceHandler
					.getMinesPersistentSnapshot(this, 0);
			if (mps != null) {
				initialize();
				timeElapsed = mps.stop;
				chronometer.setBase(mps.base);
				chronometer.setText(mps.chrontext);
				end = mps.end;
				mineCount = mps.mineCount;
				tvMineCount.setText(getString(R.string.mines_remaining) + ": "
						+ mineCount);
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						solution[i][j] = mps.solution[i][j];
						view[i][j].setText(mps.view[i][j]);
						if (view[i][j].getText().toString().equals("")) {
							view[i][j].setTextColor(Color.BLACK);
							view[i][j].setBackgroundColor(Color
									.parseColor("#DDDDDD"));
						} else if (view[i][j].getText().toString().equals(" ")) {
							view[i][j].setTextColor(Color.BLACK);
							view[i][j].setBackgroundColor(Color.WHITE);
						} else if (view[i][j].getText().toString().equals("X")) {
							view[i][j].setTextColor(Color.RED);
							view[i][j].setBackgroundColor(Color
									.parseColor("#DDDDDD"));
						} else {
							view[i][j].setTextColor(Color.BLACK);
							view[i][j].setBackgroundColor(Color.WHITE);
						}
					}
				}
				resumeChronometer();
			} else {
				System.out.println("nothing to load!");
			}
			Toast.makeText(this, R.string.succload, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, R.string.errload, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * method called when save button is touched saves all required fields
	 * 
	 * @param v
	 *            touched view
	 */
	public void save(View v) {
		try {
			MinesPersistentSnapshot mps = new MinesPersistentSnapshot();
			mps.stop = timeElapsed;
			mps.base = chronometer.getBase();
			mps.chrontext = chronometer.getText().toString();
			mps.end = end;
			mps.mineCount = mineCount;
			mps.solution = new String[dimensionX][dimensionY];
			mps.view = new String[dimensionX][dimensionY];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					mps.solution[i][j] = solution[i][j];
					mps.view[i][j] = view[i][j].getText().toString();
				}
			}
			PersistenceHandler.setMinesPersistentSnapshot(this, 0, mps);
			if (menu.getVisibility() == View.VISIBLE) {
				((Button) findViewById(R.id.loadgame)).setEnabled(true);
			}
			Toast.makeText(this, R.string.succsave, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, R.string.errsave, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * toggle music on/off
	 * 
	 * @param v
	 *            touched view
	 */
	public void sound(View v) {
		Button music = (Button) findViewById(R.id.music);
		if ((musicPlayer != null) && musicPlayer.isPlaying()) {
			music.setText(R.string.soundon);
			musicPlayer.stop();
		} else if ((musicPlayer != null) && !musicPlayer.isPlaying()) {
			music.setText(R.string.soundoff);
			playMusic();
		}
	}

	/**
	 * plays music mines.mid
	 */
	private void playMusic() {
		AssetFileDescriptor afd;
		try {
			// Read the music file from the asset folder
			afd = getAssets().openFd("mines.mid");
			// Creation of new media player
			musicPlayer = new MediaPlayer();
			// Set the player music source
			musicPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			// Set the looping and play the music
			musicPlayer.setLooping(true);
			musicPlayer.prepare();
			musicPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * end game
	 * 
	 * @param v
	 *            touched view
	 */
	public void quit(View v) {
		finish();
	}

	/**
	 * prints solution or view for debug and testing purposes
	 * 
	 * @param field
	 *            s for solution or v for view
	 */
	private void print(char field) {
		switch (field) {
		case 's':
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			for (int i = 0; i < dimensionX; i++) {
				System.out.print("|");
				for (int j = 0; j < dimensionY; j++) {
					System.out.print(solution[i][j]);
				}
				System.out.println("|");
			}
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			break;

		case 'v':
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			for (int i = 0; i < dimensionX; i++) {
				System.out.print("|");
				for (int j = 0; j < dimensionY; j++) {
					System.out.print(view[i][j].getText().toString());
				}
				System.out.println("|");
			}
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			break;

		default:
			break;
		}
	}

}
