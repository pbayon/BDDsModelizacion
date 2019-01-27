import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BDD {
	int root;
	HashMap<Integer, Triple> T;
	HashMap<Triple, Integer> H;
	int numVar;

	public BDD(Tree t) { // 1er constructor, O(2^n)
		numVar = t.numVar();
		bits = new int[numVar];
		T = new HashMap<Integer, Triple>();
		T.put(0, new Triple(numVar, -1, -1));
		T.put(1, new Triple(numVar, -1, -1));
		H = new HashMap<Triple, Integer>();
		root = build(t, 0);

	}

	static int[] bits;

	private int build(Tree t, int i) {
		// TODO Auto-generated method stub
		if (i == numVar) {
			return t.eval(bits, t.root());
		}
		int x;
		int y;
		bits[i] = 0;
		int v0 = build(t, i + 1);
		bits[i] = 1;
		int v1 = build(t, i + 1);
		return mk(i, v0, v1);
	}

	private int mk(int i, int l, int h) {
		// TODO Auto-generated method stub
		Triple t;
		if (l == h)
			return h;
		else if (H.containsKey(t = new Triple(i, l, h)))
			return H.get(t);
		else {
			int u = T.size();
			T.put(u, t);
			H.put(t, u);
			return u;
		}
	}



	public BDD(Node node) {
		numVar = Tree.n;
		H = new HashMap<Triple, Integer>();
		T = new HashMap<Integer, Triple>();
		if (node.isVariable()) {
			T.put(0, new Triple(numVar, -1, -1));
			T.put(1, new Triple(numVar, -1, -1));
			T.put(2, new Triple(node.val(), 0, 1));
			root = 2;
		} else if (node.val() == NOT) {
			BDD bdd = new BDD(node.left());
			if (bdd.root==0 || bdd.root==1)
				root = (bdd.root+1)%2;
			else root = bdd.root;
			for (Map.Entry<Integer, Triple> e : bdd.T.entrySet())
				e.getValue().swap();
			T = (bdd.T);
		} else {
			T.put(0, new Triple(numVar, -1, -1));
			T.put(1, new Triple(numVar, -1, -1));
			BDD bdd1 = new BDD(node.left());
			BDD bdd2 = new BDD(node.right());
			HashMap<Par, Integer> G= new HashMap<Par, Integer>();
			int op = node.val();
			root = apply(op,G,bdd1,bdd2,bdd1.root, bdd2.root);
			bdd1.T.clear();
			bdd2.T.clear();
		}
	}

	private int apply(int op, HashMap<Par, Integer> G, BDD bdd1, BDD bdd2, int u1, int u2) {
		int u;
		Par p = new Par(u1, u2);
		if (G.containsKey(p))
			return G.get(p);
		else if ((u1 == 0 || u1 == 1) && (u2 == 0 || u2 == 1))
			u = op(op, u1, u2);
		else if (bdd1.var(u1) == bdd2.var(u2))
			u = mk(bdd1.var(u1), apply(op,G,bdd1,bdd2,bdd1.low(u1), bdd2.low(u2)), apply(op,G,bdd1,bdd2,bdd1.high(u1), bdd2.high(u2)));
		else if (bdd1.var(u1) < bdd2.var(u2))
			u = mk(bdd1.var(u1), apply(op,G,bdd1,bdd2,bdd1.low(u1), u2), apply(op,G,bdd1,bdd2,bdd1.high(u1), u2));
		else
			u = mk(bdd2.var(u2), apply(op,G,bdd1,bdd2,u1, bdd2.low(u2)), apply(op,G,bdd1,bdd2,u1, bdd2.high(u2)));
		G.put(p, u);
		return u;

	}

	private int low(int u1) {
		// TODO Auto-generated method stub
			return T.get(u1).low();
	}

	private int high(int u1) {
		// TODO Auto-generated method stub
			return T.get(u1).high();
	}

	private int var(int u1) {
		// TODO Auto-generated method stub
			return T.get(u1).var();
	}

	private int op(int op, int u1, int u2) {
		// TODO Auto-generated method stub
		switch (op) {
		case NOT:
			return (u1 + 1) % 2;
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
	
	public void print() {
		for(Entry<Integer, Triple> entry : T.entrySet()) {
		    Integer key = entry.getKey();
		    Triple value = entry.getValue();
		    System.out.println("u = "+key);
		    System.out.println("\tvar(u)="+value.var()+",low(u)="+value.low()+",high(u)="+value.high());
		}
	}
	public HashMap<Integer, Boolean> anySat() throws Exception { 
		ArrayList<String> variables = Tree.variables;
		HashMap<Integer,Boolean> solucion = anySat(root, new HashMap<Integer, Boolean>());
		for(Map.Entry<Integer,Boolean> e : solucion.entrySet()) {
			System.out.println(variables.get(e.getKey())+"->"+e.getValue());
		}
		return solucion;
	}

	private HashMap<Integer, Boolean> anySat(int u, HashMap<Integer, Boolean> asig) throws Exception {
		if (u == 0)
			throw new Exception("Se llamó al método anySat pero la función es insatisfacible");
		else if (u == 1)
			return asig;
		else {
			Triple t = T.get(u);
			if (t.low() == 0) {
				asig.put(t.var(), true);
				return anySat(t.high(), asig);
			} else {
				asig.put(t.var(), false);
				return anySat(t.low(), asig);
			}

		}
	}
	public void toDot(String name) {
		System.out.println("digraph " + name + " {");
		if (root == 0)
			System.out.println("0 [shape=box];");// insatisfacible
		else if (root == 1)
			System.out.println("1 [shape=box];");// tautology
		else {
			System.out.println("0 [shape=box];");
			System.out.println("1 [shape=box];");
			ArrayList<String> variables = Tree.variables;
			for (int i = 2; i <= root; i++) {
				Triple t = T.get(i);
				System.out.println(i + "[label=" + variables.get(t.var()) + ",xlabel=" + i + "];");
			}
			for (int i = root; i >= 2; i--) {
				Triple t = T.get(i);
				int l = t.low();
				int h = t.high();
				System.out.println(i + "->" + l + "[style=dashed];");
				System.out.println(i + "->" + h + ";");
			}
		}
		System.out.println("}");

	}
	
	    
}
