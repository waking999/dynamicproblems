package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
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
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GreedyDSM1V0 implements ITask, IGreedyDS<Integer> {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSM1V0.class);
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

	private String indicator;

	/**
	 * the desired dominating set
	 */
	List<Integer> dominatingSet;

	public List<Integer> getDominatingSet() {
		return dominatingSet;
	}

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	/**
	 * parameters (k,r) for the fpt algroithm
	 */
	private int k;
	private int rUpperBoundary;

	public GreedyDSM1V0(List<String[]> adjacencyMatrix, int k, int rUpperBoundary) {
		this.adjacencyMatrix = adjacencyMatrix;

		this.k = k;
		this.rUpperBoundary = rUpperBoundary;

	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set
	 * 
	 * @throws InterruptedException
	 */
	public void computing()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		long start = System.nanoTime();
		initialization();
		greedy();
		long end = System.nanoTime();
		long runningTime = end - start;
		this.runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_TOTAL, runningTime);

	}

	private void initialization() {
		dominatingSet = new ArrayList<Integer>();

		this.runningTimeMap = new HashMap<String, Long>();
		GreedyDSUtil.initRunningTimeMap(this.runningTimeMap);

	}

	/* a list for storing the solutions in each iteration */
	private List<List<Integer>> solutionList;
	/* a list for storing the sub-graph in each iteration */
	private List<Graph<Integer, String>> graphList;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void greedy()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		Graph<Integer, String> gOriginal = AlgorithmUtil.prepareGenericGraph(adjacencyMatrix);

		/* apply poly-rr */
		Graph<Integer, String> g = GreedyDSUtil.applyPolyReductionRules(gOriginal, this.runningTimeMap,
				GreedyDSUtil.POLY_RR_2_VALVE);
		;
		/* apply degree-rr */
		DegreeRRReturn drrr = GreedyDSUtil.applyDegreeReductionRules(g, this.runningTimeMap);

		Map<Integer, Boolean> gDominatedMap = drrr.getDominatedMap();
		List<Integer> dI = drrr.getDsAfterDegreeRR();
		Collection<Integer> uList = drrr.getVerticesAfterDegreeRR();

		/* the size of the queue */
		int queueSize = k + 1;

		solutionList = new ArrayList<List<Integer>>(queueSize);
		graphList = new ArrayList<Graph<Integer, String>>(queueSize);

		// sort a list of vertex from lowest degree to highest
		List<Integer> vList = OrderPackageUtil.getVertexListDegreeAsc(g);

		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		// AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix, gI, dI);
		// GreedyDSUtil.addCloseNeighborToSubgraph(g, gI, dI);
		AlgorithmUtil.prepareGraph(g, gI, uList);

		GreedyDSUtil.setStatusOfIthVertexInQueue(0, solutionList, dI, graphList, gI);

		List<Integer> undominatedVertices = null;
		Integer v;
		Integer u;

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

		do {
			/*
			 * get last round sub-graph G_{i-1} and ds solution D_{i-1}
			 */
			Graph<Integer, String> gIPre = graphList.get(previousPos);
			List<Integer> dIPre = solutionList.get(previousPos);

			gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
			if (AlgorithmUtil.isAllDominated(gDominatedMap)) {
				break;
			}
			undominatedVertices = GreedyDSUtil.getUndominatedVertices(gDominatedMap);
			v = GreedyDSUtil.getTheFirstItemInOrderedListAndInAnotherList(vList, undominatedVertices);
			/*
			 * if vi is not dominated, i) get its highest utility neighbor
			 * vi;ii) put ui in DI; and iii) construct a GI with N[ui]; and iv)
			 * if it is a moment of regret (in michael's original idea, it
			 * always true),
			 * 
			 * divide gI into V and DI, divide V into Vk (the k highest degree
			 * vertices in V) and Vl (vertices not in Vk), divide DI into
			 * Dk(Vk's neigbors which do not have neighbors in Vl), and Dl(the
			 * neighbors of Vl)
			 * 
			 * take Dk, GI, r as parameters to invoke dds fpt to get a solution
			 * DDSI; v) set the smaller solution (of DI and DDSI ) to be
			 * solution for GI
			 */

			// i)
			u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, gDominatedMap);
			// ii)
			// AlgorithmUtil.addElementToList(dI, u);
			dI = new ArrayList<Integer>();
			dI.addAll(dIPre);
			AlgorithmUtil.addElementToList(dI, u);

			// iii)
			// GreedyDSUtil.addCloseNeighborToSubgraph(g, gI, u);
			gI = AlgorithmUtil.copyGraph(gIPre);
			uList = GreedyDSUtil.putUVInList(v, u);
			AlgorithmUtil.prepareGraph(g, gI, uList);

			// MomentRegretReturn<Integer, String> mrr = null;
			List<Integer> ddsI = null;
			if (isMomentOfRegret()) {

				/*
				 * back k vertices, if the number of vertices that has been
				 * visited is less than k, we will back the the smallest number,
				 * in another words, we only back to 0 rather than negative
				 * position
				 */

				backKIndex = currentIndex - k;
				if (backKIndex < 0) {
					backKIndex = 0;
				}

				// log.debug(i + ":dds fpt");
				previousPos = backKIndex % queueSize;

				// mrr = GreedyDSUtil.applyAtMomentOfRegret(vList, dI, gI,
				// this.indicator, k, this.rUpperBoundary,
				// this.runningTimeMap);

				ddsI = GreedyDSUtil.invokeDDSFPT(previousPos, solutionList, graphList, gI, dI, ddsI, this.indicator,
						this.rUpperBoundary, this.runningTimeMap, true);

			}

			// if ((mrr.getDds() != null && mrr.getDds().size() > 0) &&
			// mrr.getDds().size() < dI.size()) {
			// dI = mrr.getDds();
			// Graph<Integer, String> gICopyNextRound = mrr.getGraph();
			// GreedyDSUtil.addCloseNeighborToSubgraph(g, gICopyNextRound, dI);
			// gI = gICopyNextRound;
			// }

			if ((ddsI != null && ddsI.size() > 0) && ddsI.size() < dI.size()) {
				dI = ddsI;
				// Graph<Integer, String> gICopyNextRound = mrr.getGraph();
				// GreedyDSUtil.addCloseNeighborToSubgraph(g, gICopyNextRound,
				// dI);
				// gI = gICopyNextRound;
			}

			// vi)
			GreedyDSUtil.setStatusOfIthVertexInQueue(currentPos, solutionList, dI, graphList, gI);

			// vii)
			previousPos = currentPos;

			// viii)
			gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);

		} while (!AlgorithmUtil.isAllDominated(gDominatedMap));

		// do guarantee, minimal, ls at the last step;
		List<Integer> gDI = GreedyDSUtil.useGreedyToCalcDS(g, this.runningTimeMap);
		if (dI.size() < gDI.size()) {
			this.dominatingSet = dI;
		} else {
			this.dominatingSet = gDI;
		}

		GreedyDSUtil.applyMinimal(g, this.dominatingSet, this.runningTimeMap);
		GreedyDSUtil.applyLS(g, this.dominatingSet, this.runningTimeMap);

	}

	private boolean isMomentOfRegret() {
		IMomentOfRegret mor = new MomentOfRegretDelta();
		return mor.isMomentOfRegret(null);

	}

}
