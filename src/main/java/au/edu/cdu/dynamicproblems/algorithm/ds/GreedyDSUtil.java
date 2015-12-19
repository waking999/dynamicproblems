package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.DegreeRRReturn;
import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * 
 * @author kwang1
 *
 *
 */
public class GreedyDSUtil {
	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSUtil.class);
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
	public static Map<Integer, Boolean> presetDominatedMap(Collection<Integer> vList) {
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

	/**
	 * get dominated map in a graph by a dominating set
	 * 
	 * @param g,
	 *            graph
	 * 
	 * @param dI,
	 *            a staging dominating set
	 * @return
	 */
	public static <E> Map<Integer, Boolean> getDominatedMap(Graph<Integer, E> g, List<Integer> dI) {

		Map<Integer, Boolean> dominatedMapPre = presetDominatedMap(g.getVertices());

		for (Integer v : dI) {
			dominatedMapPre.put(v, true);

			Collection<Integer> vNeig = g.getNeighbors(v);
			for (Integer u : vNeig) {
				dominatedMapPre.put(u, true);
			}
		}

		return dominatedMapPre;

	}

	/**
	 * 
	 * apply the reduction rule in 2004 paper (Michael, Niedermeier) This will
	 * reduce the size of graph instances
	 * 
	 * @param g,
	 *            the graph before applying reduction rule
	 * @return
	 */
	public static Graph<Integer, String> applyPolyReductionRules(Graph<Integer, String> g, Map<String,Long> runningTimeMap) {
		long start = System.nanoTime();
		
		Graph<Integer, String> gRR = AlgorithmUtil.applySingleVertexReductionRule(g);
		gRR = AlgorithmUtil.applyPairVerticesReductionRule(gRR);
		
		long end = System.nanoTime();
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_POLYRR, (end - start));
		
		return gRR;
	}

	/**
	 * apply the reduction rule based on degree(0,1,2) This will calculate the
	 * exact part of dominating set
	 * 
	 * @param g,
	 *            the graph before applying the reduction rule
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static DegreeRRReturn applyDegreeReductionRules(Graph<Integer, String> g, Map<String,Long> runningTimeMap) {
		
		long start = System.nanoTime();
		List<Integer> dsAfterDegreeRR = new ArrayList<Integer>();
		List<Integer> verticesAfterDegreeRR = new ArrayList<Integer>();

		Collection<Integer> vertices = g.getVertices();

		Map<Integer, Boolean> dominatedMap = GreedyDSUtil.presetDominatedMap(vertices);

		for (Integer v : vertices) {
			if (!dominatedMap.get(v)) {
				int degree = g.degree(v);
				if (degree == 0) {
					addDominatingVertex(dsAfterDegreeRR, verticesAfterDegreeRR, v, dominatedMap);
				} else if (degree == 1) {
					Collection<Integer> vNegb = g.getNeighbors(v);
					/*
					 * add v's neighbor u to gOperated and dominating set add
					 * u's neighbors to gOperated and mark them
					 * dominated(including v)
					 */
					for (Integer u : vNegb) {

						addDominatingVertexAndItsNeigbors(g, dsAfterDegreeRR, verticesAfterDegreeRR, u, dominatedMap);
					}
				}
			}
		}

		for (Integer v : vertices) {
			if (!dominatedMap.get(v)) {
				int degree = g.degree(v);

				if (degree == 2) {
					// get v's neighbor u,w
					Collection<Integer> vNegb = g.getNeighbors(v);

					List<Integer> vNegbList = new ArrayList<Integer>();
					for (Integer x : vNegb) {
						vNegbList.add(x);
					}

					Integer u = vNegbList.get(0);
					Integer w = vNegbList.get(1);

					// get u,w's utility
					int uUtility = AlgorithmUtil.getVertexUtility(g, u, dominatedMap);
					int wUtility = AlgorithmUtil.getVertexUtility(g, w, dominatedMap);

					Collection<Integer> uNegb = getClosedNeighborsWithoutV(g, v, u);

					Collection<Integer> wNegb = getClosedNeighborsWithoutV(g, v, w);

					if (uUtility > wUtility) {
						// u has the higher priority to be added into
						// dominating
						// set than w
						addHigherNeighborOfVToDS(dsAfterDegreeRR, v, u, w, uNegb, wNegb, verticesAfterDegreeRR,
								uUtility, wUtility, dominatedMap);
					} else {
						addHigherNeighborOfVToDS(dsAfterDegreeRR, v, w, u, wNegb, uNegb, verticesAfterDegreeRR,
								wUtility, uUtility, dominatedMap);
					}
				}
			}
		}

		long end = System.nanoTime();
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_DEGREERR, (end - start));

		
		return new DegreeRRReturn(dsAfterDegreeRR, verticesAfterDegreeRR, dominatedMap);
	}

	private static void addDominatingVertex(List<Integer> ds, List<Integer> vList, Integer u,
			Map<Integer, Boolean> dominatedMap) {
		AlgorithmUtil.addElementToList(ds, u);
		addDominatedVertex(vList, u, dominatedMap);
	}

	private static void addDominatedVertex(List<Integer> vList, Integer u, Map<Integer, Boolean> dominatedMap) {
		AlgorithmUtil.addElementToList(vList, u);
		dominatedMap.put(u, true);
	}

	private static void addDominatingVertexAndItsNeigbors(Graph<Integer, String> g, List<Integer> ds,
			List<Integer> vList, Integer v, Map<Integer, Boolean> dominatedMap) {
		addDominatingVertex(ds, vList, v, dominatedMap);
		Collection<Integer> vNegb = g.getNeighbors(v);
		for (Integer w : vNegb) {
			addDominatedVertex(vList, w, dominatedMap);
		}
	}

	private static <V, E> Collection<V> getClosedNeighborsWithoutV(Graph<V, E> g, V v, V w) {
		Collection<V> wNegb = g.getNeighbors(w); // N(w)
		wNegb.add(w); // N[w]
		wNegb.remove(v); // N[w]\v
		return wNegb;
	}

	private static void addHigherNeighborOfVToDS(List<Integer> ds, Integer v, Integer u, Integer w,
			Collection<Integer> uNegb, Collection<Integer> wNegb, List<Integer> initalVerteices, int uUtility,
			int wUtility, Map<Integer, Boolean> dominatedMap) {
		if (AlgorithmUtil.isAllDominated(dominatedMap, wNegb) && (wUtility - 1) == 0) {

			addNeighborOfVToDS(ds, v, u, uNegb, initalVerteices, dominatedMap);
		} else if (AlgorithmUtil.isAllDominated(dominatedMap, uNegb) && (uUtility - 1) == 0) {
			addNeighborOfVToDS(ds, v, w, wNegb, initalVerteices, dominatedMap);
		} else {
			addNeighborOfVToDS(ds, v, u, uNegb, initalVerteices, dominatedMap);
		}
	}

	private static void addNeighborOfVToDS(List<Integer> ds, Integer v, Integer w, Collection<Integer> wNegb,
			List<Integer> initalVerteices, Map<Integer, Boolean> dominatedMap) {
		/*
		 * if N[u]\v (including u) are dominated: add w to gOperated and
		 * dominating set and mark it dominated add w's neighbors to gOperated
		 * and mark them dominated(including v)
		 */
		AlgorithmUtil.addElementToList(ds, w);

		for (Integer x : wNegb) {
			addDominatedVertex(initalVerteices, x, dominatedMap);
		}
		addDominatedVertex(initalVerteices, v, dominatedMap);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setG0(int pos, Graph<Integer, String> g, DegreeRRReturn drrr, Map<Integer, Boolean> dominatedMap,
			List<Integer> vList, List<List<Integer>> vertexSolutionList, List<Graph<Integer, String>> vertexGraphList) {
		List<Integer> dI = null;
		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		Collection<Integer> uList = null;

		List<Integer> dsAfterDegreeRR = drrr.getDsAfterDegreeRR();

		if (dsAfterDegreeRR.isEmpty()) {
			Integer v = vList.get(pos);
			Integer u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, dominatedMap);

			dI = new ArrayList<Integer>();
			AlgorithmUtil.addElementToList(dI, u);

			uList = GreedyDSUtil.getUAndOneNeighborToBeDominated(g, dominatedMap, gI, u, v);

		} else {

			dI = dsAfterDegreeRR;

			uList = drrr.getVerticesAfterDegreeRR();
		}
		AlgorithmUtil.prepareGraph(g, gI, uList);
		setStatusOfIthVertexInQueue(pos, vertexSolutionList, dI, vertexGraphList, gI);

	}

	/**
	 * 
	 * @param previousIndex
	 * @param vertexSolutionList
	 * @param gI
	 * @param dI
	 * @param ddsI
	 * @param indicator
	 * @param rUpperBoundary
	 * @param runningTimeMap
	 * @return
	 * @throws MOutofNException
	 * @throws ExceedLongMaxException
	 * @throws ArraysNotSameLengthException
	 */
	public static List<Integer> invokeDDSFPT(int previousIndex, List<List<Integer>> vertexSolutionList,
			Graph<Integer, String> gI, List<Integer> dI, List<Integer> ddsI, String indicator, int rUpperBoundary,
			Map<String, Long> runningTimeMap)
					throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {
		//log.debug("invoke dds");
		long start = System.nanoTime();
		/*
		 * get the solution at the back up point for future usage in dds fpt
		 */
		List<Integer> dK = vertexSolutionList.get(previousIndex);
		/*
		 * get the current sub-graph as G' for future usage in dds fpt, but it
		 * should be a copy rather than the original one because the sub-graph
		 * will be changed during dds fpt
		 */
		Graph<Integer, String> gICopy = AlgorithmUtil.copyGraph(gI);
		DDSFPT ag = new DDSFPT(indicator, gICopy, dK, rUpperBoundary);

		/*
		 * the difference between the current solution and the solution at back
		 * up point could be a considerable solution in dds fpt
		 */
		Collection<Integer> dsDiff = CollectionUtils.subtract(dI, dK);

		ag.setConsiderableCandidateVertices4DS(dsDiff);
		ag.computing();

		ddsI = ag.getDs2();

		long end = System.nanoTime();
		Long existingRunningTime = runningTimeMap.get(AlgorithmUtil.RUNNING_TIME_DDS);
		if (existingRunningTime == null) {
			existingRunningTime = Long.valueOf(0);
		}
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_DDS, existingRunningTime + (end - start));
		return ddsI;
	}

	/**
	 * 
	 * @param g
	 * @param dominatingSet
	 * @param runningTimeMap
	 * @throws ArraysNotSameLengthException
	 */
	public static <V, E> void applyMinimal(Graph<V, E> g, List<V> dominatingSet, Map<String, Long> runningTimeMap)
			throws ArraysNotSameLengthException {

		long start = System.nanoTime();
		dominatingSet = AlgorithmUtil.minimal(g, dominatingSet);
		long end = System.nanoTime();

		Long existingRunningTime = runningTimeMap.get(AlgorithmUtil.RUNNING_TIME_MINI);
		if (existingRunningTime == null) {
			existingRunningTime = Long.valueOf(0);
		}
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_MINI, existingRunningTime + (end - start));
	}

	/**
	 * 
	 * @param g
	 * @param dominatingSet
	 * @param runningTimeMap
	 */
	public static <V, E> void applyLS(Graph<V, E> g, List<V> dominatingSet, Map<String, Long> runningTimeMap) {

		long start = System.nanoTime();
		AlgorithmUtil.grasp(g, dominatingSet);
		long end = System.nanoTime();

		Long existingRunningTime = runningTimeMap.get(AlgorithmUtil.RUNNING_TIME_LS);
		if (existingRunningTime == null) {
			existingRunningTime = Long.valueOf(0);
		}
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_LS, existingRunningTime + (end - start));
	}
	/**
	 * 
	 * @param g
	 * @return
	 * @throws InterruptedException
	 */
	public static List<Integer> useGreedyToCalcDS(Graph<Integer, String> g,Map<String,Long> runningTimeMap) throws InterruptedException {

		long start = System.nanoTime();
		
		GreedyNative ag = new GreedyNative(g);
		ag.run();

		GreedyVoteGr ag1 = new GreedyVoteGr(g);
		ag1.run();

		List<Integer> ds = ag.getDominatingSet();
		int dsSize = ds.size();
		List<Integer> ds1 = ag1.getDominatingSet();
		int ds1Size = ds1.size();
		
		long end = System.nanoTime();
		Long existingRunningTime = runningTimeMap.get(AlgorithmUtil.RUNNING_TIME_GUARANTEE);
		if (existingRunningTime == null) {
			existingRunningTime = Long.valueOf(0);
		}
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_GUARANTEE, existingRunningTime + (end - start));

		
		if (dsSize <= ds1Size) {
			return ds;
		} else {
			return ds1;
		}

	}
	
	/**
	 * choose a vertex according to the weight, implemented according to the
	 * algorithm description in the paper
	 * 
	 * @return a vertex
	 */
	public static <V,E> V chooseVertex(Graph<V,E> g,Map<V,Float> weightMap) {

		List<V> vList = OrderPackageUtil.getVertexListWeightDesc(g, weightMap);
		return vList.get(0);

	}
	/**
	 * 
	 * @param runningTimeMap
	 */
	public static void initRunningTimeMap(Map<String,Long> runningTimeMap) {
		
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_POLYRR, Long.valueOf(0));
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_DEGREERR, Long.valueOf(0));
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_DDS, Long.valueOf(0));
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_GUARANTEE, Long.valueOf(0));
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_MINI, Long.valueOf(0));
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_LS, Long.valueOf(0));
		runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_TOTAL, Long.valueOf(0));
	}
}
