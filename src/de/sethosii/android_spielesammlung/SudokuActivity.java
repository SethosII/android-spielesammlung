package de.sethosii.android_spielesammlung;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;

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
	//the chronometer
	Chronometer chron;
	// tag for loggings
	private final static String Tag = "SudokuActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		win = (Button) findViewById(R.id.winbutton);
		optionsmenu = (LinearLayout) findViewById(R.id.menu);
		chron = (Chronometer)findViewById(R.id.chronometer);
		
		win.setVisibility(View.GONE);
		optionsmenu.setVisibility(View.GONE);
		
		
		// if difficulty is higher than 50 an endless loop is generated
		diff = 50;
		inputs = new ArrayList<Integer>();
		fields = new Integer[9][9];

		getallfields();

		return true;

	}

	// orders the fields
	public void getallfields() {
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

	// set focused field
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

			// checks if player won
			if (fillcheck() == true) {
				if (inputcheck() == true) {
					chron.stop();
					win.setText(R.string.win);
					win.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	// gets the selected field
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

	// deletes all inputs
	public void deleteInput(View v) {
		for (int id : inputs) {
			Button b = (Button) findViewById(id);
			b.setText("");
		}
	}

	// generates Sudoku
	public void generateSudoku(View v) {
		int pointer = 8;
		int[] run = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] random = new int[9];
		int shift = 0;
		sudoku = new Integer[9][9];
		int hswitch = 0;
		int vswitch = 0;

		//hide win message
		win.setVisibility(View.GONE);
		
		//count time
		chron.setBase(SystemClock.elapsedRealtime());
        chron.start();

		// deletes previous inputs
		if (focused != null) {
			focused.setBackgroundColor(Color.WHITE);
			focused = null;
		}

		// picks numbers of run without putting back into random
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

		// deletes fields that don´t change the uniqueness
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
				if (checksolutions(gapsudoku) == false) {
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

	}

	// checks if all fields were filled by player
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

	// checks if all fields were filled correctly by player
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

	public void showMenu(View v) {
		if (optionsmenu.getVisibility() == View.GONE) {
			optionsmenu.setVisibility(View.VISIBLE);
		} else if (optionsmenu.getVisibility() == View.VISIBLE) {
			optionsmenu.setVisibility(View.GONE);
		}
	}

	public void quit(View v) {
		chron.stop();
		finish();
	}

	// checks if sudoku with gap has a distinct solution
	public boolean checksolutions(Integer[][] gapsudoku) {
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

						// if a distinct solution for a field was found, set
						// solution, repeat everything
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

	// checks if a number is valid at the position (y,x)
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
