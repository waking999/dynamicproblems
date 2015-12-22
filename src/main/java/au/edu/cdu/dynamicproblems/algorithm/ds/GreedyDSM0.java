package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.CollectionUtils;
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

	private void greedy() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		/* sort a list of vertex from lowest degree to highest */
		List<Integer> vList = OrderPackageUtil.getVertexListDegreeAsc(g);

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
		Map<Integer, Boolean> gDominatedMap = GreedyDSUtil.presetDominatedMap(vList);
		Integer u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, gDominatedMap);
		// iii)
		List<Integer> dI = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(dI, u);
		// iv)
		Graph<Integer, String> gI = new SparseMultigraph<Integer, String>();
		addCloseNeighborToSubgraph(g, gI, u);

		gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
		List<Integer> undominatedVertices = getUndominatedVertices(gDominatedMap);

		// for (i = 1; i < this.numOfVertices; i++) {

		do {
			// i++;
			/*
			 * this dominated map is for the whole graph to satisfy quit
			 * condition
			 */
			gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
			if (AlgorithmUtil.isAllDominated(gDominatedMap)) {
				break;
			}
			/* get the ith vertex in the sorted list */
			// v = vList.get(i);
			v = getTheFirstItemInOrderedListAndInAnotherList(vList, undominatedVertices);

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
				u = AlgorithmUtil.getHighestUtilityNeighborOfAVertex(v, g, gDominatedMap);
				// ii)
				AlgorithmUtil.addElementToList(dI, u);
				// iii)
				addCloseNeighborToSubgraph(g, gI, u);

				List<Integer> ddsI = null;
				Graph<Integer, String> gICopyNextRound = null;
				if (isMomentOfRegret()) {
					int gISize = gI.getVertexCount();
					int dISize = dI.size();
					int dominatedVerticesSize = gISize - dISize;

					// if the number of dominated vertices is less than k, we
					// only go back this number steps rather than k steps.
					int kCount=Math.min(k, dominatedVerticesSize);
					

					Collection<Integer> gIVertices = gI.getVertices();

					// V
					Collection<Integer> dominatedVertices = CollectionUtils.subtract(gIVertices, dI);
					// Vk
					List<Integer> kVertices = new ArrayList<Integer>();

					int count = kCount;
					for (int j = this.numOfVertices - 1; j >= 0; j--) {
						if (count > 0) {
							Integer w = vList.get(j);
							if (dominatedVertices.contains(w)) {
								AlgorithmUtil.addElementToList(kVertices, w);
								count--;
							}
						}
					}
					// Vl
					dominatedVertices.removeAll(kVertices);

					// Dk
					List<Integer> dominatingKVertices = new ArrayList<Integer>();
					for (Integer w : kVertices) {
						Collection<Integer> wNeig = gI.getNeighbors(w);
						Collection<Integer> dW = CollectionUtils.intersection(wNeig, dI);
						for (Integer dw : dW) {
							Collection<Integer> dwNeig = gI.getNeighbors(dw);
							Collection<Integer> dwIntsec = CollectionUtils.intersection(dwNeig, dominatedVertices);
							if (dwIntsec.isEmpty()) {
								AlgorithmUtil.addElementToList(dominatingKVertices, dw);
							}
						}

					}

					int dominatingKVerticesSize = dominatingKVertices.size();

					/* being less than 2 is too trivial */
					if (dominatingKVerticesSize >= 2) {
						long start = System.nanoTime();
						// Dl
						List<Integer> dICopy = new ArrayList<Integer>();
						dICopy.addAll(dI);
						dICopy.removeAll(dominatingKVertices);

						// a copy of gI for next round
						gICopyNextRound = AlgorithmUtil.copyGraph(gI);
						for (Integer w : kVertices) {
							gICopyNextRound.removeVertex(w);
						}

						for (Integer w : dominatingKVertices) {
							gICopyNextRound.removeVertex(w);
						}

						// a copy of gI for dds fpt because the graph will
						// be
						// modified;
						Graph<Integer, String> gICopyDDS = AlgorithmUtil.copyGraph(gI);
						// log.debug("m="+dominatingKVerticesSize);
						int realRUpperBoundary = Math.min(dominatingKVerticesSize - 1, rUpperBoundary);

						DDSFPT ag = new DDSFPT(indicator, gICopyDDS, dICopy, realRUpperBoundary);

						ag.setConsiderableCandidateVertices4DS(dominatedVertices);
						ag.computing();

						ddsI = ag.getDs2();

						long end = System.nanoTime();
						Long existingRunningTime = runningTimeMap.get(AlgorithmUtil.RUNNING_TIME_DDS);
						if (existingRunningTime == null) {
							existingRunningTime = Long.valueOf(0);
						}
						runningTimeMap.put(AlgorithmUtil.RUNNING_TIME_DDS, existingRunningTime + (end - start));
					}

				}

				if ((ddsI != null && ddsI.size()>0) && ddsI.size() < dI.size()) {
					dI = ddsI;

					addCloseNeighborToSubgraph(g, gICopyNextRound, dI);
					gI = gICopyNextRound;
				}

				// viii)
				gDominatedMap = GreedyDSUtil.getDominatedMap(g, dI);
				// if (AlgorithmUtil.isAllDominated(gDominatedMap)) {
				// break;
				// }
				undominatedVertices = getUndominatedVertices(gDominatedMap);

			}

		} while (!AlgorithmUtil.isAllDominated(gDominatedMap));

		this.dominatingSet = dI;

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

//	private <V> String getUndominatedNum(Map<V, Boolean> dominatedMap) {
//		StringBuffer sb = new StringBuffer();
//
//		List<V> undominatedVertices = getUndominatedVertices(dominatedMap);
//
//		int count = undominatedVertices.size();
//
//		for (V v : undominatedVertices) {
//			sb.append(v.toString()).append(AlgorithmUtil.COMMA);
//		}
//
//		sb.append(":").append(count);
//		return sb.toString();
//	}

	private <V> List<V> getUndominatedVertices(Map<V, Boolean> dominatedMap) {
		List<V> rtnList = new ArrayList<V>();

		Set<V> keySet = dominatedMap.keySet();

		for (V key : keySet) {
			if (!dominatedMap.get(key)) {
				AlgorithmUtil.addElementToList(rtnList, key);
			}
		}

		return rtnList;
	}

	private void addCloseNeighborToSubgraph(Graph<Integer, String> gRef, Graph<Integer, String> gI, Integer u) {
		List<Integer> verticesToAddInGraph = new ArrayList<Integer>();

		Collection<Integer> uNeig = gRef.getNeighbors(u);
		for (Integer w : uNeig) {
			if (!gI.containsVertex(w)) {
				AlgorithmUtil.addElementToList(verticesToAddInGraph, w);
			}
		}
		AlgorithmUtil.addElementToList(verticesToAddInGraph, u);

		AlgorithmUtil.prepareGraph(gRef, gI, verticesToAddInGraph);
	}

	private void addCloseNeighborToSubgraph(Graph<Integer, String> gRef, Graph<Integer, String> gI,
			List<Integer> uList) {
		List<Integer> verticesToAddInGraph = new ArrayList<Integer>();

		for (Integer u : uList) {
			Collection<Integer> uNeig = gRef.getNeighbors(u);
			for (Integer w : uNeig) {
				if (!gI.containsVertex(w)) {
					AlgorithmUtil.addElementToList(verticesToAddInGraph, w);
				}
			}
			AlgorithmUtil.addElementToList(verticesToAddInGraph, u);
		}
		AlgorithmUtil.prepareGraph(gRef, gI, verticesToAddInGraph);
	}

	private Integer getTheFirstItemInOrderedListAndInAnotherList(Collection<Integer> orderedList,
			Collection<Integer> anotherLst) {
		for (Integer v : orderedList) {
			if (anotherLst.contains(v)) {
				return v;
			}
		}
		return null;
	}
}
