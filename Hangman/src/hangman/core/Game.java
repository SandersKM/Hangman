package hangman.core;

import java.util.Stack;

public class Game {

	public String word;

	private Guess g;

	public HangmanParts partsLeft;

	public Status status;

	public Blanks blanks;

	public Game(Dictionary d, HangmanParts partsStack) {
		partsLeft = partsStack;
		word = d.getNewWord();
		blanks = new Blanks(word.length());
		status = Status.CONTINUE;
	}

	public char[] newGuess(char guessedLetter) {
		g = new Guess(word.toCharArray(), guessedLetter);
		if (g.inWord()) {
			Stack<Integer> indexes = g.whereInWord();
			blanks.updateBlanks(indexes, guessedLetter);
			return blanks.blanks;
		} else {
			return new char[0];
		}
	}

	public void updateStatus() {
		if (blanks.blanksFull()) {
			status = Status.WIN;
		} else if (partsLeft.dead()) {
			status = Status.LOSS;
		}
	}

	public Status getStatus() {
		return status;
	}

}
