package de.sethosii.android_spielesammlung.persistence;

import java.util.Arrays;

public abstract class PersistentGameDataBase {

	public class HighScoreEntry {
		public int sore = 0;
		public String player = "";

		private HighScoreEntry(int score, String player) {
			this.sore = score;
			this.player = player;
		}
	}

	/** gespeicherte HighScores für eine Art von Spiel */
	public HighScoreEntry[] scoring;

	public void addHighScore(int score, String player) {
		int newIndex = 0;
		// Array verlängern
		if (scoring == null) {
			scoring = new HighScoreEntry[1];
		} else {
			newIndex = scoring.length;
			scoring = Arrays.copyOf(scoring, newIndex + 1);
		}

		// neuen Wert anfügen
		HighScoreEntry entry = new HighScoreEntry(score, player);
		scoring[newIndex] = entry;
	}

}
