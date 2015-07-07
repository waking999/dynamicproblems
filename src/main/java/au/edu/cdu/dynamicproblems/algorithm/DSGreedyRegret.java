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
	 * a copy of the graph and operations (vertex/edge deletion ) happened on
	 * it;
	 */
	//private Graph<Integer, Integer> gOperated;
	/**
	 * 
	 * a sorted vertices with their degree (from highest degree to the lowest)
	 */
	@SuppressWarnings("unused")
	private String indicator;

	@SuppressWarnings("unused")
	private List<VertexDegree> vertexDegreeList;
	/**
	 * the desired dominating set
	 */
	List<Integer> dominatingSet;
	/**
	 * a set containing some vertex which can be put into dominating set in
	 * preprocess stage
	 */
	List<Integer> preDS;

	public List<Integer> getDominatingSet() {
		return dominatingSet;
	}

	/**
	 * number of vertices
	 */
	@SuppressWarnings("unused")
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
		//preprocess();
		greedy();
		//greedy(this.gOperated, this.preDS);
		//postprocess();
		long end = System.nanoTime();
		this.runningTime = end - start;

	}

	private void initialization() {
		this.vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g);
		this.dominatingSet = new ArrayList<Integer>();

	}

//	private void preprocess() {
//		this.preDS = new ArrayList<Integer>();
//		this.gOperated = AlgorithmUtil.copyGrapy(g);
//		/*
//		 * 1.degree 0: put such vertices into preDS and remove it from g,
//		 * because they are always dominated by themselves 2.degree 1: add its
//		 * neighbor into preDS, remove it from g
//		 */
//
//		Collection<Integer> vertices = gOperated.getVertices();
//		for (int v : vertices) {
//			int degree = gOperated.degree(v);
//			if (degree == 0) {
//				this.preDS.add(v);
//				this.gOperated.removeVertex(v);
//			} else if (degree == 1) {
//				Collection<Integer> vNegb = gOperated.getNeighbors(v);
//				for (Integer u : vNegb) {
//					this.preDS.add(u);
//				}
//				this.gOperated.removeVertex(v);
//
//			}
//		}
//
//	}

//	private void postprocess() {
//		// this.dominatingSet = (List<Integer>) CollectionUtils.union(
//		// this.dominatingSet, this.preDS);
//	}

//	private void greedy(Graph<Integer, Integer> g, List<Integer> preDS) {
//		
//		this.dominatingSet = this.preDS;
//		
//		if (AlgorithmUtil.isDS(g, preDS)) {
//			
//			return;
//		}
//
//		List<VertexDegree> vertexDegreeList = AlgorithmUtil
//				.sortVertexAccordingToDegree(g);
//		int gVerCount = g.getVertexCount();
//		while(gVerCount>0){
//			Integer v = vertexDegreeList.get(0).getVertex();
//			Collection<Integer> neigOpen = g.getNeighbors(v);
//			Collection<Integer> vItself = new ArrayList<Integer>(1);
//			vItself.add(v);
//			Collection<Integer> neigClose = null;
//			if (neigOpen != null) {
//				neigClose = CollectionUtils.union(neigOpen, vItself);
//			} else {
//				neigClose = CollectionUtils.union(new ArrayList<Integer>(),
//						vItself);
//			}
//		}
//	}

	//@SuppressWarnings("unused")
	private void greedy() {

		List<VertexDegree> vertexDegreeList = AlgorithmUtil
				.sortVertexAccordingToDegree(g);

		int gVerCount = g.getVertexCount();
		while (gVerCount > 0) {
			Integer v = vertexDegreeList.get(0).getVertex();
			Collection<Integer> neigOpen = g.getNeighbors(v);
			List<Integer> vItself = new ArrayList<Integer>(1);
			//vItself.add(v);
			AlgorithmUtil.addElementToList(vItself, v);
			
			Collection<Integer> neigClose = null;
			if (neigOpen != null) {
				neigClose = CollectionUtils.union(neigOpen, vItself);
			} else {
				neigClose = CollectionUtils.union(new ArrayList<Integer>(),
						vItself);
			}

			searchHighestUtility: for (VertexDegree vd : vertexDegreeList) {
				Integer u = vd.getVertex();
				for (Integer w : neigClose) {
					if (u.equals(w)) {
						//dominatingSet.add(u);
						AlgorithmUtil.addElementToList(dominatingSet, u);
						Collection<Integer> neighborsOfU = g.getNeighbors(u);
						// remove u's neighbours from g
						if (neighborsOfU != null && neighborsOfU.size() > 0) {
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
			vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g);
			gVerCount = g.getVertexCount();
		}

	}

}
