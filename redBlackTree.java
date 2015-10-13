import java.util.Scanner;

//Colours RED = 1, BLACK = 0
public class redBlackTree {
	
	public class Node{ // An Node object is one node in a binary tree of ints
		public int data; // data of node
		public int colour; // colour: red = 1 or black = 0
		public Node left, right, parent;
		
		public Node(int data, int colour) { // creates a leaf node
			this(data,colour,null,null);
		}
		
		public Node(int data, int colour, Node left, Node right) { // creates a branch node
			this.data = data;
			this.left = left;
			this.right = right;
			this.colour = 1; // inserted nodes are red initially
			if (left!=null) left.parent = this;
			if (right!=null) right.parent = this;
			this.parent = null;
		}
		
		public Node grandparent(Node n) {
			if (n!=null && n.parent!=null) { // not the root or child of root
				return n.parent.parent;
			} else
				return null;
		}
		
		public Node uncle(Node n) {
			Node g = grandparent(n);
		    if (g == null) { // no grandparent therefore no uncle
		    	return null;
		    }
		    if(n.parent != g.left) // uncle is on right
		    	return g.right;
		    else
		    	return g.left;
		}
	}
	
	class Tree { // entire binary tree of ints
		private Node Root; // null when its an empty tree
		
		void insert(int x) { // Insert x as new item
			Node insertedNode = new Node(x, 1, null, null);
			if (Root==null) { // starting with empty tree
				Root = insertedNode;
			}
			else {
				Node n = Root;
			}
		}
		
		boolean isEmpty() {	// Return true if empty; else false
			return true;
		}
		void clearTree() {	// Remove all items
			
		}
		void printTree() {	// Print all items (In-order traversal)
			
		}
	}



	public static void main(String[] arg){
		
	}
}
