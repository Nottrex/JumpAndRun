package game.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Util class used to store save data of the game
 */
public class SaveHandler {
	private static String saveDirectory = System.getProperty("user.dir") + File.separator + "saves";

	static {
		new File(saveDirectory).mkdir();
	}

	/**
	 * returns saves found in the save directory
	 * @return the saves by mapping the save slot to the full name of the save
	 */
	public static Map<Integer, String> getSaves() {
		File[] listOfFiles = new File(saveDirectory).listFiles();

		Map<Integer, String> saves = new HashMap<>();

		if (listOfFiles == null) return saves;
		for (File f : listOfFiles) {
			if (f.isFile() && f.getName().endsWith(".save")) {
				saves.put(Integer.valueOf(f.getName().split("-")[0]), f.getName().split("-")[1].replaceAll(".save", ""));
			}
		}
		return saves;
	}

	/**
	 * Reads a save from the disk and returns the content of the save game
	 * @param saveName the name of the save
	 * @return all values set for the loaded save game
	 */
	public static Map<String, Integer> readSave(String saveName) {
		Map<String, Integer> values = new HashMap<>();

		try {
			Scanner scanner = new Scanner(new File(saveDirectory + File.separator + saveName + ".save"));
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("~");
				values.put(line[0], Integer.parseInt(line[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	/**
	 * Saves a game o a given save file
	 * @param values all the values set for the current game
	 * @param saveName the name of the save
	 */
	public static void writeSave(Map<String, Integer> values, String saveName) {
		File[] oldSave = new File(saveDirectory).listFiles(pathname -> pathname.getName().startsWith(saveName.substring(0, 2)) && pathname.getName().endsWith(".save"));
		if (oldSave != null) for (File file : oldSave) file.delete();

		try {
			PrintWriter writer = new PrintWriter(new File(saveDirectory + File.separator + saveName + ".save"));
			for (String key : values.keySet()) {
				writer.println(key + "~" + values.get(key));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
