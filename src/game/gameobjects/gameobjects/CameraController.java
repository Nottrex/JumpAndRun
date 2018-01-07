package game.gameobjects.gameobjects;

import game.Game;
import game.gameobjects.AbstractGameObject;
import game.gameobjects.gameobjects.entities.entities.Player;

import java.util.List;

public class CameraController extends AbstractGameObject {
	@Override
	public void update(Game game) {
		float x = 0;
		float y = 0;

		List<Player> players = game.getPlayers();
		for (Player p: players) {
			x += p.getHitBox().getCenterX();
			y += p.getHitBox().getCenterY();
		}

		if (players.size() != 0) {
			x /= players.size();
			y /= players.size();
		}


		game.getCamera().setPosition(x, y);
	}

	@Override
	public float getPriority() {
		return -1;
	}
}
