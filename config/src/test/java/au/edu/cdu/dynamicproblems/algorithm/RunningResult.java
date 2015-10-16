package au.edu.cdu.dynamicproblems.algorithm;

public class RunningResult {
	public void setSize(int size) {
		this.size = size;
	}
	public void setK(int k) {
		this.k = k;
	}
	public void setR(int r) {
		this.r = r;
	}
	public void setNanoSec(long nanoSec) {
		this.nanoSec = nanoSec;
	}
	@Override
	public String toString() {
		return "RunningResult [size=" + size + ", k=" + k + ", r=" + r
				+ ", nanoSec=" + nanoSec + "]";
	}
	private int size;
	private int k;
	private int r;
	private long nanoSec;
	public int getSize() {
		return size;
	}
	public int getK() {
		return k;
	}
	public int getR() {
		return r;
	}
	public long getNanoSec() {
		return nanoSec;
	}
	public RunningResult(int size, int k, int r, long nanoSec) {
		
		this.size = size;
		this.k = k;
		this.r = r;
		this.nanoSec = nanoSec;
	}
	
	
}
