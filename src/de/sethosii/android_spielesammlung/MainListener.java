package de.sethosii.android_spielesammlung;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainListener implements OnClickListener {
	
	private MainActivity mainActivity;
	

	public MainListener(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 0:
			Intent mines = new Intent(mainActivity, MinesActivity.class);
			mainActivity.startActivity(mines);
			System.out.println("Mines");
			break;
		case 1:
			Intent sudoku = new Intent(mainActivity, SudokuActivity.class);
			mainActivity.startActivity(sudoku);
			System.out.println("Sudoku");
			break;
		case 2:
			Intent spaceslider = new Intent(mainActivity, SpacesliderActivity.class);
			mainActivity.startActivity(spaceslider);
			System.out.println("Spaceslider");
			break;
		default:
			System.out.println(v.getId());
			System.out.println("unbekannt");
		}
	}

}
