package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class GreedyDSRegretReductionMark implements IGreedyDS, ITask {
	@SuppressWarnings("unused")
	private static Logger log = LogUtil
			.getLogger(GreedyDSRegretReductionMark.class);
	private long runningTime;

	// private List<Integer> isolatedDS;
	// private Collection<Integer> gOriginalEdgeList;

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
	// private List<VertexDegree> vertexDegreeList;
	// /**
	// * the complete graph
	// */
	// private Graph<Integer, Integer> gK;
	// private List<Integer> ds1;

	private List<Integer> ds;

	// private List<Integer> preDS;

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

	// private int numOfVertices;

	public GreedyDSRegretReductionMark(String indicator, List<String[]> am,
			int k, int r) {
		this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		// this.numOfVertices = am.size();
		this.ds = null;
	}

	public GreedyDSRegretReductionMark(String indicator) {
		this.indicator = indicator;

		// this.numOfVertices = am.size();
		this.ds = null;
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
		// sb.append(this.getClass().getName()).append(":").append(this.indicator)
		// .append(":");
		// sb.append(this.runningTime + " ns:");
		// sb.append(this.ds.size() + ":");
		// for (Integer i : this.ds) {
		// sb.append(i).append(" ");
		// }
		sb.append(",").append(this.ds.size()).append(",").append(k).append(",")
				.append(r).append(", ").append(this.runningTime);
		result.setString(sb.toString());
		return result;
	}

	public Result getResult() {
		Result result = new Result();
		// result.setIndex(threadId);

		StringBuffer sb = new StringBuffer();
		// sb.append(this.getClass().getName()).append(":").append(this.indicator)
		// .append(":");
		// sb.append(this.runningTime + " ns:");
		// sb.append(this.ds.size() + ":");
		// for (Integer i : this.ds) {
		// sb.append(i).append(" ");
		// }
		sb.append(",").append(this.ds.size()).append(",").append(k).append(",")
				.append(r).append(", ").append(this.runningTime);
		result.setString(sb.toString());
		return result;
	}

	public void computing() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {

		initialization();
		long start = System.nanoTime();
		preprocess();

		start();
		long end = System.nanoTime();

		this.runningTime = end - start;
	}

	private void initialization() {

		// this.numOfVertices = am.size();

		this.gOriginal = AlgorithmUtil.prepareGraph(am);

		this.ds = new ArrayList<Integer>();

		dominatedMap = new HashMap<Integer, Boolean>();
		Collection<Integer> vertices = gOriginal.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
		}
	}

	private void preprocess() {
		// this.preDS = new ArrayList<Integer>();
		// start a new graph from scratch
		this.gOperated = new SparseMultigraph<Integer, Integer>();

		Collection<Integer> vertices = gOriginal.getVertices();
		List<Integer> initalVerteices = new ArrayList<Integer>();

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
						addHigherNeighborOfVToDS(v, u, w, uNegb, wNegb,
								initalVerteices);
					} else {
						addHigherNeighborOfVToDS(v, w, u, wNegb, uNegb,
								initalVerteices);
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

	private void addHigherNeighborOfVToDS(Integer v, Integer u, Integer w,
			Collection<Integer> uNegb, Collection<Integer> wNegb,
			List<Integer> initalVerteices) {
		if (AlgorithmUtil.isAllDominated(dominatedMap, wNegb)) {
			
			addNeighborOfVToDS(v, u, uNegb, initalVerteices);
		} else if (AlgorithmUtil.isAllDominated(dominatedMap, uNegb)) {
			addNeighborOfVToDS(v, w, wNegb, initalVerteices);
		} else {
			addNeighborOfVToDS(v, u, uNegb, initalVerteices);
		}
	}

	private void addNeighborOfVToDS(Integer v, Integer w,
			Collection<Integer> wNegb, List<Integer> initalVerteices) {
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

	private void start() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {

		if (AlgorithmUtil.isAllDominated(dominatedMap)) {

			return;
		}

		Collection<Integer> gOriginalVertices = gOriginal.getVertices();
		Collection<Integer> gOperatedVertices = gOperated.getVertices();

		Collection<Integer> leftVertices = CollectionUtils.subtract(
				gOriginalVertices, gOperatedVertices);

		int leftVerticesSize = leftVertices.size();

		int rounds = (leftVerticesSize - 1) / k + 1;

		List<VertexDegree> vertexDegreeList = AlgorithmUtil
				.sortVertexAccordingToDegree(gOriginal, gOperatedVertices);

		for (int i = 1; i <= rounds; i++) {
			int fromIndex = (i - 1) * k;
			int toIndex = i * k;
			toIndex = Math.min(toIndex, leftVerticesSize);

			List<VertexDegree> vdList = vertexDegreeList.subList(fromIndex,
					toIndex);

			List<Integer> vList = AlgorithmUtil.getVertexList(vdList);

			AlgorithmUtil.prepareGraph(this.am, gOperated, vList);
			Graph<Integer, Integer> gOperatedCopy = AlgorithmUtil
					.copyGrapy(gOperated);
			DDSFPT ag = new DDSFPT(indicator, gOperatedCopy, ds, r);

			ag.computing();
			ds = ag.getDs2();
		}

	}

	// @SuppressWarnings("unused")
	// private void start() throws MOutofNException, ExceedLongMaxException,
	// ArraysNotSameLengthException {
	// List<VertexDegree> vertexDegreeList = AlgorithmUtil
	// .sortVertexAccordingToDegree(this.gOriginal);
	// VertexDegree vd0 = vertexDegreeList.get(0);
	// Integer v0 = vd0.getVertex();
	//
	// // prepare an initial graph with only one vertex which is of the highest
	// // degree and an initial dominating set of this graph
	// Graph<Integer, Integer> gI = new SparseMultigraph<Integer, Integer>();
	// gI.addVertex(v0);
	//
	// // ds.add(v0);
	// AlgorithmUtil.addElementToList(ds, v0);
	//
	// int rounds = (this.numOfVertices - 1) / this.k + 1;
	//
	// for (int i = 1; i <= rounds; i++) {
	// int fromIndex = (i - 1) * k + 1;
	// int toIndex = i * k;
	// toIndex = Math.min(toIndex, this.numOfVertices - 1);
	//
	// List<VertexDegree> vdList = vertexDegreeList.subList(fromIndex,
	// toIndex + 1);
	// List<Integer> vList = AlgorithmUtil.getVertexList(vdList);
	//
	// gI = AlgorithmUtil.prepareGraph(this.am, gI, vList);
	// Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGrapy(gI);
	// DDSFPT ag = new DDSFPT(indicator, gICopy, ds, r);
	//
	// ag.computing();
	// ds = ag.getDs2();
	//
	// }
	//
	// }

}
