package au.edu.cdu.dynamicproblems.algorithm.ds;

public class RoundResult {

	private int size;
	private long runningTime;
	private String file;
	private int round;

	public RoundResult(int size, long runningTime, String file, int round) {
		super();
		this.size = size;
		this.runningTime = runningTime;
		this.file = file;
		this.round = round;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	

	public long getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(long runningTime) {
		this.runningTime = runningTime;
	}

}
