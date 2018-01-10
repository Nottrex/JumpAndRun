package game;

import game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
	private List<GameObject> gameObjects;
	private float spawnX, spawnY, playerDrawingPriority;

	public GameMap() {
		gameObjects = new ArrayList<>();
	}

	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public void setSpawnPoint(float x, float y, float playerDrawingPriority) {
		this.spawnX = x;
		this.spawnY = y;
		this.playerDrawingPriority = playerDrawingPriority;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public float getSpawnX() {
		return spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}

	public float getPlayerDrawingPriority() {
		return playerDrawingPriority;
	}
}
