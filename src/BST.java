
/**
 * @author davidhan
 */
import java.util.Comparator;

public class BST {
	public BSTNode myRoot;
	private Comparator<Track> myComparator;
	public static int idNum = 0;

	public BST(Comparator<Track> comp) {
		myComparator = comp;
	}

	/*
	 * Insert the given track in the appropriate place in the tree. It may or
	 * may not be necessary to make a completely new node.
	 */
	public void insert(Track t) {
		// create a root node if tree doesn't exist
		if (myRoot == null) {
			myRoot = new BSTNode(0);
			myRoot.addTrack(t);
		} else if (this.treeSearch(t) != null) {
			// insert track if node already exists
			this.treeSearch(t).addTrack(t);
		} else {
			// find the position to create new node
			BSTNode pointer = myRoot;
			BSTNode parentPointer = null;
			int compareValue = 0;
			while (pointer != null) {
				compareValue = myComparator.compare(t, pointer.getTrack());
				if (compareValue < 0) {
					parentPointer = pointer;
					pointer = pointer.leftChild;
				} else {
					parentPointer = pointer;
					pointer = pointer.rightChild;
				}
			} // pointer has been found
			if (compareValue < 0) {
				parentPointer.leftChild = new BSTNode(parentPointer, t, (int) (parentPointer.id + 50 * Math.random()));
				// not sure of how to keep a distinct id which is why I used
				// Math.random. hoping math.random won't give
				// an id number that's already been used.
			} else {
				parentPointer.rightChild = new BSTNode(parentPointer, t, (int) (parentPointer.id + 50 * Math.random()));
			}
		}

	}

	/*
	 * Call printout on each node, in 'inorder' fashion.
	 */
	public void inorderTreeWalk() {
		inorderTreeWalk(this.myRoot);
	}

	/*
	 * Call printout on each node, in 'inorder' fashion, for the tree rooted at
	 * the given node.
	 */
	public void inorderTreeWalk(BSTNode n) {
		if (n != null) {
			inorderTreeWalk(n.leftChild);
			n.printOut();
			inorderTreeWalk(n.rightChild);
		}
	}

	/*
	 * Find the Node that matches the item
	 */
	public BSTNode treeSearch(Track item) {
		return treeSearch(this.myRoot, item);
	}

	/*
	 * Return the Node in the tree rooted at the given node that matches the
	 * given track.
	 */
	public BSTNode treeSearch(BSTNode start, Track item) {
		if (start == null || myComparator.compare(item, start.getTrack()) == 0) {
			return start;
		}
		if (myComparator.compare(item, start.getTrack()) < 0) {
			return treeSearch(start.leftChild, item);
		} else {
			return treeSearch(start.rightChild, item);
		}
	}

	/*
	 * Find the "smallest" node
	 */
	public BSTNode treeMin(BSTNode start) {
		BSTNode min = null;
		while (start != null) {
			min = start;
			start = start.leftChild;
		}
		return min;
	}

	/*
	 * Find the "biggest" node
	 */
	public BSTNode treeMax(BSTNode start) {
		BSTNode max = null;
		while (start != null) {
			max = start;
			start = start.rightChild;
		}
		return max;
	}

	/*
	 * Find the node that is the successor of the given node.
	 */
	public BSTNode treeSuccessor(BSTNode start) {
		if (start.rightChild != null) {
			return this.treeMin(start.rightChild);
		} else {
			BSTNode successor = start.parent;
			while (successor != null && start == successor.rightChild) {
				start = successor;
				successor = successor.parent;
			}
			return successor;
		}
	}

	/*
	 * Delete a track.
	 */
	public void delete(Track t) {
		BSTNode found = treeSearch(myRoot, t);
		if (found != null) {
			found.deleteTrack(t);
			if (found.numTracks() == 0) {
				deleteNode(found);
			}
		}
	}

	/*
	 * For the parent of the node to be deleted, it will update the pointer for
	 * its children with replace. Finally it will delete the node in question.
	 */
	public void fixParentNode(BSTNode z, BSTNode replace) {
		// no children: update parent node or make root node become null
		// one child:
		// two child

		if (z.parent != null) {
			if (replace != null) {
				int compareValue = myComparator.compare(replace.getTrack(), z.parent.getTrack());
				if (compareValue < 0) {// z is a left child
					z.parent.leftChild = replace;
				} else {
					z.parent.rightChild = replace;
				}
			} else {
				z.parent.leftChild = null;
				z.parent.rightChild = null;
			}
		} else {// no parent
			if (replace != null) {
				if (replace.leftChild != null || replace.rightChild != null) {
					int compareReplace = myComparator.compare(replace.parent.getTrack(), replace.getTrack());
					if (compareReplace < 0) {
						replace.rightChild.parent = replace.parent;
						replace.parent.rightChild = replace.rightChild;
					} else {
						replace.leftChild.parent = replace.parent;
						replace.parent.leftChild = replace.leftChild;
					}

				}
			}
			myRoot = replace;
			replace = null;

		}
		z = null;

	}

	/*
	 * Delete a node from the tree.
	 */
	public void deleteNode(BSTNode z) {
		if (z.leftChild == null && z.rightChild == null) {// no children
			// System.out.println("Got no kids!");
			this.fixParentNode(z, null);
		} else if (z.leftChild != null && z.rightChild == null) {
			// only one left child
			// System.out.println("Got one kid");
			this.fixParentNode(z, z.leftChild);
		} else if (z.leftChild == null && z.rightChild != null) {
			// only one right child
			// System.out.println("Got one kid");
			this.fixParentNode(z, z.rightChild);
		} else {// two children
			// System.out.println("Got two kids");
			BSTNode successor = this.treeSuccessor(z);
			successor.rightChild = z.rightChild;
			successor.leftChild = z.leftChild;
			this.fixParentNode(z, successor);
		}

	}
}
