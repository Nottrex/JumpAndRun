package game.gamemap;

import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.cameracontroller.CameraController;
import game.gameobjects.gameobjects.entities.entities.DeadBodyHandler;
import game.gameobjects.gameobjects.particle.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
	private List<GameObject> gameObjects;
	private CameraController cameraController;

	private float spawnX, spawnY, playerDrawingPriority;
	private String directory, name;

	public GameMap() {
		gameObjects = new ArrayList<>();

		cameraController = new CameraController();
		this.addGameObject(cameraController);
		this.addGameObject(new ParticleSystem());
		this.addGameObject(new DeadBodyHandler());
	}

	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public void setSpawnPoint(float x, float y, float playerDrawingPriority) {
		this.spawnX = x;
		this.spawnY = y;
		this.playerDrawingPriority = playerDrawingPriority;

		cameraController.setSpawn(x, y);
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

	public void setMapInfo(String directory, String name) {
		this.directory = directory;
		this.name = name;
	}

	public String getDirectory() {
		return directory;
	}

	public String getName() {
		return name;
	}
}
