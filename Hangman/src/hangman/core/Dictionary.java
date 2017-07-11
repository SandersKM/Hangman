package hangman.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {

	public ArrayList<String> dictionary;

	public Dictionary() {
		dictionary = getDictionary();
	}

	public ArrayList<String> getDictionary() {
		dictionary = new ArrayList<String>();
		try {
			File input = new File("wordsEn.txt");
			Scanner in = new Scanner(input);
			while (in.hasNextLine()) {
				dictionary.add(in.nextLine());
			}
			in.close();

		} catch (IOException ioe) {
			System.exit(1);
		}
		return dictionary;
	}

	public String getNewWord() {
		// Returns a word that is 7 or fewer letters in length.
		// wordLenghtLimit could be up to 9 if you are feeling brave.
		int wordLengthLimit = 7;
		String newWord = "thisIsJustAPlaceholder";
		while (newWord.length() > wordLengthLimit) {
			newWord = dictionary.get((int) ((Math.random() * 100000) % dictionary.size()));
		}
		return newWord.toUpperCase();
	}

}
