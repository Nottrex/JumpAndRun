package game.window;

public interface Drawable {
	/**
	 * the drawing priority determines how early the drawable is drawn
	 * therefore this is equal to the depth -> higher priority means
	 * this is drawn behind the other drawables
	 * @return the drawing priority
	 */
	float getDrawingPriority();

	/**
	 * executed, when this drawable is added to a window
	 * @param window the window this has been added to
	 */
	void setup(Window window);

	/**
	 * executed every time the window repaints
	 * therefore this object has to draw itself
	 * @param window the window this has been added to
	 * @param time the time (in ms) of the repaint
	 */
	void draw(Window window, long time);

	/**
	 * executed, when this drawable is removed from a window
	 * @param window the window this is removed from
	 */
	void cleanUp(Window window);
}
