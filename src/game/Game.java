package game;

import game.gameObjects.CollisionObject;
import game.gameObjects.Drawable;
import game.gameObjects.GameObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	private boolean running = true;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;
	private List<Drawable> drawables;

	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	public Game() {
		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		drawables = new LinkedList<>();

		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		gameLoop();
	}

	private void gameLoop() {
		while (running) {
			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();
				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) drawables.add((Drawable) gameObject);
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) drawables.remove(gameObject);
			}

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
