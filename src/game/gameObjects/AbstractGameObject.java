package game.gameObjects;

import game.Game;

public abstract class AbstractGameObject implements GameObject {
	protected Game game;

	@Override
	public void init(Game game) {
		this.game = game;
	}
}
