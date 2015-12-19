package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.VertexPriority;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

/**
 * This class is used for implementing Greedy Vote Gr Algorithm in the
 * paper:Laura A Sanchis. Experimental analysis of heuristic algorithms for the
 * dominating set problem. Algorithmica, 33(1):3–18, 2002.
 * 
 * @author kai wang
 */
public class GreedyVoteGr implements ITask, IGreedyDS<Integer> {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyVoteGr.class);
	private long runningTime;
	public Map<String, Long> getRunningTimeMap(){
		return null;
	}

	private TaskLock lock;

	public TaskLock getLock() {
		return lock;
	}

	public void setLock(TaskLock lock) {
		this.lock = lock;
	}

	/**
	 * the major method to be invoked to run the algorithm
	 * 
	 * @return the formated running result,including dominating set size and
	 *         running time
	 */
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
	private Graph<Integer, String> g;
	/**
	 * 
	 * a sorted vertices with their degree (from highest degree to the lowest)
	 */
	@SuppressWarnings("unused")
	private String indicator;

	Map<Integer, Boolean> dominatedMap;
	Map<Integer, Float> voteMap;
	Map<Integer, Float> weightMap;

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

	public GreedyVoteGr(List<String[]> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix);

	}

	public GreedyVoteGr(String indicator, List<String[]> adjacencyMatrix) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix);

	}

	public GreedyVoteGr(Graph<Integer, String> g) {
		this.g = g;
		this.numOfVertices = g.getVertexCount();

	}

	public GreedyVoteGr(String indicator, Graph<Integer, String> g) {
		this.indicator = indicator;
		this.g = g;
		this.numOfVertices = g.getVertexCount();
	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set the major difference between
	 * this and that of Greedy Vote is invoking grasp in extra
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

		dominatedMap = new HashMap<Integer, Boolean>(this.numOfVertices);
		voteMap = new HashMap<Integer, Float>(this.numOfVertices);
		weightMap = new HashMap<Integer, Float>(this.numOfVertices);
		Collection<Integer> vertices = g.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
			int degree = g.degree(v);
			float vote = 1.0f / (1 + degree);
			voteMap.put(v, vote);
			weightMap.put(v, vote);
		}

		for (Integer v : vertices) {
			Collection<Integer> vNeig = g.getNeighbors(v);
			float weightv = weightMap.get(v);
			for (Integer u : vNeig) {
				float voteu = voteMap.get(u);
				weightv += voteu;
			}
			weightMap.put(v, weightv);
		}
	}

	private void greedy() {

		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {
			Integer v = chooseVertex();

			AlgorithmUtil.addElementToList(dominatingSet, v);
			adjustWeight(v);

		}

		this.dominatingSet = AlgorithmUtil.grasp(this.g, this.dominatingSet);

	}

	private void adjustWeight(Integer v) {
		Collection<Integer> vNeigs = g.getNeighbors(v);
		boolean coveredv = dominatedMap.get(v);
		weightMap.put(v, 0.0f);
		float votev = voteMap.get(v);
		for (Integer u : vNeigs) {
			float weightu = weightMap.get(u);
			if (weightu - 0.0f > VertexPriority.ZERO_DIFF) {
				float voteu = voteMap.get(u);
				if (!coveredv) {
					weightMap.put(u, weightu - votev);
				}
				boolean coveredu = dominatedMap.get(u);
				if (!coveredu) {
					dominatedMap.put(u, true);
					weightu = weightMap.get(u);
					weightMap.put(u, weightu - voteu);

					Collection<Integer> uNeigs = g.getNeighbors(u);
					for (Integer w : uNeigs) {
						float weightw = weightMap.get(w);
						if (weightu - 0.0f > VertexPriority.ZERO_DIFF) {
							weightMap.put(w, weightw - voteu);
						}
					}

				}
			}

		}
		dominatedMap.put(v, true);
	}

	private Integer chooseVertex() {
		// List<VertexPriority<Integer>> vertexPriorityList = new
		// ArrayList<VertexPriority<Integer>>(this.numOfVertices);
		//
		// Set<Integer> keySet = this.weightMap.keySet();
		// for (Integer key : keySet) {
		// float weight = weightMap.get(key);
		// AlgorithmUtil.addElementToList(vertexPriorityList, new
		// VertexPriority<Integer>(key, weight));
		//
		// }
		//
		// Collections.sort(vertexPriorityList);
		// VertexPriority vw = vertexPriorityList.get(0);
		//
		// return vw.getVertex();
		List<Integer> vList = OrderPackageUtil.getVertexListWeightDesc(this.g, this.weightMap);
		return vList.get(0);
	}

	

}
