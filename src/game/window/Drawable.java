package game.window;

public interface Drawable {
	float getDrawingPriority();

	void setup(Window window);

	void draw(Window window, long time);

	void cleanUp(Window window);
}
