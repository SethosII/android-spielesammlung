package de.sethosii.android_spielesammlung.persistence;

public class SpacesliderPersistentSnapshot extends PersistentSnapshotBase {
	/** save current difficulty */
	public float difficulty;
	/** save remaining lives */
	public int liveCount;
	/** save ship position */
	public int shipX;
	/** save number of asteroids */
	public int asteroidCount;
	/** save x-position of all asteroids */
	public int[] asteroidX;
	/** save y-position of all asteroids */
	public int[] asteroidY;
	/** save speed of all asteroids */
	public int[] asteroidSpeed;
	/** save size of all asteroids */
	public int[] asteroidSize;
}
