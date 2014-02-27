package de.sethosii.android_spielesammlung;

import de.sethosii.android_spielesammlung.persistence.MinesPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.MinesPersistentSnapshot;
import de.sethosii.android_spielesammlung.persistence.PersistenceHandler;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SpacesliderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        View ssliderView = new SpacesliderView(this);
        setContentView(ssliderView);
//if (true)
//return;


/*
        // Infos speichern
		MinesPersistentGameData mpgdw = new MinesPersistentGameData();
		mpgdw.addHighScore(1000000);
		mpgdw.addHighScore(13);
		PersistenceHandler.setMinesPersistentGameData(this, mpgdw);

        // Infos lesen
		MinesPersistentGameData mpgdr = PersistenceHandler.getMinesPersistentGameData(this);
		if (mpgdr != null) {
			;
		}

        // Snapshot speichern
		MinesPersistentSnapshot mpsw = new MinesPersistentSnapshot();
		mpsw.stop = 1000000;
		mpsw.player = "John Wayne";
		mpsw.minesCount = 10;
		mpsw.dimensionX = 48;
		mpsw.dimensionY = 11;
		mpsw.fieldState = new int[mpsw.dimensionX][mpsw.dimensionY];
		for (int i = 0; i < mpsw.dimensionX; i++) {
			for (int j = 0; j < mpsw.dimensionY; j++) {
				mpsw.fieldState[i][j] = (i << 4) + j;
			}
		}
		PersistenceHandler.setMinesPersistentSnapshot(this, 0, mpsw);

        // Snapshot lesen
		MinesPersistentSnapshot mpsr = PersistenceHandler.getMinesPersistentSnapshot(this, 0);
		if (mpsr != null) {
			;
		}



		android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(GridLayout.VERTICAL);

		// Textfeld
		TextView txt = new TextView(this);
		txt.setText("Spaceslider");
		txt.setLayoutParams(params);
		layout.addView(txt);

		LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setContentView(layout, layoutparams);
*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
