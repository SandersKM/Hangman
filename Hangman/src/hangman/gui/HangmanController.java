package hangman.gui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import hangman.core.Dictionary;
import hangman.core.Game;
import hangman.core.HangmanParts;
import hangman.core.Letter;
import hangman.core.Status;
import hangman.learn.Data;
import hangman.learn.Probabilities;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HangmanController {

	@FXML
	Circle hangHead;

	@FXML
	Line hangBase, hangPole, hangLeftLeg, hangRightLeg, hangLeftArm, hangRightArm, hangTorso, hangTop, hangRope;

	@FXML
	Pane letterPane, hangPane;

	@FXML
	ArrayList<Letter> clickableLetters, lettersInBlanks;

	@FXML
	ArrayList<Line> blanks;

	@FXML
	CheckBox hintsBox;

	@FXML
	Boolean hints;

	@FXML
	String unknownWord;

	@FXML
	private Movement clock;

	@FXML
	Dictionary dictionary;

	@FXML
	HangmanParts partsStack;

	@FXML
	Game game;

	@FXML
	Probabilities prob;

	@FXML
	Data data;

	private long FRAMES_PER_SEC = 50L;
	private long INTERVAL = 1000000000L / FRAMES_PER_SEC;

	private class Movement extends AnimationTimer {

		private long last = 0;
		private ArrayList<Letter> letters;

		public void setWords(ArrayList<Letter> letters) {
			this.letters = letters;
		}

		@Override
		public void handle(long now) {
			if (now - last > INTERVAL) {
				for (Letter let : letters) {
					let.write();
				}
				last = now;
			}
		}
	}

	public void initialize() {
		hints = false;
		dictionary = new Dictionary();
		data = new Data(dictionary);
		data.getData();
	}

	public void newGame() {
		prob = new Probabilities(data);
		letterPane.getChildren().clear();
		partsStack = new HangmanParts(intitializeHangmanPartsStack());
		lettersInBlanks = new ArrayList<Letter>();
		clickableLetters = new ArrayList<Letter>();
		initialLetters();
		game = new Game(dictionary, partsStack);
		unknownWord = game.word;
		// uncomment below if you want to cheat!
		// System.out.println(unknownWord);
		blanks = new ArrayList<Line>();
		makeBlanks(game.word.length());
		clock = new Movement();
		clock.setWords(clickableLetters);
		clock.start();
		if (hints) {
			heatLetters();
		}
	}

	private void endGame() {
		for (Letter let : clickableLetters) {
			let.setVisible(false);
		}
		if (game.status == Status.WIN) {
			fillInBlanks(game.blanks.blanks);
			setGameOverText(true);
		} else {
			fillInBlanks(unknownWord.toCharArray());
			setGameOverText(false);
		}
	}

	private void setGameOverText(boolean win) {
		Text gameOverResult = new Text();
		Text gameOverYou = new Text();
		gameOverYou.setText("YOU");
		if (win) {
			gameOverResult.setText("WIN!");
			gameOverResult.setFill(Color.GREEN);
			gameOverYou.setFill(Color.GREEN);
		} else {
			gameOverResult.setText("LOSE!");
			gameOverResult.setFill(Color.RED);
			gameOverYou.setFill(Color.RED);
		}
		Font value = new Font("Comic Sans MS", 75);
		gameOverYou.setY(180.0);
		gameOverYou.setX(141.0);
		gameOverResult.setY(260.0);
		gameOverResult.setX(141.0);
		gameOverResult.setFont(value);
		gameOverYou.setFont(value);
		gameOverYou.setVisible(true);
		gameOverResult.setVisible(true);
		letterPane.getChildren().add(gameOverYou);
		letterPane.getChildren().add(gameOverResult);
	}

	// The following methods have to do with the GUI's interactive letters

	private void initialLetters() {
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		// these coordinate arrays are used to position the letters in the GUI
		int[] xCoordinate = new int[] { 52, 101, 145, 189, 243, 280, 323, 366, 407, 85, 120, 165, 202, 248, 294, 344,
				379, 54, 99, 143, 186, 233, 270, 322, 369, 404 };
		int[] yCoordinate = new int[] { 179, 179, 179, 179, 179, 179, 179, 179, 179, 240, 240, 240, 240, 240, 240, 240,
				240, 301, 301, 301, 301, 301, 301, 301, 301, 301 };
		for (int i = 0; i < 26; i++) {
			addLetters(alphabet[i], xCoordinate[i], yCoordinate[i], true, clickableLetters);
		}
	}

	private void addLetters(char c, double x, double y, boolean init, ArrayList<Letter> group) {
		Text t = new Text();
		t.setText("" + c + "");
		Font value = new Font("Comic Sans MS", 44);
		t.setFont(value);
		if (init) {
			t.setOnMouseClicked(event -> {
				try {
					clicked(event);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			});
		}
		Letter letter = new Letter(c, x, y, t, letterPane);
		group.add(letter);
		if (!init) {
			// positions letters in the blanks
			t.setX(x);
			t.setY(y);
		}
		if (!init && (game.status == Status.LOSS)) {
			// this will tell you the word if you lose the game
			t.setFill(Color.RED);
		}
		letterPane.getChildren().add(t);
	}

	public Letter findLetter(Node n, ArrayList<Letter> letters) {
		for (Letter let : letters) {
			if (let.getText().equals(n)) {
				return let;
			}
		}
		return null;
	}

	public void clicked(MouseEvent event) throws FileNotFoundException {
		Node n = (Node) event.getSource();
		Letter l = findLetter(n, clickableLetters);
		if (l != null) {
			char[] result = game.newGuess(l.getRep());
			if (!game.status.isOver()) {
				if (result.length > 0) {
					fillInBlanks(result);
					prob.updateGuessLists(l.getRep(), true);
				} else if (result.length == 0) {
					partsStack.wrongGuess().setVisible(true);
					prob.updateGuessLists(l.getRep(), false);
				}
				l.setVisible(false);
				clickableLetters.remove(l);
				if (hints) {
					heatLetters();
				}
				game.updateStatus();
			}
			if (game.status.isOver()) {
				endGame();
			}
		}
	}

	// These methods deal with the letters' colors

	public void hintsPressed() {
		hints = hintsBox.isSelected();
		if (game != null) {
			if (hints && game != null) {
				heatLetters();
			} else {
				lettersToBlack();
			}
		}
	}

	public void heatLetters() {
		Hashtable<Letter, Double> hash = prob.letterHash(clickableLetters);
		Double[] minMax = prob.getMinMax(hash);
		for (int i = 0; i < clickableLetters.size(); i++) {
			Letter letter = clickableLetters.get(i);
			Double cuttoff = prob.mean(hash);
			// scaling is needed to get all of the data in the appropriate range
			// of values for colors (0.0 to 1.0)
			Double scaling = prob.scaling(hash.get(letter), minMax[0], minMax[1]);
			if (hash.get(letter) >= cuttoff) {
				// the best choice will be light green
				letter.getText().setFill(Color.color(0.0, scaling, 0.0));
			} else {
				// the worst choice will be light red
				letter.getText().setFill(Color.color(1 - scaling, 0.0, 0.0));
			}
		}
	}

	public void lettersToBlack() {
		// resets all of the letters to black if AI isn't being used
		for (int i = 0; i < clickableLetters.size(); i++) {
			Letter letter = clickableLetters.get(i);
			letter.getText().setFill(Color.BLACK);
		}
	}

	// The following methods pertain to the Blanks shown in the GUI

	public void makeBlanks(int wordLength) {
		int[] xArray = new int[] { 128, 173, 218, 262, 306, 351, 397, 440, 483, 524 };
		for (int i = 0; i < wordLength; i++) {
			Line l = new Line();
			l.setStartX(xArray[i] - 25);
			l.setEndX(xArray[i]);
			l.setStartY(101);
			l.setEndY(101);
			blanks.add(l);
			letterPane.getChildren().add(l);
		}
	}

	private void fillInBlanks(char[] result) {
		for (int i = 0; i < result.length; i++) {
			if (result[i] != ' ') {
				Line b = blanks.get(i);
				addLetters(result[i], b.getStartX(), b.getStartY() - 10, false, lettersInBlanks);
			}
		}
	}

	// The following methods pertain to the Hangman Image

	public Stack<Shape> intitializeHangmanPartsStack() {
		// resets all hangman parts so they are not shown
		// returns a stack of the parts
		Stack<Shape> hangmanParts = new Stack<Shape>();
		hangHead.setVisible(false);
		hangTorso.setVisible(false);
		hangRightArm.setVisible(false);
		hangLeftArm.setVisible(false);
		hangRightLeg.setVisible(false);
		hangLeftLeg.setVisible(false);
		hangBase.setVisible(false);
		hangPole.setVisible(false);
		hangTop.setVisible(false);
		hangRope.setVisible(false);
		hangmanParts.push(hangRope);
		hangmanParts.push(hangTop);
		hangmanParts.push(hangPole);
		hangmanParts.push(hangBase);
		hangmanParts.push(hangRightArm);
		hangmanParts.push(hangLeftArm);
		hangmanParts.push(hangRightLeg);
		hangmanParts.push(hangLeftLeg);
		hangmanParts.push(hangTorso);
		hangmanParts.push(hangHead);
		return hangmanParts;
	}

}
