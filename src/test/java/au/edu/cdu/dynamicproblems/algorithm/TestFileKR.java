package au.edu.cdu.dynamicproblems.algorithm;

public class TestFileKR {
	private String filePath;
	private int[][] krArray;

	TestFileKR(String filePath, int[][] krArray) {
		this.filePath = filePath;
		this.krArray = krArray;
	}

	public String getFilePath() {
		return filePath;
	}

	public int[][] getKrArray() {
		return krArray;
	}
}