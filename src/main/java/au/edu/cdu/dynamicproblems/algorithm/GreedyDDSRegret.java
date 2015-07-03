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

public class GreedyDDSRegret implements IAlgorithm, ITask {
	private static Logger log = LogUtil.getLogger(GreedyDDSRegret.class);
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
	private List<VertexDegree> vertexDegreeList;
	// /**
	// * the complete graph
	// */
	// private Graph<Integer, Integer> gK;
	// private List<Integer> ds1;

	private List<Integer> ds;

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

	private int numOfVertices;

	public GreedyDDSRegret(String indicator, List<String[]> am, int k, int r) {
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
		long start = System.nanoTime();
		initialization();
		start();
		long end = System.nanoTime();
		this.runningTime = end - start;
	}

	private void initialization() {

		// construct a complete graph
		this.numOfVertices = am.size();
		// List<String[]> amK = AlgorithmUtil
		// .generateCompleteGraph(this.numOfVertices);
		// this.gK = AlgorithmUtil.prepareGraph(amK);
		this.gOriginal = AlgorithmUtil.prepareGraph(am);
		this.vertexDegreeList = AlgorithmUtil
				.sortVertexAccordingToDegree(gOriginal);
		// this.gOriginalEdgeList = gOriginal.getEdges();
		// preprocess();
		this.ds = new ArrayList<Integer>();

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

	private void start() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		VertexDegree vd0 = this.vertexDegreeList.get(0);
		Integer v0 = vd0.getVertex();

		// prepare an initial graph with only one vertex which is of the highest
		// degree and an initial dominating set of this graph
		Graph<Integer, Integer> gI = new SparseMultigraph<Integer, Integer>();
		gI.addVertex(v0);

		ds.add(v0);

		int rounds = (this.numOfVertices - 1) / this.k + 1;

		for (int i = 1; i <= rounds; i++) {
			int fromIndex = (i - 1) * k + 1;
			int toIndex = i * k;
			toIndex = Math.min(toIndex, this.numOfVertices - 1);

			List<VertexDegree> vdList = this.vertexDegreeList.subList(
					fromIndex, toIndex + 1);
			List<Integer> vList = AlgorithmUtil.getVertexList(vdList);

			gI = AlgorithmUtil.prepareGraph(this.am, gI, vList);
			Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGrapy(gI);
			DDSFPT ag = new DDSFPT(indicator, gICopy, ds, r);

			ag.computing();
			ds = ag.getDs2();

		}

	}

}
