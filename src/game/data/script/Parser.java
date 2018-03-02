package game.data.script;

import game.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Parser {
	private static final Parser PARSER;
	public static final char BOOLEAN = 'α';
	public static final char NUMBER = 'β';
	public static final char NUMBER_ = 'η';
	public static final char VAR = 'γ';
	public static final char COMMAND = 'δ';
	public static final char COMMAND_BLOCK = 'ε';
	public static final char COMMAND_BLOCK_NON_NULL = 'ζ';

	static {
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


	List<Replacement> replacements;

	public Parser(List<Replacement> replacements) {
		this.replacements = replacements;
	}

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

	private List<Replacement> getReplacementsForKey(char key) {
		return replacements.stream().filter(r -> r.getKey() == key).collect(Collectors.toList());
	}

	private boolean isTerminal(char c) {
		for (Replacement r : replacements) {
			if (r.getKey() == c) {
				return false;
			}
		}
		return true;
	}

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
