package game.data.script;

import game.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Translate a given text into a tree by breaking it up into the allowed replacements
 */
public class Parser {
	private static final Parser PARSER;

	//Non Terminals:
	public static final char BOOLEAN = 'α';
	public static final char NUMBER = 'β';
	public static final char NUMBER_ = 'η';
	public static final char VAR = 'γ';
	public static final char COMMAND = 'δ';
	public static final char COMMAND_BLOCK = 'ε';
	public static final char COMMAND_BLOCK_NON_NULL = 'ζ';

	static {
		//Allowed replacements:
		List<Replacement> replacements = new ArrayList<>();
		replacements.add(new Replacement(BOOLEAN, "!" + BOOLEAN, 1, (t, g) -> !(boolean) t.getChild(0).get(g), true));
		replacements.add(new Replacement(BOOLEAN, "(" + BOOLEAN + ")", 1, (t, g) -> t.getChild(0).get(g), true));
		replacements.add(new Replacement(BOOLEAN, "(" + BOOLEAN + "||" + BOOLEAN + ")", 2, (t, g) -> ((boolean) t.getChild(0).get(g)) || ((boolean) t.getChild(1).get(g)), true));
		replacements.add(new Replacement(BOOLEAN, "(" + BOOLEAN + "&&" + BOOLEAN + ")", 2, (t, g) -> ((boolean) t.getChild(0).get(g)) && ((boolean) t.getChild(1).get(g)), true));
		replacements.add(new Replacement(BOOLEAN, "true", 0, (t, g) -> true, true));
		replacements.add(new Replacement(BOOLEAN, "True", 0, (t, g) -> true, true));
		replacements.add(new Replacement(BOOLEAN, "false", 0, (t, g) -> false, true));
		replacements.add(new Replacement(BOOLEAN, "False", 0, (t, g) -> false, true));
		replacements.add(new Replacement(BOOLEAN, NUMBER + "", 1, (t, g) -> (Integer) t.getChild(0).get(g) > 0, true));
		replacements.add(new Replacement(BOOLEAN, NUMBER + ">" + NUMBER, 2, (t, g) -> (Integer) t.getChild(0).get(g) > (Integer) t.getChild(1).get(g), true));
		replacements.add(new Replacement(BOOLEAN, NUMBER + "<" + NUMBER, 2, (t, g) -> (Integer) t.getChild(0).get(g) < (Integer) t.getChild(1).get(g), true));
		replacements.add(new Replacement(BOOLEAN, NUMBER + "<=" + NUMBER, 2, (t, g) -> (Integer) t.getChild(0).get(g) <= (Integer) t.getChild(1).get(g), true));
		replacements.add(new Replacement(BOOLEAN, NUMBER + ">=" + NUMBER, 2, (t, g) -> (Integer) t.getChild(0).get(g) >= (Integer) t.getChild(1).get(g), true));
		replacements.add(new Replacement(BOOLEAN, NUMBER + "==" + NUMBER, 2, (t, g) -> t.getChild(0).get(g) == t.getChild(1).get(g), true));

		for (final char c : "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789".toCharArray()) {
			replacements.add(new Replacement(VAR, "" + c, 0, (t, g) -> "" + c, true));
			replacements.add(new Replacement(VAR, c + "" + VAR, 1, (t, g) -> c + ((String) t.getChild(0).get(g)), true));
		}

		for (final char c : "0123456789".toCharArray()) {
			replacements.add(new Replacement(NUMBER_, "" + c, 0, (t, g) -> "" + c, true));
			replacements.add(new Replacement(NUMBER_, c + "" + VAR, 1, (t, g) -> c + ((String) t.getChild(0).get(g)), true));
		}
		replacements.add(new Replacement(NUMBER, "#" + VAR, 1, (t, g) -> g.getValue((String) t.getChild(0).get(g)), false));
		replacements.add(new Replacement(NUMBER, "" + NUMBER_, 1, (t, g) -> Integer.valueOf((String) t.getChild(0).get(g)), true));
		replacements.add(new Replacement(NUMBER, "(" + NUMBER + "+" + NUMBER + ")", 2, (t, g) -> (int) t.getChild(0).get(g) + (int) t.getChild(1).get(g), true));
		replacements.add(new Replacement(NUMBER, "(" + NUMBER + "-" + NUMBER + ")", 2, (t, g) -> (int) t.getChild(0).get(g) - (int) t.getChild(1).get(g), true));
		replacements.add(new Replacement(NUMBER, "(" + NUMBER + "*" + NUMBER + ")", 2, (t, g) -> (int) t.getChild(0).get(g) * (int) t.getChild(1).get(g), true));
		replacements.add(new Replacement(NUMBER, "(" + NUMBER + "/" + NUMBER + ")", 2, (t, g) -> (int) t.getChild(0).get(g) / (int) t.getChild(1).get(g), true));

		replacements.add(new Replacement(COMMAND, "#" + VAR + "=" + NUMBER + ";", 2, (t, g) -> {
			g.setValue((String) t.getChild(0).get(g), (int) t.getChild(1).get(g));
			return null;
		}, false));
		replacements.add(new Replacement(COMMAND, "if(" + BOOLEAN + "){" + COMMAND_BLOCK + "}", 2, (t, g) -> {
			if ((boolean) t.getChild(0).get(g)) {
				t.getChild(1).get(g);
			}
			return null;
		}, true));
		replacements.add(new Replacement(COMMAND, "while(" + BOOLEAN + "){" + COMMAND_BLOCK + "}", 2, (t, g) -> {
			while ((boolean) t.getChild(0).get(g)) {
				t.getChild(1).get(g);
			}
			return null;
		}, true));

		replacements.add(new Replacement(COMMAND_BLOCK_NON_NULL, COMMAND + "", 1, (t, g) -> t.getChild(0).get(g), true));
		replacements.add(new Replacement(COMMAND_BLOCK_NON_NULL, COMMAND + "" + COMMAND_BLOCK_NON_NULL, 2, (t, g) -> {
			t.getChild(0).get(g);
			return t.getChild(1).get(g);
		}, true));

		replacements.add(new Replacement(COMMAND_BLOCK, COMMAND_BLOCK_NON_NULL + "", 1, (t, g) -> t.getChild(0).get(g), true));
		replacements.add(new Replacement(COMMAND_BLOCK, "", 0, (t, g) -> null, true));

		PARSER = new Parser(replacements);
	}


	private List<Replacement> replacements;

	public Parser(List<Replacement> replacements) {
		this.replacements = replacements;
	}

	/**
	 * generates a list of replacements
	 * replacing the first occurrence of a non terminal for all the replacements in the list results in the value
	 * @param startSymbol the startValue to be matched to the value
	 * @param value to be parsed
	 * @return a list of replacements
	 */
	public List<Replacement> parse(String startSymbol, String value) {
		while (!startSymbol.isEmpty() && !value.isEmpty() && isTerminal(startSymbol.charAt(0)) && value.charAt(0) == startSymbol.charAt(0)) {
			startSymbol = startSymbol.substring(1);
			value = value.substring(1);
		}

		if (startSymbol.isEmpty()) {
			if (value.isEmpty()) return new ArrayList<>();
			else return null;
		}

		if (isTerminal(startSymbol.charAt(0))) return null;
		else {
			for (Replacement r : getReplacementsForKey(startSymbol.charAt(0))) {
				List<Replacement> rep = parse(r.getReplacement() + startSymbol.substring(1), value);
				if (rep != null) {
					rep.add(r);
					return rep;
				}
			}
		}

		return null;
	}

	/**
	 * @param key a non terminal character
	 * @return all replacements replacing this key by a new value
	 */
	private List<Replacement> getReplacementsForKey(char key) {
		return replacements.stream().filter(r -> r.getKey() == key).collect(Collectors.toList());
	}

	/**
	 * @param c the character
	 * @return if a given character is a terminal character
	 */
	private boolean isTerminal(char c) {
		for (Replacement r : replacements) {
			if (r.getKey() == c) {
				return false;
			}
		}
		return true;
	}

	/**
	 * converts a list of replacements into a tree structure
	 * @param list the list of replacements generated by parse
	 * @return a tree representing the script
	 */
	public Tree toTree(List<Replacement> list) {
		Stack<Replacement> replacementStack = new Stack<>();
		for (Replacement r : list) {
			replacementStack.push(r);
		}

		if (replacementStack.isEmpty()) return null;

		Replacement r = replacementStack.pop();
		Tree tree = new Tree(null, r.getChilds(), r.getValue(), r.isConstant());

		while (!replacementStack.isEmpty()) {
			Tree missing = tree;

			while (missing.getChildAmount() > 0) {
				missing = missing.getChild(missing.getChildAmount() - 1);
			}

			while (missing.getChildAmount() == missing.getTotalChildAmount()) {
				missing = missing.getParent();
			}

			r = replacementStack.pop();
			missing.addChild(new Tree(missing, r.getChilds(), r.getValue(), r.isConstant()));
		}

		return tree;
	}

	/**
	 * parsing a simple string like abcdef results in a very large tree: a-b-c-d-e-f
	 * this function merges constant values into one -> abcdef
	 * @param tree the tree to optimize
	 * @return an optimized tree
	 */
	public Tree optimizeTree(Tree tree) {
		if (tree.isConstant()) {
			final Object value = tree.get(null);
			return new Tree(null, 0, (t, game) -> value, true);
		} else {
			for (int i = 0; i < tree.getChildAmount(); i++) {
				tree.setChild(i, optimizeTree(tree.getChild(i)));
			}
			return tree;
		}
	}

	/**
	 * parses and optimizes the given text for a given start symbol
	 * @param start the non terminal start character e.g. Parser.BOOLEAN
	 * @param value the string to be parsed
	 * @return a tree representing the loaded script
	 */
	public static Tree loadScript(char start, String value) {

		try {
			value = value.replaceAll("\n", "").replaceAll(" ", "");

			return PARSER.optimizeTree(PARSER.toTree(PARSER.parse(start + "", value)));
		} catch (Exception e) {
			ErrorUtil.printError("Loading Script: " + value + " as " + start);
			return null;
		}
	}
}
