package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpacesliderActivity extends Activity {

	// display live count
	private TextView tvLiveCount;
	// space area
	private LinearLayout space;
	// menu
	private LinearLayout menu;
	// confirm
	private LinearLayout confirm;
	// game end button
	private Button endButton;
	// display time
	private Chronometer chronometer;


    private SpacesliderView.OnLiveChangeListener onYes = new SpacesliderView.OnLiveChangeListener()
    {
        public void onLiveChange(View v, int liveCount)
        {
        	if (liveCount == 0) {
        		chronometer.stop();
        	}
            tvLiveCount.setText(getString(R.string.lives_remaining) + ":\n" + liveCount);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_spaceslider);

		space = (LinearLayout) findViewById(R.id.space);
		menu = (LinearLayout) findViewById(R.id.menu);
		menu.setVisibility(View.GONE);
		confirm = (LinearLayout) findViewById(R.id.confirmDialog);
		confirm.setVisibility(View.GONE);
		endButton = (Button) findViewById(R.id.endbutton);
		endButton.setVisibility(View.GONE);

		chronometer = (Chronometer) findViewById(R.id.chronometer);

		tvLiveCount = (TextView) findViewById(R.id.liveCount);

		SpacesliderView ssliderView = new SpacesliderView(this);
		ssliderView.setOnLiveChangeListener(onYes);
		space.addView(ssliderView);

		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();

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
