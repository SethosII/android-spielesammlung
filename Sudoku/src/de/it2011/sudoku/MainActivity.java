package de.it2011.sudoku;

import java.util.ArrayList;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	int diff;
	Integer[][] buttons;
	ArrayList<Integer> inputs;
	Button focused;
	Button focused_old;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		diff = 40;
		inputs = new ArrayList<Integer>();
		buttons = new Integer[9][9];

		getallButtons();

		return true;

	}

	public void getallButtons() {
		int counter = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = R.id.field11 + counter;
				counter++;
			}
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 3; j < 6; j++) {
				buttons[i][j] = R.id.field11 + counter;
				counter++;
			}
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 6; j < 9; j++) {
				buttons[i][j] = R.id.field11 + counter;
				counter++;
			}
		}
	}

	public void setNumbers(View v) {
		// adds field to clicked fields
		inputs.add(focused.getId());

		// sets the focused field
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

		if (fillcheck() == true) {
			if (inputcheck() == true) {
				Button bmenu = (Button) findViewById(R.id.input1);
				bmenu.setText("WIN!");
			}
		}

	}

	public void getButton(View v) {
		// get the selected Button

		focused_old = focused;
		focused = (Button) findViewById(v.getId());
		focused.setBackgroundColor(Color.GRAY);
		if (focused_old != null) {
			focused_old.setBackgroundColor(Color.WHITE);
		}
	}

	public void deleteInput(View v) {
		for (int id : inputs) {
			Button b = (Button) findViewById(id);
			b.setText("");
		}
	}

	public void generateSudoku(View v) {
		Integer[][] sudoku = new Integer[9][9];
		int[] run = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int j = 0;
		int k = 0;
		for (int i = 0; i < 9; i++) {
			for (j = 0; j < 9; j++) {
				sudoku[i][j] = run[k];
				if (k == 8) {
					k = 0;
				} else {
					k++;
				}
			}
			switch (i) {
			case (0):
				k = 6;
				break;
			case (1):
				k = 3;
				break;
			case (2):
				k = 8;
				break;
			case (3):
				k = 5;
				break;
			case (4):
				k = 2;
				break;
			case (5):
				k = 7;
				break;
			case (6):
				k = 4;
				break;
			case (7):
				k = 1;
				break;

			}
		}

		// sets the calculated Buttons
		for (int f = 0; f < 9; f++) {
			for (int g = 0; g < 9; g++) {
				Button b = (Button) findViewById(buttons[f][g]);
				b.setText(sudoku[f][g].toString());
				b.setEnabled(false);
			}
		}

		// deletes certain fields
		for (int m = 0; m < diff; m++) {
			int rx = (int) (Math.random() * 9);
			int ry = (int) (Math.random() * 9);
			Button b = (Button) findViewById(buttons[rx][ry]);
			if (b.getText() == "") {
				m--;
			} else if (b.getText() != "") {
				b.setText("");
				b.setEnabled(true);
			}

		}
	}

	// checks if already all fields are filled
	public boolean fillcheck() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Button b = (Button) findViewById(buttons[i][j]);
				if (b.getText() == "") {
					return false;
				}
			}
		}
		return true;
	}

	public boolean inputcheck() {
		boolean checkx = true;
		boolean checky = true;
		boolean checkxy = true;
		int xup = 0;
		int xdown = 0;
		int yup = 0;
		int ydown = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Button b = (Button) findViewById(buttons[i][j]);

				// checks horizontal
				for (int k = 0; k < 9; k++) {
					Button bx = (Button) findViewById(buttons[i][k]);
					if (((b.getText().equals(bx.getText())))
							&& (!(b.equals(bx)))) {
						checkx = false;
					}
				}

				// checks vertical
				for (int l = 0; l < 9; l++) {
					Button by = (Button) findViewById(buttons[l][j]);
					if (((b.getText().equals(by.getText())))
							&& (!(b.equals(by)))) {
						checky = false;
					}
				}
				if(i>=0 && i<=2)
				{
					yup=2;
					ydown=0;
				}
				if(i>=3 && i<=5)
				{
					yup=5;
					ydown=3;
				}
				if(i>=6 && i<=8)
				{
					yup=8;
					ydown=6;
				}
				if(j>=0 && i<=2)
				{
					xup=2;
					xdown=0;
				}
				if(j>=3 && i<=5)
				{
					xup=5;
					xdown=3;
				}
				if(j>=6 && i<=8)
				{
					xup=8;
					xdown=6;
				}
				while(ydown<=yup)
				{
					while(xdown<=xup)
					{
						Button bxy = (Button)findViewById(buttons[ydown][xdown]);
						
						if (((b.getText().equals(bxy.getText())))
								&& (!(b.equals(bxy)))) {
							checkxy = false;
						}
						
						xdown++;
					}
					ydown++;
				}
			}
		}
		return checkx && checky && checkxy;

	}
}
