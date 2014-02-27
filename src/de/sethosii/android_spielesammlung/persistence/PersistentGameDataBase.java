package de.sethosii.android_spielesammlung.persistence;

import java.util.Arrays;

public abstract class PersistentGameDataBase {
//player wird vorerst nicht implementiert
	public class HighScoreEntry {
		public long score = 0;
		//public String player = "";

		private HighScoreEntry(long score) {
			this.score = score;
			//this.player = player;
		}
	}

	/** gespeicherte HighScores für eine Art von Spiel */
	public HighScoreEntry[] scoring;

	public void addHighScore(long score) {
		int newIndex = 0;
		// Array verlängern
		if (scoring == null) {
			scoring = new HighScoreEntry[1];
		} else {
			newIndex = scoring.length;
			scoring = Arrays.copyOf(scoring, newIndex + 1);
		}

		// neuen Wert anfügen
		HighScoreEntry entry = new HighScoreEntry(score);
		scoring[newIndex] = entry;
	}

}
