package de.sethosii.android_spielesammlung;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.sethosii.android_spielesammlung.persistence.PersistenceHandler;
import de.sethosii.android_spielesammlung.persistence.SpacesliderPersistentGameData;
import de.sethosii.android_spielesammlung.persistence.SpacesliderPersistentSnapshot;

public class SpacesliderActivity extends Activity {

	private SpacesliderView ssliderView;

	/** display live count */
	private TextView tvLiveCount;
	/** display difficulty */
	private TextView tvDifficulty;
	/** space area */
	private LinearLayout space;
	/** menu */
	private LinearLayout menu;
	/** confirm */
	private LinearLayout confirm;
	/** game end button */
	private Button endButton;
	/** display time */
	private Chronometer chronometer;
	/** time elapsed */
	private long timeElapsed;

	private SpacesliderView.OnSpacesliderChangeListener onValChange = new SpacesliderView.OnSpacesliderChangeListener() {
		@Override
		public void onLiveChange(View v, int liveCount) {
			tvLiveCount.setText(getString(R.string.lives_remaining) + '\n' + liveCount);

			if (liveCount == 0) {
				chronometer.stop();
				timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();

				Context c = (v != null ? v.getContext() : null);

				// get old highscore
				SpacesliderPersistentGameData sspgd = PersistenceHandler
						.getSpacesliderPersistentGameData(c);
				// score > highscore: save
				if (sspgd != null) {
					if ((sspgd.scoring != null) && (sspgd.scoring.length > 0)) {
						if (timeElapsed > sspgd.scoring[0].score) {
							sspgd.scoring[0].score = timeElapsed;
						} else {
							sspgd = null;
						}
					} else {
						// no highscore: save
						sspgd.addHighScore(timeElapsed);
					}
				} else {
					sspgd = new SpacesliderPersistentGameData();
					sspgd.addHighScore(timeElapsed);
				}

				if (sspgd != null) {
					// save new highscore
					PersistenceHandler.setSpacesliderPersistentGameData(c, sspgd);
				}


				// ssliderView.setVisibility(View.GONE);

				endButton.setVisibility(View.VISIBLE);
				endButton.setText(R.string.over);
			}
		}

		@Override
		public void onDifficultyChange(View v, float difficulty) {
			tvDifficulty.setText("Difficulty"/* getString(R.string.lives_remaining) */+ '\n'
					+ String.format("%.2f", difficulty));
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
		
		chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
			private long nextRT = 0;

			@Override
			public void onChronometerTick(Chronometer chronometer) {
				long elapsedRT = SystemClock.elapsedRealtime();

				if (elapsedRT > nextRT) {
					if (nextRT != 0) {
						ssliderView.increaseDifficulty();
					}
					nextRT = elapsedRT + 10000;
				}
			}
		});

		tvLiveCount = (TextView) findViewById(R.id.liveCount);
		tvDifficulty = (TextView) findViewById(R.id.difficulty);

		ssliderView = new SpacesliderView(this);
		ssliderView.setOnLiveChangeListener(onValChange);
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

	/**
	 * Method called when new-game button is touched.
	 * 
	 * @param v
	 *            the view that called the method (new-game button)
	 */
	public void clear(View v) {
		// TODO
		ssliderView.reset();
		if (ssliderView.getVisibility() != View.VISIBLE) {
			ssliderView.setVisibility(View.VISIBLE);
		}
		if (endButton.getVisibility() == View.VISIBLE) {
			endButton.setVisibility(View.GONE);
		}
	}

	/**
	 * Method called when cross button is touched.
	 * 
	 * @param v
	 *            the view that called the method (cross button)
	 */
	public void changeMode(View v) {
		// TODO
		finish();
	}

	/**
	 * open/close menu. Method called when menu button is touched.
	 * 
	 * @param v
	 *            the view that called the method (menu button)
	 */
	public void menu(View v) {
		if (menu.getVisibility() == View.VISIBLE) {
			menu.setVisibility(View.GONE);
			ssliderView.setRunning(true);
			// menuButton.setColorFilter(null);
			// if (end == EnumGameState.NOT_FINISHED) {
			// resumeChronometer();
			// }
		} else {
			confirm.setVisibility(View.GONE);
			menu.setVisibility(View.VISIBLE);
			ssliderView.setRunning(false);
			if (PersistenceHandler.getSpacesliderPersistentSnapshot(this, 0) == null) {
				((Button) findViewById(R.id.loadgame)).setEnabled(false);
			} else {
				((Button) findViewById(R.id.loadgame)).setEnabled(true);
			}
			// menuButton.setColorFilter(Color.RED);
			// if (end == EnumGameState.NOT_FINISHED) {
			// stopChronometer();
			// }
		}
	}

	/**
	 * Load saved state.
	 * 
	 * @param v
	 *            the view that called the method (load button)
	 */
	public void load(View v) {
		// read snapshot
		SpacesliderPersistentSnapshot ssps = PersistenceHandler.getSpacesliderPersistentSnapshot(
				this, 0);
		if (ssps == null) {
			return;
		}

		// loads chronometer
		timeElapsed = ssps.stop;
		chronometer.setBase(ssps.base);
		chronometer.setText(ssps.chrontext);

		// ...and the rest
		ssliderView.applySnapshot(ssps);
	}

	/**
	 * Saves current state.
	 * 
	 * @param v
	 *            the view that called the method (save button)
	 */
	public void save(View v) {
		// get snapshot
		SpacesliderPersistentSnapshot ssps = new SpacesliderPersistentSnapshot();

		// saves chronometer
		ssps.stop = timeElapsed;
		ssps.base = chronometer.getBase();
		ssps.chrontext = chronometer.getText().toString();

		// ...and the rest
		ssliderView.fillSnapshot(ssps);

		// write snapshot
		PersistenceHandler.setSpacesliderPersistentSnapshot(this, 0, ssps);
	}

	/**
	 * toggle music on/off
	 * 
	 * @param v touched view
	 */
	public void sound(View v) {
//		Button music = (Button) findViewById(R.id.music);
//		if ((musicPlayer != null) && musicPlayer.isPlaying()) {
//			music.setText(R.string.soundon);
//			musicPlayer.stop();
//		} else if ((musicPlayer != null) && !musicPlayer.isPlaying()) {
//			music.setText(R.string.soundoff);
//			playMusic();
//		}
	}

	/**
	 * Finishes the game.
	 * 
	 * @param v
	 *            the view that called the method (quit button)
	 */
	public void quit(View v) {
		finish();
	}

}
