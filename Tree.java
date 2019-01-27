import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Tree {
	private Node root;
	public static ArrayList<String> variables;
	static int n; //numero de variables
	static StringTokenizer tokens;
	static String t;//token actual

	public Tree(StringTokenizer tokens) throws Exception {
		variables = new ArrayList<String>();
		Tree.tokens = tokens;
		t = tokens.nextToken();
		n = 0;
		root = A();
		if (tokens.hasMoreTokens())
			throw new Exception("Se complet� el an�lisis sint�ctico pero se recibi� el token:\t" + tokens.nextToken());
	}

	private static void valida(String s) throws Exception {
		if (!t.equals(s))
			throw new Exception(
					"Error en el an�lisis sint�ctico se esperaba el token:\t" + s + "\n\tPero se recibi�:\t" + t);
		else if (tokens.hasMoreTokens())
			t = tokens.nextToken();
	}

	/*
	 * Gram�tica: A ::= B | B <-> A B ::= C | C -> B C ::= D | D | C D ::= E | E & F
	 * F ::= (A) | ! F | var
	 */
	private Node A() throws Exception {
		Node b = B();
		if (t.equals("<->")) {
			valida("<->");
			Node a = A();
			return new Node(-6, b, a);
		} else {
			return b;
		}
	}

	private Node B() throws Exception {
		Node c = C();
		int x;
		if (t.equals("->")) {
			valida("->");
			Node b = B();
			return new Node(-5, c, b);
		} else {
			return c;
		}
	}

	private Node C() throws Exception {
		Node d = D();
		if (t.equals("|")) {
			valida("|");
			Node c = C();
			return new Node(-4, d, c);
		} else {
			return d;
		}
	}

	private Node D() throws Exception {
		Node e = E();
		if (t.equals("&")) {
			valida("&");
			Node d = D();
			return new Node(-3, e, d);
		} else {
			return e;
		}
	}

	private Node E() throws Exception {
		switch (t) {
		case "(":
			valida("(");
			Node a = A();
			valida(")");
			return a;
		case "!":
			valida("!");
			Node e = E();
			return new Node(-2, e, null);
		default:
			if (isAlpha(t)) {
				int pos = variable(t);
				return new Node(pos);
			} else
				throw new Exception("Error l�xico, token no soportado:\t" + t);
		}
	}

	private int variable(String var) throws Exception {
		valida(var);
		if (variables.contains(var))
			return variables.indexOf(var);
		else {
			variables.add(var);
			return n++;
		}
	}

	public Tree(Node root) {
		this.root = root;
	}

	public int numVar() {
		return n;
	}

	public Node root() {
		return root;
	}
	public int depth(Node n) {
		if (n.isVariable()) return 1;
		else if (n.right()==null) return 1 + depth(n.left());
		else return 1 + Math.max(depth(n.left()), depth(n.right()));
	}
	private boolean isAlpha(String str) {
		for (int i = 0; i < str.length(); i++) {
			Character c = str.charAt(i);
			if (i == 0 && Character.isDigit(c))
				return false;
			if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	private int op(int op,int u1, int u2) {
		// TODO Auto-generated method stub
		switch (op) {


		case NOT:
			return (u1+1)%2;
		case AND:
			return (u1 == 1 && u2 == 1) ? 1 : 0;
		case OR:
			return (u1 == 1 || u2 == 1) ? 1 : 0;
		case IF:
			return (u1 == 1 && u2 == 0) ? 0 : 1;
		case IIF:
			return (u1 == u2) ? 1 : 0;
		}
		return 0;
	}
	final static int NOT = -2;
	final static int AND = -3;
	final static int OR = -4;
	final static int IF = -5;
	final static int IIF = -6;
	public int eval(int[] bits,Node n) {
		// TODO Auto-generated method stub
		if(n.isVariable())
			return bits[n.val()];
		else
			if(n.val()==NOT)
				return op(NOT,eval(bits,n.left()),0);
			else
				return op(n.val(),eval(bits,n.left()),eval(bits,n.right()));
	}
	public void order(ArrayList<String> newOrder) throws Exception {
		ArrayList<String> result = new ArrayList<String>(n);
		if(newOrder.size()>n)
			throw new Exception();
		for (String s : newOrder) {
			if(!variables.contains(s))
				throw new Exception();
			else
				result.add(s);
		}
		for(String s : variables)
			if(!result.contains(s))
				result.add(s);
		System.out.println(variables.toString());
		variables = result;
		System.out.println(variables.toString());
	}
}
