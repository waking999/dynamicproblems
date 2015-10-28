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

public class GreedyNativeV0 implements IAlgorithm, ITask {

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

	public GreedyNativeV0(String indicator, List<String[]> am) {
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

		// Collection<Integer> gOrigialVertices = gOriginal.getVertices();
		// int gOriginalVerticeSize = gOrigialVertices.size();
		// Collection<Integer> gInitialVertices = gInitial.getVertices();
		//
		// Collection<Integer> undominatedVertices =
		// CollectionUtils.subtract(gOrigialVertices, gInitialVertices);
		//
		// int undomiantedVerticesSize = undominatedVertices.size();
		//
		//
		// while (undomiantedVerticesSize > 0) {
		// List<Integer> kVerticesDS=new ArrayList<Integer>();
		// List<Integer> kVertices=new ArrayList<Integer>();
		// Graph<Integer,Integer> gI=AlgorithmUtil.copyGrapy(gInitial);
		//
		// int fromIndex=0;
		// int toIndex=Math.min(k, undomiantedVerticesSize);
		// List<VertexDegree>
		// vdList=AlgorithmUtil.sortVertexAccordingToDegreeInclude(gOriginal,
		// undominatedVertices);
		// Collections.reverse(vdList);
		//
		// List<Integer> vList=AlgorithmUtil.getVertexList(vdList);
		//
		// List<Integer> subVList=vList.subList(fromIndex, toIndex);
		//
		// for(Integer u:subVList){
		// Integer w=getHighestDegreeNeighborOfAVertex(u,this.vdOriginalList);
		// AlgorithmUtil.addElementToList(kVerticesDS, w);
		// AlgorithmUtil.addElementToList(kVertices, w);
		// AlgorithmUtil.addElementToList(kVertices, u);
		//
		// }
		//
		// AlgorithmUtil.prepareGraph(am,gI, kVertices);
		//
		// List<Integer> dsInitialCopy = new ArrayList<Integer>();
		// dsInitialCopy.addAll(dsInitial);
		//
		// int paramR = Math.min(kVerticesDS.size(), r);
		//
		// DDSFPT ag = new DDSFPT(indicator, gI, dsInitial, paramR);
		// // Collection<Integer> considerableCandidateVertices4DS=
		// // CollectionUtils.union(t.getV2(), this.dsInitial);
		//
		// ag.setConsiderableCandidateVertices4DS(kVerticesDS);
		// ag.setOriginalVertexNum(gOriginalVerticeSize);
		// ag.computing();
		//
		// //List<Integer> dominatedUndominatedDifference = new
		// ArrayList<Integer>();
		//
		// dsInitial = ag.getDs2();
		// Collection<Integer> addedDSVertices =
		// CollectionUtils.subtract(dsInitial, dsInitialCopy);
		// List<Integer> verticesToAddInGraph=new ArrayList<Integer>();
		// for (Integer v : addedDSVertices) {
		// dominatedMap.put(v, true);
		// AlgorithmUtil.addElementToList(verticesToAddInGraph, v);
		// undominatedVertices.remove(v);
		//
		// Collection<Integer> vNeg = gOriginal.getNeighbors(v);
		// for (Integer u : vNeg) {
		// dominatedMap.put(u, true);
		//
		// if (!gInitial.containsVertex(u)) {
		// AlgorithmUtil.addElementToList(verticesToAddInGraph, u);
		// //dominatedUndominatedDifference.add(u);
		// }
		// }
		// undominatedVertices.removeAll(vNeg);
		//
		// }
		//
		//
		//
		// undomiantedVerticesSize = undominatedVertices.size();
		//
		// if(undomiantedVerticesSize==0){
		// break;
		// }
		//
		// AlgorithmUtil.prepareGraph(am, gInitial, verticesToAddInGraph);
		//
		// }
		//
		// this.ds = this.dsInitial;
	}

}
