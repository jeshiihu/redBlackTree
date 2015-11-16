//import java.awt.Color;

enum Colour {BLACK, RED};

public class rbTree {
	public node root;
	
	public rbTree() { // create an empty tree
		root = null;
	}
	public node search(int key) {
		node current = root;
		while(current != null) {
			if(current.key == key) return current;
			else if(current.key > key) current = current.l;
			else current = current.r;
		}
		return current;
	}
	
	void insert(int key) {
		if (search(key) != null) return; // do nothing if duplicate
		else insert(new node(key));
	}
	private void insert(node insertNode) {
		if(root == null) 
			root = insertNode; // if nothing is in the tree
		else {
			node n = root;
			while(true) {
				if(insertNode.key == n.key) return;
				else if(insertNode.key < n.key) {
					if(n.l == null) {
						n.l = insertNode;
						break;
					} else n = n.l;
				}
				else if(insertNode.key > n.key) {
					if(n.r == null) {
						n.r = insertNode;
						break;
					} else n = n.r;
				}
			} insertNode.p = n;
		} insertCase1(insertNode);	
	}

	private void insertCase1(node n) { // no root
		if(n.p == null) n.c = Colour.BLACK;
		else insertCase2(n);
	}
	
	private void insertCase2(node n) { // black parent
		if(n.p.c == Colour.BLACK) return;
		else insertCase3(n);
	}
	
	private void insertCase3(node n) { // red parent, red uncle
		if(n.u(n)!=null && n.u(n).c==Colour.RED) {
			n.p.c = Colour.BLACK;
			n.u(n).c = Colour.BLACK;
			n.g(n).c = Colour.RED;
			insertCase1(n.g(n));
		}
		else insertCase4(n);
	}
	private void insertCase4(node n) { // red parent, black uncle
		 // left of right or right of left (inner)
		if(n==n.p.r && n.p == n.g(n).l) {
			rotateL(n.p);
			n = n.l;
		}
		else if(n==n.p.l && n.p == n.g(n).r) { 
			rotateR(n.p);
			n = n.r;
		}
		insertCase5(n);
	}
	
	private void insertCase5(node n) { // red parent, black uncle
		// left of left or right of right (outer)
		n.p.c = Colour.BLACK;
		n.g(n).c = Colour.RED;
		if(n == n.p.l) rotateR(n.g(n));
		else rotateL(n.g(n));
	}
	
	private void rotateL(node n) {
		node savedL = n.r.l;
		if (n == root) {
			root = n.r;
			root.p = null;
		} else {
			n.p.r = n.r;
			n.p.r.p = n.p;
		}
		n.r.l=n;
		n.p = n.r;
		n.r = savedL;
		if (savedL != null) savedL.p = n;
	}
	private void rotateR(node n) {
		node savedR = n.l.r;
		if (n == root) {
			root = n.l;
			root.p = null;
		} else {
			n.p.l = n.l;
			n.p.l.p = n.p;
		}
		n.l.r = n;
		n.p = n.l;
		n.l = savedR;
		if (savedR != null) savedR.p = n;
	}
	
	node getSucc(node n) {
		node succ = null;
		node succParent = null;
		node current = n.l;
		while(current!=null) {
			succParent = succ;
			succ = current;
			current = current.r;
		}
		if(succ!=n.l){
			succParent.r = succ.l;
			succ.l = n.l;
		}
		return succ;
	}

	void replaceNode(node n, node child) {
		if(n==root) {
			root = child;
			root.r = n.r;
			n.r.p = root;
			n.l.p = root;
		}
		else if(n==n.p.l) n.p.l = child;
		else if(n==n.p.r) n.p.r = child;
		child.p = n.p;
	}
	
	void delete(int key) {
		if(search(key) == null) return; //tree does not contain this key, do nothing
		node removeNode = search(key);
		if(removeNode.l==null && removeNode.r==null) { // no children just delete node
			boolean isLeft = false;
			if(removeNode.p.l==removeNode) {
				removeNode.p.l = null;
				isLeft = true;
			}
			if(removeNode.p.r==removeNode) removeNode.p.r = null;
			if(removeNode.c == Colour.BLACK && removeNode.p == root) {
				if(isLeft) rotateL(root);
				else rotateR(root);
			}
		}
		else if(removeNode.l==null&&removeNode.r!=null || removeNode.l!=null&&removeNode.r==null) // one child
			deleteOneChild(removeNode);
		else deleteTwoChildren(removeNode);
	}
	void deleteTwoChildren(node n) {
		node succ = getSucc(n);
		deleteCase1(succ);
		replaceNode(n, succ);
		succ.r = n.r;
	}
	void deleteOneChild(node n) {
		node child = (n.r==null) ? n.l:n.r;
		replaceNode(n,child);
		if(n.c == Colour.BLACK) {
			if(child.c == Colour.RED) child.c = Colour.BLACK;
			else deleteCase1(child);
		}
	}
	void deleteCase1(node n) { // N is new root
		if(n.p!=null) deleteCase2(n);
	}
	void deleteCase2(node n) { // sibling is red and so swap colours of P and S
		node sib = n.s(n);
		if(sib.c==Colour.RED) {
			n.p.c = Colour.RED;
			sib.c = Colour.BLACK;
			if(n == n.p.l) rotateL(n.p);
			if(n == n.p.r) rotateR(n.p);
		} deleteCase3(n);
	}
	void deleteCase3(node n) { //P, S, and S's children are all black
		node sib = n.s(n);
		if(n.p.c==Colour.BLACK && sib.c==Colour.BLACK &&
				((sib.l!=null) ? sib.l.c:Colour.BLACK) ==Colour.BLACK && ((sib.r!=null) ? sib.r.c:Colour.BLACK)==Colour.BLACK) {
			sib.c = Colour.RED;
			deleteCase1(n.p);
		} else deleteCase4(n);
	}
	void deleteCase4(node n) { //S and its children are black but P is red
		node sib = n.s(n);
		if(n.p.c==Colour.RED && sib.c==Colour.BLACK &&
				((sib.l!=null) ? sib.l.c:Colour.BLACK)==Colour.BLACK && ((sib.r!=null) ? sib.r.c:Colour.BLACK)==Colour.BLACK) {
			sib.c = Colour.RED;
			n.p.c = Colour.BLACK;
		} else deleteCase5(n);
	}
	void deleteCase5(node n) { //S and right of S is black, left of S is left, and N is left of parent
		node sib = n.s(n);
		if(sib.c==Colour.BLACK) {
			if(n==n.p.l && ((sib.r!=null) ? sib.r.c:Colour.BLACK)==Colour.BLACK && 
					((sib.l!=null) ? sib.l.c:Colour.BLACK)==Colour.RED) {
				sib.c = Colour.RED;
				if(sib.l!=null) sib.l.c = Colour.BLACK;
				rotateR(sib);
			} else if(n==n.p.r && sib.l.c==Colour.BLACK && sib.r.c==Colour.RED) {
				sib.c = Colour.RED;
				if(sib.r!=null) sib.r.c = Colour.BLACK;
				rotateL(sib);
			}
		} deleteCase6(n);
	}
	void deleteCase6(node n) { //S is black, right of S is red, and N is left of P
		node sib = n.s(n);
		sib.c = n.p.c;
		n.p.c = Colour.BLACK;
		if(n==n.p.l) {
			if(sib.r!=null) sib.r.c = Colour.BLACK;
			rotateL(n.p);
		} else {
			if(sib.l!=null) sib.l.c = Colour.BLACK;
			rotateR(n.p);
		}
	}
	
	void isEmpty() {
		if (root == null) System.out.println("BST is empty.");
	}
	void clearTree(){
		root = null;
	}
	void printTree(){
		System.out.println("---Printing Tree---");
		if(root == null) System.out.println("  BST is empty");
		else printTree(root); 
	}
	private void printTree(node n){
		if(n.l != null) printTree(n.l);
		if(n==root) System.out.println("  Node:"+n.key + " " + n.c + ", Parent:null");
		else {
			if(n.p.l == n) System.out.println("  Node:"+n.key +  " " + n.c + ", Parent:left of " + n.p.key);
			if(n.p.r == n) System.out.println("  Node:"+n.key +  " " + n.c + ", Parent:right of " + n.p.key);
		}
		if(n.r != null) printTree(n.r);
	}

	public static void main (String[] arg){
	rbTree tree = new rbTree();
	testInsert(tree);
	testDelete(tree);
	tree.clearTree();
	}

	private static void testInsert(rbTree tree) { // change keys to test tree
		System.out.println("== Testing Insertion ==");
			tree.insert(1);
			tree.insert(2);
			tree.insert(3);
			tree.insert(4);
			tree.insert(5);
			tree.insert(6);
			tree.insert(7);
			tree.insert(8);
			tree.printTree();
			System.out.println("");
	}
	private static void testDelete(rbTree tree) { // change keys to test tree
		System.out.println("== Testing Deletion ==");
			tree.delete(1); System.out.println("deleted 1");
			tree.delete(2); System.out.println("deleted 2");
//			tree.delete(3); System.out.println("deleted 3");
			tree.printTree();
			System.out.println("");
	}
}


class node {
	public int key;
	public node l, r, p;
	public Colour c;

	public node(int key) {
		this(key, null, null, Colour.RED);
	}
	
	public node(int key, node left, node right, Colour colour) {
		this.key = key;
		this.l = left;
		this.r = right;
		this.c = colour;
		if(left != null) left.p = this;
		if(right != null) right.p = this;
		this.p = null;
	}


	public node g(node n) { // grandparent
		 if ((n != null) && (n.p != null)) return n.p.p;
		 else return null;
	}
	
	public node u(node n) { //uncle
	 node gp = g(n);
	 if (gp == null) return null; // No grandparent means no uncle
	 if (n.p == gp.l) return gp.r;
	 else return gp.l;
	}
	
	public node s(node n) {
		if(n == n.p.l) return n.p.r;
		else return n.p.l;
	}
}

