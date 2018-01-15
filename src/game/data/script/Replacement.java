package game.data.script;

public class Replacement {
	private char key;
	private String replacement;
	private int childs;
	private Tree.TreeValue value;
	private boolean constant;

	public Replacement(char key, String replacement, int childs, Tree.TreeValue value, boolean constant) {
		this.key = key;
		this.replacement = replacement;
		this.childs = childs;
		this.value = value;
		this.constant = constant;
	}

	public boolean isConstant() {
		return constant;
	}

	public char getKey() {
		return key;
	}

	public String getReplacement() {
		return replacement;
	}

	public int getChilds() {
		return childs;
	}

	public Tree.TreeValue getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key + " -> " + replacement;
	}
}
