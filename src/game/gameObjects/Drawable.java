package game.gameObjects;

public interface Drawable extends GameObject {
	public float getDrawingPriority();
	public void draw();
}
