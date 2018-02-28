package game.gameobjects;

import game.Game;

public interface GameObject {
	/**
	 * A higher update priority results in earlier updates for this object
	 * @return a the update priority of this object 
	**/
	float getPriority();

	/**
	 * called once per game tick (60 times per second)
	 * @param game the Game instance containing this object
	**/
	void update(Game game);

	/**
	 * this gameObject is added to the game
	 * @param game the Game instance containing this object
	**/
	void init(Game game);

	/**
	 * this gameObject is removed from the game 
	 * @param game the Game instance containing this object
	 * @param mapChange boolean if this object is removed due to mapChange or because removeGameObject was called
	**/
	void remove(Game game, boolean mapChange);
}
