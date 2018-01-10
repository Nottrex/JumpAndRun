package game;

import game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
	private List<GameObject> objects;
	private float spawnX, spawnY;

	public GameMap() {
		objects = new ArrayList<>();
	}

	public void addObject(GameObject object) {
		objects.add(object);
	}

	public void setSpawnpoint(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
	}

	public List<GameObject> getObjects() {
		return objects;
	}

	public float getSpawnX() {
		return spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}
}
