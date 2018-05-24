
public class Triple {
	public int i, l, h;

	public Triple(int i, int l, int h) {
		this.i = i;
		this.l = l;
		this.h = h;
	}

	@Override
	public boolean equals(Object t) {
		if (t instanceof Triple) {
			Triple a = (Triple) t;
			return i == a.i && l == a.l && h == a.h;
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return pair(i, pair(l, h)) % 15485863;
	}

	private int pair(int a, int b) {
		// TODO Auto-generated method stub
		return ((a + b) * (a + b + 1)) / 2 + a;
	}

	public void swap() {
		if (l == 0 || l == 1 )
			l = (l+1)%2;
		if (h == 0 || h == 1)
			h = (h+1)%2;
		

	}

	public int low() {
		return l;
	}

	public int high() {
		return h;
	}

	public int var() {
		return i;
	}
}
