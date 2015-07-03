package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;

import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import edu.uci.ics.jung.graph.Graph;

public class DSGreedyRegret implements ITask, IAlgorithm {

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

		sb.append(",").append(this.dominatingSet.size()).append(",")
				.append(this.runningTime);
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

	public DSGreedyRegret(List<String[]> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public DSGreedyRegret(String indicator, List<String[]> adjacencyMatrix) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public DSGreedyRegret(Graph<Integer, Integer> g) {
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

		List<VertexDegree> vertexDegreeList = AlgorithmUtil
				.sortVertexAccordingToDegree(g);

		int gVerCount = g.getVertexCount();
		while (gVerCount > 0) {
			Integer v = vertexDegreeList.get(0).getVertex();
			Collection<Integer> neigOpen = g.getNeighbors(v);
			Collection<Integer> vItself = new ArrayList<Integer>(1);
			vItself.add(v);
			Collection<Integer> neigClose =null;
			if(neigOpen!=null){
				neigClose = CollectionUtils.union(neigOpen,
					vItself);
			}else{
				neigClose =CollectionUtils.union(new ArrayList<Integer>(),
						vItself);
			}

			searchHighestUtility: for (VertexDegree vd : vertexDegreeList) {
				Integer u = vd.getVertex();
				for (Integer w : neigClose) {
					if (u.equals(w)) {
						dominatingSet.add(u);
						
						Collection<Integer> neighborsOfU = g.getNeighbors(u);
						// remove u's neighbours from g
						if (neighborsOfU != null && neighborsOfU.size()>0) {
							for (Integer x : neighborsOfU) {
								g.removeVertex(x);
							}
						}
						// remove u from g
						g.removeVertex(u);
						

						
						break searchHighestUtility;
					}
				}
			}
			vertexDegreeList = AlgorithmUtil
					.sortVertexAccordingToDegree(g);
			gVerCount = g.getVertexCount();
		}

	}

}
