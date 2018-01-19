package game.data.script;

import game.Game;

public class Tree {

	private Tree[] childs;
	private TreeValue value;
	private int childAmount;
	private Tree parent;
	private boolean constant;

	public Tree(Tree parent, int totalChildAmount, TreeValue value, boolean constant) {
		this.childs = new Tree[totalChildAmount];
		this.value = value;
		this.childAmount = 0;
		this.parent = parent;
		this.constant = constant;
	}

	public Tree(TreeValue value) {
		this.childs = new Tree[0];
		this.value = value;
		this.childAmount = 0;
		this.parent = null;
		this.constant = false;
	}

	public Tree getChild(int id) {
		return childs[id];
	}

	public void addChild(Tree tree) {
		childs[childAmount] = tree;
		childAmount++;
	}

	public void setChild(int index, Tree tree) {
		childs[index] = tree;
	}

	public int getSize() {
		int size = 0;
		for (Tree child : childs) {
			size += child.getSize();
		}
		return 1 + size;
	}

	public int getDepth() {
		int depth = 0;
		for (Tree child: childs) {
			depth = Math.max(depth, child.getDepth());
		}
		return depth+1;
	}

	public boolean isConstant() {
		boolean constantC = true;
		for (Tree c: childs) {
			constantC = constantC && c.isConstant();
		}
		return constant && constantC;
	}

	public Tree getParent() {
		return parent;
	}

	public int getTotalChildAmount() {
		return childs.length;
	}

	public int getChildAmount() {
		return childAmount;
	}

	public Object get(Game game) {
		return value.get(this, game);
	}

	public interface TreeValue {
		Object get(Tree tree, Game game);
	}
}
