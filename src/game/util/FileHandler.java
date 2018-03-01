package game.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class FileHandler {

	/**
	 * Reads a text file from the jar in the package src/res/files/
	 * @param fileName the filename in the package
	 * @return the text of the file
	 */
	public static String loadFile(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("res/files/" + fileName)));

			StringBuilder source = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				source.append(line).append("\n");
			}
			reader.close();

			return source.toString();
		} catch (Exception e) {
			ErrorUtil.printError(String.format("Loading file: %s", fileName));
		}
		return null;
	}

	/**
	 * Checks if a file exists in the jar in the package src/res/files/
	 * @param fileName the filename in the package
	 * @return if the file is present
	 */
	public static boolean fileExists(String fileName) {
		File file = new File("src/res/files/" + fileName);
		return file.exists();
	}
}
