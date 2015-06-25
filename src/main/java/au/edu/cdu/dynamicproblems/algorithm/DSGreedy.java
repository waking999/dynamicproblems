package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import edu.uci.ics.jung.graph.Graph;

public class DSGreedy implements ITask, IAlgorithm {

	// private static Logger log = LogUtil.getLogger(DSGreedy.class);
	private long runningTime;

	private TaskLock lock;

	public TaskLock getLock() {
		return lock;
	}

	public void setLock(TaskLock lock) {
		this.lock = lock;
	}

	public Result run() throws InterruptedException {

		computing();
		Thread.sleep(1000);
		Result r = getResult();

		return r;
	}

	public Result getResult() {
		Result r = new Result();
		r.setHasSolution(true);
		StringBuffer sb = new StringBuffer();
//		sb.append(this.getClass().getName()).append(":").append(this.indicator)
//				.append(":").append(this.runningTime + " ns:")
//				.append(this.dominatingSet.size()).append(":");
//		for (Integer i : this.dominatingSet) {
//			sb.append(i).append(" ");
//		}
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
	private String indicator;

	private List<VertexDegree> vertexDegreeList;
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

	public DSGreedy(List<String[]> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public DSGreedy(String indicator, List<String[]> adjacencyMatrix) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public DSGreedy(Graph<Integer, Integer> g) {
		this.g = g;
		this.numOfVertices = g.getVertexCount();

	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set
	 */
	public void computing() {
		long start = System.nanoTime();
		initialization();
		greedy();
		long end = System.nanoTime();
		this.runningTime = end - start;

	}

	private void initialization() {
		this.vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g);
		this.dominatingSet = new ArrayList<Integer>();
	}

	private void greedy() {

		List<Integer> T = new ArrayList<Integer>();

		for (int j = 0; j < numOfVertices; j++) {
			T.add(vertexDegreeList.get(j).getVertex());
		}

		/*
		 * idea: Take all vertices of the highest degree as an approximate
		 * solution
		 */

		while (!T.isEmpty()) {
			// get the vertex with the highest degree
			Integer v = T.get(0);

			dominatingSet.add(v);
			T.remove(v);

			Collection<Integer> neighborsOfV = g.getNeighbors(v);

			// remove v's neighbours from T
			T.removeAll(neighborsOfV);

		}

	}

}
