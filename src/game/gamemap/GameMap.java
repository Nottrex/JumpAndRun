package game.gamemap;

import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.CameraController.CameraController;
import game.gameobjects.gameobjects.particle.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
	private List<GameObject> gameObjects;
	private CameraController cameraController;

	private float spawnX, spawnY, playerDrawingPriority;

	public GameMap() {
		gameObjects = new ArrayList<>();

		cameraController = new CameraController();
		this.addGameObject(cameraController);
		this.addGameObject(new ParticleSystem());
	}

	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public void setSpawnPoint(float x, float y, float playerDrawingPriority) {
		this.spawnX = x;
		this.spawnY = y;
		this.playerDrawingPriority = playerDrawingPriority;
	}

	public CameraController getCameraController() {
		return cameraController;
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
