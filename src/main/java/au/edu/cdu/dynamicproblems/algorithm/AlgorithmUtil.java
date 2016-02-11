package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.VertexPriority;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * this class is used for some common methods used in algorithms as a utility
 * 
 * @author kai wang
 *
 */
public class AlgorithmUtil {
	@SuppressWarnings("unused")
	private static Logger log = LogUtil.getLogger(AlgorithmUtil.class);

	public static final String CONNECTED = "1";
	public static final String UNCONNECTED = "0";

	public static final String BLANK = " ";

	// used for vertex covers to show if they are dominated or not
	public final static byte MARKED = 1;
	public final static byte UNMARKED = 0;

	public static final String COMMA = ",";
	public static final String UNDERLINE = "_";

	public final static boolean DESC_ORDER = false;
	public final static boolean ASC_ORDER = true;

	public final static String RUNNING_TIME_TOTAL = "Total";
	public final static String RUNNING_TIME_DDS = "DDS";
	public final static String RUNNING_TIME_MINI = "Minimal";
	public final static String RUNNING_TIME_LS = "LS";
	public final static String RUNNING_TIME_POLYRR = "Poly-RR";
	public final static String RUNNING_TIME_DEGREERR = "Degree-RR";
	public final static String RUNNING_TIME_GUARANTEE = "Guarantee";
	// used for left pad for binary string of an integer
	// @Deprecated
	// private static final String BINARY_LEFT_PAD = "0";

	// the ascii code of 0
	private static final byte ASCII_0_SEQ_NO = 48;

	private final static boolean CHOSEN = true;
	private final static boolean UNCHOSEN = false;

	/**
	 * generate an instance of Graph with internal parameters
	 * 
	 * @param adjacencyMatrix
	 *            , adjacency matrix of a graph
	 * @return a graph
	 */
	@Deprecated
	public static Graph<Integer, Integer> prepareGraph(List<String[]> adjacencyMatrix) {

		int numOfVertices = adjacencyMatrix.size();
		Graph<Integer, Integer> g = new SparseMultigraph<Integer, Integer>();
		for (int i = 0; i < numOfVertices; i++) {
			g.addVertex(i);
		}

		for (int i = 0; i < numOfVertices; i++) {
			String[] rowArr = adjacencyMatrix.get(i);
			for (int j = i + 1; j < numOfVertices; j++) {
				if (CONNECTED.equals(rowArr[j].trim())) {
					// the label of edge is decided by the label of the two
					// endpoints
					int edge = getEdgeLabelBy2VerticesLabel(numOfVertices, i, j);
					g.addEdge(edge, i, j);

				}
			}
		}
		return g;
	}

	/**
	 * prepare a graph (supporting adding/deleting vertices and edges)
	 * 
	 * @param adjacencyMatrix,
	 *            the adjacency matrix
	 * @return
	 */
	public static Graph<Integer, String> prepareGenericGraph(List<String[]> adjacencyMatrix) {

		int numOfVertices = adjacencyMatrix.size();
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		for (int i = 0; i < numOfVertices; i++) {
			g.addVertex(i);
		}

		for (int i = 0; i < numOfVertices; i++) {
			String[] rowArr = adjacencyMatrix.get(i);
			for (int j = i + 1; j < numOfVertices; j++) {
				if (CONNECTED.equals(rowArr[j].trim())) {
					/*
					 * the label of edge is decided by the label of the two
					 * endpoints
					 */
					String edge = getEdgeLabelBy2VerticesLabel(i, j);
					g.addEdge(edge, i, j);

				}
			}
		}
		return g;
	}

	/**
	 * add vertices in vList to g and add edges between such vertices to g as
	 * well
	 * 
	 * @param adjacencyMatrix
	 * @param g
	 * @param vList
	 * @return
	 */
	public static Graph<Integer, String> prepareGenericGraph(List<String[]> adjacencyMatrix, Graph<Integer, String> g,
			Collection<Integer> vList) {
		if (vList.isEmpty()) {
			return g;
		}

		int numOfVertices = adjacencyMatrix.size();

		for (Integer i : vList) {
			if (!g.containsVertex(i)) {
				g.addVertex(i);
			}
		}

		Collection<Integer> gVertices = g.getVertices();
		for (int i = 0; i < numOfVertices; i++) {
			if (gVertices.contains(i)) {

				String[] rowArr = adjacencyMatrix.get(i);
				for (int j = 0; j < numOfVertices; j++) {
					if (i < j) {
						if (gVertices.contains(j)) {

							if (CONNECTED.equals(rowArr[j].trim())) {
								/*
								 * the label of edge is decided by the label of
								 * the two endpoints
								 */
								String edge = getEdgeLabelBy2VerticesLabel(i, j);
								if (!g.containsEdge(edge)) {
									g.addEdge(edge, i, j);
								}

							}
						}
					}
				}
			}
		}

		return g;
	}

	@Deprecated
	public static Graph<Integer, Integer> prepareGraph(int numOfVertices, Graph<Integer, Integer> gRef,
			Graph<Integer, Integer> g, Collection<Integer> vList) {
		if (vList.isEmpty()) {
			return g;
		}

		for (Integer v : vList) {
			if (!g.containsVertex(v)) {
				g.addVertex(v);
			}
		}

		Collection<Integer> gVertices = g.getVertices();

		for (Integer v : vList) {
			Collection<Integer> vNeigInRef = gRef.getNeighbors(v);
			Collection<Integer> vNeigInRefToAdd = CollectionUtils.intersection(gVertices, vNeigInRef);

			for (Integer u : vNeigInRefToAdd) {

				int e = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, v, u);
				if (!g.containsEdge(e)) {
					g.addEdge(e, v, u);
				}

			}
		}

		return g;

	}

	public static Graph<Integer, String> prepareGraph(Graph<Integer, String> gRef, Graph<Integer, String> g,
			Collection<Integer> vList) {
		if (vList.isEmpty()) {
			return g;
		}

		for (Integer v : vList) {
			if (!g.containsVertex(v)) {
				g.addVertex(v);
			}
		}

		Collection<Integer> gVertices = g.getVertices();

		for (Integer v : vList) {
			Collection<Integer> vNeigInRef = gRef.getNeighbors(v);
			Collection<Integer> vNeigInRefToAdd = CollectionUtils.intersection(gVertices, vNeigInRef);

			for (Integer u : vNeigInRefToAdd) {

				String e = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(v, u);
				if (!g.containsEdge(e)) {
					g.addEdge(e, v, u);
				}

			}
		}

		return g;

	}

	/**
	 * add vertices in vList to g and add edges between such vertices to g as
	 * well
	 * 
	 * @param adjacencyMatrix
	 * @param g
	 * @param vList
	 * @return
	 */
	@Deprecated
	public static Graph<Integer, Integer> prepareGraph(List<String[]> adjacencyMatrix, Graph<Integer, Integer> g,
			Collection<Integer> vList) {
		if (vList.isEmpty()) {
			return g;
		}

		int numOfVertices = adjacencyMatrix.size();

		for (Integer i : vList) {
			if (!g.containsVertex(i)) {
				g.addVertex(i);
			}
		}

		Collection<Integer> gVertices = g.getVertices();
		for (int i = 0; i < numOfVertices; i++) {
			if (gVertices.contains(i)) {

				String[] rowArr = adjacencyMatrix.get(i);
				for (int j = 0; j < numOfVertices; j++) {
					if (i < j) {
						if (gVertices.contains(j)) {

							if (CONNECTED.equals(rowArr[j].trim())) {
								/*
								 * the label of edge is decided by the label of
								 * the two endpoints
								 */
								int edge = getEdgeLabelBy2VerticesLabel(numOfVertices, i, j);
								g.addEdge(edge, i, j);

							}
						}
					}
				}
			}
		}

		return g;
	}

	public static List<String[]> transferEdgePairToMatrix(List<String> lines) {
		String line0 = lines.get(0);
		String[] line0Array = line0.split(BLANK);
		String numOfVerStr = line0Array[0];
		int numOfVertices = Integer.parseInt(numOfVerStr);

		List<String[]> adjacencyMatrix = new ArrayList<String[]>(numOfVertices);
		for (int i = 0; i < numOfVertices; i++) {
			String[] row = new String[numOfVertices];
			Arrays.fill(row, UNCONNECTED);
			adjacencyMatrix.add(row);
		}

		int linesSize = lines.size();
		for (int i = 1; i < linesSize; i++) {
			String line = lines.get(i);
			String[] lineArray = line.split(BLANK);
			int v1 = Integer.parseInt(lineArray[0]) - 1;
			int v2 = Integer.parseInt(lineArray[1]) - 1;

			adjacencyMatrix.get(v1)[v2] = CONNECTED;
			adjacencyMatrix.get(v2)[v1] = CONNECTED;
		}
		return adjacencyMatrix;
	}

	/**
	 * get vertex list from vertexdegree list
	 * 
	 * @param vdList
	 * @return
	 */
	@Deprecated
	public static List<Integer> getVertexList(List<VertexDegree> vdList) {
		List<Integer> vList = new ArrayList<Integer>();
		for (VertexDegree vd : vdList) {
			addElementToList(vList, vd.getVertex());
		}
		return vList;
	}

	@Deprecated
	public static List<Integer> getVertexListFromMap(TreeMap<Integer, Integer> vdMap, int fromIndex, int toIndex) {
		Set<Integer> keySet = vdMap.keySet();

		List<Integer> keyList = new ArrayList<Integer>(keySet);

		return keyList.subList(fromIndex, toIndex);
	}

	/**
	 * get edge label by the 2 vertices incident to it
	 * 
	 * @param numOfVertices
	 *            , the number of all vertices in the graph
	 * @param v1
	 *            , vertex 1's label
	 * @param v2
	 *            , vertex 2's label
	 * @return
	 */
	@Deprecated
	public static int getEdgeLabelBy2VerticesLabel(int numOfVertices, int v1, int v2) {
		int min = Math.min(v1, v2);
		int max = Math.max(v1, v2);
		int edge = min * numOfVertices + max;
		return edge;
	}

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static String getEdgeLabelBy2VerticesLabel(int v1, int v2) {
		int min = Math.min(v1, v2);
		int max = Math.max(v1, v2);
		StringBuilder sb = new StringBuilder();
		sb.append(min).append(UNDERLINE).append(max);
		return sb.toString();
	}

	/**
	 * get a list of sorted vertices with their degrees from a graph
	 * 
	 * @param g
	 *            , an instance of Graph,
	 * @return List<VertexDegree>, a list of vertices with their degrees
	 */
	@Deprecated
	public static List<VertexDegree> sortVertexAccordingToDegree(Graph<Integer, Integer> g) {

		// get the sorted vertex according their degree
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();
		Collection<Integer> vertices = g.getVertices();
		for (int i : vertices) {
			int degree = g.degree(i);
			addElementToList(vertexDegreeList, new VertexDegree(i, degree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	/**
	 * get a list of sorted vertices with utility (the number of their
	 * un-dominated neighbors) from a graph
	 * 
	 * @param g
	 *            , an instance of Graph,
	 * @param dominatedMap
	 *            , a marked map showing vertices and if it's dominated
	 * @return List<VertexDegree>, a sorted list of vertices with the number of
	 *         their un-dominated neighbors
	 */
	@Deprecated
	public static List<VertexDegree> sortVertexAccordingToUtility(Graph<Integer, Integer> g,
			Map<Integer, Boolean> dominatedMap) {
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();
		Collection<Integer> vertices = g.getVertices();
		for (Integer v : vertices) {
			Collection<Integer> vNeigs = g.getNeighbors(v);
			int unDominatedDegree = 0;
			for (Integer u : vNeigs) {
				if (!dominatedMap.get(u)) {
					unDominatedDegree++;
				}
			}
			addElementToList(vertexDegreeList, new VertexDegree(v, unDominatedDegree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToUtilityASC(Graph<Integer, Integer> g,
			Map<Integer, Boolean> dominatedMap) {
		return sortVertexMapAccordingToUtilityInclude(g, dominatedMap, null, ASC_ORDER);
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToUtility(Graph<Integer, Integer> g,
			Map<Integer, Boolean> dominatedMap, boolean order) {
		return sortVertexMapAccordingToUtilityInclude(g, dominatedMap, null, order);
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToUtilityIncludeASC(Graph<Integer, Integer> g,
			Map<Integer, Boolean> dominatedMap, Collection<Integer> includeList) {
		return sortVertexMapAccordingToUtilityInclude(g, dominatedMap, includeList, ASC_ORDER);
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToUtilityInclude(Graph<Integer, Integer> g,
			Map<Integer, Boolean> dominatedMap, Collection<Integer> includeList, boolean order) {

		if (includeList == null) {
			includeList = g.getVertices();
		}

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (Integer v : includeList) {
			int utility = AlgorithmUtil.getVertexUtility(g, v, dominatedMap);
			map.put(v, utility);
		}

		TreeMap<Integer, Integer> sorted = null;

		if (order == DESC_ORDER) {
			sorted = new TreeMap<Integer, Integer>(new ValueComparatorDescOld(map));
		} else {
			sorted = new TreeMap<Integer, Integer>(new ValueComparatorAscOld(map));
		}

		sorted.putAll(map);
		return sorted;
	}

	/**
	 * get the highest utility neighbor of a vertex
	 * 
	 * @param v,
	 *            the vertex
	 * @param g,
	 *            the graph instance
	 * @param dominatedMap,
	 *            the dominated map (a map keeping pair of <vertex, dominated>)
	 * @return
	 */
	public static <V, E> V getHighestUtilityNeighborOfAVertex(V v, Graph<V, E> g, Map<V, Boolean> dominatedMap) {

		List<V> vList = OrderPackageUtil.getVertexListUtilityDesc(g, dominatedMap);

		Collection<V> vNeg = g.getNeighbors(v);
		vNeg.add(v);

		for (V u : vList) {
			if (vNeg.contains(u)) {
				return u;
			}
		}

		return null;

	}

	/**
	 * get the highest vote neighbor of a vertex
	 * 
	 * @param v,
	 *            the vertex
	 * @param g,
	 *            the graph instance
	 * @param dominatedMap,
	 *            the dominated map (a map keeping pair of <vertex, dominated>)
	 * @param weightMap,
	 * @param voteMap
	 * @return
	 */
	public static <V, E> V getHighestVoteNeighborOfAVertex(V v, Graph<V, E> g, 
			Map<V, Float> weightMap) {
		//
		// List<V> vList = OrderPackageUtil.getVertexListVoteDesc(g,
		// dominatedMap, weightMap, voteMap);
		//
		// Collection<V> vNeg = g.getNeighbors(v);
		// vNeg.add(v);
		//
		// for (V u : vList) {
		// if (vNeg.contains(u)) {
		// return u;
		// }
		// }
		//
		// return null;

		Collection<V> vNeg = g.getNeighbors(v);
		vNeg.add(v);
		weightMap=sortByValue(weightMap);
		Set<V> keySet=weightMap.keySet();
		for(V u:keySet){
			if(vNeg.contains(u)){
				return u;
			}
		}
		return null;

	}

	private static <K, L extends Comparable<? super L>>

	Map<K, L> sortByValue(Map<K, L> map) {
		List<Map.Entry<K, L>> list = new LinkedList<Map.Entry<K, L>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, L>>() {
			public int compare(Map.Entry<K, L> o1, Map.Entry<K, L> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, L> result = new LinkedHashMap<K, L>();
		for (Map.Entry<K, L> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToDegree(Graph<Integer, Integer> g) {
		return sortVertexMapAccordingToDegreeInclude(g, null, ASC_ORDER);
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToDegree(Graph<Integer, Integer> g, boolean order) {
		return sortVertexMapAccordingToDegreeInclude(g, null, order);
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToDegreeInclude(Graph<Integer, Integer> g,
			Collection<Integer> includeList) {

		return sortVertexMapAccordingToDegreeInclude(g, includeList, ASC_ORDER);
	}

	@Deprecated
	public static TreeMap<Integer, Integer> sortVertexMapAccordingToDegreeInclude(Graph<Integer, Integer> g,
			Collection<Integer> includeList, boolean order) {

		if (includeList == null) {
			includeList = g.getVertices();
		}

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (Integer v : includeList) {
			int utility = AlgorithmUtil.getVertexDegree(g, v);
			map.put(v, utility);
		}
		TreeMap<Integer, Integer> sorted = null;

		if (order == DESC_ORDER) {
			sorted = new TreeMap<Integer, Integer>(new ValueComparatorDescOld(map));
		} else {
			sorted = new TreeMap<Integer, Integer>(new ValueComparatorAscOld(map));
		}
		sorted.putAll(map);
		return sorted;
	}

	/**
	 * sort a list of vertex according to their degree
	 * 
	 * @param g
	 * @param vList
	 * @return
	 */
	@Deprecated
	public static List<VertexDegree> sortVertexAccordingToDegreeInclude(Graph<Integer, Integer> g,
			Collection<Integer> vList) {

		// get the sorted vertex according their degree
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();

		for (Integer i : vList) {
			int degree = g.degree(i);
			addElementToList(vertexDegreeList, new VertexDegree(i, degree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	/**
	 * sort a list of vertex according to their utility (the number of their
	 * un-dominated neighbors) from a graph
	 * 
	 * @param g,
	 *            an instance of Graph,
	 * @param vList,
	 *            unsorted vertex list
	 * @param dominatedMap
	 *            , a marked map showing vertices and if it's dominated
	 * @return List<VertexDegree>, a sorted list of vertices with the number of
	 *         their un-dominated neighbors
	 */
	@Deprecated
	public static List<VertexDegree> sortVertexAccordingToUtilityInclude(Graph<Integer, Integer> g,
			Collection<Integer> vList, Map<Integer, Boolean> dominatedMap) {
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();

		for (Integer v : vList) {
			Collection<Integer> vNeigs = g.getNeighbors(v);
			int unDominatedDegree = 0;
			for (Integer u : vNeigs) {
				if (!dominatedMap.get(u)) {
					unDominatedDegree++;
				}
			}
			addElementToList(vertexDegreeList, new VertexDegree(v, unDominatedDegree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	/**
	 * 
	 * @param g
	 * @param v
	 * @param dominatedMap
	 * @return
	 */
	public static <V, E> int getVertexUtility(Graph<V, E> g, V v, Map<V, Boolean> dominatedMap) {
		Collection<V> vNeigs = g.getNeighbors(v);
		int unDominatedDegree = 0;
		for (V u : vNeigs) {
			if (!dominatedMap.get(u)) {
				unDominatedDegree++;
			}
		}
		return unDominatedDegree;
	}

	/**
	 * 
	 * @param g
	 * @param v
	 * @return
	 */
	public static <V, E> int getVertexDegree(Graph<V, E> g, V v) {

		int unDominatedDegree = g.degree(v);
		return unDominatedDegree;
	}

	// public static Integer
	// getVertexFromClosedNeighborhoodWithHighestUtility(Integer v,
	// Graph<Integer, Integer> g,
	// List<VertexDegree> vdList, Map<Integer, Boolean> dominatedMap) {
	// Collection<Integer> vNeigs = g.getNeighbors(v);
	// vNeigs.add(v);
	//
	// for (VertexDegree vd : vdList) {
	// Integer u = vd.getVertex();
	// if (vNeigs.contains(u) && !dominatedMap.get(u)) {
	// return u;
	// }
	// }
	//
	// return null;
	//
	// }

	/**
	 * get a list of sorted vertices with their degrees from a graph except the
	 * vertices in excludeList
	 * 
	 * @param g
	 * @param excludeList
	 * @return
	 */
	@Deprecated
	public static List<VertexDegree> sortVertexAccordingToDegreeExclude(Graph<Integer, Integer> g,
			Collection<Integer> excludeList) {

		// get the sorted vertex according their degree
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();
		Collection<Integer> vertices = g.getVertices();
		vertices = CollectionUtils.subtract(vertices, excludeList);
		for (Integer i : vertices) {
			int degree = g.degree(i);
			addElementToList(vertexDegreeList, new VertexDegree(i, degree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	/**
	 * get sorted vertices in vl according to their degree in sorted vdl
	 * 
	 * @param vl
	 * @param vdl
	 * @return
	 */
	@Deprecated
	public static List<Integer> sortVertexAccordingToSortedVertexList(List<Integer> vl, List<VertexDegree> vdl) {
		int vlSize = vl.size();
		List<Integer> rtn = new ArrayList<Integer>(vlSize);
		for (VertexDegree vd : vdl) {
			int v = vd.getVertex();
			if (vl.contains(v)) {
				// log.debug(v + "-" + vd.getDegree());
				// rtn.add(v);
				addElementToList(rtn, v);
			}
		}

		return rtn;
	}

	/**
	 * decide if the vertices in dominatedMap are all marked as dominated.
	 * 
	 * @param dominatedMap
	 * @return true, all vertices are dominated;false,no
	 */
	public static boolean isAllDominated(Map<Integer, Boolean> dominatedMap) {

		Collection<Boolean> values = dominatedMap.values();
		for (Boolean b : values) {
			if (!b) {
				return false;
			}
		}

		return true;
	}

	// public static int getDominatedNumber(Map<Integer, Boolean> dominatedMap)
	// {
	// int count = 0;
	// Collection<Boolean> values = dominatedMap.values();
	// for (Boolean b : values) {
	// if (b) {
	// count++;
	// }
	// }
	// return count;
	// }

	/**
	 * if the vertices in the list are all marked as dominated.
	 * 
	 * @param dominatedMap
	 * @param vList
	 * @return
	 */
	public static boolean isAllDominated(Map<Integer, Boolean> dominatedMap, Collection<Integer> vList) {

		for (Integer v : vList) {
			if (!dominatedMap.get(v)) {
				return false;
			}
		}
		return true;

	}

	/**
	 * construct a complete graph with n vertices
	 * 
	 * @param n
	 *            , the number of vertices
	 * @return a complete graph with n vertices
	 */
	@Deprecated
	public static List<String[]> generateCompleteGraph(int n) {
		List<String[]> am = new ArrayList<String[]>(n);
		// construct a complete graph
		for (int i = 0; i < n; i++) {
			String[] row = new String[n];
			for (int j = 0; j < n; j++) {
				row[j] = CONNECTED;
			}
			// am.add(row);
			addElementToList(am, row);
		}
		return am;
	}

	// /**
	// * construct a complete graph from a graph
	// *
	// * @param g
	// * @param numOfVertices
	// * @return
	// */
	// public static Graph<Integer, Integer>
	// constructCompleteGraph(Graph<Integer, Integer> g, int numOfVertices) {
	// Graph<Integer, Integer> gK = new SparseMultigraph<Integer, Integer>();
	//
	// Collection<Integer> vertices = g.getVertices();
	// for (Integer v : vertices) {
	// gK.addVertex(v);
	// }
	//
	// for (Integer i : vertices) {
	// for (Integer j : vertices) {
	// if (i < j) {
	// int edge = getEdgeLabelBy2VerticesLabel(numOfVertices, i, j);
	// gK.addEdge(edge, i, j);
	// }
	// }
	// }
	//
	// return gK;
	// }

	/**
	 * generate a random graph
	 * 
	 * @param numOfVertices
	 *            , the number of vertices in the graph
	 * @param adjacentRatio
	 *            ,a real number between 0 and 1. the purpose of this ratio is
	 *            to reduce the number of neighbors of a vertex in order to
	 *            increase the number of elements in ds. Otherwise, a vertex may
	 *            link to all other vertices and become the only element in the
	 *            ds.
	 * @return an adjancy matrix of the random graph
	 */
	public static List<String[]> generateRandGraph(int numOfVertices, float adjacentRatio) {
		List<String[]> adjacencyMatrix = null;

		adjacencyMatrix = new ArrayList<String[]>(numOfVertices);
		for (int i = 0; i < numOfVertices; i++) {
			String[] row = new String[numOfVertices];
			// initialize every cell in the row to be UNCONNECTED,
			Arrays.fill(row, UNCONNECTED);
			// generate a random number of the vertices linking to the current
			// row no.
			int adjacentNum = (int) Math.round(Math.random() * numOfVertices);
			// make use of the ratio to reduce density
			adjacentNum = Math.round(adjacentRatio * adjacentNum);
			for (int j = 0; j < adjacentNum; j++) {
				int position;

				position = (int) Math.floor(Math.random() * numOfVertices);

				if (CONNECTED.equals(row[position])) {
					j--;
				} else {
					row[position] = CONNECTED;
				}
			}

			row[i] = CONNECTED;
			// adjacencyMatrix.add(row);
			addElementToList(adjacencyMatrix, row);
		}

		return adjacencyMatrix;

	}

	// /**
	// * Do the edge deletion to get henning distance
	// *
	// * @param am1
	// * , the adjacent matrix of graph g1
	// * @param g1
	// * , the graph object of g1
	// * @param ds1
	// * , a dominating set of g1
	// * @param k
	// * , the maximum number of edge deletion
	// * @return an instance of HEdit containing the output adjacent matrix and
	// * operation list
	// */
	// public static HEdit hEditEdgeDeletion(List<String[]> am1, Graph<Integer,
	// Integer> g1, List<Integer> ds1, int k) {
	// List<String[]> operationList = new ArrayList<String[]>();
	//
	// // generate a copy of adjacency matrix 1
	// List<String[]> am2 = new ArrayList<String[]>(am1);
	//
	// Collection<Integer> vertices1 = g1.getVertices();
	// // get the complementary set of dominating set 1 in graph 1
	// List<Integer> complementOfDS1 = (List<Integer>)
	// CollectionUtils.subtract(vertices1, ds1);
	//
	// List<Integer> randomKVerInComplementOfDS1Set = getKRandomVerticesInSet(k,
	// complementOfDS1);
	//
	// for (Integer cDsv : randomKVerInComplementOfDS1Set) {
	// Collection<Integer> neighboursOfCDsv = g1.getNeighbors(cDsv);
	//
	// for (Integer nCdsv : neighboursOfCDsv) {
	// if (ds1.contains(nCdsv)) {
	// am2.get(cDsv)[nCdsv] = UNCONNECTED;
	// am2.get(nCdsv)[cDsv] = UNCONNECTED;
	//
	// // change operation list
	// String[] operation = { UNCONNECTED, Integer.toString(cDsv),
	// Integer.toString(nCdsv) };
	//
	// addElementToList(operationList, operation);
	// }
	// }
	//
	// }
	//
	// HEdit hEdit = new HEdit();
	// hEdit.setOperationList(operationList);
	// hEdit.setOutputAdjacencyMatrix(am2);
	//
	// return hEdit;
	//
	// }

	// /**
	// * get at most k number of random numbers which are between n1 and n2; in
	// * another words, the number of random numbers may be less than k
	// *
	// * @param k
	// * , the number of random vertices
	// * @param n1
	// * , the bottom bound of the random numbers
	// * @param n2
	// * , the up bound of the random numbers
	// * @return k number of random numbers which are between n1 and n2;
	// */
	// public static List<Integer> getKRandomVerticesInSet(int k,
	// Collection<Integer> s) {
	// int sSize = s.size();
	// Integer[] sArray = new Integer[sSize];
	//
	// sArray = s.toArray(sArray);
	//
	// List<Integer> rtn = new ArrayList<Integer>();
	// if (sSize <= 0) {
	// return rtn;
	// }
	// if (sSize < k) {
	// k = sSize;
	// }
	//
	// for (int i = 0; i < k; i++) {
	// int ran = (int) (Math.random() * (sSize - 1));
	// Integer sRan = sArray[ran];
	// if (rtn.contains(sRan)) {
	// i--;
	// } else {
	// addElementToList(rtn, sRan);
	// }
	// }
	//
	// return rtn;
	// }

	/**
	 * get a random float number in a range
	 * 
	 * @param min
	 *            , the bottom bound of the random number
	 * @param max
	 *            ,the up bound of the random number
	 * @return,a random float number in a range
	 */
	public static float randomInRang(float min, float max) {
		Random random = new Random();
		float s = random.nextFloat() * (max - min) + min;
		return s;
	}

	/**
	 * avoid adding duplicated elements into a list
	 * 
	 * @param list
	 *            , the list receiving elements
	 * @param e
	 *            , an element
	 * @return the list
	 */
	public static <E> Collection<E> addElementToList(Collection<E> list, E e) {
		if (!list.contains(e)) {
			list.add(e);
		}
		return list;
	}

	/**
	 * do the or operation of each corresponding elements of 2 byte arrays. the
	 * 2 byte arrays are of the same length
	 * 
	 * @param ruler1
	 * @param ruler2
	 * @return
	 */
	public static byte[] arrayOr(byte[] ruler1, byte[] ruler2) throws ArraysNotSameLengthException {
		int ruler1Len = ruler1.length;
		int ruler2Len = ruler2.length;
		if (ruler1Len != ruler2Len) {
			throw new ArraysNotSameLengthException("The two byte arrays are not of the same length.");
		}

		for (int i = 0; i < ruler1Len; i++) {

			ruler1[i] = (byte) (ruler1[i] | ruler2[i]);
		}

		return ruler1;
	}

	// /**
	// * get neighbours of vertices in a set
	// *
	// * @param g
	// * , a graph
	// * @param S
	// * , a set of vertices
	// * @return
	// */
	// public static List<Integer> getNeighborsOfS(Graph<Integer, Integer> g,
	// List<Integer> S) {
	// List<Integer> ngs = new ArrayList<Integer>();
	// for (Integer s : S) {
	// Collection<Integer> col = g.getNeighbors(s);
	// if (col != null) {
	// ngs = (List<Integer>) CollectionUtils.union(ngs, col);
	// }
	// }
	// return ngs;
	// }

	/**
	 * get neighbours of vertices in a set
	 * 
	 * @param g
	 *            , a graph
	 * @param S
	 *            , a set of vertices
	 * @return
	 */
	public static <V, E> List<V> getNeighborsOfS(Graph<V, E> g, Collection<V> S) {
		Collection<V> ngs = new ArrayList<V>();
		for (V s : S) {
			Collection<V> col = g.getNeighbors(s);
			if (col != null) {
				ngs = CollectionUtils.union(ngs, col);
			}
		}
		return (List<V>) ngs;
	}

	/**
	 * decide if it is a dominating set solution of a graph
	 * 
	 * @param g
	 *            , graph
	 * @param ds
	 *            , a potential dominating set
	 * @return true, is dominating set ; no, not
	 */
	public static <V, E> boolean isDS(Graph<V, E> g, List<V> ds) {
		Collection<V> vertices = g.getVertices();
		Collection<V> complementaryDS = CollectionUtils.subtract(vertices, ds);

		for (V v : ds) {
			// get neighbours of the vertices in dominating set
			Collection<V> neighborsOfV = g.getNeighbors(v);
			// remove the neighbours from the complementary set
			if (neighborsOfV != null) {
				complementaryDS = CollectionUtils.subtract(complementaryDS, neighborsOfV);
			}
		}
		/*
		 * if the complementary set is not empty, it means there are some
		 * vertices are not dominated and in turn the input set is not a
		 * dominating set for the graph
		 */
		if (!complementaryDS.isEmpty()) {
			return false;
		}
		return true;

	}

	// /**
	// * get a copy of source graph
	// *
	// * @param src
	// * , source graph
	// * @return a copy of source graph
	// */
	// @Deprecated
	// public static Graph<Integer, Integer> copyGraph(Graph<Integer, Integer>
	// src) {
	// Graph<Integer, Integer> dest = new SparseMultigraph<Integer, Integer>();
	// for (Integer v : src.getVertices())
	// dest.addVertex(v);
	//
	// for (Integer e : src.getEdges())
	// dest.addEdge(e, src.getIncidentVertices(e));
	//
	// return dest;
	// }

	/**
	 * get a copy of source graph
	 * 
	 * @param src
	 *            , source graph
	 * @return a copy of source graph
	 */
	public static <V, E> Graph<V, E> copyGraph(Graph<V, E> src) {
		Graph<V, E> dest = new SparseMultigraph<V, E>();
		for (V v : src.getVertices())
			dest.addVertex(v);

		for (E e : src.getEdges())
			dest.addEdge(e, src.getIncidentVertices(e));

		return dest;
	}

	// /**
	// * get the number of edge difference of two adjacent matrixes, because the
	// * adjacent matrix is symmetrical, it only count the half
	// *
	// * @param am1
	// * , adjacent matrix 1
	// * @param am2
	// * , adjacent matrix 2
	// * @return the number of edge difference of two adjacent matrixes
	// */
	// public static int getDifferentEdgeNumber(List<String[]> am1,
	// List<String[]> am2)
	// throws ArraysNotSameLengthException {
	//
	// int n = am1.size();
	// int count = 0;
	// for (int i = 0; i < n; i++) {
	//
	// String[] am1row = am1.get(i);
	// String[] am2row = am2.get(i);
	// count += arrayXorDifference(am1row, am2row);
	// }
	//
	// return count / 2;
	// }

	// /**
	// * do the Exclusive-OR operation of each corresponding elements of 2
	// string
	// * arrays and then get the number of differences. the 2 string arrays are
	// of
	// * the same length
	// *
	// * @param a1
	// * , string array 1
	// * @param a2
	// * , string array 2
	// * @return the number of differences
	// */
	// private static int arrayXorDifference(String[] a1, String[] a2) throws
	// ArraysNotSameLengthException {
	// int a1Len = a1.length;
	// int a2Len = a2.length;
	// if (a1Len != a2Len) {
	// throw new ArraysNotSameLengthException("The two byte arrays are not of
	// the same length.");
	// }
	//
	// int count = 0;
	//
	// for (int i = 0; i < a1Len; i++) {
	// byte a1i = Byte.parseByte(a1[i]);
	// byte a2i = Byte.parseByte(a2[i]);
	// if ((a1i ^ a2i) == 1) {
	// count++;
	// }
	// }
	//
	// return count;
	//
	// }

	// /**
	// * convert a binary byte array to a long integer
	// *
	// * @param binary
	// * , a binary byte array @return, a long integer corresponding to
	// * the binary byte array
	// * @throws ExceedLongMaxException
	// */
	// public static long arrayToLong(byte[] binary) throws
	// ExceedLongMaxException {
	// // we assume that the size of binary does not exceed 63;
	// int binarySize = binary.length;
	// if (binarySize >= (long) (Math.log(Long.MAX_VALUE) / Math.log(2))) {
	// throw new ExceedLongMaxException("Excceed the max value allowed for Long
	// integer.");
	// }
	//
	// long sum = 0;
	// for (int i = binarySize - 1; i >= 0; i--) {
	// sum += (long) (Math.pow(2, binarySize - 1 - i) * binary[i]);
	// }
	//
	// return sum;
	//
	// }
	/**
	 * 
	 * @param binary
	 * @return
	 */
	public static String arrayToString(byte[] binary) {
		int binarySize = binary.length;
		char[] chArray = new char[binarySize];
		for (int i = 0; i < binarySize; i++) {
			chArray[i] = (char) (binary[i] + ASCII_0_SEQ_NO);
		}
		String rtn = new String(chArray);
		return rtn;
	}

	/**
	 * 
	 * @param binaryStr
	 * @return
	 */
	public static byte[] stringToBinaryArray(String binaryStr) {
		byte[] binaryArray = binaryStr.getBytes();
		int binaryArraySize = binaryArray.length;
		for (int i = 0; i < binaryArraySize; i++) {
			binaryArray[i] -= ASCII_0_SEQ_NO;
		}
		return binaryArray;
	}

	// public static List<Integer> stringToIntList(String binaryStr) {
	// List<Integer> intList = new ArrayList<Integer>();
	//
	// byte[] binaryArray = binaryStr.getBytes();
	// int binaryArraySize = binaryArray.length;
	// for (int i = 0; i < binaryArraySize; i++) {
	// binaryArray[i] -= ASCII_0_SEQ_NO;
	// if (binaryArray[i] == AlgorithmUtil.MARKED) {
	// addElementToList(intList, i);
	// }
	// }
	//
	// return intList;
	// }

	// /**
	// *
	// * @param size
	// * @param list
	// * @return
	// */
	// public static String intListToString(int size, List<Integer> list) {
	//
	// char[] chArray = new char[size];
	// for (int i = 0; i < size; i++) {
	// chArray[i] = (char) (ASCII_0_SEQ_NO);
	// }
	//
	// for (Integer i : list) {
	// chArray[i] = (char) (ASCII_0_SEQ_NO + 1);
	// }
	//
	// String rtn = new String(chArray);
	// return rtn;
	//
	// }

	// /**
	// * convert a long integer to a binary byte array
	// *
	// * @param size
	// * , the target binary byte array length
	// * @param binaryLong
	// * , the long integer
	// * @return a binary byte array corresponding to the long integer
	// */
	// @Deprecated
	// public static byte[] longToBinaryArray(int size, Long binaryLong) {
	// String binaryStr = StringUtils.leftPad(Long.toBinaryString(binaryLong),
	// size, BINARY_LEFT_PAD);
	// return stringToBinaryArray(binaryStr);
	// }
	/**
	 * 
	 * @param ds
	 * @param n
	 * @param m
	 * @param g
	 * @return
	 * @throws ArraysNotSameLengthException
	 */
	public static <V, E> boolean[] verifySubDS(List<V> ds, int n, int m, Graph<V, E> g)
			throws ArraysNotSameLengthException {
		if (m > n) {
			m = n;
		}

		boolean isSolution = false;
		boolean isEnd = false;

		boolean[] chosen = new boolean[n];
		Arrays.fill(chosen, UNCHOSEN);

		Arrays.fill(chosen, 0, m, CHOSEN);

		// int count = 0;
		// count++;
		isSolution = verifyChosen(ds, chosen, m, n, g);

		if (isSolution) {
			return chosen;
		}

		do {
			int pose = 0;
			int sum = 0;
			for (int i = 0; i < (n - 1); i++) {
				if (chosen[i] == CHOSEN && chosen[i + 1] == UNCHOSEN) {
					chosen[i] = UNCHOSEN;
					chosen[i + 1] = CHOSEN;
					pose = i;
					break;
				}
			}
			// count++;

			isSolution = verifyChosen(ds, chosen, m, n, g);

			if (isSolution) {
				return chosen;
			}

			for (int i = 0; i < pose; i++) {
				if (chosen[i] == CHOSEN) {
					sum++;
				}
			}

			boolean[] copyOfChosen = Arrays.copyOf(chosen, chosen.length);

			Arrays.fill(chosen, 0, sum, CHOSEN);
			Arrays.fill(chosen, sum, pose, UNCHOSEN);

			if (!Arrays.equals(copyOfChosen, chosen)) {
				// count++;
				isSolution = verifyChosen(ds, chosen, m, n, g);

				if (isSolution) {
					return chosen;
				}
			}

			isEnd = true;
			for (int i = n - m; i < n; i++) {

				if (chosen[i] == UNCHOSEN) {
					isEnd = false;
					break;
				}

			}

		} while (!isEnd);
		if (!isSolution) {
			return null;
		} else {
			return chosen;
		}

	}

	/**
	 * 
	 * @param ds
	 * @param chosen
	 * @param m
	 * @param n
	 * @param g
	 * @return
	 * @throws ArraysNotSameLengthException
	 */
	private static <V, E> boolean verifyChosen(List<V> ds, boolean[] chosen, int m, int n, Graph<V, E> g)
			throws ArraysNotSameLengthException {
		List<V> tempDs = new ArrayList<V>(m);

		for (int i = 0; i < n; i++) {
			if (chosen[i]) {
				tempDs.add(ds.get(i));
			}
		}

		return isDS(g, tempDs);

	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static <T> List<T> getFirstItemInCollection(Collection<T> s) {
		// List<T> rtn = new ArrayList<T>();
		//
		// for (T t : s) {
		// rtn.add(t);
		// break;
		// }
		//
		// return rtn;
		return getFirstNItemsInCollection(1, s);
	}

	/**
	 * 
	 * @param n
	 * @param s
	 * @return
	 */
	public static <T> List<T> getFirstNItemsInCollection(int n, Collection<T> s) {
		List<T> rtn = new ArrayList<T>();
		int count = 0;
		for (T t : s) {
			rtn.add(t);
			count++;
			if (count == n) {
				break;
			}
		}

		return rtn;
	}

	/**
	 * Apply DS reduction rule 1 in michael's paper
	 * 
	 * @param numOfVertices,
	 *            number of vertices in the original graph,it is used for
	 *            computing edge label
	 * @param g,
	 *            the original graph
	 * @return reduced graph
	 */
	@Deprecated
	public static Graph<Integer, Integer> applySingleVertexReductionRule(int numOfVertices, Graph<Integer, Integer> g) {
		Graph<Integer, Integer> gPrime = AlgorithmUtil.copyGraph(g);

		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();

		Collection<Integer> vertices = g.getVertices();

		for (Integer v : vertices) {
			visited.put(v, false);
		}

		for (Integer v : vertices) {

			if (!visited.get(v)) {
				// N1(v) := {u ∈ N(v) | N(u) \ N[v] = ∅},
				Collection<Integer> vNeig = gPrime.getNeighbors(v);
				List<Integer> n1 = new ArrayList<Integer>();
				for (Integer u : vNeig) {
					Collection<Integer> uNeig = gPrime.getNeighbors(u);
					Collection<Integer> n1diff = CollectionUtils.subtract(uNeig, vNeig);
					n1diff.remove(v);
					if (!n1diff.isEmpty()) {
						n1.add(u);
					}

				}
				// N2(v) := {u ∈ N(v) \ N1(v) | N(u) ∩ N1(v) = ∅},
				List<Integer> n2 = new ArrayList<Integer>();
				Collection<Integer> n2base = CollectionUtils.subtract(vNeig, n1);
				for (Integer u : n2base) {
					Collection<Integer> uNeig = gPrime.getNeighbors(u);
					Collection<Integer> uIntsec = CollectionUtils.intersection(uNeig, n1);
					if (!uIntsec.isEmpty()) {
						n2.add(u);
					}
				}
				// N3(v) := N(v) \ (N1(v) ∪ N2(v)).
				Collection<Integer> n3 = CollectionUtils.subtract(vNeig, CollectionUtils.union(n1, n2));

				/*
				 * Rule 1 If N3(v) = ∅ for some vertex v, then : i) remove
				 * N2(v) and N3(v) from G and; ii) add a new vertex v with the
				 * edge {v, v}.
				 * 
				 * An equivlant way of ii) is ii.1) to keep a vertex w in N2(v)
				 * ∪ N3(v)), ii.2) remove edges between w and N(w)\{v}
				 * 
				 */
				if (!n3.isEmpty()) {
					List<Integer> vInList = new ArrayList<Integer>();
					vInList.add(v);

					Collection<Integer> n2n3 = CollectionUtils.union(n2, n3);
					List<Integer> wPrime = AlgorithmUtil.getFirstItemInCollection(n2n3);
					Collection<Integer> n2n3Except = CollectionUtils.subtract(n2n3, wPrime);

					for (Integer w : n2n3Except) {
						gPrime.removeVertex(w);
						visited.put(w, true);
					}

					for (Integer w : wPrime) {
						Collection<Integer> wNeig = gPrime.getNeighbors(w);

						wNeig = CollectionUtils.subtract(wNeig, vInList);
						for (Integer x : wNeig) {
							int wx = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, x, w);
							gPrime.removeEdge(wx);
						}

						visited.put(w, true);
					}

				}

				visited.put(v, true);
			}

		}

		return gPrime;
	}

	/**
	 * Apply DS reduction rule 1 in michael's paper
	 * 
	 * 
	 * @param g,
	 *            the original graph
	 * @return reduced graph
	 */

	public static Graph<Integer, String> applySingleVertexReductionRule(Graph<Integer, String> g) {
		Graph<Integer, String> gPrime = AlgorithmUtil.copyGraph(g);

		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();

		Collection<Integer> vertices = g.getVertices();

		for (Integer v : vertices) {
			visited.put(v, false);
		}

		for (Integer v : vertices) {

			if (!visited.get(v)) {
				// N1(v) := {u ∈ N(v) | N(u) \ N[v] = ∅},
				Collection<Integer> vNeig = gPrime.getNeighbors(v);
				List<Integer> n1 = new ArrayList<Integer>();
				for (Integer u : vNeig) {
					Collection<Integer> uNeig = gPrime.getNeighbors(u);
					Collection<Integer> n1diff = CollectionUtils.subtract(uNeig, vNeig);
					n1diff.remove(v);
					if (!n1diff.isEmpty()) {
						n1.add(u);
					}

				}
				// N2(v) := {u ∈ N(v) \ N1(v) | N(u) ∩ N1(v) = ∅},
				List<Integer> n2 = new ArrayList<Integer>();
				Collection<Integer> n2base = CollectionUtils.subtract(vNeig, n1);
				for (Integer u : n2base) {
					Collection<Integer> uNeig = gPrime.getNeighbors(u);
					Collection<Integer> uIntsec = CollectionUtils.intersection(uNeig, n1);
					if (!uIntsec.isEmpty()) {
						n2.add(u);
					}
				}
				// N3(v) := N(v) \ (N1(v) ∪ N2(v)).
				Collection<Integer> n3 = CollectionUtils.subtract(vNeig, CollectionUtils.union(n1, n2));

				/*
				 * Rule 1 If N3(v) != ∅ for some vertex v, then : i) remove
				 * N2(v) and N3(v) from G and; ii) add a new vertex v' with the
				 * edge {v, v'}.
				 * 
				 * An equivlant way of ii) is ii.1) to keep a vertex w in N2(v)
				 * ∪ N3(v)), ii.2) remove edges between w and N(w)\{v}
				 * 
				 */
				if (!n3.isEmpty()) {
					List<Integer> vInList = new ArrayList<Integer>();
					vInList.add(v);

					Collection<Integer> n2n3 = CollectionUtils.union(n2, n3);
					List<Integer> wPrime = AlgorithmUtil.getFirstItemInCollection(n2n3);
					Collection<Integer> n2n3Except = CollectionUtils.subtract(n2n3, wPrime);

					for (Integer w : n2n3Except) {
						gPrime.removeVertex(w);
						visited.put(w, true);
					}

					for (Integer w : wPrime) {
						Collection<Integer> wNeig = gPrime.getNeighbors(w);

						wNeig = CollectionUtils.subtract(wNeig, vInList);
						for (Integer x : wNeig) {
							String wx = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(x, w);
							gPrime.removeEdge(wx);
						}

						visited.put(w, true);
					}

				}

				visited.put(v, true);
			}

		}

		return gPrime;
	}

	/**
	 * Apply DS reduction rule 2 in michael's paper
	 * 
	 * @param numOfVertices,
	 *            number of vertices in the original graph,it is used for
	 *            computing edge label
	 * @param g,the
	 *            original graph
	 * @return reduced graph
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Graph<Integer, Integer> applyPairVerticesReductionRule(int numOfVertices, Graph<Integer, Integer> g) {
		Graph<Integer, Integer> gPrime = AlgorithmUtil.copyGraph(g);

		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();

		Collection<Integer> vertices = g.getVertices();

		for (Integer v : vertices) {
			visited.put(v, false);
		}

		for (Integer v : vertices) {

			if (!visited.get(v)) {

				for (Integer w : vertices) {
					if (!visited.get(w)) {
						Collection<Integer> vNeig = gPrime.getNeighbors(v);

						if (v.equals(w)) {
							continue;
						}

						if (vNeig.contains(w)) {
							continue;
						}

						Collection<Integer> wNeig = gPrime.getNeighbors(w);

						Collection<Integer> vwNeig = CollectionUtils.union(vNeig, wNeig);

						List<Integer> n1 = new ArrayList<Integer>();
						for (Integer u : vwNeig) {
							Collection<Integer> uNeig = gPrime.getNeighbors(u);
							if (uNeig == null) {
								uNeig = CollectionUtils.EMPTY_COLLECTION;
							}
							Collection<Integer> n1diff = CollectionUtils.subtract(uNeig, vwNeig);
							n1diff.remove(v);
							n1diff.remove(w);
							if (!n1diff.isEmpty()) {
								n1.add(u);
							}
						}

						List<Integer> n2 = new ArrayList<Integer>();
						Collection<Integer> n2base = CollectionUtils.subtract(vwNeig, n1);
						for (Integer u : n2base) {
							Collection<Integer> uNeig = gPrime.getNeighbors(u);
							if (uNeig == null) {
								uNeig = CollectionUtils.EMPTY_COLLECTION;
							}
							Collection<Integer> uIntsec = CollectionUtils.intersection(uNeig, n1);
							if (!uIntsec.isEmpty()) {
								n2.add(u);
							}
						}

						Collection<Integer> n3 = CollectionUtils.subtract(vwNeig, CollectionUtils.union(n1, n2));

						if (!n3.isEmpty()) {
							List<Integer> vInList = new ArrayList<Integer>();
							vInList.add(v);

							List<Integer> wInList = new ArrayList<Integer>();
							wInList.add(w);

							List<Integer> vwInList = new ArrayList<Integer>();
							vwInList.add(v);
							vwInList.add(w);

							if (isAVertexDominateASet(v, n3, gPrime) || isAVertexDominateASet(w, n3, gPrime)) {
								// n3 can be dominated by a single vertex from
								// {v,w}
								if (CollectionUtils.isSubCollection(n3, vNeig)
										&& CollectionUtils.isSubCollection(n3, wNeig)) {
									Collection<Integer> insec = CollectionUtils.intersection(n2, vNeig);
									insec = CollectionUtils.intersection(insec, wNeig);
									insec = CollectionUtils.union(n3, insec);
									List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(2, insec);

									Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

									for (Integer x : insecExcept) {
										gPrime.removeVertex(x);
										visited.put(x, true);
									}

									for (Integer z : zList) {
										Collection<Integer> zNeig = gPrime.getNeighbors(z);

										zNeig = CollectionUtils.subtract(zNeig, vwInList);
										for (Integer x : zNeig) {
											int xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, x, z);
											gPrime.removeEdge(xz);
										}

										visited.put(z, true);
									}

								} else if (CollectionUtils.isSubCollection(n3, vNeig)
										&& !CollectionUtils.isSubCollection(n3, wNeig)) {
									Collection<Integer> insec = CollectionUtils.intersection(n2, vNeig);
									insec = CollectionUtils.union(n3, insec);

									List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(1, insec);

									Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

									for (Integer x : insecExcept) {
										gPrime.removeVertex(x);
										visited.put(x, true);
									}

									for (Integer z : zList) {

										// leave vz,remove xz
										Collection<Integer> zNeig = gPrime.getNeighbors(z);

										zNeig = CollectionUtils.subtract(zNeig, vInList);
										for (Integer x : zNeig) {
											int xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, x, z);
											gPrime.removeEdge(xz);
										}

										visited.put(z, true);
									}

								} else if (CollectionUtils.isSubCollection(n3, wNeig)
										&& !CollectionUtils.isSubCollection(n3, vNeig)) {
									Collection<Integer> insec = CollectionUtils.intersection(n2, wNeig);
									insec = CollectionUtils.union(n3, insec);

									List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(1, insec);

									Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

									for (Integer x : insecExcept) {
										gPrime.removeVertex(x);
										visited.put(x, true);
									}

									for (Integer z : zList) {

										// leave wz,remove xz
										Collection<Integer> zNeig = gPrime.getNeighbors(z);

										zNeig = CollectionUtils.subtract(zNeig, wInList);
										for (Integer x : zNeig) {
											int xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, x, z);
											gPrime.removeEdge(xz);
										}

										visited.put(z, true);
									}
								}
							} else {
								// n3 cannot be dominated by a single vertex
								// from {v,w}
								Collection<Integer> insec = CollectionUtils.union(n3, n2);

								List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(2, insec);

								Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

								for (Integer x : insecExcept) {
									gPrime.removeVertex(x);
									visited.put(x, true);
								}

								for (Integer z : zList) {
									Collection<Integer> zNeig = gPrime.getNeighbors(z);

									for (Integer x : zNeig) {
										int xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, x, z);
										gPrime.removeEdge(xz);
									}
								}

								Integer z0 = zList.get(0);
								Integer z1 = zList.get(1);

								int z0v = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, z0, v);
								gPrime.addEdge(z0v, z0, v);
								int z1w = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(numOfVertices, z1, w);
								gPrime.addEdge(z1w, z1, w);

								visited.put(z0, true);
								visited.put(z1, true);
							}
						}
					}
					visited.put(w, true);
				}
			}
			visited.put(v, true);
		}
		return gPrime;
	}

	/**
	 * Apply DS reduction rule 2 in michael's paper
	 * 
	 * 
	 * @param g,the
	 *            original graph
	 * @return reduced graph
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static Graph<Integer, String> applyPairVerticesReductionRule(Graph<Integer, String> g) {
		Graph<Integer, String> gPrime = AlgorithmUtil.copyGraph(g);

		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();

		Collection<Integer> vertices = g.getVertices();

		for (Integer v : vertices) {
			visited.put(v, false);
		}

		for (Integer v : vertices) {

			if (!visited.get(v)) {

				for (Integer w : vertices) {
					if (!visited.get(w)) {

						if (v.equals(w)) {
							continue;
						}

						Collection<Integer> vNeig = gPrime.getNeighbors(v);

						if (vNeig.contains(w)) {
							continue;
						}

						Collection<Integer> wNeig = gPrime.getNeighbors(w);

						Collection<Integer> vwNeig = CollectionUtils.union(vNeig, wNeig);

						List<Integer> n1 = new ArrayList<Integer>();
						for (Integer u : vwNeig) {
							Collection<Integer> uNeig = gPrime.getNeighbors(u);
							if (uNeig == null) {
								uNeig = CollectionUtils.EMPTY_COLLECTION;
							}
							Collection<Integer> n1diff = CollectionUtils.subtract(uNeig, vwNeig);
							n1diff.remove(v);
							n1diff.remove(w);
							if (!n1diff.isEmpty()) {
								n1.add(u);
							}
						}

						List<Integer> n2 = new ArrayList<Integer>();
						Collection<Integer> n2base = CollectionUtils.subtract(vwNeig, n1);
						for (Integer u : n2base) {
							Collection<Integer> uNeig = gPrime.getNeighbors(u);
							if (uNeig == null) {
								uNeig = CollectionUtils.EMPTY_COLLECTION;
							}
							Collection<Integer> uIntsec = CollectionUtils.intersection(uNeig, n1);
							if (!uIntsec.isEmpty()) {
								n2.add(u);
							}
						}

						Collection<Integer> n3 = CollectionUtils.subtract(vwNeig, CollectionUtils.union(n1, n2));

						if (!n3.isEmpty()) {
							List<Integer> vInList = new ArrayList<Integer>();
							vInList.add(v);

							List<Integer> wInList = new ArrayList<Integer>();
							wInList.add(w);

							List<Integer> vwInList = new ArrayList<Integer>();
							vwInList.add(v);
							vwInList.add(w);

							if (isAVertexDominateASet(v, n3, gPrime) || isAVertexDominateASet(w, n3, gPrime)) {
								// n3 can be dominated by a single vertex from
								// {v,w}
								if (CollectionUtils.isSubCollection(n3, vNeig)
										&& CollectionUtils.isSubCollection(n3, wNeig)) {
									Collection<Integer> insec = CollectionUtils.intersection(n2, vNeig);
									insec = CollectionUtils.intersection(insec, wNeig);
									insec = CollectionUtils.union(n3, insec);
									List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(2, insec);

									Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

									for (Integer x : insecExcept) {
										gPrime.removeVertex(x);
										visited.put(x, true);
									}

									for (Integer z : zList) {
										Collection<Integer> zNeig = gPrime.getNeighbors(z);

										zNeig = CollectionUtils.subtract(zNeig, vwInList);
										for (Integer x : zNeig) {
											String xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(x, z);
											gPrime.removeEdge(xz);
										}

										visited.put(z, true);
									}

								} else if (CollectionUtils.isSubCollection(n3, vNeig)
										&& !CollectionUtils.isSubCollection(n3, wNeig)) {
									Collection<Integer> insec = CollectionUtils.intersection(n2, vNeig);
									insec = CollectionUtils.union(n3, insec);

									List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(1, insec);

									Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

									for (Integer x : insecExcept) {
										gPrime.removeVertex(x);
										visited.put(x, true);
									}

									for (Integer z : zList) {

										// leave vz,remove xz
										Collection<Integer> zNeig = gPrime.getNeighbors(z);

										zNeig = CollectionUtils.subtract(zNeig, vInList);
										for (Integer x : zNeig) {
											String xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(x, z);
											gPrime.removeEdge(xz);
										}

										visited.put(z, true);
									}

								} else if (CollectionUtils.isSubCollection(n3, wNeig)
										&& !CollectionUtils.isSubCollection(n3, vNeig)) {
									Collection<Integer> insec = CollectionUtils.intersection(n2, wNeig);
									insec = CollectionUtils.union(n3, insec);

									List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(1, insec);

									Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

									for (Integer x : insecExcept) {
										gPrime.removeVertex(x);
										visited.put(x, true);
									}

									for (Integer z : zList) {

										// leave wz,remove xz
										Collection<Integer> zNeig = gPrime.getNeighbors(z);

										zNeig = CollectionUtils.subtract(zNeig, wInList);
										for (Integer x : zNeig) {
											String xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(x, z);
											gPrime.removeEdge(xz);
										}

										visited.put(z, true);
									}
								}
							} else {
								// n3 cannot be dominated by a single vertex
								// from {v,w}
								Collection<Integer> insec = CollectionUtils.union(n3, n2);

								List<Integer> zList = AlgorithmUtil.getFirstNItemsInCollection(2, insec);

								Collection<Integer> insecExcept = CollectionUtils.subtract(insec, zList);

								for (Integer x : insecExcept) {
									gPrime.removeVertex(x);
									visited.put(x, true);
								}

								for (Integer z : zList) {
									Collection<Integer> zNeig = gPrime.getNeighbors(z);

									for (Integer x : zNeig) {
										String xz = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(x, z);
										gPrime.removeEdge(xz);
									}
								}

								Integer z0 = zList.get(0);
								Integer z1 = zList.get(1);

								String z0v = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(z0, v);
								gPrime.addEdge(z0v, z0, v);
								String z1w = AlgorithmUtil.getEdgeLabelBy2VerticesLabel(z1, w);
								gPrime.addEdge(z1w, z1, w);

								visited.put(z0, true);
								visited.put(z1, true);
							}
						}
					}
					visited.put(w, true);
				}
			}
			visited.put(v, true);
		}
		return gPrime;
	}

	/**
	 * if a vertex set(vList) is dominated by another vertex (v)
	 * 
	 * @param v,
	 *            the dominating vertex
	 * @param vList,
	 *            the dominated vertex set
	 * @param g,
	 *            the graph instance
	 * @return true: vList is dominated by v;false, no
	 */
	public static <V, E> boolean isAVertexDominateASet(V v, Collection<V> vList, Graph<V, E> g) {
		Collection<V> vNeig = g.getNeighbors(v);
		if (CollectionUtils.subtract(vList, vNeig).isEmpty()) {
			return true;
		}
		return false;

	}

	/**
	 * if a vertex (u) is dominated by another vertex (v)
	 * 
	 * @param v,
	 *            the dominating vertex
	 * @param u,
	 *            the dominated vertex
	 * @param g,
	 *            the graph instance
	 * @return true: u is dominated by v;false, no
	 */
	public static <V, E> boolean isAVertexDominateAVertex(V v, V u, Graph<V, E> g) {
		if (u.equals(v)) {
			// a vertex always dominates itself
			return true;
		}
		Collection<V> vNeig = g.getNeighbors(v);
		if (vNeig.contains(u)) {
			return true;
		}
		return false;

	}

	// /**
	// * if a vertex set(edSet) is dominated by a vertex in a set (ingSet)
	// *
	// * @param ingSet,
	// * the set where the dominating vertex is in
	// * @param edSet,
	// * the dominated vertex set
	// * @param g,
	// * the graph instance
	// * @return true: vList is dominated by v;false, no
	// */
	// public static boolean isAVertexInASetDominateASet(Collection<Integer>
	// ingSet, Collection<Integer> edSet,
	// Graph<Integer, Integer> g) {
	// for (Integer v : ingSet) {
	// boolean isDominate = isAVertexDominateASet(v, edSet, g);
	// if (isDominate) {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	/**
	 * minimalization to reduce redundant vertices
	 * 
	 * @param g
	 * @param ds
	 * @return
	 * @throws ArraysNotSameLengthException
	 */
	public static <V, E> List<V> minimal(Graph<V, E> g, List<V> ds) throws ArraysNotSameLengthException {

		int distance = 1;
		int dsSize = ds.size();
		boolean[] chosen = AlgorithmUtil.verifySubDS(ds, dsSize, dsSize - distance, g);
		if (chosen == null) {
			return ds;
		} else {
			List<V> tempDs = new ArrayList<V>(dsSize - distance);

			for (int i = 0; i < dsSize; i++) {
				if (chosen[i]) {
					tempDs.add(ds.get(i));
				}
			}
			return tempDs;
		}
	}

	/**
	 * a GRASP local search
	 * 
	 * @param g,
	 *            the graph
	 * @param d,
	 *            the dominating set
	 * @return the dominating set after local search
	 */
	public static <V, E> List<V> grasp(Graph<V, E> g, List<V> d) {
		Collection<V> vertices = g.getVertices();
		int numOfVertices = vertices.size();
		Map<V, Integer> coveredbyMap = new HashMap<V, Integer>(numOfVertices);

		for (V v : vertices) {
			coveredbyMap.put(v, 0);
		}

		for (V w : d) {

			coveredbyMap.put(w, coveredbyMap.get(w).intValue() + 1);
			Collection<V> wNeig = g.getNeighbors(w);
			for (V v : wNeig) {
				coveredbyMap.put(v, coveredbyMap.get(v).intValue() + 1);
			}
		}
		int dSize = d.size();
		for (int i = 0; i < dSize - 1; i++) {
			V vi = d.get(i);
			for (int j = i + 1; j < dSize; j++) {
				V vj = d.get(j);
				if (!vi.equals(vj)) {
					List<V> U = new ArrayList<V>();
					for (V vk : vertices) {
						int covby = coveredbyMap.get(vk);
						if (AlgorithmUtil.isAVertexDominateAVertex(vi, vk, g)) {
							covby--;
						}
						if (AlgorithmUtil.isAVertexDominateAVertex(vj, vk, g)) {
							covby--;
						}
						if (covby == 0) {
							AlgorithmUtil.addElementToList(U, vk);
						}
					}
					if (U.isEmpty()) {
						d.remove(vi);
						d.remove(vj);
						// log.debug("ds changed here.");
						return grasp(g, d);
					} else {
						for (V vk : vertices) {
							if (AlgorithmUtil.isAVertexDominateASet(vk, U, g)) {
								d.remove(vi);
								d.remove(vj);
								d.add(vk);
								// log.debug("ds changed here.");
								return grasp(g, d);
							}
						}
					}
				}
			}
		}

		return d;
	}

	/**
	 * if one of a vertex v's close neighbors (besides u) has already in the
	 * solution list d, it is a moment of regret; otherwise not
	 * 
	 * @param v,
	 *            the vertex whose highest utility close neighbor will be added
	 *            to d
	 * @param u,
	 *            the highest utility close neighbor of v
	 * @param g,
	 *            the graph v is in
	 * @param d,
	 *            a set which is being built to be the solution
	 * @return true, it is a moment of regret; false, it is not.
	 */
	@Deprecated
	public static <V, E> boolean isMomentOfRegret(V v, Graph<V, E> g, List<V> d, V u) {
		Collection<V> vNeig = g.getNeighbors(v);
		vNeig.add(v);

		Collection<V> intsec = CollectionUtils.intersection(vNeig, d);
		intsec.remove(u);

		if (intsec.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}
	
	
	/**
	 * adjust weight of a vertex, implemented according to the algorithm
	 * description in the paper
	 * 
	 * @param v,
	 *            the vertex
	 */
	public static void adjustWeight(Graph<Integer,String> g, Map<Integer,Boolean> dominatedMap, Map<Integer,Float> weightMap,Map<Integer,Float> voteMap,Integer v) {
		Collection<Integer> vNeigs = g.getNeighbors(v);
		boolean coveredv = dominatedMap.get(v);
		weightMap.put(v, 0.0f);
		float votev = voteMap.get(v);
		for (Integer u : vNeigs) {
			float weightu = weightMap.get(u);
			if (weightu - 0.0f > VertexPriority.ZERO_DIFF) {
				float voteu = voteMap.get(u);
				if (!coveredv) {
					weightMap.put(u, weightu - votev);
				}
				boolean coveredu = dominatedMap.get(u);
				if (!coveredu) {
					dominatedMap.put(u, true);
					weightu = weightMap.get(u);
					weightMap.put(u, weightu - voteu);

					Collection<Integer> uNeigs = g.getNeighbors(u);
					for (Integer w : uNeigs) {
						float weightw = weightMap.get(w);
						if (weightu - 0.0f > VertexPriority.ZERO_DIFF) {
							weightMap.put(w, weightw - voteu);
						}
					}

				}
			}

		}
		dominatedMap.put(v, true);
	}
}
