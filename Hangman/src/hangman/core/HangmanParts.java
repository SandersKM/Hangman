package hangman.core;

import java.util.Stack;
import javafx.scene.shape.Shape;

public class HangmanParts {

	Stack<Shape> partsStack;

	public HangmanParts(Stack<Shape> partsStack) {
		this.partsStack = partsStack;
	}

	public int partsLeft() {
		return partsStack.size();
	}

	public boolean dead() {
		// if dead, the game is lost
		return partsStack.size() == 0;
	}

	public Shape wrongGuess() {
		Shape part = partsStack.pop();
		return part;
	}
}
