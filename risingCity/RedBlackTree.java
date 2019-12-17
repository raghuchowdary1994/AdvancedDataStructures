import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
// Class of Red Black Tree
public class RedBlackTree {

	// External Null Node
	private RedBlackNode ext_null_node = new RedBlackNode();
	// Root of the RedBlack Tree
	private RedBlackNode root = ext_null_node;

	public RedBlackNode getRoot() {
		return root;
	}

	public void setRoot(RedBlackNode root) {
		this.root = root;
	}

	public static final char RED = 'r';
	public static final char BLACK = 'b';

	
	// Insert operation into RedBlack tree
	public void insert(Building bld) {
		RedBlackNode rbn = new RedBlackNode(bld);
		rbn.setColor(RED);
		rbn.setLeft(ext_null_node);
		rbn.setRight(ext_null_node);
		bld.refNodeinTree = rbn;

		// case when Red Black Tree is empty
		if (root == ext_null_node) {
			root = rbn;
			root.setColor(BLACK);
			root.setParent(ext_null_node);
			return;
		}
		RedBlackNode curr = root;
		RedBlackNode prev = ext_null_node;

		// searching the location of insertion
		while (curr != ext_null_node) {
			prev = curr;
			if (rbn.getBld().getBuildingNum() < curr.getBld().getBuildingNum()) {
				curr = curr.getLeft();

			} else {
				curr = curr.getRight();
			}
		}
		// set parent pointer
		rbn.setParent(prev);

		if (rbn.getBld().getBuildingNum() < prev.getBld().getBuildingNum()) {
			prev.setLeft(rbn);
		} else {
			prev.setRight(rbn);
		}
		// call function to fix the constraints over red black tree
		checkRRConflicts(rbn);

	}

	// Function Checks the adjacent Red Red conflicts and takes required actions
	public void checkRRConflicts(RedBlackNode node) {

		while (node.getParent().getColor() == RED) {

			// if node is left child of parent
			if (node.getParent().getParent().getLeft() == node.getParent()) {

				RedBlackNode parent_sibling = node.getParent().getParent().getRight();
				// sibling of parent is red 
				if (parent_sibling != ext_null_node && parent_sibling.getColor() == RED) {
					node.getParent().setColor(BLACK);
					parent_sibling.setColor(BLACK);
					node.getParent().getParent().setColor(RED);
					node = node.getParent().getParent();
				} else {
					// sibling of parent is black and node is parent's right child
					if (node == node.getParent().getRight()) {
						node = node.getParent();
						left_Rotate(node);
					}
					// sibling of parent is black and node is parent's left child
					node.getParent().setColor(BLACK);
					node.getParent().getParent().setColor(RED);
					right_Rotate(node.getParent().getParent());

				}
			} else {
				// node is right child of parent
				RedBlackNode parent_sibling = node.getParent().getParent().getLeft();
				// sibling of parent is red
				if (parent_sibling != ext_null_node && parent_sibling.getColor() == RED) {
					node.getParent().setColor(BLACK);
					parent_sibling.setColor(BLACK);
					node.getParent().getParent().setColor(RED);
					node = node.getParent().getParent();
				} else {
					// sibling of parent is black and node is parent's left child
					if (node == node.getParent().getLeft()) {
						node = node.getParent();
						right_Rotate(node);
					}
					// sibling of parent is black and node is parent's right child
					node.getParent().setColor(BLACK);
					node.getParent().getParent().setColor(RED);
					left_Rotate(node.getParent().getParent());
				}

			}

		}
		// set color of root as always black
		root.setColor(BLACK);

	}
	
	// function performs a left rotation around the node x
	public void left_Rotate(RedBlackNode x) {

		RedBlackNode y = x.getRight();
		x.setRight(y.getLeft());

		if (y.getLeft() != ext_null_node) {
			y.getLeft().setParent(x);
		}

		y.setParent(x.getParent());

		if (x.getParent() == ext_null_node) {
			this.root = y;
		} else if (x == x.getParent().getLeft()) {
			x.getParent().setLeft(y);
		} else {
			x.getParent().setRight(y);
		}

		y.setLeft(x);
		x.setParent(y);

	}
	// function performs a right rotation around the node x
	public void right_Rotate(RedBlackNode x) {

		RedBlackNode y = x.getLeft();
		x.setLeft(y.getRight());

		if (y.getRight() != ext_null_node) {
			y.getRight().setParent(x);
		}
		y.setParent(x.getParent());

		if (x.getParent() == ext_null_node) {
			root = y;
		} else if (x == x.getParent().getRight()) {
			x.getParent().setRight(y);
		} else {
			x.getParent().setLeft(y);
		}

		y.setRight(x);
		x.setParent(y);
	}

	// remove operation on red black tree
	public void removeBuilding(Building bld) {
		delete(bld.refNodeinTree);
	}

	// remove node on red black tree
	public void delete(RedBlackNode node) {

		RedBlackNode node_toremoved = node;
		RedBlackNode node_toReplace;
		char origColor = node_toremoved.getColor();

		if (node.getLeft() == ext_null_node) {
				// node has no left child
			node_toReplace = node.getRight();
			// replace node with its right child
			rb_transplant(node, node.getRight());

		} else if (node.getRight() == ext_null_node) {
				// node has no right child
			node_toReplace = node.getLeft();
			// replace node with its left child
			rb_transplant(node, node.getLeft());

		} else {
			// node has 2 children
			// finding minimum in right subtree and replace with node to be deleted
			node_toremoved = findminRightSubTree(node.getRight());
			origColor = node_toremoved.getColor();
			node_toReplace = node_toremoved.getRight();
			// remove node to be removed and replace with node to replace 
			if (node_toremoved.getParent() == node) {
				node_toReplace.setParent(node_toremoved);
			} else {
				// node to replace is not the right child of node to be deleted 
				// replace node_toremoved's parent by the node_to removed right child
				rb_transplant(node_toremoved, node_toremoved.getRight());
				node_toremoved.setRight(node.getRight());
				node_toremoved.getRight().setParent(node_toremoved);

			}

			// replace node_toremoved as child of its parent by node_toremoved which was got from searching
			rb_transplant(node, node_toremoved);
			// replace node to removed left child with nodes left child
			node_toremoved.setLeft(node.getLeft());
			node_toremoved.getLeft().setParent(node_toremoved);
			// node_toremoved is colored same as node
			node_toremoved.setColor(node.getColor());

		}
		// removed node is black then black nodes are imbalanced along the paths
		if (origColor == BLACK) {
			checkDoubleBlackConditions(node_toReplace);
		}

	}
	// get the minimum node in the subtree
	public RedBlackNode findminRightSubTree(RedBlackNode root) {
		while (root.getLeft() != ext_null_node) {
			root = root.getLeft();
		}
		return root;
	}

	// function purpose is to balance the no of black nodes in each path of the tree
	public void checkDoubleBlackConditions(RedBlackNode node) {

		while (node != root && node.getColor() == BLACK) {

			if (node == node.getParent().getLeft()) {
				RedBlackNode w = node.getParent().getRight();
				if (w.getColor() == RED) {
					// node sibling w is red
					w.setColor(BLACK);
					node.getParent().setColor(RED);
					left_Rotate(node.getParent());
					w = node.getParent().getRight();

				}

				if (w.getLeft().getColor() == BLACK && w.getRight().getColor() == BLACK) {
					// nodes sibling is black and both children are black
					w.setColor(RED);
					node = node.getParent();

				} else {

					if (w.getRight().getColor() == BLACK) {
						// nodes sibling is black, left child of w is red and w's right child is black 
						w.getLeft().setColor(BLACK);
						w.setColor(RED);
						right_Rotate(w);
						w = node.getParent().getRight();

					}
					// nodes sibling is black and right child of w is red 
					w.setColor(node.getParent().getColor());
					node.getParent().setColor(BLACK);
					w.getRight().setColor(BLACK);
					left_Rotate(node.getParent());
					node = root;

				}

			} else {

				RedBlackNode w = node.getParent().getLeft();

				if (w.getColor() == RED) {
					// nodes sibling w is red
					w.setColor(BLACK);
					node.getParent().setColor(RED);
					right_Rotate(node.getParent());
					w = node.getParent().getLeft();
				}

				if (w.getRight().getColor() == BLACK && w.getLeft().getColor() == BLACK) {
					// nodes sibling w is black and both its children are black
					w.setColor(RED);
					node = node.getParent();

				} else {

					if (w.getLeft().getColor() == BLACK) {
						//nodes sibling w is black , right child of w is red and w left child is black
						w.getRight().setColor(BLACK);
						w.setColor(RED);
						left_Rotate(w);
						w = node.getParent().getLeft();

					}
					// nodes sibling w is black, left child of w is red 
					w.setColor(node.getParent().getColor());	
					node.getParent().setColor(BLACK);
					w.getLeft().setColor(BLACK);
					right_Rotate(node.getParent());
					node = root;

				}
			}

		}

		node.setColor(BLACK);

	}
	
	// subtree rooted at node u replace with subtree node rooted at v
	public void rb_transplant(RedBlackNode u, RedBlackNode v) {

		if (u.getParent() == ext_null_node) {
			root = v;
		} else if (u == u.getParent().getLeft()) {
			u.getParent().setLeft(v);
		} else {
			u.getParent().setRight(v);
		}

		v.setParent(u.getParent());
	}

	// print a building present in red black tree
	public String printBuilding(int bld) throws IOException {

		String str = search(this.root, bld);
		if (str.isEmpty()) {
			return "(0,0,0)";
		}

		return str;

	}
	// search a node building in the red black tree
	private String search(RedBlackNode node, int bld) {

		if (node.getBld() == null)
			return "";

		if (node.getBld().getBuildingNum() == bld) {
			StringBuilder str = new StringBuilder("");
			return str.append("(").append(node.getBld().getBuildingNum()).append(",")
					.append(node.getBld().getExecuted_time()).append(",").append(node.getBld().getTotal_time())
					.append(")").toString();
		}

		if (node.getBld().getBuildingNum() > bld) {
			return search(node.getLeft(), bld);
		}

		return search(node.getRight(), bld);
	}

	// prints the Buildings in the given range1 to range2
	public String printBuilding(int range1, int range2) throws IOException {

		StringBuilder sb = null;
		Stack<RedBlackNode> st = new Stack<RedBlackNode>();

		if (this.root == ext_null_node)
			return "(0,0,0)";
		st.push(root);

		while (st.size() > 0) {
			RedBlackNode p = st.peek();
			st.pop();
			if (p != ext_null_node) {

				if (p.getBld().getBuildingNum() >= range1 && p.getBld().getBuildingNum() <= range2) {
					if (sb == null) {
						sb = new StringBuilder();
						sb.append("(").append(p.getBld().getBuildingNum()).append(",")
								.append(p.getBld().getExecuted_time()).append(",").append(p.getBld().getTotal_time())
								.append(")");

					} else
						sb.append(",").append("(").append(p.getBld().getBuildingNum()).append(",")
								.append(p.getBld().getExecuted_time()).append(",").append(p.getBld().getTotal_time())
								.append(")");
				}
				
				if (p.getBld().getBuildingNum() < range2) {
					st.push(p.getRight());
				}
				
				if (range1 < p.getBld().getBuildingNum()) {
					st.push(p.getLeft());
				}
				

			}
		}
		if (sb == null) {
			return "(0,0,0)";
		}

		return sb.toString();

	}
	
		
	

//	public static void byLevel(RedBlackNode root) {
//		Queue<RedBlackNode> level = new LinkedList<>();
//		level.add(root);
//		while (!level.isEmpty()) {
//			RedBlackNode node = level.poll();
//			if (node.getBld() != null)
//				System.out.print(node.getBld().getBuildingNum() + " " + node.getBld().getExecuted_time() + " ");
//			if (node.getLeft() != null)
//				level.add(node.getLeft());
//			if (node.getRight() != null)
//				level.add(node.getRight());
//		}
//	}

}
