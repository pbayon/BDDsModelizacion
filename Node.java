
public class Node {
	private int val;
	private Node left;
	private Node right;
	public Node(int val) {
		this.val=val;
		left=null;
		right=null;
	}
	public Node(int val, Node left,Node right) {
		this.val=val;
		this.left=left;
		this.right=right;
	}
	public boolean isVariable() {
		return left==null && right == null;
	}
	public Node left() {
		return left;
	}
	public Node right() {
		return right;
	}
	public boolean hasLeft() {
		return left==null;
	}
	public boolean hasRight() {
		return right==null;
	}
	public int val() {
		// TODO Auto-generated method stub
		return val;
	}
	
	
}
