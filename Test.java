import java.util.ArrayList;
import java.util.StringTokenizer;

public class Test {

	public static void main(String[] args) throws Exception {
		System.out.println(nQueens(4));
		StringTokenizer tokens = new StringTokenizer(nQueens(8));
		Tree t = new Tree(tokens);
		
		System.out.println(Tree.n);
		long inicio = System.currentTimeMillis();
		BDD bdd = new BDD(t.root());
		System.out.println(System.currentTimeMillis()-inicio);
		bdd.anySat();
		bdd.print();
		

	}

	public static String nQueens(int n) {
		String r = "";
		for (int i = 0; i < n; i++) {
			r += (" ( ");
			for (int j = 0; j < n; j++) {
				if (j < n - 1)
					r += (" q" + i + j + " | ");
				else
					r += (" q" + i + j);
			}
			r += (" ) ");
			if (i < n - 1)
				r += (" & ");
		}
		r += " & ";
		for (int i = 0; i < n; i++) {

			for (int j = 0; j < n; j++) {

				r += (" ( ");
				r += (" q" + i + j + " -> ");
				for (int i1 = 0; i1 < n; i1++) {
					for (int j1 = 0; j1 < n; j1++) {
						if (!(i1 == i && j1 == j)) {
							if (i1 == i || j1 == j || Math.abs(i1 - i) == Math.abs(j1 - j))
								r += (" ! q" + i1 + j1 + " &");
						}
					}
				}

				if (r.charAt(r.length() - 1) == '&')
					r = r.substring(0, r.length() - 1);
				r += (" ) ");
				if (!(i == n - 1 && j == n - 1))
					r += (" &");
			}
		}
		return r;
	}

}
