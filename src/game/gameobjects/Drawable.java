package game.gameobjects;

import game.window.Window;

public interface Drawable extends GameObject {
	float getDrawingPriority();

	void setup(Window window);

	void draw(Window window, long time);

	void cleanUp(Window window);
}
