package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.DegreeRRReturn;
import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyDSM1 implements ITask, IGreedyDS<Integer> {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSM1.class);
	private Map<String, Long> runningTimeMap;

	public Map<String, Long> getRunningTimeMap() {
		return runningTimeMap;
	}

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
		return GreedyDSUtil.getResult(this.k, this.rUpperBoundary, this.dominatingSet, this.runningTimeMap);
	}
	//
	// /**
	// * the graph
	// */
	// private Graph<Integer, String> g;

	private String indicator;

	/**
	 * the desired dominating set
	 */
	List<Integer> dominatingSet;

	public List<Integer> getDominatingSet() {
		return dominatingSet;
	}

	// /**
	// * number of vertices
	// */
	// private int numOfVertices;

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	/**
	 * parameters (k,r) for the fpt algroithm
	 */
	private int k;
	private int rUpperBoundary;

	public GreedyDSM1(List<String[]> adjacencyMatrix, int k, int rUpperBoundary) {
		this.adjacencyMatrix = adjacencyMatrix;
		// this.numOfVertices = adjacencyMatrix.size();
		// this.g = AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix);

		this.k = k;
		this.rUpperBoundary = rUpperBoundary;

	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set
	 * @throws InterruptedException 
	 */
	public void computing() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		long start = System.nanoTime();
		initialization();
		greedy();
		long end = System.nanoTime();
		long runningTime = end - start;
		this.runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_TOTAL, runningTime);

	}

	// /* a graph generated according to the adjacency matrix */
	// private Graph<Integer, String> gOriginal;

	private int queueSize = 0;

	private void initialization() {
		dominatingSet = new ArrayList<Integer>();
		
		this.runningTimeMap = new HashMap<String, Long>();
		GreedyDSUtil.initRunningTimeMap(this.runningTimeMap);
		

		/* the size of the queue */
		queueSize = k + 1;

	}

	

	// private List<Integer> dsAfterDegreeRR;
	// private List<Integer> verticesAfterDegreeRR;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void greedy() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		Graph<Integer, String> gOriginal = AlgorithmUtil.prepareGenericGraph(adjacencyMatrix);
		
		/* apply poly-rr */
		Graph<Integer, String> g = GreedyDSUtil.applyPolyReductionRules(gOriginal,this.runningTimeMap);

		/* apply degree-rr */
		DegreeRRReturn drrr = GreedyDSUtil.applyDegreeReductionRules(g,this.runningTimeMap);
		
		Map<Integer, Boolean> dominatedMap = drrr.getDominatedMap();

		// sort a list of vertex from lowest degree to highest
//		IPriorityOrder<Integer, String> pocb = new DegreeAsc<Integer, String>();
//		List<Integer> vList = pocb.getOrderedVertexList(g);
		
		List<Integer> vList=OrderPackageUtil.getVertexListDegreeAsc(g);

		List<Integer> dI;
		Graph<Integer, String> gI;
		List<Integer> uList;
		int i = 0;

		/* a list for storing the solutions in each iteration */
		List<List<Integer>> vertexSolutionList = new ArrayList<List<Integer>>(queueSize);

		/* a list for storing the sub-graph in each iteration */
		List<Graph<Integer, String>> vertexGraphList = new ArrayList<Graph<Integer, String>>(queueSize);

		GreedyDSUtil.setG0(i, g, drrr, dominatedMap, vList, vertexSolutionList, vertexGraphList);

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
		 * vertices at the leftside of current index, it could be larger than k
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

		int vListSize = vList.size();
		for (i = 1; i < vListSize; i++) {

			// get previous sub-graph and dominating set to calculate
			// dominatedMap
			Graph<Integer, String> gIPre = vertexGraphList.get(previousPos);
			List<Integer> dIPre = vertexSolutionList.get(previousPos);
			dominatedMap = GreedyDSUtil.getDominatedMap(g, dIPre);

			if (AlgorithmUtil.isAllDominated(dominatedMap)) {
				break;
			}

			Integer v = vList.get(i);
			//if (!dominatedMap.get(v)) {
				currentIndex++;
				Integer u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, dominatedMap);

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
				AlgorithmUtil.prepareGraph(g, gI, uList);

				// previousIndex++;
				// if (previousIndex >= queueSize) {
				// previousIndex = 0;
				// }
				currentPos = currentIndex % queueSize;

				// GreedyDSUtil.setStatusOfIthVertexInQueue(curentSolutionPos,
				// vertexSolutionList, dI, vertexGraphList, gI);

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
					if (backKIndex >=0) {
						 
					previousPos = backKIndex % queueSize;

					
					ddsI = GreedyDSUtil.invokeDDSFPT(previousPos, vertexSolutionList, gI, dI, ddsI, this.indicator,
							this.rUpperBoundary,this.runningTimeMap);
					}
				} 

				if (ddsI != null && ddsI.size() <= dI.size()) {
					// long startMin = System.nanoTime();
					// ddsI = AlgorithmUtil.minimal(gI, ddsI);
					// long endMin = System.nanoTime();
					// this.runningTimeMap.put("Minimal", (endMin - startMin));
					//
					// long startGrasp = System.nanoTime();
					// ddsI = AlgorithmUtil.grasp(gI, ddsI);
					// long endGrasp = System.nanoTime();
					// this.runningTimeMap.put("GRASP", (endGrasp -
					// startGrasp));
					/*
					 * if we get a smaller solution from dds fpt than the
					 * current solution, we will reset the ith position of the
					 * queue i) no change to gI; ii) no change to dominatedMap;
					 * iii) change dI
					 */
					dI=ddsI;

				} 
				
				GreedyDSUtil.setStatusOfIthVertexInQueue(currentPos, vertexSolutionList, dI, vertexGraphList, gI);

				previousPos = currentPos;

		
			//}
			if (AlgorithmUtil.isAllDominated(dominatedMap)) {
				break;
			}
		}

		
		//do guarantee, minimal, ls at the last step;
		
		dI = vertexSolutionList.get(currentPos);
		
		List<Integer> gDI=GreedyDSUtil.useGreedyToCalcDS(g,this.runningTimeMap);
		if (dI.size() < gDI.size()) {
			this.dominatingSet = dI;
		} else {
			this.dominatingSet = gDI;
		}
		
		
		GreedyDSUtil.applyMinimal(g,this.dominatingSet, this.runningTimeMap);
		GreedyDSUtil.applyLS(g,this.dominatingSet, this.runningTimeMap);
	

	}

	

	

}
