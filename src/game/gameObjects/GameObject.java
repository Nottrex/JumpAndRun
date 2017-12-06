package game.gameObjects;

import game.Game;

public interface GameObject {
	public float getDrawingPriority();
	public void update(Game game);
}
