package hangman.learn;

import java.util.ArrayList;

import hangman.core.Dictionary;

public class Data {
	int[][] letterPairs;
	int[] letterIndividual;
	char[] letters;
	int wordCount;
	Dictionary dictionary;

	// I worked with Dr. Goadrich to determine how to store and use this data

	public Data(Dictionary d) {
		letterPairs = new int[26][26];
		letterIndividual = new int[26];
		letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		wordCount = 0;
		dictionary = d;
	}

	// the methods below retrieve the data

	public Double percentageLetter(char target, boolean contained) {
		for (int i = 0; i < letters.length; i++) {
			if (letters[i] == target) {
				if (contained) {
					return (1.0 * letterIndividual[i]) / wordCount;
				} else {
					return 1 - ((1.0 * letterIndividual[i]) / wordCount);
				}
			}
		}
		return 1.0;
	}

	public Double percentageBothLetters(char target, char target2, boolean contained2) {
		int targetIndex = getTargetIndex(target);
		int targetIndex2 = getTargetIndex(target2);
		Double percentContained = 1.0;
		// since I used mirroring in the 2D array, I have to order the targets
		// correctly to retrieve the data
		if (targetIndex < targetIndex2) {
			percentContained *= ((1.0 * letterPairs[targetIndex2][targetIndex]) / wordCount);
		} else {
			percentContained *= ((1.0 * letterPairs[targetIndex][targetIndex2]) / wordCount);
		}
		if (contained2) {
			return percentContained;
		}
		return 1 - percentContained;
	}

	public int getTargetIndex(char target) {
		// finds index of a char and it's associated data
		for (int i = 0; i < letters.length; i++) {
			if (letters[i] == target) {
				return i;
			}
		}
		return 0;
	}

	// the methods below initialize the data

	public void getData() {
		for (String word : dictionary.dictionary) {
			containsLetters(word);
		}
	}

	public void containsLetters(String word) {
		wordCount++;
		ArrayList<Character> w = wordToList(word.toUpperCase());
		for (int i = 0; i < letters.length; i++) {
			if (w.contains(letters[i])) {
				// a 1D array is used to store individual letter counts
				letterIndividual[i]++;
				// goes into a mirrored 2D array to store pairs
				for (int j = 0; j < i; j++) {
					if (w.contains(letters[j])) {
						letterPairs[i][j]++;
					}
				}
			}
		}
	}

	public ArrayList<Character> wordToList(String word) {
		// I needed convert the words to lists so that I
		// could use the contains method
		ArrayList<Character> w = new ArrayList<Character>();
		for (int i = 0; i < word.length(); i++) {
			w.add(word.charAt(i));
		}
		return w;
	}

}
