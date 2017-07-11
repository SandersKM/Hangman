package hangman.core;

import java.util.Stack;

public class Blanks {

	public int blanksLeft;

	public char[] blanks;

	public Blanks(int numBlanks) {
		blanksLeft = numBlanks;
		blanks = initializeBlanks();
	}

	public char[] initializeBlanks() {
		blanks = new char[blanksLeft];
		for (int i = 0; i < blanks.length; i++) {
			blanks[i] = ' ';
		}
		return blanks;
	}

	public void updateBlanks(Stack<Integer> indexes, char guessedLetter) {
		while (!indexes.isEmpty()) {
			int index = indexes.pop();
			blanks[index] = guessedLetter;
			blanksLeft--;
		}
	}

	public boolean blanksFull() {
		// if true, the game is won
		return blanksLeft == 0;
	}

}
