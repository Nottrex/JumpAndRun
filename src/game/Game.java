package game;

import game.data.Sprite;
import game.data.hitbox.HitBox;
import game.gamemap.GameMap;
import game.gamemap.MapLoader;
import game.gameobjects.CollisionObject;
import game.gameobjects.GameObject;
import game.gameobjects.gameobjects.Fade;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.entities.entities.Coin;
import game.gameobjects.gameobjects.entities.entities.DeadBody;
import game.gameobjects.gameobjects.entities.entities.Player;
import game.gameobjects.gameobjects.entities.entities.ScreenEntity;
import game.gameobjects.gameobjects.particle.ParticleSystem;
import game.util.TimeUtil;
import game.window.Camera;
import game.window.Drawable;
import game.window.Keyboard;
import game.window.Window;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game {
	private Window window;

	private int gameTick;

	private List<GameObject> gameObjects;
	private List<CollisionObject> collisionObjects;
	private List<Player> players;
	private List<Integer> inputs;
	private List<Color> playerColors;
	private GameMap map;

	private int fadeStart;
	private String newMap;
	private Queue<GameObject> toRemove;
	private Queue<GameObject> toAdd;

	private ParticleSystem particleSystem;

	private Map<String, Integer> values;
	private List<Ability> abilities;

	private ScreenEntity coinCounterCoin;
	private Text coinCounter;

	public Game(Window window) {
		this.window = window;

		gameTick = 0;

		players = new ArrayList<>();
		inputs = new ArrayList<>();
		playerColors = new ArrayList<>();
		gameObjects = new LinkedList<>();
		collisionObjects = new LinkedList<>();
		toRemove = new ConcurrentLinkedQueue<>();
		toAdd = new ConcurrentLinkedQueue<>();

		values = new HashMap<>();
		values.put("coins", 10);
		abilities = new ArrayList<>();

		coinCounter = new Text(-1000f, "0", 1, 1-(0.1f/6), 0.1f, false, 1, 1);
		coinCounterCoin = new ScreenEntity(new HitBox(1, 1, 0.4f/3, 0.4f/3), -1000, Coin.idle, 1, 1);
		addGameObject(coinCounter);
		addGameObject(coinCounterCoin);

		setGameMap(Constants.SYS_PREFIX + "menu", false);
	}

	public void gameLoop() {
		long time;
		while (window.isRunning()) {
			gameTick++;
			coinCounter.setText(values.getOrDefault("coins", 0).toString());
			coinCounter.setPosition(1 - coinCounterCoin.getWidth(), 1-(0.1f/6), 0.1f);
			time = TimeUtil.getTime();

			handleInput();

			if (newMap != null && gameTick - fadeStart >= Constants.FADE_TIME / 2) {
				GameMap newGameMap = MapLoader.load(this, newMap);
				if (map != null) {
					for (GameObject gameObject : map.getGameObjects()) {
						this.removeGameObject(gameObject);
					}
				}

				for (GameObject gameObject : newGameMap.getGameObjects()) {
					this.addGameObject(gameObject);
				}

				for (Player player : players) {
					player.respawn(newGameMap.getSpawnX(), newGameMap.getSpawnY(), newGameMap.getPlayerDrawingPriority());
				}

				map = newGameMap;
				newMap = null;
			}

			while (!toRemove.isEmpty()) {
				GameObject gameObject = toRemove.poll();

				gameObjects.remove(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.remove(gameObject);
				if (gameObject instanceof Drawable) window.removeDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = null;
				if (gameObject instanceof Player) {
					int id = players.indexOf(gameObject);
					players.remove(id);
					inputs.remove(id);
					playerColors.remove(id);
				}
			}

			while (!toAdd.isEmpty()) {
				GameObject gameObject = toAdd.poll();

				gameObject.init(this);

				gameObjects.add(gameObject);
				if (gameObject instanceof CollisionObject) collisionObjects.add((CollisionObject) gameObject);
				if (gameObject instanceof Drawable) window.addDrawable((Drawable) gameObject);
				if (gameObject instanceof ParticleSystem) particleSystem = (ParticleSystem) gameObject;
				if (gameObject instanceof Player) {
					players.add((Player) gameObject);
					((Player) gameObject).setColor(playerColors.get(players.size() - 1));
				}
			}

			collisionObjects.sort((o1, o2) -> Float.compare(o2.getCollisionPriority(), o1.getCollisionPriority()));
			gameObjects.sort((o1, o2) -> Float.compare(o2.getPriority(), o1.getPriority()));

			for (GameObject gameObject : gameObjects) {
				gameObject.update(this);
			}

			long newTime = TimeUtil.getTime();

			TimeUtil.sleep((int) (1000.0f / Constants.TPS - (newTime - time)));
		}

		cleanUp();
	}

	private void handleInput() {
		Keyboard keyboard = window.getKeyboard();

		for (int i = 0; i < 18; i++) {
			if (keyboard.isPressed(Options.controls.get("UP" + i)) && !inputs.contains(i)) {
				Player newPlayer = new Player(map.getSpawnX(), map.getSpawnY(), map.getPlayerDrawingPriority());
				this.addGameObject(newPlayer);
				inputs.add(i);
				playerColors.add(new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1));
			}
		}
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			int input = inputs.get(i);

			player.setJumping(keyboard.isPressed(Options.controls.get("UP" + input)));
			player.setMx(keyboard.getPressed(Options.controls.get("RIGHT" + input)) - keyboard.getPressed(Options.controls.get("LEFT" + input)));
			player.setDown(keyboard.isPressed(Options.controls.get("DOWN" + input)));
			player.setInteracting(keyboard.isPressed(Options.controls.get("INTERACT" + input)));
			player.setAttacking(keyboard.isPressed(Options.controls.get("ATTACK" + input)));
		}
	}

	private void cleanUp() {
		Options.save();
	}

	public boolean setGameMap(String name, boolean fade) {
		if (newMap == null) {
			newMap = name;

			if (fade) {
				this.addGameObject(new Fade());
				fadeStart = gameTick;
			} else {
				fadeStart = gameTick - Constants.FADE_TIME;
			}

			return true;
		}

		return false;
	}

	public void addGameObject(GameObject gameObject) {
		if (!toAdd.contains(gameObject) && !gameObjects.contains(gameObject)) toAdd.add(gameObject);
	}

	public void removeGameObject(GameObject gameObject) {
		if (!toRemove.contains(gameObject) && gameObjects.contains(gameObject)) toRemove.add(gameObject);
		if (gameObject instanceof Player) {
			Player p = (Player) gameObject;
			this.addGameObject(new DeadBody(p.getHitBox().x, p.getHitBox().y, "player"));
		}
	}

	public List<CollisionObject> getCollisionObjects() {
		return collisionObjects;
	}

	public Camera getCamera() {
		return window.getCamera();
	}

	public float getAspectRatio() {
		return window.getAspectRatio();
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getGameTick() {
		return gameTick;
	}

	public int getValue(String key) {
		return values.getOrDefault(key, 0);
	}

	public int getKeyAmount(String key, int... value) {
		String[] keySet = values.keySet().toArray(new String[0]);
		int amount = 0;
		for (String s: keySet) {
			if (s.contains(key)) {
				if (value.length == 0) {
					amount++;
				} else {
					for (int v : value) {
						if (v == values.get(s)) amount++;
					}
				}
			}
		}
		return amount;
	}

	public void setValue(String key, int value) {
		values.put(key, value);
	}

	public boolean hasAbility(Ability ability) {
		return abilities.contains(ability);
	}

	public void addAbility(Ability ability) {
		if (!abilities.contains(ability)) abilities.add(ability);
	}
}
