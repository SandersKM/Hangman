package hangman.core;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class Letter {
	private double x;
	private double y;
	private Text text;
	@SuppressWarnings("unused")
	private Pane pane;
	private char rep;

	public Letter(char c, double x, double y, Text text, Pane pane) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.pane = pane;
		rep = c;
	}

	public Text getText() {
		return text;
	}

	public char getRep() {
		// returns the char it shows on screen
		return rep;
	}

	public void write() {
		text.setTranslateX(x);
		text.setTranslateY(y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setVisible(boolean visible) {
		text.setVisible(visible);
	}
}
