
public class Par {
	public int v0, v1;
	public Par(int i,int j){
		v0=i;
		v1=j;
	}
	public boolean equals(Object o) {
		if(o instanceof Par) {
		Par p = (Par) o;
		return v0==p.v0 && v1 == p.v1;
		}
		else
			return false;
	}
	public int hashCode() {
		return pair(v0,v1)%15485863;
	}
	private int pair(int a, int b) {
		// TODO Auto-generated method stub
		return ((a+b)*(a+b+1))/2+a;
	}
	
}
