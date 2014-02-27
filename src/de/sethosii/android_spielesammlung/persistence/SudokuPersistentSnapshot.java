package de.sethosii.android_spielesammlung.persistence;

import java.util.ArrayList;

public class SudokuPersistentSnapshot extends PersistentSnapshotBase {
	public String[][] fieldState;
	public ArrayList<Integer> disabled;
	public ArrayList<Integer> inputs;
}
