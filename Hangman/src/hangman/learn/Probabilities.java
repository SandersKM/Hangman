package hangman.learn;

import java.util.ArrayList;
import java.util.Hashtable;

import hangman.core.Letter;

public class Probabilities {

	public Statistics stat;

	public ArrayList<Character> correctGuesses;

	public ArrayList<Character> incorrectGuesses;

	public Probabilities(Data d) {
		stat = new Statistics(d);
		correctGuesses = new ArrayList<Character>();
		incorrectGuesses = new ArrayList<Character>();
	}

	public void updateGuessLists(char alpha, boolean correct) {
		if (correct) {
			correctGuesses.add(alpha);
		} else {
			incorrectGuesses.add(alpha);
		}
	}

	public Hashtable<Letter, Double> letterHash(ArrayList<Letter> clickableLetters) {
		Hashtable<Letter, Double> hash = new Hashtable<Letter, Double>();
		for (Letter let : clickableLetters) {
			if (!correctGuesses.contains(let.getRep()) && !incorrectGuesses.contains(let.getRep())) {
				Double value = stat.calculation(let.getRep(), correctGuesses, incorrectGuesses);
				hash.put(let, value);
			}
		}
		return hash;
	}

	// the following are mathematical functions

	public Double mean(Hashtable<Letter, Double> hash) {
		Double totalValue = 0.0;
		for (Letter key : hash.keySet()) {
			totalValue += hash.get(key);
		}
		return totalValue / hash.size();
	}

	public Double[] getMinMax(Hashtable<Letter, Double> hash) {
		Double min = 1.0;
		Double max = 0.0;
		for (Letter key : hash.keySet()) {
			if (min > hash.get(key)) {
				min = hash.get(key);
			}
			if (max < hash.get(key)) {
				max = hash.get(key);
			}
		}
		return new Double[] { min, max };
	}

	public Double scaling(Double value, Double min, Double max) {
		// I found this scaling equation at
		// https://stats.stackexchange.com/questions/70801/how-to-normalize-data-to-0-1-range
		return ((value - min) / (max - min));
	}

}
