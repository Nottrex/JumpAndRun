package game.data.script;

import game.Game;

/**
 * Implementation of a tree structure
 */
public class Tree {

	private Tree[] childs;		//child nodes of this node
	private TreeValue value;	//replacement used in this step
	private int childAmount;	//amount of child nodes
	private Tree parent;		//parent node or null if there is none
	private boolean constant;	//if the value of this tree and all of its child nodes is constant

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

	/**
	 * @param id of the child node
	 * @return returns the child node with the given id
	 */
	public Tree getChild(int id) {
		return childs[id];
	}

	/**
	 * adds a child node to this node
	 * @param tree the node to be added
	 */
	public void addChild(Tree tree) {
		childs[childAmount] = tree;
		childAmount++;
	}

	/**
	 * replaces a child node at the given index by this new one
	 * @param index of the node to be replaced
	 * @param tree the new node to be added
	 */
	public void setChild(int index, Tree tree) {
		childs[index] = tree;
	}

	/**
	 * @return calculates the total amount of nodes below this node
	 */
	public int getSize() {
		int size = 0;
		for (Tree child : childs) {
			size += child.getSize();
		}
		return 1 + size;
	}

	public int getDepth() {
		int depth = 0;
		for (Tree child : childs) {
			depth = Math.max(depth, child.getDepth());
		}
		return depth + 1;
	}

	/**
	 * @return if the value for this and all following nodes is constant
	 */
	public boolean isConstant() {
		boolean constantC = true;
		for (Tree c : childs) {
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

	/**
	 * @param game the game context
	 * @return the result of the node operation
	 */
	public Object get(Game game) {
		return value.get(this, game);
	}

	public interface TreeValue {
		Object get(Tree tree, Game game);
	}
}
