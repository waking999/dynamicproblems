/**
 * implement Michael's original idea but with utility rather than degree and reduction rules
 */

package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GreedyDSV3 implements IGreedyDS, ITask {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSV3.class);
	private long runningTime;

	@Override
	public Result run() throws InterruptedException {
		long threadId = Thread.currentThread().getId();
		Result r = null;
		try {
			computing();

			Thread.sleep(1000);

			r = getResult(threadId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	public long getRunningTime() {
		return runningTime;
	}

	public String getIndicator() {
		return indicator;
	}

	private TaskLock lock;

	@Override
	public TaskLock getLock() {
		return lock;
	}

	@Override
	public void setLock(TaskLock lock) {
		this.lock = lock;
	}

	private String indicator;
	private List<String[]> am;

	private List<Integer> ds;

	// used for pre-process
	private List<Integer> dsInitial;
	private List<Integer> initialVertices;

	public List<Integer> getDs() {
		return ds;
	}

	private Map<Integer, Boolean> dominatedMap;
	/**
	 * the number of edge deletion
	 */
	private int k;
	/**
	 * the incremental size of dominating set
	 */
	private int r;
	/**
	 * the original graph
	 */
	private Graph<Integer, Integer> gOriginal;

	/**
	 * the graph after pre-process
	 */
	private Graph<Integer, Integer> gInitial;

	public GreedyDSV3(String indicator, List<String[]> am, int k, int r) {
		this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		this.ds = null;
		this.dsInitial = null;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public void setAm(List<String[]> am) {
		this.am = am;
	}

	public void setK(int k) {
		this.k = k;
	}

	public void setR(int r) {
		this.r = r;
	}

	public Result getResult(long threadId) {
		Result result = new Result();
		result.setIndex(threadId);

		StringBuffer sb = new StringBuffer();

		sb.append(",").append(this.ds.size()).append(",").append(k).append(",").append(r).append(", ")
				.append(this.runningTime);
		result.setString(sb.toString());
		return result;
	}

	public Result getResult() {
		Result result = new Result();

		StringBuffer sb = new StringBuffer();

		sb.append(",").append(this.ds.size()).append(",").append(k).append(",").append(r).append(", ")
				.append(this.runningTime);
		result.setString(sb.toString());
		return result;
	}

	public void computing()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {

		initialization();
		long start = System.nanoTime();
		preprocess();

		start();
		long end = System.nanoTime();

		this.runningTime = end - start;
	}

	private TreeMap<Integer, Integer> vdOriginalMap;

	private void initialization() {

		this.gOriginal = AlgorithmUtil.prepareGraph(am);

		this.ds = new ArrayList<Integer>();
		this.dsInitial = new ArrayList<Integer>();

		dominatedMap = new HashMap<Integer, Boolean>();
		Collection<Integer> vertices = gOriginal.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
		}
		// order vertex according to their degree from lowest to highest
		vdOriginalMap = AlgorithmUtil.sortVertexMapAccordingToUtilityASC(gOriginal, dominatedMap);

	}

	private Integer getHighestUtilityNeighborOfAVertex(Integer v, TreeMap<Integer, Integer> vdMap) {
		Collection<Integer> vNeg = gOriginal.getNeighbors(v);
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

	private void addDominatingVertexAndItsNeigbors(List<Integer> ds, List<Integer> potentialVList, Integer v) {
		addDominatingVertex(ds, potentialVList, v);
		Collection<Integer> vNegb = gOriginal.getNeighbors(v);
		for (Integer w : vNegb) {
			addDominatedVertex(potentialVList, w);
		}
	}

	private void addDominatingVertex(List<Integer> ds, List<Integer> potentialVList, Integer u) {
		AlgorithmUtil.addElementToList(ds, u);
		addDominatedVertex(potentialVList, u);
	}

	private void addDominatedVertex(List<Integer> potentialVList, Integer u) {
		AlgorithmUtil.addElementToList(potentialVList, u);
		dominatedMap.put(u, true);
	}

	private void preprocess() {
		this.gInitial = new SparseMultigraph<Integer, Integer>();

		initialVertices = new ArrayList<Integer>();

		Collection<Integer> vertices = gOriginal.getVertices();

		for (Integer v : vertices) {

			if (!this.dominatedMap.get(v)) {
				int degree = gOriginal.degree(v);

				if (degree == 0) {
					addDominatingVertex(this.dsInitial, initialVertices, v);
				} else if (degree == 1) {
					Collection<Integer> vNegb = gOriginal.getNeighbors(v);
					/*
					 * add v's neighbor u to gOperated and dominating set add
					 * u's neighbors to gOperated and mark them
					 * dominated(including v)
					 */
					for (Integer u : vNegb) {

						addDominatingVertexAndItsNeigbors(this.dsInitial, this.initialVertices, u);
					}

				}
			}

		}

		for (Integer v : vertices) {
			if (!dominatedMap.get(v)) {
				int degree = gOriginal.degree(v);

				if (degree == 2) {
					// get v's neighbor u,w
					Collection<Integer> vNegb = gOriginal.getNeighbors(v);

					List<Integer> vNegbList = new ArrayList<Integer>();
					for (Integer x : vNegb) {
						vNegbList.add(x);
					}

					Integer u = vNegbList.get(0);
					Integer w = vNegbList.get(1);

					// get u,w's degree
					int uUtility = AlgorithmUtil.getVertexUtility(gOriginal, u, dominatedMap);
					int wUtility = AlgorithmUtil.getVertexUtility(gOriginal, w, dominatedMap);

					Collection<Integer> uNegb = getClosedNeighborsWithoutV(v, u);

					Collection<Integer> wNegb = getClosedNeighborsWithoutV(v, w);

					if (uUtility > wUtility) {
						// u has the higher priority to be added into
						// dominating
						// set than w
						addHigherNeighborOfVToDS(this.dsInitial, v, u, w, uNegb, wNegb, initialVertices, uUtility,
								wUtility);
					} else {
						addHigherNeighborOfVToDS(this.dsInitial, v, w, u, wNegb, uNegb, initialVertices, wUtility,
								uUtility);
					}

				}
			}
		}

		if (initialVertices.isEmpty()) {
			

		
			Integer v = this.vdOriginalMap.lastKey();

			addDominatingVertexAndItsNeigbors(this.dsInitial,this.initialVertices, v);
		}
		
		AlgorithmUtil.prepareGraph(am, gInitial, initialVertices);

	}

	private Collection<Integer> getClosedNeighborsWithoutV(Integer v, Integer w) {
		Collection<Integer> wNegb = gOriginal.getNeighbors(w); // N(w)
		wNegb.add(w); // N[w]
		wNegb.remove(v); // N[w]\v
		return wNegb;
	}

	private void addHigherNeighborOfVToDS(List<Integer> ds, Integer v, Integer u, Integer w, Collection<Integer> uNegb,
			Collection<Integer> wNegb, List<Integer> initalVerteices, int uUtility, int wUtility) {
		if (AlgorithmUtil.isAllDominated(dominatedMap, wNegb) && (wUtility - 1) == 0) {

			addNeighborOfVToDS(ds, v, u, uNegb, initalVerteices);
		} else if (AlgorithmUtil.isAllDominated(dominatedMap, uNegb) && (uUtility - 1) == 0) {
			addNeighborOfVToDS(ds, v, w, wNegb, initalVerteices);
		} else {
			addNeighborOfVToDS(ds, v, u, uNegb, initalVerteices);
		}
	}
	
	private void addNeighborOfVToDS(List<Integer> ds, Integer v, Integer w, Collection<Integer> wNegb,
			List<Integer> initalVerteices) {
		/*
		 * if N[u]\v (including u) are dominated: add w to gOperated and
		 * dominating set and mark it dominated add w's neighbors to gOperated
		 * and mark them dominated(including v)
		 */
		AlgorithmUtil.addElementToList(ds, w);

		for (Integer x : wNegb) {
			addDominatedVertex(initalVerteices, x);
		}
		addDominatedVertex(initalVerteices, v);
	}

	private void start()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {
		Collection<Integer> gOrigialVertices = gOriginal.getVertices();
		int gOriginalVerticeSize = gOrigialVertices.size();
		Collection<Integer> gInitialVertices = gInitial.getVertices();

		Collection<Integer> undominatedVertices = CollectionUtils.subtract(gOrigialVertices, gInitialVertices);

		int undomiantedVerticesSize = undominatedVertices.size();

		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {
			List<Integer> kVerticesDS = new ArrayList<Integer>();
			List<Integer> kVertices = new ArrayList<Integer>();
			Graph<Integer, Integer> gI = AlgorithmUtil.copyGrapy(gInitial);

			int fromIndex = 0;
			int toIndex = Math.min(k, undomiantedVerticesSize);
			TreeMap<Integer, Integer> vdMap = AlgorithmUtil.sortVertexMapAccordingToUtilityIncludeASC(gOriginal,
					dominatedMap, undominatedVertices);
			TreeMap<Integer, Integer> allVdMap = AlgorithmUtil.sortVertexMapAccordingToUtilityASC(gOriginal, dominatedMap);

			List<Integer> vList = AlgorithmUtil.getVertexListFromMap(vdMap, fromIndex, toIndex);

			for (Integer u : vList) {
				Integer w = getHighestUtilityNeighborOfAVertex(u, allVdMap);
				AlgorithmUtil.addElementToList(kVerticesDS, w);
				AlgorithmUtil.addElementToList(kVertices, w);
				AlgorithmUtil.addElementToList(kVertices, u);

			}

			AlgorithmUtil.prepareGraph(am, gI, kVertices);

			List<Integer> dsInitialCopy = new ArrayList<Integer>();
			dsInitialCopy.addAll(dsInitial);

			int paramR = Math.min(kVerticesDS.size(), r);

			DDSFPT ag = new DDSFPT(indicator, gI, dsInitial, paramR);

			ag.setConsiderableCandidateVertices4DS(kVerticesDS);
			ag.setOriginalVertexNum(gOriginalVerticeSize);
			ag.computing();

			dsInitial = ag.getDs2();
			Collection<Integer> addedDSVertices = CollectionUtils.subtract(dsInitial, dsInitialCopy);
			List<Integer> verticesToAddInGraph = new ArrayList<Integer>();
			for (Integer v : addedDSVertices) {
				dominatedMap.put(v, true);
				AlgorithmUtil.addElementToList(verticesToAddInGraph, v);
				undominatedVertices.remove(v);

				Collection<Integer> vNeg = gOriginal.getNeighbors(v);
				for (Integer u : vNeg) {
					dominatedMap.put(u, true);

					if (!gInitial.containsVertex(u)) {
						AlgorithmUtil.addElementToList(verticesToAddInGraph, u);
					}
				}
				undominatedVertices.removeAll(vNeg);

			}

			undomiantedVerticesSize = undominatedVertices.size();

			if (undomiantedVerticesSize == 0) {
				break;
			}

			AlgorithmUtil.prepareGraph(am, gInitial, verticesToAddInGraph);

		}

		this.ds = this.dsInitial;
	}

}
