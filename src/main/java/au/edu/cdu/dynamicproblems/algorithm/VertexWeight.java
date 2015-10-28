package au.edu.cdu.dynamicproblems.algorithm;

/**
 * a java bean containing vertex and its degree
 * 
 * @author Kai Wang
 * 
 */
public class VertexWeight implements Comparable<VertexWeight> {
	public final static double ZERO_DIFF=1E-12;
	/**
	 * the label of a vertex
	 */
	private Integer vertex;
	/**
	 * the degree of the vertex
	 */
	private float weight;

	public VertexWeight(Integer vertex, float weight) {
		this.vertex = vertex;
		this.weight = weight;
	}

	public Integer getVertex() {
		return vertex;
	}

	public float getVote() {
		return weight;
	}

	public int compareTo(VertexWeight arg0) {
		/*
		 * sort the vertexweights in a list by their degree from highest to the
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
