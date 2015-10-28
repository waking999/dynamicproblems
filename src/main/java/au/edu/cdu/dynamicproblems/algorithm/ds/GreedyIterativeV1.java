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

public class GreedyIterativeV1 implements ITask, IAlgorithm {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyIterativeV1.class);
	private long runningTime;

	public long getRunningTime() {
		return runningTime;
	}

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
	@SuppressWarnings("unused")
	private int numOfVertices;

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	public GreedyIterativeV1(List<String[]> adjacencyMatrix) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public GreedyIterativeV1(String indicator, List<String[]> adjacencyMatrix) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public GreedyIterativeV1(Graph<Integer, Integer> g) {
		this.g = g;
		this.numOfVertices = g.getVertexCount();

	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set
	 */
	public void computing() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
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
			//List<VertexDegree> vdList = AlgorithmUtil.sortVertexAccordingToUtility(g, dominatedMap);
			List<VertexDegree> vdList=AlgorithmUtil.sortVertexAccordingToUtility(g, dominatedMap);
						

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
	
	private Integer getProabilityVertex(List<VertexDegree> vdList){
		Integer[] vertices=new Integer[this.numOfVertices];
		int[] utilities=new int[this.numOfVertices];
		
		for(int i=0;i<this.numOfVertices;i++){
			VertexDegree vd=vdList.get(i);
			vertices[i]=vd.getVertex();
			if(i==0){
				
				utilities[i]=vd.getDegree();
			}else{
				utilities[i]=vd.getDegree()+utilities[i-1];
			}
			
		}
		
		int maxNum=utilities[this.numOfVertices-1];
		
		int randNum=(int) Math.round(Math.random() * maxNum);
		
		if(randNum<=utilities[0]){
			return vertices[0];
		}
		
		int i=0;
		while(randNum>utilities[i]){
			i++;
		}
		
		return vertices[i];
		
	}

}
