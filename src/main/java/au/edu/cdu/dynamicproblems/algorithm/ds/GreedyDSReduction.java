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
	public static final int STRATEGY_DEGREE_DESC = 1;
	public static final int STRATEGY_DEGREE_ASC = 2;
	public static final int STRATEGY_UTILITY_DESC = 3;
	public static final int STRATEGY_UTILITY_ASC = 4;

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyDSReduction.class);
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
	private List<Integer> initalVerteices;

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
	 * the graph
	 */
	private Graph<Integer, Integer> gOriginal;

	private Graph<Integer, Integer> gOperated;

	private int strategy;

	public GreedyDSReduction(String indicator, List<String[]> am, int k, int r, int strategy) {
		this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		this.ds = null;
		this.strategy = strategy;
	}

	public GreedyDSReduction(String indicator, int strategy) {
		this.indicator = indicator;

		this.ds = null;
		this.strategy = strategy;
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

	public void computing() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		initialization();
		long start = System.nanoTime();
		preprocess();

		start(strategy);
		long end = System.nanoTime();

		this.runningTime = end - start;
	}

	private void initialization() {

		this.gOriginal = AlgorithmUtil.prepareGraph(am);

		this.ds = new ArrayList<Integer>();

		dominatedMap = new HashMap<Integer, Boolean>();
		Collection<Integer> vertices = gOriginal.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
		}
	}

	private void preprocess() {

		// start a new graph from scratch
		this.gOperated = new SparseMultigraph<Integer, Integer>();

		Collection<Integer> vertices = gOriginal.getVertices();
		initalVerteices = new ArrayList<Integer>();

		for (Integer v : vertices) {

			int degree = gOriginal.degree(v);

			if (degree == 0) {
				addDominatingVertex(initalVerteices, v);
			} else if (degree == 1) {
				Collection<Integer> vNegb = gOriginal.getNeighbors(v);
				// add v's neighbor u to gOperated and dominating set
				// add u's neighbors to gOperated and mark them
				// dominated(including v)
				for (Integer u : vNegb) {

					addDominatingVertex(initalVerteices, u);
					Collection<Integer> uNegb = gOriginal.getNeighbors(u);
					for (Integer w : uNegb) {
						addDominatedVertex(initalVerteices, w);
					}
				}

			}

		}

		// check degree 2 vertices after all degree 1 vertices finished marking
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
					int uDegree = gOriginal.degree(u);
					int wDegree = gOriginal.degree(w);

					Collection<Integer> uNegb = getClosedNeighborsWithoutV(v, u);

					Collection<Integer> wNegb = getClosedNeighborsWithoutV(v, w);

					if (uDegree > wDegree) {
						// u has the higher priority to be added into dominating
						// set than w
						addHigherNeighborOfVToDS(v, u, w, uNegb, wNegb, initalVerteices);
					} else {
						addHigherNeighborOfVToDS(v, w, u, wNegb, uNegb, initalVerteices);
					}

				}
			}
		}

		AlgorithmUtil.prepareGraph(am, gOperated, initalVerteices);

	}

	private void addDominatingVertex(List<Integer> initalVerteices, Integer u) {
		AlgorithmUtil.addElementToList(this.ds, u);
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

	private void addHigherNeighborOfVToDS(Integer v, Integer u, Integer w, Collection<Integer> uNegb,
			Collection<Integer> wNegb, List<Integer> initalVerteices) {
		if (AlgorithmUtil.isAllDominated(dominatedMap, wNegb)) {

			addNeighborOfVToDS(v, u, uNegb, initalVerteices);
		} else if (AlgorithmUtil.isAllDominated(dominatedMap, uNegb)) {
			addNeighborOfVToDS(v, w, wNegb, initalVerteices);
		} else {
			addNeighborOfVToDS(v, u, uNegb, initalVerteices);
		}
	}

	private void addNeighborOfVToDS(Integer v, Integer w, Collection<Integer> wNegb, List<Integer> initalVerteices) {
		/*
		 * if N[u]\v (including u) are dominated: add w to gOperated and
		 * dominating set and mark it dominated add w's neighbors to gOperated
		 * and mark them dominated(including v)
		 */
		AlgorithmUtil.addElementToList(this.ds, w);

		for (Integer x : wNegb) {
			addDominatedVertex(initalVerteices, x);
		}
		addDominatedVertex(initalVerteices, v);
	}

	private void startByDegree(int strategy)
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		if (AlgorithmUtil.isAllDominated(dominatedMap)) {

			return;
		}

		Collection<Integer> gOriginalVertices = gOriginal.getVertices();
		Collection<Integer> gOperatedVertices = gOperated.getVertices();

		Collection<Integer> leftVertices = CollectionUtils.subtract(gOriginalVertices, gOperatedVertices);

		int leftVerticesSize = leftVertices.size();

		int rounds = (leftVerticesSize - 1) / k + 1;

		List<VertexDegree> vertexDegreeList = getVDByStrategy(this.strategy, leftVertices);

		for (int i = 1; i <= rounds; i++) {
			int fromIndex = (i - 1) * k;
			int toIndex = i * k;
			toIndex = Math.min(toIndex, leftVerticesSize);

			List<VertexDegree> vdList = vertexDegreeList.subList(fromIndex, toIndex);

			List<Integer> vList = AlgorithmUtil.getVertexList(vdList);

			AlgorithmUtil.prepareGraph(this.am, gOperated, vList);
			Graph<Integer, Integer> gOperatedCopy = AlgorithmUtil.copyGrapy(gOperated);
			DDSFPT ag = new DDSFPT(indicator, gOperatedCopy, ds, r);

			ag.computing();
			ds = ag.getDs2();

			for (Integer v : ds) {
				if (!dominatedMap.get(v)) {
					dominatedMap.put(v, true);
				}
				Collection<Integer> vNeg = gOriginal.getNeighbors(v);
				for (Integer u : vNeg) {
					if (!dominatedMap.get(u)) {
						dominatedMap.put(u, true);
					}
				}
			}

			if (AlgorithmUtil.isAllDominated(dominatedMap)) {
				break;
			}
		}

	}

	private void startByUtility(int strategy)
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {

		Collection<Integer> gOriginalVertices = gOriginal.getVertices();
		Collection<Integer> gOperatedVertices = gOperated.getVertices();

		Collection<Integer> leftVertices = CollectionUtils.subtract(gOriginalVertices, gOperatedVertices);

		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {

			int leftVerticesSize = leftVertices.size();
			List<VertexDegree> vertexDegreeList = getVDByStrategy(this.strategy, leftVertices);
			int fromIndex = 0;
			int toIndex = Math.min(k, leftVerticesSize);

			List<VertexDegree> vdList = vertexDegreeList.subList(fromIndex, toIndex);
			List<Integer> vList = AlgorithmUtil.getVertexList(vdList);

			AlgorithmUtil.prepareGraph(this.am, gOperated, vList);
			Graph<Integer, Integer> gOperatedCopy = AlgorithmUtil.copyGrapy(gOperated);
			DDSFPT ag = new DDSFPT(indicator, gOperatedCopy, ds, r);

			ag.computing();
			ds = ag.getDs2();

			for (Integer v : ds) {
				if (!dominatedMap.get(v)) {
					dominatedMap.put(v, true);
				}
				leftVertices.remove(v);

				Collection<Integer> vNeg = gOriginal.getNeighbors(v);

				for (Integer u : vNeg) {
					if (!dominatedMap.get(u)) {
						dominatedMap.put(u, true);
					}
					leftVertices.remove(u);
				}

			}

		}

	}

	private void start(int strategy) throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {
		switch (strategy) {
		case STRATEGY_DEGREE_DESC:
		case STRATEGY_DEGREE_ASC: {
			startByDegree(strategy);

		}
		case STRATEGY_UTILITY_DESC:
		case STRATEGY_UTILITY_ASC: {
			startByUtility(strategy);
		}

		}

	}

	private List<VertexDegree> getVDByStrategy(int strategy, Collection<Integer> leftVertices) {
		switch (strategy) {
		case STRATEGY_DEGREE_DESC: {
			return getVDDescByDegree(leftVertices);
		}
		case STRATEGY_DEGREE_ASC: {
			return getVDAscByDegree(leftVertices);
		}
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

	private List<VertexDegree> getVDDescByUtility(Collection<Integer> leftVertices) {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToUndomiatedDegreeInclude(gOriginal,
				leftVertices, dominatedMap);

		return vertexDegreeList;
	}

	private List<VertexDegree> getVDAscByUtility(Collection<Integer> leftVertices) {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToUndomiatedDegreeInclude(gOriginal,
				leftVertices, dominatedMap);
		Collections.reverse(vertexDegreeList);

		return vertexDegreeList;
	}

	private List<VertexDegree> getVDDescByDegree(Collection<Integer> leftVertices) {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegreeInclude(gOriginal, leftVertices);

		return vertexDegreeList;
	}

	private List<VertexDegree> getVDAscByDegree(Collection<Integer> leftVertices) {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegreeInclude(gOriginal, leftVertices);
		Collections.reverse(vertexDegreeList);
		return vertexDegreeList;
	}

}
