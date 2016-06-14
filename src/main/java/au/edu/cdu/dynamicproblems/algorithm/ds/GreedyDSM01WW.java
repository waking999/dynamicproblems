package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
 * implement fpt2 idea
 * 
 * order list L by Vote(V) and get highest Vote(V) neighbor
 * 
 * @author kwang1
 *
 */
public class GreedyDSM01WW implements ITask, IGreedyDS<Integer> {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSM01WW.class);
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

	public GreedyDSM01WW(List<String[]> adjacencyMatrix, int k, int rUpperBoundary) {
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
//	/**
//	 * move degree 2 vertices to the top of the list;
//	 * @param vList
//	 * @param g
//	 * @return
//	 */
//	private List<Integer> reorderVList(List<Integer> vList,Graph<Integer,String> g){
//		List<Integer> rtnList=new ArrayList<Integer>();
//		
//		for(Integer v: vList){
//			if(g.degree(v)==2){
//				rtnList.add(v);
//			}
//		}
//		
//		for(Integer v:vList){
//			if(!rtnList.contains(v)){
//				rtnList.add(v);
//			}
//		}
//		
//		return rtnList;
//	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void greedy()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		Graph<Integer, String> gOriginal = AlgorithmUtil.prepareGenericGraph(adjacencyMatrix);

		/* apply poly-rr */
		Graph<Integer, String> g = GreedyDSUtil.applyPolyReductionRules(gOriginal, this.runningTimeMap,
				GreedyDSUtil.POLY_RR_2_VALVE);

		Collection<Integer> vertices = g.getVertices();
		int verticesSize = vertices.size();

		voteMap = new HashMap<Integer, Float>(verticesSize);
		weightMap = new HashMap<Integer, Float>(verticesSize);

		for (Integer v : vertices) {
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

		/* apply degree-rr */
		DegreeRRReturn drrr = GreedyDSUtil.applyDegreeReductionRules(g, this.runningTimeMap);

		Map<Integer, Boolean> gDominatedMap = drrr.getDominatedMap();
		List<Integer> dI = drrr.getDsAfterDegreeRR();

		for (Integer v : vertices) {
			AlgorithmUtil.adjustWeight(g, gDominatedMap, weightMap, voteMap, v);
		}
		/*
		 * it is possible that a graph has been dominated at this point, so a if
		 * branch is needed here
		 */

		// sort a list of vertex from lowest vote to highest
		List<Integer> vList = OrderPackageUtil.getVertexListWeightAsc(g, weightMap);
		//vList=reorderVList(vList,g);
		// List<Integer> vList = OrderPackageUtil.getVertexListDegreeDesc(g);

		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		// AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix, gI, dI);
		GreedyDSUtil.addCloseNeighborToSubgraph(g, gI, dI);
		// List<Integer> undominatedVertices = null;
		Integer v;
		Integer u;
		int i = -1;
		//Set<Map<String,List<Integer>>> historyCandidateDomVerMap=new HashSet<Map<String,List<Integer>>>();
		Set<Collection<Integer>> historyVertexCover=new HashSet<Collection<Integer>>();
		do {
			i++;
			gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
			if (AlgorithmUtil.isAllDominated(gDominatedMap)) {
				break;
			}
			// undominatedVertices =
			// GreedyDSUtil.getUndominatedVertices(gDominatedMap);
			// v =
			// GreedyDSUtil.getTheFirstItemInOrderedListAndInAnotherList(vList,
			// undominatedVertices);
			v = vList.get(i);
			if (!gDominatedMap.get(v)) {
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
				// u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g
				// ,gDominatedMap);

				AlgorithmUtil.adjustWeight(g, gDominatedMap, weightMap, voteMap, u);
				// ii)
				AlgorithmUtil.addElementToList(dI, u);
				// iii)
				// GreedyDSUtil.addCloseNeighborToSubgraph(g, gI, u);
				// only add u,v to new graph
				List<Integer> addedToNewGraph = new ArrayList<Integer>();
				addedToNewGraph.add(u);
				addedToNewGraph.add(v);
				AlgorithmUtil.prepareGraph(g, gI, addedToNewGraph);

				MomentRegretReturn<Integer, String> mrr = null;
				if (isMomentOfRegret()) {
					mrr = GreedyDSUtil.applyAtMomentOfRegret(vList, dI, gI, this.indicator, k, this.rUpperBoundary,
							this.runningTimeMap, true,historyVertexCover);

				}

				if ((mrr.getDds() != null && mrr.getDds().size() > 0) && mrr.getDds().size() < dI.size()) {
					dI = mrr.getDds();
					// Graph<Integer, String> gICopyNextRound = mrr.getGraph();
					// GreedyDSUtil.addCloseNeighborToSubgraph(g,
					// gICopyNextRound, dI);
					// gI = gICopyNextRound;
				}

				// viii)
				gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
			} else {
				List<Integer> addedToNewGraph = new ArrayList<Integer>();

				addedToNewGraph.add(v);
				AlgorithmUtil.prepareGraph(g, gI, addedToNewGraph);
			}
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
