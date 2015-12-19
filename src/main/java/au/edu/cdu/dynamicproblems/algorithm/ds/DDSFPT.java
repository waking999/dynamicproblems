package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.exception.NChooseMNoSolutionException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class DDSFPT implements IAlgorithm, ITask {
	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(DDSFPT.class);
	private static final int R_START = 1;
	private long runningTime;
	private String eMsg;
	private Collection<Integer> vertexCover;

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

	/**
	 * a dominating set solution of graph 1
	 */
	private List<Integer> ds1;
	/**
	 * the adjacency matrix of graph 2
	 */
	private List<String[]> adjacencyMatrix2;
	/**
	 * graph 2
	 */
	private Graph<Integer, String> g2;
	/**
	 * the desired dominating set of graph 2
	 */
	private List<Integer> ds2;
	private int r;
	private String indicator;
	private boolean hasLessR;

	public List<Integer> getDs2() {
		return ds2;
	}

	public Result getResult(long threadId) {
		Result result = new Result();
		result.setIndex(threadId);
		result.setHasSolution(this.hasLessR);

		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName()).append(":").append(this.indicator).append(":");
		sb.append(this.runningTime + " ns:");
		sb.append("ds1Size=" + this.ds1.size() + ":");
		sb.append("r=" + r + ":");
		if (this.hasLessR) {
			sb.append(this.ds2.size());
		} else {
			sb.append(this.eMsg);
		}
		sb.append(":");
		if (this.hasLessR) {

			for (Integer i : this.ds2) {
				sb.append(i).append(" ");
			}
		}

		result.setString(sb.toString());
		return result;
	}

	public DDSFPT(String indicator, List<String[]> adjacencyMatrix2, List<Integer> dominatingSet1, int r) {
		this.indicator = indicator;
		this.adjacencyMatrix2 = adjacencyMatrix2;
		this.ds1 = dominatingSet1;
		this.g2 = AlgorithmUtil.prepareGenericGraph(this.adjacencyMatrix2);
		this.r = r;
		this.hasLessR = false;

	}

	public DDSFPT(String indicator, Graph<Integer, String> g2, List<Integer> dominatingSet1, int r) {
		this.indicator = indicator;
		this.ds1 = dominatingSet1;
		this.g2 = g2;
		this.r = r;
		this.hasLessR = false;
	}

	private Collection<Integer> considerableCandidateVertices4DS; 

	public void setConsiderableCandidateVertices4DS(Collection<Integer> considerableCandidateVertices4DS) {
		this.considerableCandidateVertices4DS = considerableCandidateVertices4DS;
	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set of graph 2
	 */
	public void computing() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException {
		long start = System.nanoTime();
		initialization();
		reductionRulesOnGraph2();

		int vertexCoverSize = vertexCover.size();

		try {
			if (vertexCoverSize > 0) {

				domAVcFpt(vertexCover, g2, r);

			} else {
				this.hasLessR = true;
				this.ds2 = ds1;
			}
		} catch (NChooseMNoSolutionException e) {
			this.eMsg = e.getMessage();
		} finally {
			long end = System.nanoTime();
			this.runningTime = end - start;
		}

	}

	private void initialization() {
		this.ds2 = new ArrayList<Integer>();
	}

	/**
	 * apply reduction rules in graph 2
	 * 
	 * @return a vertex cover in reduced graph
	 */
	private void reductionRulesOnGraph2() {
		/*
		 * prepare graph 2 to get B(neighboursOfDs1InG2), C(vertexCover) in
		 * order to apply the reduction rules
		 */
		Collection<Integer> verticesInG2 = g2.getVertices();
		int verticesNum = verticesInG2.size();
		Collection<Integer> neighboursOfDs1InG2 = AlgorithmUtil.getNeighborsOfS(g2, ds1);
		neighboursOfDs1InG2 = (List<Integer>) CollectionUtils.subtract(neighboursOfDs1InG2, ds1);
		vertexCover = CollectionUtils.subtract(verticesInG2, neighboursOfDs1InG2);
		vertexCover = CollectionUtils.subtract(vertexCover, ds1);

		
		
		// apply reduction rules
		g2 = r1(g2, ds1);

		g2 = r2(g2, neighboursOfDs1InG2, vertexCover);

		g2 = r3(g2, neighboursOfDs1InG2, verticesNum);


	}

	

	private void domAVcFpt(Collection<Integer> vertexCover, Graph<Integer, String> gStar, int r)
			throws MOutofNException, NChooseMNoSolutionException, ExceedLongMaxException, ArraysNotSameLengthException {

		int tryR = R_START;
		if (r < tryR) {
			tryR = r;
		}
		DomAVCFPT ag = null;
		do {

			ag = new DomAVCFPT(gStar, (List<Integer>) vertexCover, tryR);
			ag.setConsiderableCandidateVertices4DS(considerableCandidateVertices4DS);
			try {
				ag.computing();

				if (ag.hasLessR()) {

					this.hasLessR = true;

				} else {
					// can not find a solution with parameter r;
					this.hasLessR = false;
					tryR++;
					if (tryR > r) {
						// r may be bigger so that the running time will be very
						// big.in this worst case, using vertex cover as ds2.
						// log.debug("using vertex cover as the ds2 in worst
						// case.");
						break;
					}
				}
			} catch (NChooseMNoSolutionException e) {
				this.hasLessR = false;
				tryR++;
				if (tryR > r) {
					// r may be bigger so that the running time will be very
					// big.in this worst case, using vertex cover as ds2.
					// log.debug("using vertex cover as the ds2 in worst
					// case.");
					break;
				}
			}
		} while (!this.hasLessR);
		if (this.hasLessR) {
			Collection<Integer> SStar = ag.getDominatingVertexCoverSet();
			if(SStar.size()>=this.considerableCandidateVertices4DS.size()){
				SStar=this.considerableCandidateVertices4DS;
			}
			
			this.ds2 = (List<Integer>) CollectionUtils.union(ds1, SStar);
		} else {
			this.ds2 = (List<Integer>) CollectionUtils.union(ds1, vertexCover);
		}
	}

	/**
	 * apply reduction rule 1:if v belongs to ds1, remove v and its incident
	 * edges from g2
	 * 
	 * @param g
	 *            , graph 2
	 * @param S
	 *            , dominating set of graph 1(ds1)
	 * @return
	 */
	private <V,E> Graph<V, E> r1(Graph<V, E> g, Collection<V> S) {
		for (V s : S) {
			synchronized (s) {
				g.removeVertex(s);
			}
		}
		return g;
	}

	/**
	 * apply reudction rule 2: if v belongs to B, and N(v) intersect C is empty,
	 * remove v and its incident edges in g2
	 * 
	 * @param g
	 *            , the graph 2
	 * @param B
	 *            , the neighbours of ds1 in g2
	 * @param C
	 *            , the undominated vertices by ds1
	 * @return
	 */
	private <V,E> Graph<V, E> r2(Graph<V, E> g, Collection<V> B, Collection<V> C) {
		for (V b : B) {
			Collection<V> neiB = g.getNeighbors(b);
			Collection<V> intsec = null;
			if (neiB != null && C != null) {
				intsec = CollectionUtils.intersection(neiB, C);
			}

			if (intsec == null || intsec.size() == 0) {
				synchronized (DDSFPT.class) {
					g.removeVertex(b);
				}
			}
		}
		return g;
	}


	/**
	 * reduction rule 3, if(u,v) is an edge and {u,v} belongs B, remove the
	 * edge(U,v)
	 * 
	 * @param g
	 *            , the graph 2
	 * @param B
	 *            , the neighbours of ds1 in g2
	 * @return
	 */
	private Graph<Integer, String> r3(Graph<Integer, String> g, Collection<Integer> B, int verticesNum) {

		for (Integer b1 : B) {
			for (Integer b2 : B) {
				if (!b1.equals(b2)) {
					
					String edge=AlgorithmUtil.getEdgeLabelBy2VerticesLabel(b1, b2);
					if (g.containsEdge(edge)) {
						g.removeEdge(edge);
					}
				}
			}
		}

		return g;
	}

}
