package au.edu.cdu.dynamicproblems.algorithm;

public class TestParameter {
	String file;
	int k;
	int r;
	boolean beTest;

	TestParameter(String file, int k, int r, boolean beTest) {
		this.file = file;
		this.k = k;
		this.r = r;
		this.beTest = beTest;
	}

	public String getFile() {
		return file;
	}

	public boolean isBeTest() {
		return beTest;
	}

	public int getK() {
		return k;
	}

	public int getR() {
		return r;
	}
}
