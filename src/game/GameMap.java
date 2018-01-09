package game;

import game.gameobjects.gameobjects.wall.Background;
import game.gameobjects.gameobjects.wall.Wall;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
	private List<Wall> walls;
	private List<Background> backgrounds;
	private float spawnX, spawnY;

	public GameMap() {
		walls = new ArrayList<>();
		backgrounds = new ArrayList<>();
	}

	public void addWall(Wall wall) {
		walls.add(wall);
	}

	public void addBackground(Background background) {
		backgrounds.add(background);
	}

	public void setSpawnpoint(float x, float y) {
		this.spawnX = x;
		this.spawnY = y;
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public List<Background> getBackgrounds() {
		return backgrounds;
	}

	public float getSpawnX() {
		return spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}
}
