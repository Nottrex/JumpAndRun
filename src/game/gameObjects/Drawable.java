package game.gameObjects;

import game.window.Window;

public interface Drawable extends GameObject {
	float getDrawingPriority();
	void draw(Window window, long time);
}
