package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.List;

import edu.uci.ics.jung.graph.Graph;

public class MomentRegretReturn<V, E> {
	List<V> dds;
	Graph<V, E> graph;
	public MomentRegretReturn(List<V> dds, Graph<V, E> graph) {
		 
		this.dds = dds;
		this.graph = graph;
	}
	public List<V> getDds() {
		return dds;
	}
	public Graph<V, E> getGraph() {
		return graph;
	}
}
