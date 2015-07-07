package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyDDS implements IAlgorithm, ITask {
	private static Logger log = LogUtil.getLogger(GreedyDDS.class);
	private long runningTime;

	private List<Integer> isolatedDS;
	private Collection<Integer> g0EdgeList;

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

	public GreedyDDS(String indicator, List<String[]> am, int k, int r) {
		this.indicator = indicator;
		this.am = am;
		this.k = k;
		this.r = r;
		// this.numOfVertices = am.size();
		this.ds = null;
	}

	public Result getResult(long threadId) {
		Result result = new Result();
		result.setIndex(threadId);

		StringBuffer sb = new StringBuffer();
//		sb.append(this.getClass().getName()).append(":").append(this.indicator)
//				.append(":");
//		sb.append(this.runningTime + " ns:");
//		sb.append(this.ds.size() + ":");
//		for (Integer i : this.ds) {
//			sb.append(i).append(" ");
//		}
		sb.append(",").append(this.ds.size()).append(",").append(k).append(",").append(r).append(", ").append(this.runningTime);
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

		// construct a complete graph
		this.numOfVertices = am.size();
		List<String[]> amK = AlgorithmUtil
				.generateCompleteGraph(this.numOfVertices);
		this.gK = AlgorithmUtil.prepareGraph(amK);
		this.g0 = AlgorithmUtil.prepareGraph(am);
		this.vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g0);
		this.g0EdgeList = g0.getEdges();
		// preprocess();
		this.ds1 = new ArrayList<Integer>();

	}

	@SuppressWarnings("unused")
	private void preprocess() {

		this.g0 = AlgorithmUtil.prepareGraph(am);
		isolatedDS = new ArrayList<Integer>();
		Collection<Integer> vertices = g0.getVertices();
		int verSize = vertices.size();
		for (int i = 0; i < verSize; i++) {
			int degree = g0.degree(i);
			if (degree == 0) {
				//isolatedDS.add(i);
				AlgorithmUtil.addElementToList(isolatedDS, i);
				g0.removeVertex(i);
			}
		}

		numOfVertices = g0.getVertexCount();
		List<String[]> amK = AlgorithmUtil.generateCompleteGraph(numOfVertices);
		this.gK = AlgorithmUtil.prepareGraph(amK);
		this.vertexDegreeList = AlgorithmUtil.sortVertexAccordingToDegree(g0);

	}

	private void start() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		VertexDegree vd0 = this.vertexDegreeList.get(0);
		Integer v0 = vd0.getVertex();
		this.ds1.add(v0);

		boolean isFinish = false;
		boolean isSolution = false;

		Graph<Integer, Integer> gI = gK;
		do {
			//log.debug(gI.getEdgeCount());
			 //gI = doKEdgeDeletion(gI);
			gI = doKVerticesEdgeDeletion(gI);
			if (AlgorithmUtil.isDS(gI, ds1)) {
				// if the graph gi is not hurt after edge deletion;
				if (gI.getEdgeCount() == g0.getEdgeCount()) {
					isFinish = true;
					isSolution = true;
					break;
				}
				continue;
			}

			// DDSFPT subroutine
			// copy grapy gI because dds fpt may modify graph via reduction
			// rules
			Graph<Integer, Integer> gICopy = AlgorithmUtil.copyGrapy(gI);
			DDSFPT ag = new DDSFPT(indicator, gICopy, ds1, r);

			ag.computing();
			List<Integer> ds2 = ag.getDs2();
			@SuppressWarnings("unused")
			int ds1_1 = ds1.size();
			ds1 = ds2;
			@SuppressWarnings("unused")
			int ds1_2 = ds1.size();
			// if (ds1_1 != ds1_2) {
			// log.debug(ds1);
			// }

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
			// this.ds = (List<Integer>) CollectionUtils.union(ds1, isolatedDS);
			this.ds = ds1;
		} else {
			ds = null;
		}

	}

	/**
	 * make k vertices be harmed
	 * 
	 * @param g
	 * @return
	 */
	private Graph<Integer, Integer> doKVerticesEdgeDeletion(
			Graph<Integer, Integer> g) {

		// 1.get k vertices (from lowest degree to highest) linking to ds1 in gI
		// not in g0
		// 2. delete their incident edges in gI not in g0

		/*
		 * several scenarios: 1. the number of vertices connected to ds1 in gI
		 * but not in g0 is greater than k, then choose k vertices and delete
		 * their incident edges in gI not in g0 to ds1; 2.the number of vertices
		 * connected to ds1 in gI but not in g0 is less than k, then only delete
		 * these vertices' incident edges in gI not in g0
		 */
		// int kTemp = k;
		int iStart = this.numOfVertices - 1;
		int iEnd = this.numOfVertices - k;
		@SuppressWarnings("unused")
		int edgeDeletionCount = 0;
		for (int i = iStart; i >= iEnd; i--) {
			VertexDegree vd = this.vertexDegreeList.get(i);
			Integer v = vd.getVertex();

			Collection<Integer> vNgList = g.getNeighbors(v);
			Collection<Integer> vNg0List = g0.getNeighbors(v);
			Collection<Integer> vNgNotg0List = CollectionUtils.subtract(
					vNgList, vNg0List);
			int vNgNotg0ListSize = 0;
			if (vNgNotg0List != null) {
				vNgNotg0ListSize = vNgNotg0List.size();
			}
			if (vNgNotg0ListSize == 0) {
				// no edges available to be removed;
				// try the next one to try get k vertices to be hurt. but it
				// still could be unsatisfied because the limitation in size of
				// ds1
				iEnd--;
				if (iEnd < 0) {
					break;
				}
				continue;
			}
			// actually, this piece of for loop is in the if-else block before,
			// but I found it always runs whatever if the vertex will be hurt or
			// not. Therefore, I put it here and let the if-else to do the
			// harm/no-harm judgment,to control the number of outter for-loop
			// cycles
			for (Integer nv : vNgNotg0List) {
				int edge = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(
						this.numOfVertices, v, nv);
				// log.debug("(" + nv + "," + v + ") is removed");
				g.removeEdge(edge);
			}
			edgeDeletionCount += vNgNotg0ListSize;

			Collection<Integer> vNgNotg0ListInterDs1 = CollectionUtils
					.intersection(vNgNotg0List, ds1);
			if (vNgNotg0ListInterDs1 == null || vNgNotg0ListInterDs1.isEmpty()) {
				// the vertex has edges, but not to be hurt,
				// then delete these harmless edges (including edges linking to
				// ds1 and edges linking to B),
				// and try the next one to try get k vertices to be hurt. but it
				// still could be unsatisfied because the limitation in size of
				// ds1

				iEnd--;
				if (iEnd < 0) {
					break;
				}
				continue;

			} else {
				// the vertex has harmful edges
				// then delete these harmful edges (including edges linking to
				// ds1 and edges linking to B)

			}

		}
		if (iEnd < 0) {
			// it means all vertices are gone through and no vertices available
			// to be able to be hurt any more,
			// delete the left edges in g not in g0

			Collection<Integer> edgesInGNotG0 = CollectionUtils.subtract(
					g.getEdges(), g0.getEdges());
			int edgesInGNotG0Size = edgesInGNotG0.size();
			edgeDeletionCount += edgesInGNotG0Size;
			for (Integer edge : edgesInGNotG0) {
				g.removeEdge(edge);
			}

		}
		// log.debug(edgeDeletionCount + " edges are removed");
		return g;

	}

	/**
	 * delete k edges
	 * 
	 * @param gI
	 * @return
	 */
	@SuppressWarnings("unused")
	private Graph<Integer, Integer> doKEdgeDeletion(Graph<Integer, Integer> gI) {

		List<Integer> ngs = new ArrayList<Integer>();

		// get the dominating vertices' neighbours in g0
		for (Integer s : ds1) {
			Collection<Integer> col = g0.getNeighbors(s);

			ngs = (List<Integer>) CollectionUtils.union(ngs, col);
			//ngs.add(s);
			AlgorithmUtil.addElementToList(ngs, s);
			

		}

		List<Integer> sortedNgs = AlgorithmUtil
				.sortVertexAccordingToSortedVertexList(ngs,
						this.vertexDegreeList);
		int sortedNgsSize = sortedNgs.size();

		// get neighbour from lowest to highest
		int kTemp = k;
		// List<Integer> removeEdgeList = new ArrayList<Integer>();
		for (int i = sortedNgsSize - 1; i >= 0; i--) {
			Integer nv = sortedNgs.get(i);
			Collection<Integer> neighboursOfNvInGI = gI.getIncidentEdges(nv);
			Collection<Integer> neighboursOfNvInG0 = g0.getIncidentEdges(nv);
			Collection<Integer> neighboursofNvNotInG0 = CollectionUtils
					.subtract(neighboursOfNvInGI, neighboursOfNvInG0);

			int neighboursofNvNotInG0Size = neighboursofNvNotInG0.size();
			if (neighboursofNvNotInG0Size == 0) {
				continue;
			}
			List<Integer> removeNgList = null;
			if (neighboursofNvNotInG0Size > kTemp) {
				// get random vertices from
				removeNgList = AlgorithmUtil.getKRandomVerticesInSet(kTemp,
						neighboursofNvNotInG0);
				kTemp = 0;
			} else {
				removeNgList = (List<Integer>) neighboursofNvNotInG0;
				kTemp -= neighboursofNvNotInG0Size;

			}

			// remove from the graph
			for (Integer edge : removeNgList) {
				// log.debug("(" + nv + "," + rv + ") is removed");
				// int edge = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(
				// this.numOfVertices, rv, nv);

				// removeEdgeList.add(edge);
				// int v1 = edge / this.numOfVertices;
				// int v2 = edge - v1 * this.numOfVertices;
				// log.debug("(" + v1 + "," + v2 + ") is removed");
				gI.removeEdge(edge);

			}

			if (kTemp <= 0) {
				break;
			}

		}
		/*
		 * several scenarios: 1.k is less than or equals to the number of
		 * incident edges of vertices in ds1; then delete k edges;
		 * 
		 * 2.the number of incident edges of vertices in ds1 is less than k,find
		 * another k-removeEdgeListSize edges to be removed;
		 */

		if (kTemp > 0) {
			// it means scenario 2;

			Collection<Integer> gIEdgeList = gI.getEdges();
			Collection<Integer> gIEdgeListNotInG0 = CollectionUtils.subtract(
					gIEdgeList, g0EdgeList);
			if (gIEdgeListNotInG0.isEmpty()) {
				return gI;
			}
			log.debug((k - kTemp) + " edges are deleted, need extra " + kTemp
					+ " edges to be removed");

			List<Integer> allEdgeVers = new ArrayList<Integer>();
			for (Integer edge : gIEdgeListNotInG0) {
				Collection<Integer> evs = gI.getIncidentVertices(edge);
				allEdgeVers = (List<Integer>) CollectionUtils.union(
						allEdgeVers, evs);
			}

			List<Integer> sortedAllEdgeVers = AlgorithmUtil
					.sortVertexAccordingToSortedVertexList(allEdgeVers,
							this.vertexDegreeList);
			int sortedAllEdgeVersSize = sortedAllEdgeVers.size();
			List<Integer> extraRemoveList = null;
			for (int i = sortedAllEdgeVersSize - 1; i >= 0; i--) {
				Integer v = sortedAllEdgeVers.get(i);
				Collection<Integer> removeEdgeList = CollectionUtils
						.intersection(gI.getIncidentEdges(v), gIEdgeListNotInG0);
				int removeEdgeListSize = removeEdgeList.size();
				if (removeEdgeListSize > kTemp) {
					// get random vertices from
					extraRemoveList = AlgorithmUtil.getKRandomVerticesInSet(
							kTemp, removeEdgeList);
					kTemp = 0;
				} else {
					extraRemoveList = (List<Integer>) removeEdgeList;
					kTemp -= removeEdgeListSize;
				}

				for (Integer edge : extraRemoveList) {
					// int v1 = edge / this.numOfVertices;
					// int v2 = edge - v1 * this.numOfVertices;
					// log.debug("(" + v1 + "," + v2 + ") is removed");
					gI.removeEdge(edge);
				}
				if (kTemp <= 0) {
					break;
				}

			}

			// List<Integer> extraRemoveList = AlgorithmUtil
			// .getKRandomVerticesInSet(kTemp, gIEdgeListNotInG0);
			// int extraRemoveListSize = extraRemoveList.size();
			// for (int i = 0; i < extraRemoveListSize; i++) {
			// Integer edge=extraRemoveList.get(i);
			// // int v1=edge/this.numOfVertices;
			// // int v2=edge - v1*this.numOfVertices;
			// // log.debug("(" + v1 + "," + v2 + ") is removed");
			// gI.removeEdge(edge);
			// }

		}

		return gI;

	}

}
