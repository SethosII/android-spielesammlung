package de.sethosii.android_spielesammlung.persistence;

import de.sethosii.android_spielesammlung.EnumGameState;

public class MinesPersistentSnapshot extends PersistentSnapshotBase {

	//** save gamestate */
	public EnumGameState end;
	//** save remaining mines */
	public int mineCount;
	//** save solution */
	public String[][] solution;
	//** save user view */
	public String[][] view;
	
}
