package game.util;

import java.io.PrintStream;

public class ErrorUtil {
	private static final PrintStream ERR_STREAM = System.err;

	public static void printError(String error) {
		ERR_STREAM.printf("Error: %s", error);
		System.exit(-1);
	}
}
