package au.edu.cdu.dynamicproblems.algorithm;

/**
 * a java bean containing vertex and its degree
 * 
 * @author Kai Wang
 * 
 */
public class VertexVote implements Comparable<VertexVote> {
	private final static double ZERO_DIFF=0.00000001;
	/**
	 * the label of a vertex
	 */
	private Integer vertex;
	/**
	 * the degree of the vertex
	 */
	private float vote;

	public VertexVote(Integer vertex, int degree) {
		this.vertex = vertex;
		this.vote = degree;
	}

	public Integer getVertex() {
		return vertex;
	}

	public float getVote() {
		return vote;
	}

	public int compareTo(VertexVote arg0) {
		/*
		 * sort the vertexdegrees in a list by their degree from highest to the
		 * lowest
		 */
		float diff=arg0.getVote() - this.getVote();
		if(Math.abs(diff)<ZERO_DIFF){
			return 0;
		}else if (diff>0){
			return 1;
		}else{
			return -1;
		}
	}
	
	public String toString(){
		return this.getVertex()+"-"+this.getVote();
	}

}
