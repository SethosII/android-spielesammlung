package de.sethosii.android_spielesammlung.persistence;

import de.sethosii.android_spielesammlung.EnumGameState;

public class MinesPersistentSnapshot extends PersistentSnapshotBase {

	public boolean mark;
	public EnumGameState end;
	public int mineCount;
	public String[][] solution;
	public String[][] view;
	
}
