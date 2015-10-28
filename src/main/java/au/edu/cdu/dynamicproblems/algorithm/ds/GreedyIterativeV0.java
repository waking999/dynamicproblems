/**
 * implement Michael's original idea
 */

package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

public class GreedyIterativeV0 implements IAlgorithm, ITask {

	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(GreedyNativeV0Test.class);
	private long runningTime;

	@Override
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

	private List<Integer> ds;

	// used for pre-process
	// private List<Integer> dsInitial;
	// private List<Integer> initialVertices;

	public List<Integer> getDs() {
		return ds;
	}

	private Map<Integer, Boolean> dominatedMap;

	/**
	 * the original graph
	 */
	private Graph<Integer, Integer> gOriginal;

	/**
	 * the graph after pre-process
	 */
	// private Graph<Integer, Integer> gInitial;

	public GreedyIterativeV0(String indicator, List<String[]> am) {
		this.indicator = indicator;
		this.am = am;

		this.ds = null;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public void setAm(List<String[]> am) {
		this.am = am;
	}

	public Result getResult() {
		Result r = new Result();
		r.setHasSolution(true);
		StringBuffer sb = new StringBuffer();

		sb.append(",").append(this.ds.size()).append(",").append(this.runningTime);
		r.setString(sb.toString());
		return r;
	}

	public void computing()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {

		initialization();
		long start = System.nanoTime();
		// preprocess();

		start();
		long end = System.nanoTime();

		this.runningTime = end - start;
	}

	private List<VertexDegree> vdOriginalList;

	private void initialization() {

		this.gOriginal = AlgorithmUtil.prepareGraph(am);

		this.ds = new ArrayList<Integer>();
		// this.dsInitial = new ArrayList<Integer>();

		dominatedMap = new HashMap<Integer, Boolean>();
		Collection<Integer> vertices = gOriginal.getVertices();
		for (Integer v : vertices) {
			dominatedMap.put(v, false);
		}
		// order vertex according to their degree from lowest to highest
		vdOriginalList = AlgorithmUtil.sortVertexAccordingToDegree(gOriginal);
		Collections.reverse(vdOriginalList);
	}

	private Integer getHighestDegreeNeighborOfAVertex(Integer v, List<VertexDegree> vdList) {
		Collection<Integer> vNeg = gOriginal.getNeighbors(v);
		List<Integer> vNegList = new ArrayList<Integer>(vNeg);

		int vdListSize = vdList.size();
		for (int i = vdListSize - 1; i >= 0; i--) {
			VertexDegree vd = vdList.get(i);
			Integer u = vd.getVertex();

			int index = Collections.binarySearch(vNegList, u);
			if (index >= 0) {
				return vNegList.get(index);
			}
		}

		return null;
	}

	private void addDominatingVertexAndItsNeigbors(List<Integer> ds, Integer v) {
		AlgorithmUtil.addElementToList(ds, v);
		dominatedMap.put(v, true);
		Collection<Integer> vNegb = gOriginal.getNeighbors(v);
		for (Integer w : vNegb) {
			dominatedMap.put(w, true);
		}
	}

	// private void preprocess() {
	// this.gInitial = new SparseMultigraph<Integer, Integer>();
	//
	// initialVertices = new ArrayList<Integer>();
	//
	//
	// // order vertex according to their degree from lowest to highest
	//// List<VertexDegree> vertexDegreeList =
	// AlgorithmUtil.sortVertexAccordingToDegree(gOriginal);
	//// Collections.reverse(vertexDegreeList);
	//
	// // put the the highest vertex in N[lowest degree vertex] into dsInitial,
	// // gInitial
	// VertexDegree vd = this.vdOriginalList.get(0);
	// Integer v0 = vd.getVertex();
	// Integer u0 = getHighestDegreeNeighborOfAVertex(v0, this.vdOriginalList);
	//
	// addDominatingVertexAndItsNeigbors(this.dsInitial, this.initialVertices,
	// u0);
	//
	// AlgorithmUtil.prepareGraph(am, gInitial, initialVertices);
	//
	//
	// }

	private void start()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException {

		int i = 0;
		while (!AlgorithmUtil.isAllDominated(dominatedMap)) {
			VertexDegree vd = this.vdOriginalList.get(i);
			Integer v = vd.getVertex();
			Integer u = getHighestDegreeNeighborOfAVertex(v, this.vdOriginalList);
			addDominatingVertexAndItsNeigbors(this.ds, u);
			i++;
		}

	
	}

}
