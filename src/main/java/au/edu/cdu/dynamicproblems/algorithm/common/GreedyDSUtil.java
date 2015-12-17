package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import edu.uci.ics.jung.graph.Graph;

/**
 * 
 * @author kwang1
 *
 *
 */
public class GreedyDSUtil {
	/**
	 * 
	 * @param dominatingSet
	 * @param runningTimeMap
	 * @return
	 */
	public static Result getResult(int k, int r, List<Integer> dominatingSet, Map<String, Long> runningTimeMap) {
		Result result = new Result();
		result.setHasSolution(true);
		StringBuilder sb = new StringBuilder();

		sb.append(AlgorithmUtil.COMMA).append(dominatingSet.size()).append(AlgorithmUtil.COMMA).append(k)
				.append(AlgorithmUtil.COMMA).append(r).append(AlgorithmUtil.COMMA);

		Set<Entry<String, Long>> runningTimeSet = runningTimeMap.entrySet();
		for (Entry<String, Long> entry : runningTimeSet) {
			String desc = entry.getKey();
			Long runningTime = entry.getValue();
			sb.append(runningTime).append(AlgorithmUtil.COMMA).append(desc).append(AlgorithmUtil.COMMA);
		}

		result.setString(sb.toString());
		return result;
	}

	/**
	 * initialize the dominatedMap to be false
	 * 
	 * @param vList,
	 *            a list of vertices
	 * @return
	 */
	public static Map<Integer, Boolean> presetDominatedMap(List<Integer> vList) {
		Map<Integer, Boolean> dominatedMap = new HashMap<Integer, Boolean>();
		for (Integer w : vList) {
			dominatedMap.put(w, false);
		}
		return dominatedMap;
	}

	/**
	 * put u,v and an arbitrary neighbor of u (not in V(gIPre)) in a list. The
	 * list will be used to construct a new sub-graph
	 * 
	 * @param dominatedMap,
	 *            the dominated map (a map keeping pair of <vertex, dominated>)
	 * @param g,
	 *            the whole graph instance
	 * @param gIPre,
	 *            the previous sub-graph instance
	 * @param u,
	 *            the dominating vertex,
	 * @param v,
	 *            the dominated vertex
	 * @return
	 */
	public static <V, E> List<V> getUAndOneNeighborToBeDominated(Graph<V, E> g, Map<V, Boolean> dominatedMap,
			Graph<V, E> gIPre, V u, V v) {
		List<V> uList = new ArrayList<V>();
		AlgorithmUtil.addElementToList(uList, u);
		AlgorithmUtil.addElementToList(uList, v);
		Collection<V> uNeig = g.getNeighbors(u);
		uNeig.removeAll(gIPre.getVertices());

		List<V> oneNeighborList = AlgorithmUtil.getFirstNItemsInCollection(1, uNeig);
		uList.addAll(oneNeighborList);

		return uList;
	}

	/**
	 * put solution to be built, and sub-graph at the pos-th position in the
	 * storage of the queue
	 * 
	 * @param pos,
	 *            the position,
	 * @param vertexSolutionList,
	 *            a list for storing the solutions in each iteration
	 * @param dI,
	 *            a solutions in each iteration
	 * @param vertexGraphList,a
	 *            list for storing the sub-graph in each iteration
	 * @param gI,
	 *            a sub-graph in each iteration
	 */
	public static <V, E> void setStatusOfIthVertexInQueue(int pos, List<List<V>> vertexSolutionList, List<V> dI,
			List<Graph<V, E>> vertexGraphList, Graph<V, E> gI) {
		try {
			vertexSolutionList.set(pos, dI);
		} catch (Exception e) {
			vertexSolutionList.add(pos, dI);
		}

		try {
			vertexGraphList.set(pos, gI);
		} catch (Exception e) {
			vertexGraphList.add(pos, gI);
		}

	}

	public static <E> Map<Integer, Boolean> getDominatedMap(Graph<Integer, E> g, List<Integer> vList,
			List<Integer> dI) {
		Map<Integer, Boolean> dominatedMapPre = presetDominatedMap(vList);

		for (Integer v : dI) {
			dominatedMapPre.put(v, true);

			Collection<Integer> vNeig = g.getNeighbors(v);
			for (Integer u : vNeig) {
				dominatedMapPre.put(u, true);
			}
		}

		return dominatedMapPre;

	}
}
