package game.util;

import game.Constants;

import java.util.Date;

public class ErrorUtil {

	/**
	 * Prints a formatted error to the console and exits the program
	 *
	 * @param error the error to be printed
	 */
	public static void printError(String error) {
		Constants.ERR_STREAM.printf("[%s] Error: %s\n", Constants.FORMAT.format(new Date()), error);
		System.exit(-1);
	}
}
