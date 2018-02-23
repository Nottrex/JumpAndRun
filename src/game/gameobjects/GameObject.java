package game.gameobjects;

import game.Game;

public interface GameObject {
	float getPriority();

	void update(Game game);

	void init(Game game);

	void remove(Game game, boolean mapChange);
}
