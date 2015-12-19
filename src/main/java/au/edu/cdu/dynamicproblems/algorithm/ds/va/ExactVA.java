package au.edu.cdu.dynamicproblems.algorithm.ds.va;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
@Deprecated
public class ExactVA implements ITask, IAlgorithm {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(ExactVA.class);
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
	private int numOfVertices;
	
	private int exactSolutionSize;

	/**
	 * the adjacency matrix of the graph
	 */
	private List<String[]> adjacencyMatrix;

	public ExactVA(List<String[]> adjacencyMatrix,int exactSolutionSize) {
		this.adjacencyMatrix = adjacencyMatrix;
		this.exactSolutionSize=exactSolutionSize;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public ExactVA(String indicator, List<String[]> adjacencyMatrix,int exactSolutionSize) {
		this.indicator = indicator;
		this.adjacencyMatrix = adjacencyMatrix;
		this.exactSolutionSize=exactSolutionSize;
		this.numOfVertices = adjacencyMatrix.size();
		this.g = AlgorithmUtil.prepareGraph(this.adjacencyMatrix);

	}

	public ExactVA(Graph<Integer, Integer> g,int exactSolutionSize) {
		this.g = g;
		this.numOfVertices = g.getVertexCount();
		this.exactSolutionSize=exactSolutionSize;
	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set
	 */
	public void computing() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		long start = System.nanoTime();
		initialization();
		start();
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

	private void start() throws  ArraysNotSameLengthException {
		Collection<Integer> vertices=g.getVertices();
		List<Integer> vList = new ArrayList<Integer>(vertices);
		
		boolean[] chosen = AlgorithmUtil.verifySubDS(vList, this.numOfVertices, this.exactSolutionSize,
				this.g);
		if(chosen==null){
			//do nothing
		}else{
			List<Integer> tempDs = new ArrayList<Integer>(this.exactSolutionSize);

			for (int i = 0; i < this.numOfVertices; i++) {
				if (chosen[i]) {
					tempDs.add(vList.get(i));
				}
			}
			this.dominatingSet = tempDs;
		}
		
		

	}

}
