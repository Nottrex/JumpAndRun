package game.gameObjects;

import game.Game;

public interface GameObject {
	float getPriority();
	void update(Game game);
	void init(Game game);
}
