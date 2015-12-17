package au.edu.cdu.dynamicproblems.algorithm.common;

public class VertexPriority<V> {
	/**
	 * the label of a vertex
	 */
	private V vertex;
	/**
	 * the priority of the vertex
	 */
	private float priority;

	public V getVertex() {
		return this.vertex;
	}


	public float getPriority() {
		return priority;
	}


	public VertexPriority(V vertex, float priority) {
		this.vertex = vertex;
		this.priority = priority;
	}
	public String toString(){
		return this.getVertex()+"-"+Math.round(this.getPriority());
	}
	
}
