package game.gamemap;

import game.Constants;
import game.Game;
import game.data.hitbox.HitBox;
import game.data.script.Parser;
import game.data.script.Tree;
import game.gameobjects.gameobjects.Text;
import game.gameobjects.gameobjects.cameracontroller.Area;
import game.gameobjects.gameobjects.entities.entities.*;
import game.gameobjects.gameobjects.wall.Background;
import game.gameobjects.gameobjects.wall.Wall;
import game.util.ErrorUtil;
import game.util.FileHandler;
import game.util.TextureHandler;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MapLoader {


	public static GameMap load(Game g, String mapName) {
		if (mapName.equals(Constants.SYS_PREFIX + "lobby")) return createLobby(g, getMaps().toArray(new String[0]));
		if (mapName.equals(Constants.SYS_PREFIX + "options")) return createLobby(g, "menu");

		if (!FileHandler.fileExists("maps/" + mapName + ".map")) {
			GameMap map = load(g, Constants.SYS_PREFIX + "lobby");
			Text text = new Text(-100, "Something went wrong. We send you back to Menu", -0.9f, -0.9f, 0.05f, false, 0, 0, Color.RED);
			text.setTimer(300);
			map.addGameObject(text);
			return map;
		}

		GameMap map = new GameMap();
		Map<Integer, String> textureReplacements = new HashMap<>();
		Map<Float, Map<HitBox, String>> layers = new HashMap<>();

		Scanner fileScanner = new Scanner(FileHandler.loadFile("maps/" + mapName + ".map"));
		Constants.PIXEL_PER_TILE = Integer.valueOf(fileScanner.nextLine());
		float tileSize = Constants.PIXEL_PER_TILE;

		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			line = line.replaceAll(" ", "");

			if (line.startsWith("#")) {
				String[] values = line.substring("#".length()).split("-");

				int key = Integer.parseInt(values[0]);
				if (!values[1].startsWith("textures_")) ErrorUtil.printError("No such image: " + values[1]);
				String textureName = values[1].substring("textures_".length());

				textureReplacements.put(key, textureName);
			}

			if (line.startsWith("[layer;")) {
				String[] values = line.substring("[layer;".length(), line.length() - 1).split(";");

				float drawingPriority = Float.parseFloat(values[0]);
				int width = Integer.parseInt(values[1]);
				int height = Integer.parseInt(values[2]);

				Map<HitBox, String> hitBoxList;
				if (layers.containsKey(drawingPriority)) {
					hitBoxList = layers.get(drawingPriority);
				} else {
					hitBoxList = new HashMap<>();
					layers.put(drawingPriority, hitBoxList);
				}

				for (int x = 0; x < width; x++) {
					String[] valuesLine = values[3 + x].split(",");
					for (int y = 0; y < height; y++) {
						int tile = Integer.parseInt(valuesLine[y]);

						if (tile != 0) {
							String texture = textureReplacements.get(tile);
							Rectangle textureBounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);

							HitBox hitBox = new HitBox(x, -y - textureBounds.height / tileSize, textureBounds.width / tileSize, textureBounds.height / tileSize);

							switch (texture) {
								case "platform":
								case "platform_left":
								case "platform_middle":
								case "platform_right":
									hitBox.type = HitBox.HitBoxType.HALF_BLOCKING;
									break;

							}

							hitBoxList.put(hitBox, texture);
						}
					}
				}
			}


			if (line.startsWith("[put;")) {
				String[] values = line.substring("[put;".length(), line.length() - 1).replaceAll("\\[", "").replaceAll("]", "").split(";");

				float drawingPriority = Float.parseFloat(values[0]);
				String texture = textureReplacements.get(Integer.parseInt(values[1]));
				Rectangle textureBounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);
				float x = Float.parseFloat(values[2]);
				float y = -Float.parseFloat(values[3]) - textureBounds.height / tileSize;

				Map<String, String> tags = new HashMap<>();

				int i = 4;
				while (i < values.length) {
					if (values[i].equals("tag")) {
						tags.put(values[i + 1], values[i + 2]);
						i++;
						i++;
					}

					i++;
				}

				switch (texture) {
					case "player_r_idle_0":
					case "player_r_idle_1":
					case "player_r_move_0":
					case "player_r_move_1":
					case "player_r_move_2":
					case "player_r_move_3":
					case "player_r_fall":
					case "player_r_sword_0":
					case "player_r_sword_1":
					case "player_r_sword_2":
					case "player_r_sword_3":
					case "player_r_sword_4":
					case "player_r_sword_5":
					case "player_r_sword_6":
					case "player_l_idle_0":
					case "player_l_idle_1":
					case "player_l_move_0":
					case "player_l_move_1":
					case "player_l_move_2":
					case "player_l_move_3":
					case "player_l_fall":
					case "player_l_sword_0":
					case "player_l_sword_1":
					case "player_l_sword_2":
					case "player_l_sword_3":
					case "player_l_sword_4":
					case "player_l_sword_5":
					case "player_l_sword_6":
						map.setSpawnPoint(x, y, drawingPriority);
						map.getCameraController().setSpawn(x, y);
						break;
					case "coin":
						String coinID = String.format("%s_coin_%f_%f", mapName, x, y);
						if (g.getValue(coinID) == 0) g.setValue(coinID, 0);
						map.addGameObject(new Coin(x, y, drawingPriority, g.getValue(coinID) > 0, new Tree((tree, game) -> {
							game.setValue(coinID, 1);                                            //Mark this coin as collected
							game.setValue("coins", game.getValue("coins") + 1);            //Raise the total amount of collected coins
							return null;
						}), null));
						break;
					case "door_side":
					case "door_side_open_0":
					case "door_side_open_1":
					case "door_side_open":
						map.addGameObject(new Exit(x, y, drawingPriority, tags.getOrDefault("target", Constants.SYS_PREFIX + "lobby")));
						break;
					case "lantern":
						map.addGameObject(new Lantern(x, y, drawingPriority));
						break;
					case "box":
						map.addGameObject(new Box(x, y, drawingPriority));
						break;
					case "spikes_bot_blood":
						map.addGameObject(new Spikes(x, y, drawingPriority));
						break;
					case "lever_left":
					case "lever_right":
					case "lever_middle":
						String tag = tags.getOrDefault("tag", "lever");
						map.addGameObject(new Lever(x, y, drawingPriority, g.getValue(tag) > 0, Parser.loadScript(Parser.COMMAND, String.format("#%s=(#%s+1);", tag, tag)), Parser.loadScript(Parser.COMMAND, String.format("#%s=(#%s-1);", tag, tag)), null));
						break;
					case "door_6":
					case "door_5":
					case "door_4":
					case "door_3":
					case "door_2":
					case "door_1":
						map.addGameObject(new Door(x, y, drawingPriority, Parser.loadScript(Parser.BOOLEAN, tags.getOrDefault("condition", "#lever"))));
						break;
					default:
						HitBox hitBox = new HitBox(x, y, textureBounds.width / tileSize, textureBounds.height / tileSize);

						switch (texture) {
							case "platform":
							case "platform_left":
							case "platform_middle":
							case "platform_right":
								hitBox.type = HitBox.HitBoxType.HALF_BLOCKING;
								break;

						}

						add(layers, hitBox, texture, drawingPriority);
				}
			}

			if (line.startsWith("[area;")) {
				String[] values = line.substring("[area;".length(), line.length() - 1).replaceAll("\\[", "").replaceAll("]", "").split(";");

				float x1 = Float.parseFloat(values[0]);
				float y2 = -Float.parseFloat(values[1]);
				float x2 = Float.parseFloat(values[2]);
				float y1 = -Float.parseFloat(values[3]);

				Map<String, String> tags = new HashMap<>();

				int i = 4;
				while (i < values.length) {
					if (values[i].equals("tag")) {
						tags.put(values[i + 1], values[i + 2]);
						i++;
						i++;
					}

					i++;
				}

				map.getCameraController().addCameraArea(new Area(x1, y1, x2, y2));
			}
		}

		for (float drawingPriority : layers.keySet()) {
			Map<HitBox, String> layer = layers.get(drawingPriority);
			if (drawingPriority <= 0.55 && drawingPriority >= 0.45) map.addGameObject(new Wall(layer, drawingPriority));
			else map.addGameObject(new Background(layer, drawingPriority));

		}
		return map;
	}

	private static GameMap createLobby(Game g, String... mapNames) {
		GameMap map = new GameMap();
		Map<Float, Map<HitBox, String>> layers = new HashMap<>();

		{
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 8; k++) {
					add(layers, new HitBox(j, k, 1f, 1f), "block_stone_middle", 1);
					add(layers, new HitBox(j, k, 1f, 1f), "black_5", 0.75f);
				}

				add(layers, new HitBox(j, 0, 1f, 1f), "block_stone_middle", 0.5f);
				if (j < 6 && j > 2) {
					add(layers, new HitBox(j, 3, 1f, 1f), "block_wood_middle", 0.5f);
					map.addGameObject(new Ladder(6, j - 2, 0.7f));
				}
			}
			map.addGameObject(new Exit(4, 4, 0.7f, Constants.SYS_PREFIX + "options"));
			map.addGameObject(new Text(0.7f, "options", 4.5f, 6, 0.5f, true, 0.5f, 0));
			map.addGameObject(new Lantern(2, 1, 0.7f));
			map.getCameraController().addCameraArea(new Area(0, -2, 9, 9));
		}

		for (int i = 1; i <= mapNames.length; i++) {
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 8; k++) {
					add(layers, new HitBox(i * 9 + j, k, 1f, 1f), "block_stone_middle", 1);
					add(layers, new HitBox(i * 9 + j, k, 1f, 1f), "black_5", 0.75f);
				}

				add(layers, new HitBox(i * 9 + j, 0, 1f, 1f), "block_stone_middle", 0.5f);
				if (j < 6 && j > 2) {
					add(layers, new HitBox(i * 9 + j, 3, 1f, 1f), "block_wood_middle", 0.5f);
					map.addGameObject(new Ladder(i * 9 + 6, j - 2, 0.7f));
				}
			}
			map.addGameObject(new Exit(i * 9 + 4, 4, 0.7f, mapNames[i - 1]));
			map.addGameObject(new Text(0.7f, mapNames[i - 1], i * 9 + 4.5f, 6, 0.5f, true, 0.5f, 0));
			map.addGameObject(new Text(0.7f, String.valueOf(g.getKeyAmount(mapNames[i - 1] + "_coin_", 1)) + "/" + String.valueOf(g.getKeyAmount(mapNames[i - 1] + "_coin_")), i * 9 + 4.5f, 7, 0.5f, true, 0.5f, 0));
			map.addGameObject(new Lantern(i * 9 + 2, 1, 0.7f));
			map.getCameraController().addCameraArea(new Area(i * 9, -2, i * 9 + 9, 9));
		}
		map.setSpawnPoint(0, 1, 0.5f);

		for (float drawingPriority : layers.keySet()) {
			Map<HitBox, String> layer = layers.get(drawingPriority);
			if (drawingPriority <= 0.55 && drawingPriority >= 0.45) map.addGameObject(new Wall(layer, drawingPriority));
			else map.addGameObject(new Background(layer, drawingPriority));

		}

		return map;
	}

	private static void add(Map<Float, Map<HitBox, String>> layers, HitBox hitBox, String texture, float drawingPriority) {
		if (layers.containsKey(drawingPriority)) {
			layers.get(drawingPriority).put(hitBox, texture);
		} else {
			Map<HitBox, String> layer = new HashMap<>();
			layer.put(hitBox, texture);
			layers.put(drawingPriority, layer);
		}
	}

	private static java.util.List<String> getMaps() {
		File folder = new File("src/res/files/maps");
		File[] listOfFiles = folder.listFiles();
		java.util.List<String> maps = new ArrayList<>();

		for (File f : listOfFiles) {
			if (f.isFile() && f.getName().endsWith(".map")) {
				maps.add(f.getName().replaceAll(".map", ""));
			}
		}
		return maps;
	}
}
