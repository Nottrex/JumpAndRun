package game.util;

import game.GameMap;
import game.data.HitBox;
import game.gameobjects.gameobjects.Area;
import game.gameobjects.gameobjects.entities.entities.*;
import game.gameobjects.gameobjects.wall.Background;
import game.gameobjects.gameobjects.wall.Wall;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class MapLoader {


	public static GameMap load(String mapName) {
		GameMap map = new GameMap();
		Map<Integer, String> textureReplacements = new HashMap<>();
		Map<Float, List<Pair<HitBox, String>>> layers = new HashMap<>();

		Scanner fileScanner = new Scanner(FileHandler.loadFile("maps/" + mapName + ".map"));
		float tileSize = Integer.valueOf(fileScanner.nextLine());

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

				List<Pair<HitBox, String>> hitBoxList;
				if (layers.containsKey(drawingPriority)) {
					hitBoxList = layers.get(drawingPriority);
				} else {
					hitBoxList = new ArrayList<>();
					layers.put(drawingPriority, hitBoxList);
				}

				for (int x = 0; x < width; x++) {
					String[] valuesLine = values[3 + x].split(",");
					for (int y = 0; y < height; y++) {
						int tile = Integer.parseInt(valuesLine[y]);

						if (tile != 0) {
							String texture = textureReplacements.get(tile);
							Rectangle textureBounds = TextureHandler.getSpriteSheetBounds("textures_" + texture);

							hitBoxList.add(new Pair<>(new HitBox(x, -y - textureBounds.height/tileSize, textureBounds.width / tileSize, textureBounds.height / tileSize), texture));
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
				float y = -Float.parseFloat(values[3]) - textureBounds.height/tileSize;

				Map<String, String> tags = new HashMap<>();

				int i = 4;
				while (i < values.length) {
					if (values[i].equals("tag")) {
						tags.put(values[i+1], values[i+2]);
						i++;
						i++;
					}

					i++;
				}

				switch (texture) {
					case "player_r_idle_0":	case "player_r_idle_1":	case "player_r_move_0":	case "player_r_move_1": case "player_r_move_2":   case "player_r_move_3":   case "player_r_fall":   case "player_r_sword_0":   case "player_r_sword_1":   case "player_r_sword_2":   case "player_r_sword_3":   case "player_r_sword_4":   case "player_r_sword_5":   case "player_r_sword_6":   case "player_l_idle_0":   case "player_l_idle_1":   case "player_l_move_0":   case "player_l_move_1":   case "player_l_move_2":   case "player_l_move_3":   case "player_l_fall":   case "player_l_sword_0":   case "player_l_sword_1":   case "player_l_sword_2":   case "player_l_sword_3":   case "player_l_sword_4":   case "player_l_sword_5":   case "player_l_sword_6":
						map.setSpawnPoint(x, y, drawingPriority);
						map.getCameraController().setSpawn(x, y);
						break;
					case "coin":
						map.addGameObject(new Coin(x, y, drawingPriority));
						break;
					case "door_side": case "door_side_open_0": case "door_side_open_1": case "door_side_open":
						map.addGameObject(new Door(x, y, drawingPriority, tags.getOrDefault("target", "test2")));
						break;
					case "lantern":
						map.addGameObject(new Lantern(x, y, drawingPriority));
						break;
					case "box":
						map.addGameObject(new Box(x, y, drawingPriority));
						break;
					case "spikes_bot":
						map.addGameObject(new Spikes(x, y, drawingPriority));
						break;
					default:
						if (layers.containsKey(drawingPriority)) {
							layers.get(drawingPriority).add(new Pair<>(new HitBox(x, y, textureBounds.width/tileSize, textureBounds.height/tileSize), texture));
						} else {
							List<Pair<HitBox, String>> layer = new ArrayList<>();
							layer.add(new Pair<>(new HitBox(x, y, textureBounds.width/tileSize, textureBounds.height/tileSize), texture));
							layers.put(drawingPriority, layer);
						}
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
						tags.put(values[i+1], values[i+2]);
						i++;
						i++;
					}

					i++;
				}

				map.getCameraController().addCameraArea(new Area(x1, y1, x2, y2));
			}
		}

		for (float drawingPriority: layers.keySet()) {
			List<Pair<HitBox, String>> layer = layers.get(drawingPriority);

			if (drawingPriority <= 0.55 && drawingPriority >= 0.45) map.addGameObject(new Wall(layer, drawingPriority));
			else map.addGameObject(new Background(layer, drawingPriority));

		}
		return map;
	}
}
