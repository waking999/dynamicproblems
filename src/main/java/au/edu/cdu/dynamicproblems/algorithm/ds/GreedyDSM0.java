package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.algorithm.common.DegreeAsc;
import au.edu.cdu.dynamicproblems.algorithm.common.GreedyDSUtil;
import au.edu.cdu.dynamicproblems.algorithm.common.IPriorityOrder;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GreedyDSM0 implements ITask, IAlgorithm {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSM0.class);
	private Map<String, Long> runningTimeMap;

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
		return GreedyDSUtil.getResult(this.k,this.rUpperBoundary,this.dominatingSet, this.runningTimeMap);
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

//	/**
//	 * number of vertices
//	 */
//	private int numOfVertices;

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
		//this.numOfVertices = adjacencyMatrix.size();
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
		this.runningTimeMap=new HashMap<String,Long>();

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

		// sort a list of vertex from lowest degree to highest
		IPriorityOrder<Integer, String> pocb = new DegreeAsc<Integer, String>();
		List<Integer> vList = pocb.getOrderedVertexList(g);

		int i = 0;

		Map<Integer, Boolean> dominatedMap = GreedyDSUtil.presetDominatedMap(vList);
		Integer v = vList.get(i);
		Integer u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, dominatedMap);

		List<Integer> dI = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(dI, u);
		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		List<Integer> uList = GreedyDSUtil.getUAndOneNeighborToBeDominated(g, dominatedMap, gI, u, v);
		AlgorithmUtil.prepareGenericGraph(adjacencyMatrix, gI, uList);

		GreedyDSUtil.setStatusOfIthVertexInQueue(i, vertexSolutionList, dI, vertexGraphList, gI);

		/*
		 * because the size of queue is only k+1, we need reuse some positions,
		 * therefore, we need several indexes to record different information
		 */
		/* backIndex is for calculating the position after back k vertices. */
		int backKIndex = 0;
		/*
		 * previousIndex is used for to get previous status in the queue to
		 * calculate current status
		 */
		int previousIndex = 0;
		/* record the position of the best solution in the queue */
		int bestSolutionPos = 0;
		/*
		 * because a vertex will not be count if it is dominated, currentIndex
		 * works for the index of step
		 */
		int currentIndex = 0;

		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {
		//for (i = 1; i < this.numOfVertices; i++) {
			i++;
			// get latest dominatedMapI
			Graph<Integer, String> gIPre = vertexGraphList.get(previousIndex);
			List<Integer> dIPre = vertexSolutionList.get(previousIndex);

			dominatedMap = GreedyDSUtil.getDominatedMap(g, vList, dIPre);

			v = vList.get(i);
			if (!dominatedMap.get(v)) {
				currentIndex++;
				u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, dominatedMap);

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
				uList = GreedyDSUtil.getUAndOneNeighborToBeDominated(g, dominatedMap, gI, u, v);
				AlgorithmUtil.prepareGenericGraph(adjacencyMatrix, gI, uList);

				previousIndex++;
				if(previousIndex>=queueSize){
					previousIndex=0;
				}
				bestSolutionPos = currentIndex % queueSize;
				
				GreedyDSUtil.setStatusOfIthVertexInQueue(bestSolutionPos, vertexSolutionList, dI, vertexGraphList, gI);

				// if it is a moment of regret, apply dds fpt
				List<Integer> ddsI = null;
				if (AlgorithmUtil.isMomentOfRegret(v, g, dI, u)) {
					/*
					 * back k vertices
					 * 
					 * if the number of vertices that has been visited is less
					 * than k, we will not back until it equals k
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
					Graph<Integer, String> gICopy = AlgorithmUtil.copyGraph(gI);
					DDSFPTV1 ag = new DDSFPTV1(indicator, gICopy, dK, rUpperBoundary);

					/*
					 * the difference between the current solution and the
					 * solution at back up point could be a considerable
					 * solution in dds fpt
					 */
					Collection<Integer> dsDiff = CollectionUtils.subtract(dI, dK);

					ag.setConsiderableCandidateVertices4DS(dsDiff);
					ag.computing();

					ddsI = ag.getDs2();
				}
				
				if (ddsI != null && ddsI.size() <= dI.size()) {
					GreedyDSUtil.setStatusOfIthVertexInQueue(previousIndex, vertexSolutionList, ddsI,vertexGraphList,gI);
					/*
					 * if we get a smaller solution from dds fpt than the
					 * current solution, we will reset the ith position of the
					 * queue i) no change to gI; ii) no change to dominatedMap;
					 * iii) change dI
					 */

					bestSolutionPos = previousIndex;

				} 
//				else {
//					GreedyDSUtil.setStatusOfIthVertexInQueue(previousIndex, vertexSolutionList, dI,vertexGraphList,gI);
//					
//				}
				
//				if (AlgorithmUtil.isAllDominated(dominatedMap)) {
//					break;
//				}
			}

		}
		this.dominatingSet = this.vertexSolutionList.get(bestSolutionPos);
		
	}

}
