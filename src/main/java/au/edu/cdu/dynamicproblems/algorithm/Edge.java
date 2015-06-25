package au.edu.cdu.dynamicproblems.algorithm;

@Deprecated
public class Edge {
	Integer edge;
	boolean isReal;

	public Edge(int edge, boolean isReal) {
		this.edge = edge;
		this.isReal = isReal;
	}

	public Edge(int edge) {
		this.edge = edge;
		this.isReal = true;
	}

	public Integer getEdge() {
		return edge;
	}

	public boolean isReal() {
		return isReal;
	}
}
