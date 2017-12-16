package game.util;

public class WaitUtil {

	public static void sleep(int time) {
		try {
			Thread.sleep(Math.max(0, time));
		} catch(Exception e) {
			ErrorUtil.printError("Error while sleeping ???");
		}
	}
}
