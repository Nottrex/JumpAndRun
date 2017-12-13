package game;

import game.window.Window;

public class Main {
	public static void main(String[] args) {
		Game g = Game.getInstance();
		Window w = Window.getInstance();

		new Thread(g::gameLoop).start();
		w.run();
	}
}
