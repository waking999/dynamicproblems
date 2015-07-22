package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
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

public class GreedyDSReduction implements IGreedyDS, ITask {
	// public static final int STRATEGY_DEGREE_DESC = 1;
	// public static final int STRATEGY_DEGREE_ASC = 2;

	public static final int STRATEGY_UTILITY_DESC = 3;
	public static final int STRATEGY_UTILITY_ASC = 4;

	private static final int PARTITION_SIZE = 20;

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSReduction.class);
	private long runningTime;

	// private boolean withReductionRule;

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

	private int strategy;

	public GreedyDSReduction(String indicator, List<String[]> am, int k, int r, int strategy,
			boolean withReductionRule) {
		this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		this.ds = null;
		this.dsInitial = null;
		this.strategy = strategy;
		// this.withReductionRule = withReductionRule;
	}

	public GreedyDSReduction(String indicator, int strategy, boolean withReductionRule) {
		this.indicator = indicator;

		this.ds = null;
		this.dsInitial = null;
		this.strategy = strategy;
		// this.withReductionRule = withReductionRule;
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

	private void initialization() {

		this.gOriginal = AlgorithmUtil.prepareGraph(am);

		this.ds = new ArrayList<Integer>();
		this.dsInitial = new ArrayList<Integer>();
		dominatedMap = new HashMap<Integer, Boolean>();
		Collection<Integer> vertices = gOriginal.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
		}
	}

	private void preprocess() {
		/*
		 * start a new graph from scratch
		 */
		this.gInitial = new SparseMultigraph<Integer, Integer>();

		Collection<Integer> vertices = gOriginal.getVertices();
		initialVertices = new ArrayList<Integer>();

		// if (withReductionRule) {
		for (Integer v : vertices) {

			int degree = gOriginal.degree(v);

			if (degree == 0) {
				addDominatingVertex(this.dsInitial, initialVertices, v);
			} else if (degree == 1) {
				Collection<Integer> vNegb = gOriginal.getNeighbors(v);
				/*
				 * add v's neighbor u to gOperated and dominating set add u's
				 * neighbors to gOperated and mark them dominated(including v)
				 */
				for (Integer u : vNegb) {

					addDominatingVertexAndItsNeigbors(this.dsInitial, u);
				}

			}

		}

		// check degree 2 vertices after all degree 1 vertices finished
		// marking
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
		// } else {
		// List<VertexDegree> vdList = getVDByStrategy(this.strategy);
		//
		// VertexDegree vd = vdList.get(0);
		// Integer v = vd.getVertex();
		//
		// addDominatingVertexAndItsNeigbors(this.initialDS, v);

		// }

		if (initialVertices.isEmpty()) {
			List<VertexDegree> vdList = getVDByStrategy(this.strategy);

			VertexDegree vd = vdList.get(0);
			Integer v = vd.getVertex();

			addDominatingVertexAndItsNeigbors(this.dsInitial, v);
		}

		AlgorithmUtil.prepareGraph(am, gInitial, initialVertices);

	}

	private void addDominatingVertexAndItsNeigbors(List<Integer> ds, Integer v) {
		addDominatingVertex(ds, initialVertices, v);
		Collection<Integer> vNegb = gOriginal.getNeighbors(v);
		for (Integer w : vNegb) {
			addDominatedVertex(initialVertices, w);
		}
	}

	private void addDominatingVertex(List<Integer> ds, List<Integer> initalVerteices, Integer u) {
		AlgorithmUtil.addElementToList(ds, u);
		addDominatedVertex(initalVerteices, u);
	}

	private void addDominatedVertex(List<Integer> initalVerteices, Integer u) {
		AlgorithmUtil.addElementToList(initalVerteices, u);
		dominatedMap.put(u, true);
	}

	private Collection<Integer> getClosedNeighborsWithoutV(Integer v, Integer w) {
		Collection<Integer> wNegb = gOriginal.getNeighbors(w); // N(w)
		wNegb.add(w); // N[w]
		wNegb.remove(v); // N[w]\v
		return wNegb;
	}

	private void addHigherNeighborOfVToDS(List<Integer> ds, Integer v, Integer u, Integer w, Collection<Integer> uNegb,
			Collection<Integer> wNegb, List<Integer> initalVerteices, int uUtility, int wUtility) {
		if (AlgorithmUtil.isAllDominated(dominatedMap, wNegb) && wUtility == 0) {

			addNeighborOfVToDS(ds, v, u, uNegb, initalVerteices);
		} else if (AlgorithmUtil.isAllDominated(dominatedMap, uNegb) && uUtility == 0) {
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

		/*
		 * prepare: i) construct the iterative graph from initial graph; ii)
		 * initial a ds (iterativeDS) of the iterative graph
		 */
		Collection<Integer> gOriginalVertices = gOriginal.getVertices();

		// this.ds.addAll(this.dsInitial);
		List<Integer> dsIterative = new ArrayList<Integer>();
		List<Integer> dsIterativeCompare = new ArrayList<Integer>();
		dsIterative.addAll(this.dsInitial);
		dsIterativeCompare.addAll(this.dsInitial);
		/*
		 * used for initial + iterative graph (for greedyDS);
		 */
		Graph<Integer, Integer> gIterative = AlgorithmUtil.copyGrapy(gInitial);

		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {
			/*
			 * calculate the vertices between original graph and iterative
			 * graph; order them according to their utility; get top k vertices
			 * and their neighbors; so the worst iterative ds should be inital
			 * ds + k vertices; If other solutions are worse than it, take it as
			 * the solution;
			 */
			Collection<Integer> gIterativeVertices = gIterative.getVertices();
			Collection<Integer> nextIterativeVertices = CollectionUtils.subtract(gOriginalVertices, gIterativeVertices);

			int nextIterativeVerticesSize = nextIterativeVertices.size();

			List<VertexDegree> vertexDegreeList = getVDByStrategy(this.strategy, nextIterativeVertices);
			int fromIndex = 0;
			int toIndex = Math.min(k, nextIterativeVerticesSize);

			List<VertexDegree> topKVDList = vertexDegreeList.subList(fromIndex, toIndex);
			List<Integer> topKVertices = AlgorithmUtil.getVertexList(topKVDList);

			Collection<Integer> iterativeVertices = new ArrayList<Integer>();
			iterativeVertices.addAll(topKVertices);
			dsIterativeCompare.addAll(topKVertices);

			// add k vertices and their neighbors
			for (Integer v : topKVertices) {
				Collection<Integer> vNeg = gOriginal.getNeighbors(v);
				for (Integer u : vNeg) {
					AlgorithmUtil.addElementToList(iterativeVertices, u);
				}
			}
			/*
			 * the neighbours of top k vertices should exclude what has been in
			 * the initial graph;
			 */
			iterativeVertices = CollectionUtils.subtract(iterativeVertices, gIterative.getVertices());
			List<Integer> iterativeVerticesCopy=new ArrayList<Integer>();
			iterativeVerticesCopy.addAll(iterativeVertices);

			/*
			 * inital graph + iterative vertices = iteratvie graph apply native
			 * greedy algorithm on it to see if a smaller solution can be
			 * obtained
			 */
			Graph<Integer, Integer> gIterativeGreedy = AlgorithmUtil.copyGrapy(gIterative);
			AlgorithmUtil.prepareGraph(this.am, gIterativeGreedy, iterativeVertices);

			GreedyNative agn = new GreedyNative(gIterativeGreedy);
			agn.run();
			List<Integer> dsGreedy = agn.getDominatingSet();

			/*
			 * if greedy ds is worse than iterative ds, take iterative ds as the
			 * solution and parameter r should be the minimal among: the input
			 * one, greedy ds size and iterative ds size
			 */
			int paramR = r;
			int dsGreedySize = dsGreedy.size();
			int dsIterativeCompareSize = dsIterativeCompare.size();

			if (dsGreedySize < dsIterativeCompareSize) {
				dsIterativeCompareSize = dsGreedySize;
				dsIterativeCompare = dsGreedy;

			}
			paramR = Math.min(paramR, dsIterativeCompareSize-dsIterative.size());

			/*
			 * calculate the vertices between iterative graph and initial graph;
			 * order them according to their utility; take p vertices from the
			 * list; using dds to get solution
			 */
			//Collection<Integer> gIterativeGreedyVertices = gIterativeGreedy.getVertices();

			//Collection<Integer> nextPartitionVertices = CollectionUtils.subtract(gIterativeGreedyVertices,
			//		gIterativeVertices);

			List<Integer> dsPartition = new ArrayList<Integer>();
			dsPartition.addAll(dsIterative);

			Graph<Integer, Integer> gPartition = AlgorithmUtil.copyGrapy(gIterative);
			while (!AlgorithmUtil.isAllDominated(dominatedMap, iterativeVerticesCopy)) {

				//nextPartitionVertices = CollectionUtils.subtract(gIterativeGreedyVertices, gPartition.getVertices());

				vertexDegreeList = getVDByStrategy(this.strategy, iterativeVertices);
				int nextPartitionVerticesSize = iterativeVertices.size();
				int subFromIndex = 0;
				int subToIndex = Math.min(PARTITION_SIZE, nextPartitionVerticesSize);

				List<VertexDegree> partitionVDList = vertexDegreeList.subList(subFromIndex, subToIndex);
				List<Integer> partitionVertices = AlgorithmUtil.getVertexList(partitionVDList);

				iterativeVertices.removeAll(partitionVertices);
				
				AlgorithmUtil.prepareGraph(am, gPartition, partitionVertices);
				Graph<Integer, Integer> gPartitionCopy = AlgorithmUtil.copyGrapy(gPartition);
				List<Integer> dsPartitionCopy = new ArrayList<Integer>();
				dsPartitionCopy.addAll(dsPartition);
				DDSFPT ag = new DDSFPT(indicator, gPartitionCopy, dsPartition, paramR);

				ag.computing();

				dsPartition = ag.getDs2();
				Collection<Integer> addedDSVertices = CollectionUtils.subtract(dsPartition, dsPartitionCopy);

				for (Integer v : addedDSVertices) {
					dominatedMap.put(v, true);
					Collection<Integer> vNeg = gIterativeGreedy.getNeighbors(v);
					for (Integer u : vNeg) {
						dominatedMap.put(u, true);
					}

				}

			}

			int dsPartitionSize = dsPartition.size();
			if (dsPartitionSize > dsIterativeCompareSize) {
				dsPartition = dsIterativeCompare;
			}

			dsIterative = dsPartition;
			gIterative = gIterativeGreedy;

		}
		this.ds = dsIterative;

	}

	private List<VertexDegree> getVDByStrategy(int strategy, Collection<Integer> leftVertices) {
		switch (strategy) {

		case STRATEGY_UTILITY_DESC: {
			return getVDDescByUtility(leftVertices);
		}
		case STRATEGY_UTILITY_ASC: {
			return getVDAscByUtility(leftVertices);
		}

		default: {
			return null;
		}
		}

	}

	private List<VertexDegree> getVDByStrategy(int strategy) {
		switch (strategy) {

		case STRATEGY_UTILITY_DESC: {
			return getVDDescByUtility();
		}
		case STRATEGY_UTILITY_ASC: {
			return getVDAscByUtility();
		}

		default: {
			return null;
		}
		}

	}

	private List<VertexDegree> getVDDescByUtility(Collection<Integer> leftVertices) {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToUtilityInclude(gOriginal, leftVertices,
				dominatedMap);

		return vertexDegreeList;
	}

	private List<VertexDegree> getVDAscByUtility() {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToUtility(gOriginal, dominatedMap);
		Collections.reverse(vertexDegreeList);

		return vertexDegreeList;
	}

	private List<VertexDegree> getVDDescByUtility() {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToUtility(gOriginal, dominatedMap);

		return vertexDegreeList;
	}

	private List<VertexDegree> getVDAscByUtility(Collection<Integer> leftVertices) {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToUtilityInclude(gOriginal, leftVertices,
				dominatedMap);
		Collections.reverse(vertexDegreeList);

		return vertexDegreeList;
	}

	// private List<VertexDegree> getVDDescByDegree(Collection<Integer>
	// leftVertices) {
	// List<VertexDegree> vertexDegreeList =
	// AlgorithmUtil.sortVertexAccordingToDegreeInclude(gOriginal,
	// leftVertices);
	//
	// return vertexDegreeList;
	// }
	//
	// private List<VertexDegree> getVDAscByDegree(Collection<Integer>
	// leftVertices) {
	// List<VertexDegree> vertexDegreeList =
	// AlgorithmUtil.sortVertexAccordingToDegreeInclude(gOriginal,
	// leftVertices);
	// Collections.reverse(vertexDegreeList);
	// return vertexDegreeList;
	// }

}
