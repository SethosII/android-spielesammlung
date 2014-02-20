package de.sethosii.android_spielesammlung;

public class MinesLogic {

	// Markierungen setzen oder nicht
	private boolean mark;
	// Spielzustände
	private EnumEnd end;
	// Größe des Spielfelds für schnelleren Zugriff
	private int dimensionX;
	private int dimensionY;
	// Anzahl der gesetzten Minen
	private int mineCount;
	// Lösung
	private String[][] solution;
	// Spielfeld, das der Nutzer sieht
	private String[][] view;

	public MinesLogic(int x, int y, char difficulty) {
		switch (difficulty) {
		// Schwierigkeitsgrad Anfänger: 9x9 Feld, 10 Minen
		case 'e':
			mark = false;
			end = EnumEnd.UNSET;
			dimensionX = 9;
			dimensionY = 9;
			mineCount = 10;
			generateGame(x, y);
			break;

		default:
			System.out.println("Unimplemented difficuty: " + difficulty);
			break;
		}
	}

	private void generateGame(int x, int y) {
		initialize();
		placeMines(x, y);
		setNumbers();
	}

	// setzt Größe und füllt Lösung und Spielfeld mit leeren Feldern
	private void initialize() {
		solution = new String[dimensionX][dimensionY];
		view = new String[dimensionX][dimensionY];
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				solution[i][j] = " ";
				view[i][j] = " ";
			}
		}
	}

	// setzt die Minen
	private void placeMines(int x, int y) {
		int placed = 0;
		while (placed < mineCount) {
			int posX = (int) (Math.random() * dimensionX);
			int posY = (int) (Math.random() * dimensionY);
			// das erste Feld darf keine Mine enthalten
			if ((posX != x || posY != y) && !solution[posX][posY].equals("*")) {
				solution[posX][posY] = "*";
				placed++;
			}
		}
	}

	// rechnet die Zahlen der Felder aus
	private void setNumbers() {
		int number = 0;
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				if (!solution[i][j].equals("*")) {
					number = 0;
					if (i > 0) {
						if (solution[i - 1][j].equals("*")) {
							number++;
						}
						if (j < dimensionY - 1) {
							if (solution[i - 1][j + 1].equals("*")) {
								number++;
							}
						}
						if (j > 0) {
							if (solution[i - 1][j - 1].equals("*")) {
								number++;
							}
						}
					}
					if (j > 0) {
						if (solution[i][j - 1].equals("*")) {
							number++;
						}
						if (i < dimensionX - 1) {
							if (solution[i + 1][j - 1].equals("*")) {
								number++;
							}
						}
					}
					if (i < dimensionX - 1) {
						if (solution[i + 1][j].equals("*")) {
							number++;
						}
						if (j < dimensionY - 1) {
							if (solution[i + 1][j + 1].equals("*")) {
								number++;
							}
						}
					}
					if (j < dimensionY - 1) {
						if (solution[i][j + 1].equals("*")) {
							number++;
						}
					}
					if (number != 0) {
						solution[i][j] = Integer.toString(number);
					}
				}
			}
		}
	}

	// zur Ausgabe der Lösung oder des Spielfeldes
	public void print(char field) {
		switch (field) {
		case 's':
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			for (int i = 0; i < dimensionX; i++) {
				System.out.print("|");
				for (int j = 0; j < dimensionY; j++) {
					System.out.print(solution[i][j]);
				}
				System.out.println("|");
			}
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			break;

		case 'v':
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			for (int i = 0; i < dimensionX; i++) {
				System.out.print("|");
				for (int j = 0; j < dimensionY; j++) {
					System.out.print(view[i][j]);
				}
				System.out.println("|");
			}
			System.out.print("+");
			for (int i = 0; i < dimensionY; i++) {
				System.out.print("-");
			}
			System.out.println("+");
			break;

		default:
			break;
		}
	}

	// Methode die beim Berühren eines Feldes ausgeführt wird
	public void touchField(int posX, int posY) {
		// prüft ob Markiermodus gewählt ist
		if (mark) {
			// leer: Markierung setzen
			if (view[posX][posY].equals(" ")) {
				view[posX][posY] = "!";
			} else
			// markiert: Markierung entfernen
			if (view[posX][posY].equals("!")) {
				view[posX][posY] = " ";
			}
		} else {
			// prüft ob Feld nicht aufgedeckt ist
			if (view[posX][posY].equals(" ")) {
				// Feld der Lösung an diese Stelle schreiben
				view[posX][posY] = solution[posX][posY];
				// Mine aufgedeckt: Spiel verloren
				if (solution[posX][posY].equals("*")) {
					view[posX][posY] = "*";
					end = EnumEnd.LOSE;
				} else
				// leeres Feld: Umgebung aufdecken
				if (solution[posX][posY].equals(" ")) {
					revealeSurrounding(posX, posY);
				}
			}
			checkWin();
		}
	}

	// bei leerem Feld Umgebung aufdecken
	private void revealeSurrounding(int posX, int posY) {
		// Feld markieren
		view[posX][posY] = "#";
		// Umgebung prüfen, markiert: nichts machen
		// leer: markieren, sonst: Zahl auf Spielfeld übertragen
		if (posX > 0) {
			if (!solution[posX - 1][posY].equals("!")) {
				if (solution[posX - 1][posY].equals(" ")) {
					solution[posX - 1][posY] = "#";
					revealeSurrounding(posX - 1, posY);
				} else {
					view[posX - 1][posY] = solution[posX - 1][posY];
				}
			}
			if (posY < dimensionY - 1) {
				if (!solution[posX - 1][posY + 1].equals("!")) {
					if (solution[posX - 1][posY + 1].equals(" ")) {
						solution[posX - 1][posY + 1] = "#";
						revealeSurrounding(posX - 1, posY + 1);
					} else {
						view[posX - 1][posY + 1] = solution[posX - 1][posY + 1];
					}
				}
			}
			if (posY > 0) {
				if (solution[posX - 1][posY - 1].equals(" ")) {
					solution[posX - 1][posY - 1] = "#";
					revealeSurrounding(posX - 1, posY - 1);
				} else {
					view[posX - 1][posY - 1] = solution[posX - 1][posY - 1];
				}
			}
		}
		if (posY > 0) {
			if (solution[posX][posY - 1].equals(" ")) {
				solution[posX][posY - 1] = "#";
				revealeSurrounding(posX, posY - 1);
			} else {
				view[posX][posY - 1] = solution[posX][posY - 1];
			}
			if (posX < dimensionX - 1) {
				if (solution[posX + 1][posY - 1].equals(" ")) {
					solution[posX + 1][posY - 1] = "#";
					revealeSurrounding(posX + 1, posY - 1);
				} else {
					view[posX + 1][posY - 1] = solution[posX + 1][posY - 1];
				}
			}
		}
		if (posX < dimensionX - 1) {
			if (solution[posX + 1][posY].equals(" ")) {
				solution[posX + 1][posY] = "#";
				revealeSurrounding(posX + 1, posY);
			} else {
				view[posX + 1][posY] = solution[posX + 1][posY];
			}
			if (posY < dimensionY - 1) {
				if (solution[posX + 1][posY + 1].equals(" ")) {
					solution[posX + 1][posY + 1] = "#";
					revealeSurrounding(posX + 1, posY + 1);
				} else {
					view[posX + 1][posY + 1] = solution[posX + 1][posY + 1];
				}
			}
		}
		if (posY < dimensionY - 1) {
			if (solution[posX][posY + 1].equals(" ")) {
				solution[posX][posY + 1] = "#";
				revealeSurrounding(posX, posY + 1);
			} else {
				view[posX][posY + 1] = solution[posX][posY + 1];
			}
		}
	}

	// prüfen, ob Spiel gewonnen wurde
	private void checkWin() {
		int count = 0;
		for (int i = 0; i < dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				if (view[i][j].equals(" ") || view[i][j].equals("!")) {
					count++;
				}
			}
		}
		if (count <= mineCount) {
			end = EnumEnd.WIN;
		}
	}

	// Spielstatus abfragen
	public EnumEnd getEnd() {
		return end;
	}

	// Markierungsmodus ändern
	public void changeMode() {
		mark = !mark;
	}
}
