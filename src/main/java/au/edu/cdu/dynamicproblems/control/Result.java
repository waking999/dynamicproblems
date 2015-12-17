package au.edu.cdu.dynamicproblems.control;
/**
 * this java bean is used to contain information of the running result
 * @author kai wang
 *
 */
public class Result {
	@Deprecated
	private long index;
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
	@Deprecated
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
