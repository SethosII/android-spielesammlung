package de.it2011.sudoku;

import java.util.ArrayList;
import java.util.Random;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

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
	//the menu
	LinearLayout optionsmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		win = (Button)findViewById(R.id.winbutton);
		optionsmenu = (LinearLayout)findViewById(R.id.menu);
		
		win.setVisibility(View.GONE);
		optionsmenu.setVisibility(View.GONE);
		

		// difficulty hard=60;average=50;easy=40
		diff = 1;
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
		for (int i = 0; i < 9; i++) {
			for (int j = 3; j < 6; j++) {
				fields[i][j] = R.id.field11 + counter;
				counter++;
			}
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 6; j < 9; j++) {
				fields[i][j] = R.id.field11 + counter;
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
				focused.setText("1");
				break;
			case R.id.input2:
				focused.setText("2");
				break;
			case R.id.input3:
				focused.setText("3");
				break;
			case R.id.input4:
				focused.setText("4");
				break;
			case R.id.input5:
				focused.setText("5");
				break;
			case R.id.input6:
				focused.setText("6");
				break;
			case R.id.input7:
				focused.setText("7");
				break;
			case R.id.input8:
				focused.setText("8");
				break;
			case R.id.input9:
				focused.setText("9");
				break;
			case R.id.inputdel:
				focused.setText("");
			default:
				// do nothing

			}

			// checks if player won
			if (fillcheck() == true) {
				if (inputcheck() == true) {
					win.setText("You won!\n Touch to Continue!");
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

		win.setVisibility(View.GONE);
		
		// deletes previous inputs
		if (focused != null) {
			focused.setEnabled(false);
			focused.setBackgroundColor(Color.WHITE);
			focused = null;
		}

		// picks numbers of run without putting back
		for (int i = 0; i < 9; i++) {
			int index = (int) (Math.random() * (pointer + 1));
			random[i] = run[index];
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

			}
		}

		// shuffles the array

		// shuffles horizontal lines
		for (int z = 0; z < 3; z++) {
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
		for (int z = 0; z < 3; z++) {
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

		// sets the calculated fields
		for (int f = 0; f < 9; f++) {
			for (int g = 0; g < 9; g++) {
				Button b = (Button) findViewById(fields[f][g]);
				b.setText(sudoku[f][g].toString());
				b.setEnabled(false);
			}
		}

		// deletes random fields
		for (int m = 0; m < diff; m++) {
			int rx = (int) (Math.random() * 9);
			int ry = (int) (Math.random() * 9);
			Button b = (Button) findViewById(fields[rx][ry]);
			if (b.getText() == "") {
				m--;
			} else if (b.getText() != "") {
				b.setText("");
				b.setEnabled(true);
			}

		}
	}

	// checks if all fields are filled
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

	// checks if all fields were filled correctly
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
	
	public void showMenu(View v)
	{
		optionsmenu.setVisibility(View.VISIBLE);
	}
	
	public void hideMenu(View v)
	{
		optionsmenu.setVisibility(View.GONE);
	}
	
	

	// public boolean inputcheck() {
	// boolean checkx = true;
	// boolean checky = true;
	// boolean checkxy = true;
	// int xup = 0;
	// int xdown = 0;
	// int yup = 0;
	// int ydown = 0;
	// for (int i = 0; i < 9; i++) {
	// for (int j = 0; j < 9; j++) {
	// Button b = (Button) findViewById(fields[i][j]);
	//
	// // checks horizontal
	// for (int k = 0; k < 9; k++) {
	// Button bx = (Button) findViewById(fields[i][k]);
	// if (((b.getText().equals(bx.getText())))
	// && (!(b.equals(bx)))) {
	// checkx = false;
	// }
	// }
	//
	// // checks vertical
	// for (int l = 0; l < 9; l++) {
	// Button by = (Button) findViewById(fields[l][j]);
	// if (((b.getText().equals(by.getText())))
	// && (!(b.equals(by)))) {
	// checky = false;
	// }
	// }
	// if(i>=0 && i<=2)
	// {
	// yup=2;
	// ydown=0;
	// }
	// if(i>=3 && i<=5)
	// {
	// yup=5;
	// ydown=3;
	// }
	// if(i>=6 && i<=8)
	// {
	// yup=8;
	// ydown=6;
	// }
	// if(j>=0 && i<=2)
	// {
	// xup=2;
	// xdown=0;
	// }
	// if(j>=3 && i<=5)
	// {
	// xup=5;
	// xdown=3;
	// }
	// if(j>=6 && i<=8)
	// {
	// xup=8;
	// xdown=6;
	// }
	// while(ydown<=yup)
	// {
	// while(xdown<=xup)
	// {
	// Button bxy = (Button)findViewById(fields[ydown][xdown]);
	//
	// if (((b.getText().equals(bxy.getText())))
	// && (!(b.equals(bxy)))) {
	// checkxy = false;
	// }
	//
	// xdown++;
	// }
	// ydown++;
	// }
	// }
	// }
	// return checkx && checky && checkxy;
	//
	// }
}
