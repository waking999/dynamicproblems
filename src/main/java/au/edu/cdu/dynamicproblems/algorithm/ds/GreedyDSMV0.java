package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.algorithm.VertexDegree;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GreedyDSMV0 implements ITask, IAlgorithm {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSMV0.class);
	private long runningTime;

	private TaskLock lock;

	public TaskLock getLock() {
		return lock;
	}

	public void setLock(TaskLock lock) {
		this.lock = lock;
	}

	public Result run() throws InterruptedException {

		try {
			computing();
			Thread.sleep(1000);
			Result r = getResult();

			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Result getResult() {
		Result r = new Result();
		r.setHasSolution(true);
		StringBuffer sb = new StringBuffer();

		sb.append(",").append(this.dominatingSet.size()).append(",").append(k).append(",").append(rUpperBoundary)
				.append(",").append(this.runningTime);
		r.setString(sb.toString());
		return r;
	}

	/**
	 * the graph
	 */
	private Graph<Integer, Integer> g;
	/**
	 * 
	 * a sorted vertices with their degree (from highest degree to the lowest)
	 */
	private String indicator;

	// Map<Integer, Boolean> dominatedMap;
	/**
	 * the desired dominating set
	 */
	List<Integer> dominatingSet;

	public List<Integer> getDominatingSet() {
		return dominatingSet;
	}

	/**
	 * number of vertices
	 */
	private int numOfVertices;

	private int k;
	private int rUpperBoundary;

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	public GreedyDSMV0(List<String[]> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public GreedyDSMV0(List<String[]> adjacencyMatrix, int k, int rUpperBoundary) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);
		this.k = k;
		this.rUpperBoundary = rUpperBoundary;

	}

	public GreedyDSMV0(String indicator, List<String[]> adjacencyMatrix) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set
	 */
	public void computing() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {
		long start = System.nanoTime();
		initialization();
		greedy();
		long end = System.nanoTime();
		runningTime = end - start;

	}

	private void initialization() {
		dominatingSet = new ArrayList<Integer>();
	}

	private List<List<Integer>> vertexSolutionList;
	private List<Graph<Integer, Integer>> vertexGraphList;
	// private List<Map<Integer, Boolean>> vertexDominatedList;

	// private int getPreviousIndex(int previousIndex) {
	// previousIndex++;
	// if (previousIndex > (k + 1)) {
	// previousIndex = 0;
	// }
	// return previousIndex;
	// }

	private void greedy() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		/*the size of the queue*/
		int queueSize = k+1;
		
		vertexSolutionList = new ArrayList<List<Integer>>(queueSize);
		vertexGraphList = new ArrayList<Graph<Integer, Integer>>(queueSize);

		// sort a list of vertex from lowest degree to highest
		List<VertexDegree> vdList = AlgorithmUtil.sortVertexAccordingToDegree(g);
		List<Integer> vList = AlgorithmUtil.getVertexList(vdList);
		Collections.reverse(vList);

		int i = 0;

		Map<Integer, Boolean> dominatedMapI = presetDominatedMapI(vList);

		Integer v = vList.get(i);
		NavigableMap<Integer, Integer> allVdMap = AlgorithmUtil.sortVertexMapAccordingToUtility(g, dominatedMapI,
				AlgorithmUtil.ASC_ORDER);
		Integer u = getHighestUtilityNeighborOfAVertex(v, allVdMap);

		List<Integer> dI = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(dI, u);

		Graph<Integer, Integer> gI = new SparseMultigraph<Integer, Integer>();
		List<Integer> uList = getUAndOneNeighborToBeDominated(dominatedMapI, gI, u, v);
		AlgorithmUtil.prepareGraph(adjacencyMatrix, gI, uList);

		setStatusOfIthVertexInQueue(i, dominatedMapI, gI, dI);

		/*
		 * because the size of queue is only k+1, we need reuse some positions,
		 * therefore, we need several indexes to record different information
		 */
		/*backIndex is for calculating the position after back k vertices.*/
		int backKIndex = 0;
		/*previousIndex is used for to get previous status in the queue to calculate current status*/
		int previousIndex = 0;
		/* record the position of the best solution in the queue */
		int bestSolutionPos = 0;
		/*
		 * because a vertex will not be count if it is dominated, currentIndex
		 * works for the index of step
		 */
		int currentIndex = 0;
		for (i = 1; i < this.numOfVertices; i++) {

			// get latest dominatedMapI
			Graph<Integer, Integer> gIPre = vertexGraphList.get(previousIndex);
			List<Integer> dIPre = vertexSolutionList.get(previousIndex);
			dominatedMapI = getDominatedMap(vList, dIPre);

			// get each vertex in the list and the highest utility close neigh u
			// of v
			v = vList.get(i);
			if (!dominatedMapI.get(v)) {
				currentIndex++;
				allVdMap = AlgorithmUtil.sortVertexMapAccordingToUtility(g, dominatedMapI, AlgorithmUtil.ASC_ORDER);
				u = getHighestUtilityNeighborOfAVertex(v, allVdMap);

				// the solution in each round: add u
				dI = new ArrayList<Integer>();
				dI.addAll(dIPre);
				AlgorithmUtil.addElementToList(dI, u);

				/*
				 * the subgraph in each round: add u and one of its neighbor
				 * (other than vertices in previous sub graph)
				 *
				 * the purpose of adding u and only one of its neighbor is: 1.
				 * if only u, then sometimes u can be removed by dds fpt in a
				 * subgraph that u is dominated by another vertex but this makes
				 * other vertices dominated by u will be undominated; 2. if add
				 * all neighbors of u, the distance between G and G' can be huge
				 * such that slow down the process of dds fpt; 3. only u and one
				 * of its neighbor is a balance to not lose the generality and
				 * 2k is acceptable distance;
				 */
				gI = AlgorithmUtil.copyGraph(gIPre);
				uList = getUAndOneNeighborToBeDominated(dominatedMapI, gI, u, v);
				AlgorithmUtil.prepareGraph(adjacencyMatrix, gI, uList);

				previousIndex++;
				if(previousIndex>=queueSize){
					previousIndex=0;
				}
				bestSolutionPos = currentIndex % queueSize;

				setStatusOfIthVertexInQueue(bestSolutionPos, dominatedMapI, gI, dI);

				// if it is a moment of regret, apply dds fpt
				List<Integer> ddsI = null;
				if (AlgorithmUtil.isMomentOfRegret(v, g, dI, u)) {
					/*
					 * back k vertices
					 * 
					 * if the number of vertices that has been visited is less
					 * than k, then back to the first vertex
					 */
					backKIndex = currentIndex - k;
					if (backKIndex < 0) {
						continue;
					}
					previousIndex = backKIndex % queueSize;
					/*
					 * get the solution at the back up point for future usage in
					 * dds fpt
					 */
					List<Integer> dK = vertexSolutionList.get(previousIndex);
					/*
					 * get the current sub-graph as G' for future usage in dds
					 * fpt, but it should be a copy rather than the original one
					 * because the sub-graph will be changed during dds fpt
					 */
					Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGraph(gI);
					DDSFPTV0 ag = new DDSFPTV0(indicator, gICopy, dK, rUpperBoundary);

					/*
					 * the difference between the current solution and the
					 * solution at back up point could be a considerable
					 * solution in dds fpt
					 */
					Collection<Integer> dsDiff = CollectionUtils.subtract(dI, dK);

					ag.setConsiderableCandidateVertices4DS(dsDiff);
					ag.setOriginalVertexNum(this.numOfVertices);
					ag.computing();

					ddsI = ag.getDs2();

				}

				if (ddsI != null && ddsI.size() <= dI.size()) {
					setStatusOfIthVertexInQueue(previousIndex, dominatedMapI, gI, ddsI);
					/*
					 * if we get a smaller solution from dds fpt than the
					 * current solution, we will reset the ith position of the
					 * queue i) no change to gI; ii) no change to dominatedMap;
					 * iii) change dI
					 */

					bestSolutionPos = previousIndex;
					// dominatedMapI = getDominatedMap(vList,gI, ddsI);

				} else {
					setStatusOfIthVertexInQueue(previousIndex, dominatedMapI, gI, dI);
					// dominatedMapI = getDominatedMap(vList,gI, dI);
				}

				if (AlgorithmUtil.isAllDominated(dominatedMapI)) {
					break;
				}
			}
		}

		this.dominatingSet = this.vertexSolutionList.get(bestSolutionPos);

	}

	private List<Integer> getUAndOneNeighborToBeDominated(Map<Integer, Boolean> dominatedMapI,
			Graph<Integer, Integer> gIPre, Integer u, Integer v) {
		List<Integer> uList = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(uList, u);
		AlgorithmUtil.addElementToList(uList, v);
		Collection<Integer> uNeig = g.getNeighbors(u);
		uNeig.removeAll(gIPre.getVertices());

		List<Integer> oneNeighborList = AlgorithmUtil.getFirstNItemsInCollection(1, uNeig);
		uList.addAll(oneNeighborList);

		//
		// for (Integer w : uList) {
		// dominatedMapI.put(w, true);
		// }
		return uList;
	}

	/**
	 * put solution to be built, dominatedMap, and sub-graph at the ith position
	 * of the queue
	 * 
	 * @param i
	 * @param dominatedMapI
	 * @param gI
	 * @param dI
	 */
	private void setStatusOfIthVertexInQueue(int i, Map<Integer, Boolean> dominatedMapI, Graph<Integer, Integer> gI,
			List<Integer> dI) {
		try {
			vertexSolutionList.set(i, dI);
		} catch (Exception e) {
			vertexSolutionList.add(i, dI);
		}
		// try {
		// vertexDominatedList.set(i, dominatedMapI);
		// } catch (Exception e) {
		// vertexDominatedList.add(i, dominatedMapI);
		// }
		try {
			vertexGraphList.set(i, gI);
		} catch (Exception e) {
			vertexGraphList.add(i, gI);
		}

	}

	// /**
	// * set the dominatedMap the same as the previous
	// *
	// * @param i
	// * @param dominatedMapI
	// */
	// private void setBasedOnPreviousDominatedMap(int previousIndex,
	// Map<Integer, Boolean> dominatedMapI) {
	// Map<Integer, Boolean> dominatedMapPre =
	// vertexDominatedList.get(previousIndex);
	// Set<Integer> keySet = dominatedMapPre.keySet();
	// for (Integer key : keySet) {
	// dominatedMapI.put(key, dominatedMapPre.get(key));
	// }
	// }

	private Map<Integer, Boolean> getDominatedMap(List<Integer> vList, List<Integer> dI) {
		Map<Integer, Boolean> dominatedMapPre = presetDominatedMapI(vList);

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
	 * initialize the dominatedMap to be false
	 * 
	 * @param vList
	 * @return
	 */
	private Map<Integer, Boolean> presetDominatedMapI(List<Integer> vList) {
		Map<Integer, Boolean> dominatedMapI = new HashMap<Integer, Boolean>();
		for (Integer w : vList) {
			dominatedMapI.put(w, false);
		}
		return dominatedMapI;
	}

	private Integer getHighestUtilityNeighborOfAVertex(Integer v, NavigableMap<Integer, Integer> vdMap) {
		Collection<Integer> vNeg = g.getNeighbors(v);
		List<Integer> vNegList = new ArrayList<Integer>(vNeg);
		vNegList.add(v);

		Set<Integer> keySet = vdMap.descendingKeySet();

		for (Integer key : keySet) {
			if (vNegList.contains(key)) {
				return key;
			}

		}

		return null;
	}
}
