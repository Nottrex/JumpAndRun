package game;

import game.gameObjects.CollisionObject;
import game.gameObjects.Drawable;
import game.gameObjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private boolean running = true;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;
	private List<Drawable> drawables;

	private List<GameObject> toRemove;
	private List<GameObject> toAdd;

	public Game() {
		gameObjects = new ArrayList<>();
		collisionObjects = new ArrayList<>();
		drawables = new ArrayList<>();

		toRemove = new ArrayList<>();
		toAdd = new ArrayList<>();

		gameLoop();
	}

	private void gameLoop() {
		while (running) {
			for (GameObject gameObject: toAdd) {
				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) drawables.add((Drawable) gameObject);
			}
			toAdd.clear();

			for (GameObject gameObject: toRemove) {
				gameObjects.remove(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) drawables.remove(gameObject);
			}
			toRemove.clear();

			gameObjects.sort((o1, o2) -> (int) Math.signum(o1.getDrawingPriority()-o2.getDrawingPriority()));

			for (GameObject gameObject: gameObjects) {
				gameObject.update(this);
			}
		}
	}

	public void addGameObject(GameObject gameObject) {
		toAdd.add(gameObject);
	}

	public void removeGameObject(GameObject gameObject) {
		toRemove.add(gameObject);
	}

	public List<CollisionObject> getCollisionObjects() {
		return collisionObjects;
	}
}
