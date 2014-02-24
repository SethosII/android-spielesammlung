package de.sethosii.android_spielesammlung.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Bietet statische Methoden zum Laden und Speichern von Daten.
 */
public class PersistenceHandler {

	private static final String PREFS_NAME_MAIN = "PrefsFileMain";
	private static final String PREFS_NAME_MINES = "PrefsFileMines";
	private static final String PREFS_NAME_SSLIDER = "PrefsFileSpaceSlider";
	private static final String PREFS_NAME_SUDOKU = "PrefsFileSudoku";

	/**
	 * Ergänzt einen Schlüssel-String um einen Index zur Mehrfachnutzung.
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	private static String indexedKey(String key, int index) {
		if (index > 0) {
			return String.format("%s_%d", key, index);
		}
		return key;
	}

	/**
	 * Primitiever Test.
	 * @param ctx
	 * @return
	 */
	public static Long getSomeLong(Context ctx) {
		SharedPreferences settings = ctx
				.getSharedPreferences(PREFS_NAME_MAIN, Context.MODE_PRIVATE);
		long lSomeLong = settings.getLong("SomeLong", 0);

		return lSomeLong;
	}

	/**
	 * Primitiever Test.
	 * @param ctx
	 * @param lHighScore
	 */
	public static void setSomeLong(Context ctx, long lHighScore) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx
				.getSharedPreferences(PREFS_NAME_MAIN, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		editor.putLong("SomeLong", lHighScore);

		editor.commit(); // Änderung an den Preferences durchführen
	}

	/**
	 * Liefert die gespeicherten Informationen über Mines zurück.
	 * 
	 * @param ctx
	 * @return
	 */
	public static MinesPersistentGameData getMinesPersistentGameData(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_MINES,
				Context.MODE_PRIVATE);
		String jsonString = settings.getString("MinesPersistentGameData", null);

		Gson gson = new Gson();

		// Objekt aus JSON-String deserialisieren
		return gson.fromJson(jsonString, MinesPersistentGameData.class);
	}

	/**
	 * Speichert die übergebenen Informationen über Mines.
	 * 
	 * @param ctx
	 * @param obj
	 */
	public static void setMinesPersistentGameData(Context ctx, MinesPersistentGameData obj) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_MINES,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		// Objekt in JSON-String serialisieren
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);

		editor.putString("MinesPersistentGameData", jsonString);

		editor.commit(); // Änderung an den Preferences durchführen
	}

	/**
	 * Liefert den gespeicherten Spielstand von Mines zurück.
	 * 
	 * @param ctx
	 * @param index
	 * @return
	 */
	public static MinesPersistentSnapshot getMinesPersistentSnapshot(Context ctx, int index) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_MINES,
				Context.MODE_PRIVATE);
		String jsonString = settings.getString(indexedKey("MinesPersistentSnapshot", index), null);

		Gson gson = new Gson();

		// Objekt aus JSON-String deserialisieren
		return gson.fromJson(jsonString, MinesPersistentSnapshot.class);
	}

	/**
	 * Speichert den übergebenen Spielstand von Mines.
	 * 
	 * @param ctx
	 * @param index
	 * @param obj
	 */
	public static void setMinesPersistentSnapshot(Context ctx, int index,
			MinesPersistentSnapshot obj) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_MINES,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		// Objekt in JSON-String serialisieren
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);

		editor.putString(indexedKey("MinesPersistentSnapshot", index), jsonString);

		editor.commit(); // Änderung an den Preferences durchführen
	}

	/**
	 * Liefert die gespeicherten Informationen über SpaceSlider zurück.
	 * 
	 * @param ctx
	 * @return
	 */
	public static SpacesliderPersistentGameData getSpacesliderPersistentGameData(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SSLIDER,
				Context.MODE_PRIVATE);
		String jsonString = settings.getString("SpacesliderPersistentGameData", null);

		Gson gson = new Gson();

		// Objekt aus JSON-String deserialisieren
		return gson.fromJson(jsonString, SpacesliderPersistentGameData.class);
	}

	/**
	 * Speichert die übergebenen Informationen über SpaceSlider.
	 * 
	 * @param ctx
	 * @param obj
	 */
	public static void setSpacesliderPersistentGameData(Context ctx,
			SpacesliderPersistentGameData obj) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SSLIDER,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		// Objekt in JSON-String serialisieren
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);

		editor.putString("SpacesliderPersistentGameData", jsonString);

		editor.commit(); // Änderung an den Preferences durchführen
	}

	/**
	 * Liefert den gespeicherten Spielstand von SpaceSlider zurück.
	 * 
	 * @param ctx
	 * @param index
	 * @return
	 */
	public static SpacesliderPersistentSnapshot getSpacesliderPersistentSnapshot(Context ctx,
			int index) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SSLIDER,
				Context.MODE_PRIVATE);
		String jsonString = settings.getString(indexedKey("SpacesliderPersistentSnapshot", index),
				null);

		Gson gson = new Gson();

		// Objekt aus JSON-String deserialisieren
		return gson.fromJson(jsonString, SpacesliderPersistentSnapshot.class);
	}

	/**
	 * Speichert den übergebenen Spielstand von SpaceSlider.
	 * 
	 * @param ctx
	 * @param index
	 * @param obj
	 */
	public static void setSpacesliderPersistentSnapshot(Context ctx, int index,
			SpacesliderPersistentSnapshot obj) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SSLIDER,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		// Objekt in JSON-String serialisieren
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);

		editor.putString(indexedKey("SpacesliderPersistentSnapshot", index), jsonString);

		editor.commit(); // Änderung an den Preferences durchführen
	}

	/**
	 * Liefert die gespeicherten Informationen über Sudoku zurück.
	 * 
	 * @param ctx
	 * @return
	 */
	public static SudokuPersistentGameData getSudokuPersistentGameData(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SUDOKU,
				Context.MODE_PRIVATE);
		String jsonString = settings.getString("SudokuPersistentGameData", null);

		Gson gson = new Gson();

		// Objekt aus JSON-String deserialisieren
		return gson.fromJson(jsonString, SudokuPersistentGameData.class);
	}

	/**
	 * Speichert die übergebenen Informationen über Sudoku.
	 * 
	 * @param ctx
	 * @param obj
	 */
	public static void setSudokuPersistentGameData(Context ctx, SudokuPersistentGameData obj) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SUDOKU,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		// Objekt in JSON-String serialisieren
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);

		editor.putString("SudokuPersistentGameData", jsonString);

		editor.commit(); // Änderung an den Preferences durchführen
	}

	/**
	 * Liefert den gespeicherten Spielstand von Sudoku zurück.
	 * 
	 * @param ctx
	 * @param index
	 * @return
	 */
	public static SudokuPersistentSnapshot getSudokuPersistentSnapshot(Context ctx, int index) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SUDOKU,
				Context.MODE_PRIVATE);
		String jsonString = settings.getString(indexedKey("SudokuPersistentSnapshot", index), null);

		Gson gson = new Gson();

		// Objekt aus JSON-String deserialisieren
		return gson.fromJson(jsonString, SudokuPersistentSnapshot.class);
	}

	/**
	 * Speichert den übergebenen Spielstand von Sudoku.
	 * 
	 * @param ctx
	 * @param index
	 * @param obj
	 */
	public static void setSudokuPersistentSnapshot(Context ctx, int index,
			SudokuPersistentSnapshot obj) {
		// Editor Objekt zur Bearbeitung von Preferences nötig
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME_SUDOKU,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		// Objekt in JSON-String serialisieren
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);

		editor.putString(indexedKey("SudokuPersistentSnapshot", index), jsonString);

		editor.commit(); // Änderung an den Preferences durchführen
	}

}
