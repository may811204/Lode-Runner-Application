package main;

import java.io.IOException;

public class MainApp {
	public static void main (String[] args) throws IOException {
		Game game = new Game();
		game.mainStart();
		game.requestFocus();
	}
}
