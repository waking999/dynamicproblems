package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * implement michael's original idea
 * 
 * @author kwang1
 *
 */
public class GreedyDSM0 implements ITask, IGreedyDS<Integer> {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSM0.class);
	private Map<String, Long> runningTimeMap;

	private TaskLock lock;

	public TaskLock getLock() {
		return lock;
	}

	public Map<String, Long> getRunningTimeMap() {
		return null;
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
		return GreedyDSUtil.getResult(this.k, this.rUpperBoundary, this.dominatingSet, this.runningTimeMap);
	}

	/**
	 * the graph
	 */
	private Graph<Integer, String> g;

	private String indicator;

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

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	/**
	 * parameters (k,r) for the fpt algroithm
	 */
	private int k;
	private int rUpperBoundary;

	public GreedyDSM0(List<String[]> adjacencyMatrix, int k, int rUpperBoundary) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix);

		this.k = k;
		this.rUpperBoundary = rUpperBoundary;

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
		long runningTime = end - start;
		this.runningTimeMap.put("Total", runningTime);

	}

	private void initialization() {
		dominatingSet = new ArrayList<Integer>();
		this.runningTimeMap = new HashMap<String, Long>();
		GreedyDSUtil.initRunningTimeMap(this.runningTimeMap);
	}

	/* a list for storing the solutions in each iteration */
	private List<List<Integer>> vertexSolutionList;
	/* a list for storing the sub-graph in each iteration */
	private List<Graph<Integer, String>> vertexGraphList;

	private void greedy() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		/* the size of the queue */
		int queueSize = k + 1;

		vertexSolutionList = new ArrayList<List<Integer>>(queueSize);
		vertexGraphList = new ArrayList<Graph<Integer, String>>(queueSize);

		/* sort a list of vertex from lowest degree to highest */
		List<Integer> vList = OrderPackageUtil.getVertexListDegreeAsc(g);

		/*
		 * i) get the first vertex in the list(the lowest degree); and ii) get
		 * its highest utility neighbor and iii) put it in D0; and iv)
		 * initialize a G0 with them; v) put D0 and G0 in queue
		 * 
		 */
		// i)
		int i = 0;
		Integer v = vList.get(i);
		// ii)
		Map<Integer, Boolean> gDominatedMap = GreedyDSUtil.presetDominatedMap(vList);
		Integer u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, gDominatedMap);
		// iii)
		List<Integer> dI = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(dI, u);
		// iv)
		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		// List<Integer> uList = GreedyDSUtil.getUAndOneNeighborToBeDominated(g,
		// gDominatedMap, gI, u, v);
		// AlgorithmUtil.prepareGenericGraph(adjacencyMatrix, gI, uList);
		List<Integer> uList = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(uList, u);
		addCloseNeighborToSubgraph(g, gI, uList);
		// v)
		GreedyDSUtil.setStatusOfIthVertexInQueue(i, vertexSolutionList, dI, vertexGraphList, gI);

		/*
		 * because the size of queue is only k+1, we need reuse some positions,
		 * therefore, we need several indexes to record different information
		 */
		/* index shows vertex position in the list, it can be larger than k */
		/*
		 * because a vertex will not be count if it is dominated, currentIndex
		 * works for the index of step, it could be larger than k;
		 */
		int currentIndex = 0;
		/*
		 * backIndex is for calculating the position after back k vertices. k
		 * vertices at the left side of current index, it could be larger than k
		 */
		int backKIndex = 0;

		/* pos shows status position in the storage, they are less than k */
		/*
		 * previousIndex is used for to get previous status in the queue to
		 * calculate current status
		 */
		int previousPos = 0;
		/*
		 * record the position of the best solution in the queue
		 */
		int currentPos = 0;

		for (i = 1; i < this.numOfVertices; i++) {
			/*
			 * get last round sub-graph G_{i-1} and ds solution D_{i-1}
			 */
			Graph<Integer, String> gIPre = vertexGraphList.get(previousPos);
			List<Integer> dIPre = vertexSolutionList.get(previousPos);

			/*
			 * this dominated map is for the whole graph to satisfy quit
			 * condition
			 */
			gDominatedMap = GreedyDSUtil.getDominatedMap(g, dIPre);
			if (AlgorithmUtil.isAllDominated(gDominatedMap)) {
				break;
			}
			/* get the ith vertex in the sorted list */
			v = vList.get(i);

			// /*
			// * this dominated map only set vertices in existing sub-graph,
			// which
			// * means some neighbors of a dominating vertex may not be set and
			// * they may be considered in the later if branch. Otherwise, they
			// * may be excluded in the later if branch.
			// */
			// Map<Integer, Boolean> gIDominatedMap =
			// GreedyDSUtil.getDominatedMapI(g, gIPre, dIPre);

			if (!gDominatedMap.get(v)) {
				/*
				 * if v is not dominated, i) get its highest utility
				 * neighbor;ii) put it in DI; and iii) construct a GI with them;
				 * and iv) calculate the position of the queue to put DI and GI;
				 * v) if it is a moment of regret, apply dds fpt; vi) and put
				 * the smaller solution ( of DI and DDSI ) and GI in the queue;
				 * vii) move the cursor of previousPos of the queue to
				 * currentPos;viii) because DI changes, we check the
				 * dominatedMap, if all have been dominated,we will quit the
				 * loop
				 */

				// i)
				u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, gDominatedMap);
				// ii)
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
				// iii)
				gI = AlgorithmUtil.copyGraph(gIPre);
//				uList = GreedyDSUtil.getUAndOneNeighborToBeDominated(g, gDominatedMap, gI, u, v);
//				AlgorithmUtil.prepareGenericGraph(adjacencyMatrix, gI, uList);
				uList = new ArrayList<Integer>();
				AlgorithmUtil.addElementToList(uList, u);
				addCloseNeighborToSubgraph(g, gI, uList);
				
				// iv)
				/* we only count un-dominated vertex and then step next */
				currentIndex++;
				// currentPos = currentIndex % queueSize;
				currentPos++;
				if (currentPos >= queueSize) {
					currentPos = 0;
				}
				// v)
				List<Integer> ddsI = null;
				if (isMomentOfRegret()) {

					/*
					 * back k vertices, if the number of vertices that has been
					 * visited is less than k, we will back the the smallest
					 * number, in another words, we only back to 0 rather than
					 * negative position
					 */

					backKIndex = currentIndex - k;
					if (backKIndex >= 0) {
						// backKIndex = 0;
						 log.debug(i + ":dds fpt");
						previousPos = backKIndex % queueSize;

						ddsI = GreedyDSUtil.invokeDDSFPT(previousPos, vertexSolutionList, gI, dI, ddsI, this.indicator,
								this.rUpperBoundary, this.runningTimeMap);
					}
				}

				if (ddsI != null && ddsI.size() < dI.size()) {
					dI = ddsI;
				}

				// vi)
				GreedyDSUtil.setStatusOfIthVertexInQueue(currentPos, vertexSolutionList, dI, vertexGraphList, gI);

				// vii)
				previousPos = currentPos;

				// viii)
				gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
				if (AlgorithmUtil.isAllDominated(gDominatedMap)) {
					break;
				}

			} else {

				/*
				 * if v has already been dominated, i) add v to gI; ii) and
				 * replace gI at previousPos of the queue ; iii) don't move the
				 * cursor of the queue
				 */
				// i)
				uList = new ArrayList<Integer>();
				AlgorithmUtil.addElementToList(uList, v);
				AlgorithmUtil.prepareGenericGraph(adjacencyMatrix, gI, uList);
				// ii)
				GreedyDSUtil.setStatusOfIthVertexInQueue(previousPos, vertexSolutionList, dI, vertexGraphList, gI);
			}

		}
		this.dominatingSet = this.vertexSolutionList.get(currentPos);

	}

	// private void addCloseNeighborToSubgraph(Graph<Integer, String> gRef,
	// List<Integer> dI, Graph<Integer, String> gI) {
	// List<Integer> verticesToAddInGraph = new ArrayList<Integer>();
	//
	// for (Integer w : dI) {
	// Collection<Integer> wNeig = gRef.getNeighbors(w);
	// for (Integer x : wNeig) {
	// if (!gI.containsVertex(x)) {
	// AlgorithmUtil.addElementToList(verticesToAddInGraph, x);
	// }
	// }
	//
	// }
	// AlgorithmUtil.prepareGraph(gRef, gI, verticesToAddInGraph);
	// }

	private boolean isMomentOfRegret() {
		IMomentOfRegret mor = new MomentOfRegretTrue();
		return mor.isMomentOfRegret(null);

	}

	// private <V> int getUndominatedNum(Map<V, Boolean> dominatedMap) {
	// int count = 0;
	// Set<V> keySet = dominatedMap.keySet();
	//
	// for (V key : keySet) {
	// if (!dominatedMap.get(key)) {
	// count++;
	// }
	// }
	// return count;
	// }

	private void addCloseNeighborToSubgraph(Graph<Integer, String> gRef, Graph<Integer, String> gI, List<Integer> dI) {
		List<Integer> verticesToAddInGraph = new ArrayList<Integer>();

		for (Integer w : dI) {
			Collection<Integer> wNeig = gRef.getNeighbors(w);
			for (Integer x : wNeig) {
				if (!gI.containsVertex(x)) {
					AlgorithmUtil.addElementToList(verticesToAddInGraph, x);
				}
			}

		}
		AlgorithmUtil.prepareGraph(gRef, gI, verticesToAddInGraph);
	}

}
