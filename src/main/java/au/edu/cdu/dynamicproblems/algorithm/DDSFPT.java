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
import au.edu.cdu.dynamicproblems.exception.NChooseMNoSolutionException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import au.edu.cdu.dynamicproblems.view.GraphView;
import au.edu.cdu.dynamicproblems.view.Section;
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
	private Graph<Integer, Integer> g2;
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
		sb.append(this.getClass().getName()).append(":").append(this.indicator)
				.append(":");
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

	public DDSFPT(String indicator, List<String[]> adjacencyMatrix2,
			List<Integer> dominatingSet1, int r) {
		this.indicator = indicator;
		this.adjacencyMatrix2 = adjacencyMatrix2;
		this.ds1 = dominatingSet1;
		this.g2 = AlgorithmUtil.prepareGraph(this.adjacencyMatrix2);
		this.r = r;
		this.hasLessR = false;

	}

	public DDSFPT(String indicator, Graph<Integer, Integer> g2,
			List<Integer> dominatingSet1, int r) {
		this.indicator = indicator;
		this.ds1 = dominatingSet1;
		this.g2 = g2;
		this.r = r;
		this.hasLessR = false;
	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a dominating set of graph 2
	 */
	public void computing() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		long start = System.nanoTime();
		initialization();
		reductionRulesOnGraph2();
		// Collection<Integer> vertexCover = reducedInstance.getVertexCover();

		int vertexCoverSize = vertexCover.size();

		try {
			if (vertexCoverSize > 0) {
				//log.debug("real k=" + vertexCoverSize);
				// Graph<Integer, Integer> gStar = reducedInstance.getG();

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
		List<Integer> neighboursOfDs1InG2 = AlgorithmUtil.getNeighborsOfS(g2,
				ds1);
		neighboursOfDs1InG2 = (List<Integer>) CollectionUtils.subtract(
				neighboursOfDs1InG2, ds1);
		vertexCover = CollectionUtils.subtract(verticesInG2,
				neighboursOfDs1InG2);
		vertexCover = CollectionUtils.subtract(vertexCover, ds1);

		List<List<Integer>> vertexSections = new ArrayList<List<Integer>>();
//		vertexSections.add(ds1);
//		vertexSections.add((List<Integer>) vertexCover);
//		vertexSections.add(neighboursOfDs1InG2);
		AlgorithmUtil.addElementToList(vertexSections, ds1);
		AlgorithmUtil.addElementToList(vertexSections, (List<Integer>)vertexCover);
		AlgorithmUtil.addElementToList(vertexSections, neighboursOfDs1InG2);
		// viewGraph( g2,vertexSections);

		// apply reduction rules
		g2 = r1(g2, ds1);

		g2 = r2(g2, neighboursOfDs1InG2, vertexCover);

		g2 = r3(g2, neighboursOfDs1InG2, verticesNum);

		// if (!vertexCover.isEmpty()) {
		// log.debug(vertexCover.size());
		// viewGraph(g2, vertexSections);
		// }
		// viewGraph(g2, vertexSections);
		// ReducedInstance reducedInstance = new ReducedInstance(g2,
		// vertexCover);
		// return reducedInstance;

	}

	protected void viewGraph(Graph<Integer, Integer> g,
			List<List<Integer>> vertexSections) {
		float width = 1024;
		float height = 768;

		Section sec1 = new Section(0.0f, 0.0f, width / 2 * 0.9f, height,
				vertexSections.get(0));
		Section sec2 = new Section(width / 2 * 1.1f, 0f, width,
				height / 2 * 0.9f, vertexSections.get(1));
		Section sec3 = new Section(width / 2 * 1.1f, height / 2 * 1.1f, width,
				height, vertexSections.get(2));

		List<Section> sections = new ArrayList<Section>();
//		sections.add(sec1);
//		sections.add(sec2);
//		sections.add(sec3);
		AlgorithmUtil.addElementToList(sections, sec1);
		AlgorithmUtil.addElementToList(sections, sec2);
		AlgorithmUtil.addElementToList(sections, sec3);
		
		GraphView.presentGraph(g, sections, width, height);
	}

	private void domAVcFpt(Collection<Integer> vertexCover,
			Graph<Integer, Integer> gStar, int r) throws MOutofNException,
			NChooseMNoSolutionException, ExceedLongMaxException,
			ArraysNotSameLengthException {

		int tryR = R_START;
		if (r < tryR) {
			tryR = r;
		}
		DomAVCFPT ag = null;
		do {

			ag = new DomAVCFPT(gStar, (List<Integer>) vertexCover, tryR);
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
						//log.debug("using vertex cover as the ds2 in worst case.");
						break;
					}
				}
			} catch (NChooseMNoSolutionException e) {
				this.hasLessR = false;
				tryR++;
				if (tryR > r) {
					// r may be bigger so that the running time will be very
					// big.in this worst case, using vertex cover as ds2.
					//log.debug("using vertex cover as the ds2 in worst case.");
					break;
				}
			}
		} while (!this.hasLessR);
		if (this.hasLessR) {
			List<Integer> SStar = ag.getDominatingVertexCoverSet();
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
	private static Graph<Integer, Integer> r1(Graph<Integer, Integer> g,
			Collection<Integer> S) {
		for (Integer s : S) {
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
	private static Graph<Integer, Integer> r2(Graph<Integer, Integer> g,
			Collection<Integer> B, Collection<Integer> C) {
		for (Integer b : B) {
			Collection<Integer> neiB = g.getNeighbors(b);
			Collection<Integer> intsec = null;
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
	private static Graph<Integer, Integer> r3(Graph<Integer, Integer> g,
			Collection<Integer> B, int verticesNum) {

		for (Integer b1 : B) {
			for (Integer b2 : B) {
				if (!b1.equals(b2)) {
					int edge = b1 * verticesNum + b2;
					if (g.containsEdge(edge)) {
						g.removeEdge(edge);
					}
				}
			}
		}

		return g;
	}

	// class ReducedInstance {
	// Graph<Integer, Integer> g;
	// Collection<Integer> vertexCover;
	//
	// protected Graph<Integer, Integer> getG() {
	// return g;
	// }
	//
	// protected Collection<Integer> getVertexCover() {
	// return vertexCover;
	// }
	//
	// protected ReducedInstance(Graph<Integer, Integer> g,
	// Collection<Integer> vertexCover) {
	// this.g = g;
	// this.vertexCover = vertexCover;
	// }
	// }

}
