package game;

import game.gameObjects.*;
import game.gameObjects.entities.Player;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Keyboard;
import game.window.Window;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	public static final int TPS = 60;

	private Window window;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;

	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private Player player;

	public Game(Window window) {
		this.window = window;

		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		player = new Player();

		this.addGameObject(new Wall());
		this.addGameObject(player);
	}

	public void gameLoop() {
		long time;
		while (window.isRunning()) {
			time = TimeUtil.getTime();
			Keyboard keyboard = window.getKeyboard();

			player.setJumping(keyboard.isPressed(Keyboard.GAMEPAD_BUTTON_A));
			player.setDown(keyboard.getPressed(Keyboard.GAMEPAD_AXIS_LEFT_Y_RIGHT));
			player.setMx(keyboard.getPressed(Keyboard.GAMEPAD_AXIS_LEFT_X_RIGHT) - keyboard.getPressed(Keyboard.GAMEPAD_AXIS_LEFT_X_LEFT));
			if (keyboard.isPressed(Keyboard.GAMEPAD_BUTTON_B)) window.getCamera().addScreenshake(0.01f);

			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();
				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) window.removeDrawable((Drawable) gameObject);
			}

			gameObjects.sort((o1, o2) -> Float.compare(o2.getPriority(),o1.getPriority()));

			for (GameObject gameObject: gameObjects) {
				gameObject.update(this);
			}

			long newTime = TimeUtil.getTime();

			TimeUtil.sleep((int) (1000.0f/TPS - (newTime-time)));
		}
	}

	public Camera getCamera() {
		return window.getCamera();
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
