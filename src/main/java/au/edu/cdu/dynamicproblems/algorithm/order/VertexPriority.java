package au.edu.cdu.dynamicproblems.algorithm.order;

public class VertexPriority<V> {
	public final static double ZERO_DIFF=1E-12;
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
		StringBuilder sb=new StringBuilder();
		sb.append(this.getVertex()).append("-");
		
		float priority=this.getPriority();
		int round=Math.round(priority);
		float diff=Math.abs(priority-round);
		if(diff<=ZERO_DIFF){
			sb.append(round);
		}else{
			sb.append(priority);
		}
		
		
		return sb.toString();
	}
	
}
