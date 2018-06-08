
public class NodeComponents {
	private int var;
	private int low;
	private int high;

	public Triple(int var, int low, int high) {
		this.var = var;
		this.low = low;
		this.high = high;
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
		return low;
	}

	public int high() {
		return high;
	}

	public int var() {
		return var;
	}
}
