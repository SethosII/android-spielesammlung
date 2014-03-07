package de.sethosii.android_spielesammlung;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
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
	/** menu button near the border */
	private ImageButton menuButton;
	/** game end button */
	private Button endButton;
	/** display time */
	private Chronometer chronometer;
	/** time elapsed */
	private long timeElapsed = 0;
	/** media player 4 background music */
	private MediaPlayer musicPlayer;

	/**
	 * initialize all fields
	 */
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

		menuButton = (ImageButton) findViewById(R.id.showMenu);

		tvLiveCount = (TextView) findViewById(R.id.liveCount);
		tvDifficulty = (TextView) findViewById(R.id.difficulty);

		ssliderView = new SpacesliderView(this);
		ssliderView.setOnLiveChangeListener(new SpacesliderView.OnSpacesliderChangeListener() {
			// code is not called within onCreate()
			@Override
			public void onLiveChange(View v, int liveCount) {
				tvLiveCount.setText(getString(R.string.lives_remaining) + '\n' + liveCount);

				if (liveCount == 0) {
					// remembers timeElapsed
					stopChronometer();

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

			// code is not called within onCreate()
			@Override
			public void onDifficultyChange(View v, float difficulty) {
				tvDifficulty.setText(getString(R.string.difficulty)
						+ String.format("\n%.2f", difficulty));
			}
		});
		space.addView(ssliderView);

		newGame(ssliderView);

	}

	/**
	 * Stops chronometer and music on quit.
	 */
	@Override
	public void finish() {
		musicPlayer.stop();
		chronometer.stop();
		super.finish();
	}

	/**
	 * Starts music when app (view) is maximized.
	 */
	@Override
	protected void onResume() {
		if (getString(R.string.soundoff).equals(((Button) findViewById(R.id.music)).getText())) {
			playMusic();
		}
		super.onResume();
		// chronometer is restarted on closing menu
	}

	/**
	 * Stops music and chronometer when app (view) is minimized.
	 */
	@Override
	protected void onPause() {
		if (menu.getVisibility() != View.VISIBLE) {
			// calls stopChronometer();
			menu(menuButton);
		}
		musicPlayer.stop();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Start chronometer and sets base.
	 */
	private void startChronometer() {
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.start();
	}

	/**
	 * Stops chronometer and remembers stop time.
	 */
	private void stopChronometer() {
		chronometer.stop();
		timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
	}

	/**
	 * Resumes chronometer and sets base to continue.
	 */
	private void resumeChronometer() {
		chronometer.setBase(SystemClock.elapsedRealtime() - timeElapsed);
		chronometer.start();
	}

	/**
	 * Method called when new-game button is touched or the Activity is called new.
	 * 
	 * @param v
	 *            the view that called the method (new-game button)
	 */
	public void newGame(View v) {
		// TODO
		ssliderView.reset();
		if (ssliderView.getVisibility() != View.VISIBLE) {
			ssliderView.setVisibility(View.VISIBLE);
		}
		if (endButton.getVisibility() == View.VISIBLE) {
			endButton.setVisibility(View.GONE);
		}
		startChronometer();
	}

	/**
	 * Method called when cross button is touched.
	 * 
	 * @param v
	 *            the view that called the method (cross button)
	 */
	public void changeMode(View v) {
		finish();
	}

	/**
	 * Toggles (open/close) the menu. Method called when menu button is touched or app is paused.
	 * 
	 * @param v
	 *            the view that called the method (menu button)
	 */
	public void menu(View v) {
		if (menu.getVisibility() == View.VISIBLE) {
			menu.setVisibility(View.GONE);
			menuButton.setColorFilter(null);

			ssliderView.setRunning(true);
			resumeChronometer();
		} else {
			menu.setVisibility(View.VISIBLE);
			confirm.setVisibility(View.GONE);

			stopChronometer();
			ssliderView.setRunning(false);

			if (PersistenceHandler.getSpacesliderPersistentSnapshot(this, 0) == null) {
				((Button) findViewById(R.id.loadgame)).setEnabled(false);
			} else {
				((Button) findViewById(R.id.loadgame)).setEnabled(true);
			}
			menuButton.setColorFilter(Color.RED);
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
	 * Toggles music on/off.
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
	 * plays music spaceslider.mid
	 */
	private void playMusic() {
		AssetFileDescriptor afd;
		try {
			// Read the music file from the asset folder
			afd = getAssets().openFd("spaceslider.mid");
			// Creation of new media player
			musicPlayer = new MediaPlayer();
			// Set the player music source
			musicPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			// Set the looping and play the music
			musicPlayer.setLooping(true);
			musicPlayer.prepare();
			musicPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
