package game.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorUtil {
	private static final PrintStream ERR_STREAM = System.err;
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");

	public static void printError(String error) {
		ERR_STREAM.printf("[%s] Error: %s\n", FORMAT.format(new Date()), error);
		System.exit(-1);
	}
}
