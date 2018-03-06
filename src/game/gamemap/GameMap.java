package game.gamemap;

import game.Game;
import game.data.script.Tree;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.cameracontroller.CameraController;
import game.gameobjects.gameobjects.entities.entities.DeadBodyHandler;
import game.gameobjects.gameobjects.particle.ParticleSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Level loaded into the game
 */
public class GameMap {
	private List<GameObject> gameObjects;					//all gameObjects in the map
	private CameraController cameraController;				//the cameraController specific to this map

	private float spawnX, spawnY, playerDrawingPriority;	//spawn location of the player
	private String directory, name;							//map name and save location

	private Tree onLoad, onUpdate;							//map scripts

	public GameMap() {
		gameObjects = new ArrayList<>();

		//Add default gameObjects
		cameraController = new CameraController();
		this.addGameObject(cameraController);
		this.addGameObject(new ParticleSystem());
		this.addGameObject(new DeadBodyHandler());
	}

	/**
	 * execute the load script
	 * @param game context
	 */
	public void load(Game game) {
		if (onLoad != null) onLoad.get(game);
	}

	/**
	 * execute the update script
	 * @param game context
	 */
	public void update(Game game) {
		if (onUpdate != null) onUpdate.get(game);
	}

	/**
	 * change the load script
	 * @param onLoad the new script
	 */
	public void setOnLoad(Tree onLoad) {
		this.onLoad = onLoad;
	}

	/**
	 * change the update script
	 * @param onUpdate the new script
	 */
	public void setOnUpdate(Tree onUpdate) {
		this.onUpdate = onUpdate;
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
