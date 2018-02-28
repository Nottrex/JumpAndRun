package game.gameobjects;

import game.Game;

/**
 * An implementation of GameObject that stores the game instance
**/
public abstract class AbstractGameObject implements GameObject {
	protected Game game; // The variable used to store the game instance

	@Override
	public void init(Game game) {
		this.game = game;
	}

	@Override
	public void remove(Game game, boolean mapChange) {

	}
}
