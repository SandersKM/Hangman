package hangman.learn;

import java.util.ArrayList;

public class Statistics {
	Data data;

	public Statistics(Data d) {
		data = d;
	}

	// the following is the Naive Bayes calculation. Dr. Goadrich suggested I
	// use this to determine the likelihood of letters being in a word based
	// what other letters appeared in the word.

	public Double calculation(char target, ArrayList<Character> lettersAlsoContained,
			ArrayList<Character> lettersNotContained) {
		Double numerator = data.percentageLetter(target, true)
				* getNumerator(target, lettersAlsoContained, lettersNotContained);
		Double denomenator = getDenomenator(lettersAlsoContained, lettersNotContained);
		return numerator / denomenator;
	}

	public Double getNumerator(char target, ArrayList<Character> lettersAlsoContained,
			ArrayList<Character> lettersNotContained) {
		Double numerator = 1.0;
		for (int i = 0; i < lettersAlsoContained.size(); i++) {
			numerator *= data.percentageBothLetters(target, lettersAlsoContained.get(i), true);
		}
		for (int i = 0; i < lettersNotContained.size(); i++) {
			numerator *= data.percentageBothLetters(target, lettersNotContained.get(i), false);
		}
		return numerator;
	}

	public Double getDenomenator(ArrayList<Character> lettersAlsoContained, ArrayList<Character> lettersNotContained) {
		Double denomenator = 1.0;
		for (int i = 0; i < lettersAlsoContained.size(); i++) {
			denomenator *= data.percentageLetter(lettersAlsoContained.get(i), true);
		}
		for (int i = 0; i < lettersNotContained.size(); i++) {
			denomenator *= data.percentageLetter(lettersNotContained.get(i), false);
		}
		return denomenator;
	}

}
