package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MinesActivity extends Activity {

	// mark fields or not
	private boolean mark;
	// game states
	private EnumGameState end;
	// shortcuts for size
	private int dimensionX;
	private int dimensionY;
	// number of placed mines
	private int maxMineCount;
	private int mineCount;
	// solution
	private String[][] solution;
	// user view
	private Button[][] view;
	// display mine count
	private TextView tvMineCount;
	// display time
	private Chronometer chronometer;
	// menu
	private LinearLayout menu;
	// game end button
	private Button endButton;
	// time elapsed
	private long timeElapsed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acitivity_mines);

		// initialize variables for beginner difficulty
		mark = false;
		end = EnumGameState.NOT_STARTED;
		dimensionX = 9;
		dimensionY = 9;
		maxMineCount = 10;
		mineCount = maxMineCount;
		solution = new String[dimensionX][dimensionY];
		view = new Button[dimensionX][dimensionY];
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				view[i][j] = ((Button) findViewById(R.id.b11 + i * dimensionY
						+ j));
				view[i][j].setText("");
				view[i][j].setBackgroundColor(Color.LTGRAY);
			}
		}
		tvMineCount = (TextView) findViewById(R.id.mineCount);
		tvMineCount.setText(getString(R.string.mines_remaining) + ": "
				+ mineCount);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		menu = (LinearLayout) findViewById(R.id.menu);
		menu.setVisibility(View.GONE);
		endButton = (Button) findViewById(R.id.endbutton);
		endButton.setVisibility(View.GONE);
		timeElapsed = 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// generate new game
	private void generateGame(int x, int y) {
		initialize();
		placeMines(x, y);
		setNumbers();
		chronometer.start();
	}

	// reset all values
	private void initialize() {
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				solution[i][j] = "";
				view[i][j].setText("");
				view[i][j].setBackgroundColor(Color.LTGRAY);
				view[i][j].setEnabled(true);
			}
		}
		end = EnumGameState.NOT_FINISHED;
		mineCount = 10;
		tvMineCount.setText(getString(R.string.mines_remaining) + ": "
				+ mineCount);
		menu.setVisibility(View.GONE);
		endButton.setVisibility(View.GONE);
		// reset time
		chronometer.stop();
		chronometer.setBase(SystemClock.elapsedRealtime());
		timeElapsed = 0;
	}

	// place the mines
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

	// calculate the numbers
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

	// create input for touchField method
	public void getField(View v) {
		String name = getResources().getResourceEntryName(v.getId());
		int x = Integer.parseInt(name.substring(1, 2)) - 1;
		int y = Integer.parseInt(name.substring(2, 3)) - 1;
		if (end.equals(EnumGameState.NOT_STARTED) && !mark) {
			generateGame(x, y);
		}
		touchField(x, y);
	}

	// method called when field was touched
	private void touchField(int posX, int posY) {
		Button touched = view[posX][posY];
		// set flag or not
		if (mark) {
			// empty: set flag
			if (touched.getText().equals("")) {
				touched.setText("!");
				mineCount--;
				tvMineCount.setText(getString(R.string.mines_remaining) + ": "
						+ mineCount);
			} else
			// marked: remove flag
			if (touched.getText().equals("!")) {
				touched.setText("");
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

	// reveal surrounding
	private void revealSurrounding(int posX, int posY) {
		// mark field as empty
		view[posX][posY].setText(" ");
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
				view[posX][posY - 1].setBackgroundColor(Color.WHITE);
			}
			if (posX < dimensionX - 1) {
				if (solution[posX + 1][posY - 1].equals("")) {
					solution[posX + 1][posY - 1] = " ";
					revealSurrounding(posX + 1, posY - 1);
				} else {
					view[posX + 1][posY - 1]
							.setText(solution[posX + 1][posY - 1]);
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
				view[posX + 1][posY].setBackgroundColor(Color.WHITE);
			}
			if (posY < dimensionY - 1) {
				if (solution[posX + 1][posY + 1].equals("")) {
					solution[posX + 1][posY + 1] = " ";
					revealSurrounding(posX + 1, posY + 1);
				} else {
					view[posX + 1][posY + 1]
							.setText(solution[posX + 1][posY + 1]);
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
				view[posX][posY + 1].setBackgroundColor(Color.WHITE);
			}
		}
	}

	// check game state
	private void checkEnd() {
		if (end.equals(EnumGameState.LOSE)) {
			chronometer.stop();
			timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
			endButton.setVisibility(View.VISIBLE);
			endButton.setText(R.string.lose);
			for (int i = 0; i < dimensionX; i++) {
				for (int j = 0; j < dimensionY; j++) {
					view[i][j].setEnabled(false);
				}
			}
		} else {
			int count = 0;
			for (int i = 0; i < dimensionX; i++) {
				for (int j = 0; j < dimensionY; j++) {
					if (view[i][j].getText().equals("")
							|| view[i][j].getText().equals("!")) {
						count++;
					}
				}
			}
			if (count <= maxMineCount) {
				end = EnumGameState.WIN;
			}
			if (end.equals(EnumGameState.WIN)) {
				chronometer.stop();
				timeElapsed = SystemClock.elapsedRealtime()
						- chronometer.getBase();
				endButton.setVisibility(View.VISIBLE);
				endButton.setText(R.string.win);
				for (int i = 0; i < dimensionX; i++) {
					for (int j = 0; j < dimensionY; j++) {
						view[i][j].setEnabled(false);
					}
				}
			}
		}
	}

	// change mark mode
	public void changeMode(View v) {
		mark = !mark;
	}

	// reset game
	public void clear(View v) {
		initialize();
		end = EnumGameState.NOT_STARTED;
	}

	// open menu
	public void menu(View v) {
		if (menu.getVisibility() == View.VISIBLE) {
			menu.setVisibility(View.GONE);
			if (end == EnumGameState.NOT_FINISHED) {
				chronometer.setBase(chronometer.getBase()
						+ SystemClock.elapsedRealtime() - timeElapsed);
				chronometer.start();
			}
		} else {
			menu.setVisibility(View.VISIBLE);
			if (end == EnumGameState.NOT_FINISHED) {
				chronometer.stop();
				timeElapsed = SystemClock.elapsedRealtime();
			}
		}
	}

	// load game
	public void load(View v) {

	}

	// save game
	public void save(View v) {

	}

	// end game
	public void quit(View v) {
		finish();
	}

	// prints solution or view
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
