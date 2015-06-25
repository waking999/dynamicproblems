package au.edu.cdu.dynamicproblems.control;

public class Result {
	private long index;
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	private boolean hasSolution;
	
	
	public boolean hasSolution() {
		return hasSolution;
	}

	public void setHasSolution(boolean hasSolution) {
		this.hasSolution = hasSolution;
	}

	private String string;

	public String getString() {
		return index+":"+string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
