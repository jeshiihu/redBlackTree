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
	void delete(int key) {
		if(search(key) == null) return; //tree does not contain this key, do nothing
		node removeNode = search(key);
		if(removeNode.r==null && removeNode.l==null) { // node to be removed  has no children
			if(removeNode.p.l==removeNode) removeNode.p.l = null;
			if(removeNode.p.r==removeNode) removeNode.p.r = null;
			return;
		}
		deleteOneChild(removeNode);
	}
	void deleteOneChild(node n) {
		node child = (n.r != null) ? n.l: n.r; //if n.r exists, return l, else return r
		if(n.p.l==n) n.p.l = child;
		if(n.p.r==n) n.p.r = child;
		if(n.c == Colour.BLACK) {
			if(child.c == Colour.RED) child.c = Colour.BLACK;
			else deleteCase1(child);
		}
	}
	void deleteCase1(node n) { // N is new root
//		 if (n->parent != NULL)
//			  delete_case2(n);
	}
//	void delete_case2(struct node *n)
//	{
//	 struct node *s = sibling(n);
//
//	 if (s->color == RED) {
//	  n->parent->color = RED;
//	  s->color = BLACK;
//	  if (n == n->parent->left)
//	   rotate_left(n->parent);
//	  else
//	   rotate_right(n->parent);
//	 }
//	 delete_case3(n);
//	}
//	void delete_case3(struct node *n)
//	{
//	 struct node *s = sibling(n);
//
//	 if ((n->parent->color == BLACK) &&
//	     (s->color == BLACK) &&
//	     (s->left->color == BLACK) &&
//	     (s->right->color == BLACK)) {
//	  s->color = RED;
//	  delete_case1(n->parent);
//	 } else
//	  delete_case4(n);
//	}
//	void delete_case4(struct node *n)
//	{
//	 struct node *s = sibling(n);
//
//	 if ((n->parent->color == RED) &&
//	     (s->color == BLACK) &&
//	     (s->left->color == BLACK) &&
//	     (s->right->color == BLACK)) {
//	  s->color = RED;
//	  n->parent->color = BLACK;
//	 } else
//	  delete_case5(n);
//	}
//	void delete_case5(struct node *n)
//	{
//	 struct node *s = sibling(n);
//
//	 if  (s->color == BLACK) { /* this if statement is trivial,
//	due to case 2 (even though case 2 changed the sibling to a sibling's child,
//	the sibling's child can't be red, since no red parent can have a red child). */
//	/* the following statements just force the red to be on the left of the left of the parent,
//	   or right of the right, so case six will rotate correctly. */
//	  if ((n == n->parent->left) &&
//	      (s->right->color == BLACK) &&
//	      (s->left->color == RED)) { /* this last test is trivial too due to cases 2-4. */
//	   s->color = RED;
//	   s->left->color = BLACK;
//	   rotate_right(s);
//	  } else if ((n == n->parent->right) &&
//	             (s->left->color == BLACK) &&
//	             (s->right->color == RED)) {/* this last test is trivial too due to cases 2-4. */
//	   s->color = RED;
//	   s->right->color = BLACK;
//	   rotate_left(s);
//	  }
//	 }
//	 delete_case6(n);
//	}
//	void delete_case6(struct node *n)
//	{
//	 struct node *s = sibling(n);
//
//	 s->color = n->parent->color;
//	 n->parent->color = BLACK;
//
//	 if (n == n->parent->left) {
//	  s->right->color = BLACK;
//	  rotate_left(n->parent);
//	 } else {
//	  s->left->color = BLACK;
//	  rotate_right(n->parent);
//	 }
//	}
	
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
//		System.out.println(n.key + " grandparent is " + ((n.g(n) != null) ? (n.g(n).key) :("")));
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
	}

	private static void testInsert(rbTree tree) { // change keys to test tree
		System.out.println("== Testing Insertion ==");
		System.out.println("Insert: 1, 2, 3, 4, 5, 6, 7");
			tree.insert(8);
			tree.insert(7);
			tree.insert(6);
			tree.insert(5);
			tree.insert(4);
			tree.insert(3);
			tree.insert(2);
			tree.insert(1);
			tree.printTree();
			System.out.println("");
			tree.clearTree();
			
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
