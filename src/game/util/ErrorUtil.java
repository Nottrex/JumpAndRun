package game.util;

import game.Constants;

import java.util.Date;

public class ErrorUtil {

	public static void printError(String error) {
		Constants.ERR_STREAM.printf("[%s] Error: %s\n", Constants.FORMAT.format(new Date()), error);
		System.exit(-1);
	}
}
