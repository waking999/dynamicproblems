/**
 * 
 * 1) from lowest utility to highest
 * 2) and reduction rules
 * 3) and guarantee (compare with greedy native)
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
@Deprecated
public class GreedyDSVS14 implements IGreedyDS, ITask {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSVS14.class);
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
	private int numOfVertices;

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

	public GreedyDSVS14(String indicator, List<String[]> am, int k, int r) {
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
		long start = System.nanoTime();
		
		initialization();
		
		preprocess();

		start();
		long end = System.nanoTime();

		this.runningTime = end - start;
	}

	private TreeMap<Integer, Integer> vdOriginalMap;

	private void initialization() {

		this.numOfVertices = am.size();

		Graph<Integer, Integer> gOriginal0 = AlgorithmUtil.prepareGraph(am);

		Graph<Integer, Integer> gOriginal1 = AlgorithmUtil.applySingleVertexReductionRule(this.numOfVertices,
				gOriginal0);
		gOriginal1 = AlgorithmUtil.applyPairVerticesReductionRule(this.numOfVertices, gOriginal1);

		this.gOriginal = gOriginal1;

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

	// private List<Integer> getHighestUtilityNeighborOfVertices(int k,
	// List<Integer> vList,
	// TreeMap<Integer, Integer> vdMap) {
	// List<Integer> vListNeig = new ArrayList<Integer>();
	// List<Integer> highestNeigs = new ArrayList<Integer>();
	// for (Integer v : vList) {
	// Collection<Integer> vNeg = gOriginal.getNeighbors(v);
	// vListNeig.addAll(vNeg);
	// }
	//
	// Set<Integer> keySet = vdMap.descendingKeySet();
	//
	// int count = 0;
	// for (Integer key : keySet) {
	// if (vListNeig.contains(key)) {
	// highestNeigs.add(key);
	// count++;
	// }
	// if (count >= k) {
	// break;
	// }
	//
	// }
	//
	// return highestNeigs;
	// }

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

					// get u,w's utility
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

			addDominatingVertexAndItsNeigbors(this.dsInitial, this.initialVertices, v);
		}

		// AlgorithmUtil.prepareGraph(am, gInitial, initialVertices);
		AlgorithmUtil.preparGraph(numOfVertices, gOriginal, gInitial, initialVertices);

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
			Graph<Integer, Integer> gI = AlgorithmUtil.copyGraph(gInitial);
			Graph<Integer, Integer> gIStar = AlgorithmUtil.copyGraph(gI);
			
			getKVerticesAndTheirDS(undominatedVertices, undomiantedVerticesSize, kVerticesDS, kVertices);

			AlgorithmUtil.preparGraph(this.numOfVertices, gOriginal, gI, kVertices);
			List<Integer> dsInitialCopy = new ArrayList<Integer>();
			dsInitialCopy.addAll(dsInitial);

			List<Integer> ag1DS = useGreedyToCalcDS(gI);

			int greedyDiffSize = ag1DS.size() - dsInitialCopy.size();
			greedyDiffSize = greedyDiffSize >= 0 ? greedyDiffSize : 0;

			DDSFPTV0 ag2 = useDDSFPTSubToCalcDS(gOriginalVerticeSize, kVerticesDS, kVertices, gI, greedyDiffSize);
			List<Integer> ag2DS = ag2.getDs2();

			if (ag1DS.size() < ag2DS.size()) {
				this.dsInitial = ag1DS;
			} else {
				this.dsInitial = ag2DS;
			}
			
			this.dsInitial=AlgorithmUtil.minimal(gIStar,this.dsInitial);
			this.dsInitial = AlgorithmUtil.grasp(gIStar, this.dsInitial);

			List<Integer> verticesToAddInGraph = markDominatedVertices(undominatedVertices, dsInitialCopy);

			undomiantedVerticesSize = undominatedVertices.size();

			if (undomiantedVerticesSize == 0) {
				break;
			}

			AlgorithmUtil.preparGraph(this.numOfVertices, gOriginal, gInitial, verticesToAddInGraph);

			gI=null;
			gIStar=null;
		}

		this.ds=AlgorithmUtil.minimal(this.gOriginal,this.dsInitial);
		this.ds = AlgorithmUtil.grasp(this.gOriginal, this.dsInitial);
	}

	

	private List<Integer> markDominatedVertices(Collection<Integer> undominatedVertices, List<Integer> dsInitialCopy) {
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
		return verticesToAddInGraph;
	}


	private List<Integer> useGreedyToCalcDS(Graph<Integer, Integer> gI) throws InterruptedException {

		GreedyNative ag = new GreedyNative(gI);
		ag.run();

		GreedyVote ag1 = new GreedyVote(gI);
		ag1.run();

		List<Integer> ds = ag.getDominatingSet();
		int dsSize = ds.size();
		List<Integer> ds1 = ag1.getDominatingSet();
		int ds1Size = ds1.size();
		if (dsSize <= ds1Size) {
			return ds;
		} else {
			return ds1;
		}

	}

	private DDSFPTV0 useDDSFPTSubToCalcDS(int gOriginalVerticeSize, List<Integer> kVerticesDS, List<Integer> kVertices,
			Graph<Integer, Integer> gI, int greedyDiffSize)
					throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		int paramR = Math.min(kVerticesDS.size(), r);
		paramR = Math.min(greedyDiffSize, paramR);

		DDSFPTV0 ag = new DDSFPTV0(indicator, gI, dsInitial, paramR);

		ag.setConsiderableCandidateVertices4DS(kVerticesDS);
		ag.setOriginalVertexNum(gOriginalVerticeSize);
		ag.computing();

		return ag;

	}

	private void getKVerticesAndTheirDS(Collection<Integer> undominatedVertices, int undomiantedVerticesSize,
			List<Integer> kVerticesDS, List<Integer> kVertices) {
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

		

	}

}