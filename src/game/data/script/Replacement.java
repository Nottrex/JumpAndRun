package game.data.script;

/**
 * A replacement in the grammar of our language
 */
public class Replacement {
	private char key;					//The key of the replacement (non terminal)
	private String replacement;			//The result of the replacement
	private int childs;					//The amount of non terminals in the replacement string
	private Tree.TreeValue value;		//The function associated with this replacement
	private boolean constant;			//Whether this function is constant given constant descending functions

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
