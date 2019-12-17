// Represents Node of Red Black Tree
public class RedBlackNode {

/*
 * RedBlackTree Node with Fields 
 * Building, Left, Right, Parent and Color
 */
	private Building bld;
	private RedBlackNode left;
	private RedBlackNode right;
	private RedBlackNode parent;
	private char color;

	public RedBlackNode() {
		this.bld = null;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.color = 'b';
	}

	public RedBlackNode(Building bld) {
		this.bld = bld;
	}

	public Building getBld() {
		return bld;
	}

	public void setBld(Building bld) {
		this.bld = bld;
	}

	public RedBlackNode getLeft() {
		return left;
	}

	public void setLeft(RedBlackNode left) {
		this.left = left;
	}

	public RedBlackNode getRight() {
		return right;
	}

	public void setRight(RedBlackNode right) {
		this.right = right;
	}

	public RedBlackNode getParent() {
		return parent;
	}

	public void setParent(RedBlackNode parent) {
		this.parent = parent;
	}

	public char getColor() {
		return color;
	}

	public void setColor(char color) {
		this.color = color;
	}

}
