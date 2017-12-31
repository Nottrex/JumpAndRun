package game;

import game.window.Window;

public class Main {
	public static void main(String[] args) {
		Options.load();
		Window w = new Window();
		Game g = new Game(w);

		new Thread(g::gameLoop).start();
		w.run();
	}
}
