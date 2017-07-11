package hangman.core;

import java.util.Stack;

public class Guess {

	private char[] word;

	private char guess;

	public Guess(char[] word, char guessedLetter) {
		this.word = word;
		this.guess = guessedLetter;
	}

	public boolean inWord() {
		for (int i = 0; i < word.length; i++) {
			if (word[i] == guess) {
				return true;
			}
		}
		return false;
	}

	public Stack<Integer> whereInWord() {
		Stack<Integer> indexes = new Stack<Integer>();
		for (int i = 0; i < word.length; i++) {
			if (word[i] == guess) {
				indexes.push(i);
			}
		}
		return indexes;
	}

}
