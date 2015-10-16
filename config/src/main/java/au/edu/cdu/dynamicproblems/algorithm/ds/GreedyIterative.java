package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.algorithm.VertexDegree;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyIterative implements ITask, IAlgorithm {

	public long getRunningTime() {
		return runningTime;
	}

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyIterative.class);
	private long runningTime;

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
		Result r = new Result();
		r.setHasSolution(true);
		StringBuffer sb = new StringBuffer();

		sb.append(",").append(this.dominatingSet.size()).append(",").append(this.runningTime);
		r.setString(sb.toString());
		return r;
	}

	/**
	 * the graph
	 */
	private Graph<Integer, Integer> g;
	/**
	 * 
	 * a sorted vertices with their degree (from highest degree to the lowest)
	 */
	@SuppressWarnings("unused")
	private String indicator;

	Map<Integer, Boolean> dominatedMap;
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

	public GreedyIterative(List<String[]> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public GreedyIterative(String indicator, List<String[]> adjacencyMatrix) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public GreedyIterative(Graph<Integer, Integer> g) {
		this.g = g;
		this.numOfVertices = g.getVertexCount();

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
		runningTime = end - start;

	}

	private void initialization() {
		dominatingSet = new ArrayList<Integer>();

		dominatedMap = new HashMap<Integer, Boolean>();
		Collection<Integer> vertices = g.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
		}
	}

	private void greedy() {

		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {
			// get the vertex with highest utility (the number of undominated
			// neighbors)
			List<VertexDegree> vdList = AlgorithmUtil.sortVertexAccordingToUtility(g, dominatedMap);
			// VertexDegree vd = vdList.get(0);
			//
			// Integer v = vd.getVertex();

			Integer v = getProabilityVertex(vdList);

			// add it into dominating set
			AlgorithmUtil.addElementToList(dominatingSet, v);
			// set it is dominated
			dominatedMap.put(v, true);

			// set its neigbors is dominated
			Collection<Integer> wNeigs = g.getNeighbors(v);

			for (Integer u : wNeigs) {
				dominatedMap.put(u, true);
			}
		}

	}

	private Integer getProabilityVertex(List<VertexDegree> vdList) {
		/*
		 * Based on sorted vdList, we establish probability ruler where bigger
		 * utility, longer section for each vertex
		 */
		int[] vertArray = new int[numOfVertices];
		long[] probabilityArray = new long[numOfVertices];

		for (int i = 0; i < numOfVertices; i++) {
			VertexDegree vd = vdList.get(i);
			Integer v = vd.getVertex();
			int d = vd.getDegree();
			vertArray[i] = v;
			if (i == 0) {
				probabilityArray[i] = d;
			} else {
				probabilityArray[i] = d + probabilityArray[i - 1];
			}
		}
		/*
		 * get a random number to see which section it will fall
		 * vertex with bigger utility has more chances
		 */
		long maxProability = probabilityArray[numOfVertices - 1];

		long randProability = (long) Math.floor(Math.random() * maxProability);

		int idx = -1;
		do {
			idx++;
			if (probabilityArray[idx] >= randProability) {
				break;
			}
		} while (true);

		return vertArray[idx];

	}

}
