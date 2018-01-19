package game.gamemap;

import game.Ability;
import game.Game;

public class TagListener {
	public static void saveOptions() {

	}

	public static void saveAbilities(Game game) {
		for (Ability a: Ability.values()) {
			if (game.getValue(a.name()) > 0 && game.getValue("coins") >= a.getCost()) {
				game.addAbility(a);
				game.setValue("coins", game.getValue("coins") - a.getCost());
			}
		}
	}
}
