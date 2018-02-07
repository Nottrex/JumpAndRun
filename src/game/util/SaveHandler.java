package game.util;

import game.Constants;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SaveHandler {
	public static String[] getSaves() {
		File[] listOfFiles = new File("src/res/files/saves").listFiles();

		java.util.List<String> saves = new ArrayList<>();

		for (File f : listOfFiles) {
			if (f.isFile() && f.getName().endsWith(".save")) {
				saves.add(f.getName().replaceAll(".save", ""));
			}
		}
		return saves.toArray(new String[0]);
	}

	public static Map<String, Integer> readSave(String saveName) {
		Map<String, Integer> values = new HashMap<>();

		try {
			Scanner scanner = new Scanner(new File("src/res/files/saves/" + saveName + ".save"));
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("~");
				values.put(line[0], Integer.parseInt(line[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	public static void writeSave(Map<String, Integer> values, String saveName) {
		try {
			PrintWriter writer = new PrintWriter(new File("src/res/files/saves/" + saveName + ".save"));
			for (String key: values.keySet()) {
				writer.println(key + "~" + values.get(key));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
