package au.edu.cdu.dynamicproblems.algorithm.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import edu.uci.ics.jung.graph.Graph;

public class OrderPackageUtil {

	/**
	 * Get a list of vertex and it's priority by using callback functions
	 * 
	 * @param pb,
	 *            priority bean containing (g, dominated map, weight map)
	 * @param pcb,priority
	 *            call back (decide what really priority is: degree, utility,
	 *            ...)
	 * @return
	 */
	private static <V, E> List<VertexPriority<V>> getVertexPriorityList(PriorityBean<V, E> pb, IPriority pcb) {
		List<VertexPriority<V>> vertexPriorityList = new ArrayList<VertexPriority<V>>();
		Collection<V> vertices = pb.getG().getVertices();
		for (V i : vertices) {
			float priority = pcb.getPriority(pb, i);
			AlgorithmUtil.addElementToList(vertexPriorityList, new VertexPriority<V>(i, priority));
		}
	
		return vertexPriorityList;
	}

	/**
	 * Get a priority queue of vertices and their priorities.
	 * 
	 * @param g,
	 *            a graph instance
	 * @param pcb,
	 *            priority call back (decide what really priority is: degree,
	 *            utility, ...)
	 * @param ocb,
	 *            order call back (decide what order to follow: asc, desc, ...)
	 * @return
	 */
	public static <V, E> Queue<VertexPriority<V>> getOrderedVertexPriorityQueue(PriorityBean<V, E> pb,
			IPriority pcb, IOrder<V> ocb) {
		List<VertexPriority<V>> vpList = OrderPackageUtil.getVertexPriorityList(pb, pcb);
		Queue<VertexPriority<V>> q = new PriorityQueue<VertexPriority<V>>(ocb.getComparator());
		q.addAll(vpList);
		return q;
	}

	/**
	 * get vertices from an ordered vertex priority queue
	 * 
	 * @param q,
	 *            a queue of ordered vertex priority
	 * @return vertex list
	 */
	public static <V> List<V> getOrderedVertexList(Queue<VertexPriority<V>> q) {
		List<V> vList = new ArrayList<V>(q.size());
		while (!q.isEmpty()) {
			vList.add(q.poll().getVertex());
		}
		return vList;
	}

	/**
	 * 
	 * @param g
	 * @return
	 */
	public static <V, E> List<V> getVertexListDegreeAsc(Graph<V, E> g) {
		IPriorityOrder<V, E> pocb = new DegreeAsc<V, E>();
		/*
		 * there is no need for a dominated map (<vertex(V),
		 * dominated(boolean)>), and weight map for ordering by degree
		 */
		List<V> vList = pocb.getOrderedVertexList(new PriorityBean<V, E>(g, null, null));
		return vList;
	}

	/**
	 * 
	 * @param g
	 * @return
	 */
	public static <V, E> List<V> getVertexListDegreeDesc(Graph<V, E> g) {
		IPriorityOrder<V, E> pocb = new DegreeDesc<V, E>();
		/*
		 * there is no need for a dominated map (<vertex(V),
		 * dominated(boolean)>), and weight map for ordering by degree
		 */
		List<V> vList = pocb.getOrderedVertexList(new PriorityBean<V, E>(g, null, null));
		return vList;
	}

	/**
	 * 
	 * @param g
	 * @param dominatedMap
	 * @return
	 */
	public static <V, E> List<V> getVertexListUtilityDesc(Graph<V, E> g, Map<V, Boolean> dominatedMap) {
		IPriorityOrder<V, E> pocb = new UtilityDesc<V, E>();
		/*
		 * there is no need for a weight map (<vertex(V), weigh(float)>) for
		 * ordering by utility
		 */
		List<V> vList = pocb.getOrderedVertexList(new PriorityBean<V, E>(g, dominatedMap, null));
		return vList;
	}
	/**
	 * 
	 * @param g
	 * @param dominatedMap
	 * @param weightMap
	 * @param voteMap
	 * @return
	 */
	public static <V, E> List<V> getVertexListVoteDesc(Graph<V, E> g, Map<V, Boolean> dominatedMap,Map<V,Float> weightMap,Map<V,Float> voteMap) {
		IPriorityOrder<V, E> pocb = new VoteDesc<V, E>();
		List<V> vList = pocb.getOrderedVertexList(new PriorityBean<V, E>(g, dominatedMap, weightMap,voteMap));
		return vList;
	}
	/**
	 * 
	 * @param g
	 * @param dominatedMap
	 * @param weightMap
	 * @param voteMap
	 * @return
	 */
	public static <V, E> List<V> getVertexListVoteAsc(Map<V,Float> weightMap) {
		IPriorityOrder<V, E> pocb = new VoteAsc<V, E>();
		List<V> vList = pocb.getOrderedVertexList(new PriorityBean<V, E>(weightMap));
		return vList;
	}
	/**
	 * 
	 * @param g
	 * @param weightMap
	 * @return
	 */
	public static <V, E> List<V> getVertexListWeightDesc(Graph<V, E> g, Map<V, Float> weightMap) {
		IPriorityOrder<V, E> pocb = new WeightDesc<V, E>();
		/*
		 * there is no need for a dominated map (<vertex(V),
		 * dominated(boolean)>) for ordering by degree
		 */
		List<V> vList = pocb.getOrderedVertexList(new PriorityBean<V, E>(g, null, weightMap));
		return vList;
	}

	/**
	 * get vertices from an ordered vertex priority queue obtained by pcb and
	 * ocb
	 * 
	 * @param g,
	 *            a graph instance
	 * @param pcb,
	 *            priority call back (decide what really priority is: degree,
	 *            utility, ...)
	 * @param ocb,
	 *            order call back (decide what order to follow: asc, desc, ...)
	 * @return
	 */
	public static <V, E> List<V> getOrderedVertexList(PriorityBean<V, E> pb, IPriority pcb,
			IOrder<V> ocb) {
		Queue<VertexPriority<V>> q = getOrderedVertexPriorityQueue(pb, pcb, ocb);
		List<V> vList = getOrderedVertexList(q);
		return vList;
	}

}
