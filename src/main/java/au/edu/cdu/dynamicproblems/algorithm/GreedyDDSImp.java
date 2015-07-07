package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import edu.uci.ics.jung.graph.Graph;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;

@Deprecated
public class GreedyDDSImp implements IAlgorithm, ITask {
	private static Logger log = LogUtil.getLogger(GreedyDDSImp.class);
	private long runningTime;

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
	/**
	 * the complete graph
	 */
	private Graph<Integer, Integer> gK;
	private List<Integer> ds1;

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
	private Graph<Integer, Integer> g0;

	private int numOfVertices;

	public GreedyDDSImp(String indicator, List<String[]> am, int k, int r) {
		this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		this.numOfVertices = am.size();
		this.ds = null;
	}

	public Result getResult(long threadId) {
		Result result = new Result();
		result.setIndex(threadId);

		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append(":").append(this.indicator)
				.append(":");
		sb.append(this.runningTime + " ns:");
		sb.append(this.ds.size() + ":");
		for (Integer i : this.ds) {
			sb.append(i).append(" ");
		}
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
		int am1Size = am.size();
		// construct a complete graph
		List<String[]> amK = AlgorithmUtil.generateCompleteGraph(am1Size);
		this.gK = AlgorithmUtil.prepareGraph(amK);
		this.g0 = AlgorithmUtil.prepareGraph(am);
		this.vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g0);
		this.ds1 = new ArrayList<Integer>();

	}

	private void start() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		VertexDegree vd0 = this.vertexDegreeList.get(0);
		Integer v0 = vd0.getVertex();
		// this.ds1.add(v0);
		AlgorithmUtil.addElementToList(this.ds1, v0);
		boolean isFinish = false;
		boolean isSolution = false;

		Graph<Integer, Integer> gI = gK;
		do {
			int gI_1 = gI.getEdgeCount();
			log.debug(gI_1);
			gI = doEdgeDeletion(gI);
			int gI_2 = gI.getEdgeCount();
			if (gI_1 == gI_2) {
				continue;
			}

			// DDSFPT subroutine
			// copy grapy gI because dds fpt may modify graph via reduction
			// rules
			Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGrapy(gI);
			DDSFPT ag = new DDSFPT(indicator, gICopy, ds1, r);

			ag.computing();
			List<Integer> ds2 = ag.getDs2();
			int ds1_1 = ds1.size();
			ds1 = ds2;
			int ds1_2 = ds1.size();
			if (ds1_1 != ds1_2) {
				log.debug(ds1);
				log.debug(gI.getEdgeCount());
			}

			// if we find a solution, we stop the loop
			if (AlgorithmUtil.isDS(g0, ds1)) {
				isFinish = true;
				isSolution = true;
				break;
			}
			// if the left part is of the same edges as g0, we stop the loop
			if (gI.getEdgeCount() == g0.getEdgeCount()) {
				isFinish = true;
				isSolution = false;
				break;
			}

		} while (!isFinish);

		if (isFinish && isSolution) {
			this.ds = ds1;
		} else {
			ds = null;
		}

	}

	private Graph<Integer, Integer> doEdgeDeletion(Graph<Integer, Integer> gI) {
		/*
		 * several scenarios 1.the number of incident edges of vertices in ds1
		 * is less than k; 2.k is less than or equals to the number of incident
		 * edges of vertices in ds1
		 */
		List<Integer> ngs = new ArrayList<Integer>();

		// get the dominating vertices' neighbours in g0
		for (Integer s : ds1) {
			Collection<Integer> col = g0.getNeighbors(s);

			ngs = (List<Integer>) CollectionUtils.union(ngs, col);
			// ngs.add(s);
			AlgorithmUtil.addElementToList(ngs, s);
		}

		List<Integer> sortedNgs = AlgorithmUtil
				.sortVertexAccordingToSortedVertexList(ngs,
						this.vertexDegreeList);
		int sortedNgsSize = sortedNgs.size();

		int kTemp = k;
		if (sortedNgsSize < kTemp) {
			kTemp = sortedNgsSize;
		}

		// get k neighbour from lowest to highest to be removed
		int iStart = sortedNgsSize - 1;
		int iEnd = sortedNgsSize - kTemp;
		for (int i = iStart; i >= iEnd; i--) {
			Integer nv = sortedNgs.get(i);
			Collection<Integer> neighboursOfNvInGI = gI.getNeighbors(nv);
			Collection<Integer> neighboursOfNvInG0 = g0.getNeighbors(nv);
			Collection<Integer> neighboursofNvNotInG0 = CollectionUtils
					.subtract(neighboursOfNvInGI, neighboursOfNvInG0);

			int neighboursofNvNotInG0Size = neighboursofNvNotInG0.size();
			if (neighboursofNvNotInG0Size == 0) {
				iEnd--;
				if (iEnd < 0) {
					iEnd = 0;
				}
				continue;
			}
			// remove from the graph
			for (Integer rv : neighboursofNvNotInG0) {

				int edge = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(
						this.numOfVertices, rv, nv);

				gI.removeEdge(edge);

			}

		}

		return gI;

	}
}
