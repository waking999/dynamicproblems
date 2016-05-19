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
 * order list L by degree(D) and get highest utility(U) neighbor
 * 
 * @author kwang1
 *
 */
public class GreedyDSM0WW implements ITask, IGreedyDS<Integer> {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSM0WW.class);
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

	Map<Integer, Boolean> dominatedMap;
	/**
	 * a map keeps pair of(vertex, vote)
	 */
	Map<Integer, Float> voteMap;
	/**
	 * a map keeps pair of(vertex, weight)
	 */
	Map<Integer, Float> weightMap;

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	/**
	 * parameters (k,r) for the fpt algroithm
	 */
	private int k;
	private int rUpperBoundary;

	public GreedyDSM0WW(List<String[]> adjacencyMatrix, int k, int rUpperBoundary) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.g = AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix);

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
		this.runningTimeMap.put("Total", runningTime);

	}

	private void initialization() {
		dominatingSet = new ArrayList<Integer>();
		this.runningTimeMap = new HashMap<String, Long>();
		GreedyDSUtil.initRunningTimeMap(this.runningTimeMap);
	}

	private void greedy()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		Collection<Integer> vertices = g.getVertices();
		int verticesSize = vertices.size();

		voteMap = new HashMap<Integer, Float>(verticesSize);
		weightMap = new HashMap<Integer, Float>(verticesSize);
		dominatedMap = new HashMap<Integer, Boolean>(verticesSize);

		for (Integer v : vertices) {
			dominatedMap.put(v, false);
			int degree = g.degree(v);
			float vote = 1.0f / (1 + degree);
			voteMap.put(v, vote);
			weightMap.put(v, vote);
		}
		// calculate weight for each vertex
		for (Integer v : vertices) {
			Collection<Integer> vNeig = g.getNeighbors(v);
			float weightv = weightMap.get(v);
			for (Integer u : vNeig) {
				float voteu = voteMap.get(u);
				weightv += voteu;
			}
			weightMap.put(v, weightv);
		}

		/* sort a list of vertex from lowest vote to highest */
		List<Integer> vList = OrderPackageUtil.getVertexListWeightAsc(g, weightMap);

		/*
		 * i) get the first vertex v0 in the list(the lowest degree); and ii)
		 * get its highest utility neighbor u0 and iii) put u0 in D0; and iv)
		 * initialize a G0 with N[u0];
		 * 
		 */
		// i)
		int i = 0;
		Integer v = vList.get(i);
		// ii)
		// Map<Integer, Boolean> gDominatedMap =
		// GreedyDSUtil.presetDominatedMap(vList);
		Integer u = AlgorithmUtil.getHighestWeightNeighborOfAVertex(v, g, weightMap);

		AlgorithmUtil.adjustWeight(g, dominatedMap, weightMap, voteMap, u);

		// iii)
		List<Integer> dI = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(dI, u);
		// iv)
		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		GreedyDSUtil.addCloseNeighborToSubgraph(g, gI, u);

		List<Integer> undominatedVertices = null;
		do {
			/*
			 * this dominated map is for the whole graph to satisfy quit
			 * condition
			 */
			dominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
			if (AlgorithmUtil.isAllDominated(dominatedMap)) {
				break;
			}
			undominatedVertices = GreedyDSUtil.getUndominatedVertices(dominatedMap);
			/* get the undominated vertex of the lowest degree */
			v = GreedyDSUtil.getTheFirstItemInOrderedListAndInAnotherList(vList, undominatedVertices);

			if (!dominatedMap.get(v)) {
				/*
				 * if vi is not dominated, i) get its highest utility neighbor
				 * vi;ii) put ui in DI; and iii) construct a GI with N[ui]; and
				 * iv) if it is a moment of regret (in michael's original idea,
				 * it always true),
				 * 
				 * divide gI into V and DI, divide V into Vk (the k highest
				 * degree vertices in V) and Vl (vertices not in Vk), divide DI
				 * into Dk(Vk's neigbors which do not have neighbors in Vl), and
				 * Dl(the neighbors of Vl)
				 * 
				 * take Dk, GI, r as parameters to invoke dds fpt to get a
				 * solution DDSI; v) set the smaller solution (of DI and DDSI )
				 * to be solution for GI
				 */

				// i)
				u = AlgorithmUtil.getHighestWeightNeighborOfAVertex(v, g, weightMap);
				AlgorithmUtil.adjustWeight(g, dominatedMap, weightMap, voteMap, u);
				// ii)
				AlgorithmUtil.addElementToList(dI, u);
				// iii)
				GreedyDSUtil.addCloseNeighborToSubgraph(g, gI, u);

				MomentRegretReturn<Integer, String> mrr = null;
				if (isMomentOfRegret()) {
					mrr = GreedyDSUtil.applyAtMomentOfRegret(vList, dI, gI, this.indicator, k, this.rUpperBoundary,
							this.runningTimeMap, false);

				}

				if ((mrr.getDds() != null && mrr.getDds().size() > 0) && mrr.getDds().size() < dI.size()) {
					dI = mrr.getDds();
					Graph<Integer, String> gICopyNextRound = mrr.getGraph();
					GreedyDSUtil.addCloseNeighborToSubgraph(g, gICopyNextRound, dI);
					gI = gICopyNextRound;
				}

				// viii)
				dominatedMap = GreedyDSUtil.getDominatedMap(g, dI);

			}

		} while (!AlgorithmUtil.isAllDominated(dominatedMap));

		this.dominatingSet = dI;
		//GreedyDSUtil.applyMinimal(g, this.dominatingSet, this.runningTimeMap);
	}

	private boolean isMomentOfRegret() {
		IMomentOfRegret mor = new MomentOfRegretTrue();
		return mor.isMomentOfRegret(null);

	}

}
