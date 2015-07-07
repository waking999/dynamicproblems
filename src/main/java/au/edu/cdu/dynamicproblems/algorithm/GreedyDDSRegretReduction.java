package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

public class GreedyDDSRegretReduction implements IAlgorithm, ITask {
	@SuppressWarnings("unused")
	private static Logger log = LogUtil
			.getLogger(GreedyDDSRegretReduction.class);
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

	private List<Integer> preDS;

	public List<Integer> getDs() {
		return ds;
	}

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
	private int numOfVertices;

	public GreedyDDSRegretReduction(String indicator, List<String[]> am, int k,
			int r) {
		// this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		// this.numOfVertices = am.size();
		this.ds = null;
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

	public void computing() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {

		initialization();
		preprocess();
		// start();
		long start = System.nanoTime();
		start(this.gOperated, this.preDS);
		long end = System.nanoTime();
		postprocess();

		this.runningTime = end - start;
	}

	private void initialization() {

		// construct a complete graph
		this.numOfVertices = am.size();
		// List<String[]> amK = AlgorithmUtil
		// .generateCompleteGraph(this.numOfVertices);
		// this.gK = AlgorithmUtil.prepareGraph(amK);
		this.gOriginal = AlgorithmUtil.prepareGraph(am);
		// this.vertexDegreeList = AlgorithmUtil
		// .sortVertexAccordingToDegree(gOriginal);
		// this.gOriginalEdgeList = gOriginal.getEdges();
		// preprocess();
		this.ds = new ArrayList<Integer>();

	}

	private void preprocess() {
		this.preDS = new ArrayList<Integer>();
		this.gOperated = AlgorithmUtil.copyGrapy(this.gOriginal);
		/*
		 * 1.degree 0: put such vertices into preDS and remove it from g,
		 * because they are always dominated by themselves 2.degree 1: add its
		 * neighbor into preDS, remove it from g
		 */

		Collection<Integer> vertices = gOperated.getVertices();
		List<Integer> removeList = new ArrayList<Integer>();
		for (int v : vertices) {

			int degree = gOperated.degree(v);
			if (degree == 0) {
				//this.preDS.add(v);
				AlgorithmUtil.addElementToList(this.preDS, v);
				//removeList.add(v);
				AlgorithmUtil.addElementToList(removeList, v);
			} else if (degree == 1) {
				Collection<Integer> vNegb = gOperated.getNeighbors(v);
				for (Integer u : vNegb) {
					//this.preDS.add(u);
					AlgorithmUtil.addElementToList(this.preDS, u);
				}
				//removeList.add(v);

				AlgorithmUtil.addElementToList(removeList, v);
			}

		}

		for (Integer v : removeList) {

			gOperated.removeVertex(v);
		}
		removeList = null;

	}

	private void postprocess() {
		// this.dominatingSet = (List<Integer>) CollectionUtils.union(
		// this.dominatingSet, this.preDS);
	}

	private void start(Graph<Integer, Integer> gOperated, List<Integer> preDS)
			throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {

		this.ds = this.preDS;

		if (AlgorithmUtil.isDS(gOperated, preDS)) {

			return;
		}

		List<VertexDegree> vertexDegreeList = AlgorithmUtil
				.sortVertexAccordingToDegree(gOperated, preDS);

		Graph<Integer, Integer> gI = new SparseMultigraph<Integer, Integer>();

		int preDSSize = preDS.size();

		if (preDSSize > 0) {
			gI = AlgorithmUtil.prepareGraph(this.am, gI, preDS);
		}

		Collection<Integer> verticesExcludePreDS=CollectionUtils.subtract(gOperated.getVertices(), preDS);
		int verticesExcludePreDSSize= verticesExcludePreDS.size();
		
		
		int rounds = (verticesExcludePreDSSize-1) / this.k + 1;

		for (int i = 1; i <= rounds; i++) {
			int fromIndex = (i - 1) * k;
			int toIndex = i * k;
			toIndex = Math.min(toIndex, verticesExcludePreDSSize);

			List<VertexDegree> vdList = vertexDegreeList.subList(fromIndex,
					toIndex );

			List<Integer> vList = AlgorithmUtil.getVertexList(vdList);

			gI = AlgorithmUtil.prepareGraph(this.am, gI, vList);
			Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGrapy(gI);
			DDSFPT ag = new DDSFPT(indicator, gICopy, ds, r);

			ag.computing();
			ds = ag.getDs2();
		}

	}

	// @SuppressWarnings("unused")
	// private void preprocess() {
	//
	// this.g0 = AlgorithmUtil.prepareGraph(am);
	// isolatedDS = new ArrayList<Integer>();
	// Collection<Integer> vertices = g0.getVertices();
	// int verSize = vertices.size();
	// for (int i = 0; i < verSize; i++) {
	// int degree = g0.degree(i);
	// if (degree == 0) {
	// isolatedDS.add(i);
	// g0.removeVertex(i);
	// }
	// }
	//
	// numOfVertices = g0.getVertexCount();
	// List<String[]> amK = AlgorithmUtil.generateCompleteGraph(numOfVertices);
	// //this.gK = AlgorithmUtil.prepareGraph(amK);
	// this.vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g0);
	//
	// }

	@SuppressWarnings("unused")
	private void start() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		List<VertexDegree> vertexDegreeList = AlgorithmUtil
				.sortVertexAccordingToDegree(this.gOriginal);
		VertexDegree vd0 = vertexDegreeList.get(0);
		Integer v0 = vd0.getVertex();

		// prepare an initial graph with only one vertex which is of the highest
		// degree and an initial dominating set of this graph
		Graph<Integer, Integer> gI = new SparseMultigraph<Integer, Integer>();
		gI.addVertex(v0);

		//ds.add(v0);
AlgorithmUtil.addElementToList(ds, v0);

		int rounds = (this.numOfVertices - 1) / this.k + 1;

		for (int i = 1; i <= rounds; i++) {
			int fromIndex = (i - 1) * k + 1;
			int toIndex = i * k;
			toIndex = Math.min(toIndex, this.numOfVertices - 1);

			List<VertexDegree> vdList = vertexDegreeList.subList(fromIndex,
					toIndex + 1);
			List<Integer> vList = AlgorithmUtil.getVertexList(vdList);

			gI = AlgorithmUtil.prepareGraph(this.am, gI, vList);
			Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGrapy(gI);
			DDSFPT ag = new DDSFPT(indicator, gICopy, ds, r);

			ag.computing();
			ds = ag.getDs2();

		}

	}

}
