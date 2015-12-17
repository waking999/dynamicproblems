package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.List;

import edu.uci.ics.jung.graph.Graph;

/**
 * 
 * @author kwang1 An interface for implementing ordering vertices by
 *         (degree/utility/...) from (high to low/low to high/...)
 *
 * @param <V>
 *            generic type of vertex
 * @param <E>
 *            generic type of edge
 */
public interface IPriorityOrder<V, E> {
	/**
	 * get an ordered vertex list
	 * 
	 * @param g,
	 *            a graph instance
	 * @return
	 */
	public List<V> getOrderedVertexList(Graph<V, E> g);
}
