package au.edu.cdu.dynamicproblems.algorithm;

/**
 * a java bean containing vertex and its degree
 * 
 * @author Kai Wang
 * 
 */
public class VertexDegree implements Comparable<VertexDegree> {
	/**
	 * the label of a vertex
	 */
	Integer vertex;
	/**
	 * the degree of the vertex
	 */
	int degree;

	public VertexDegree(Integer vertex, int degree) {
		this.vertex = vertex;
		this.degree = degree;
	}

	public Integer getVertex() {
		return vertex;
	}

	public int getDegree() {
		return degree;
	}

	public int compareTo(VertexDegree arg0) {
		/*
		 * sort the vertexdegrees in a list by their degree from highest to the
		 * lowest
		 */
		return arg0.getDegree() - this.getDegree();
	}
	
	public String toString(){
		return this.getVertex()+"-"+this.getDegree();
	}

}
