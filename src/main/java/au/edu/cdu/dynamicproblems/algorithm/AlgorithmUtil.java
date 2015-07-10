package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class AlgorithmUtil {
	// private static Logger log = LogUtil.getLogger(AlgorithmUtil.class);

	public static final String CONNECTED = "1";
	public static final String UNCONNECTED = "0";

	// used for vertex covers to show if they are dominated or not
	public final static byte MARKED = 1;
	public final static byte UNMARKED = 0;

	// used for left pad for binary string of an integer
	private static final String BINARY_LEFT_PAD = "0";

	// the ascii code of 0
	private static final byte ASCII_0_SEQ_NO = 48;

	/**
	 * generate an instance of Graph with internal parameters
	 * 
	 * @param adjacencyMatrix
	 *            , adjacency matrix of a graph
	 * @return a graph
	 */
	public static Graph<Integer, Integer> prepareGraph(
			List<String[]> adjacencyMatrix) {

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

	// /**
	// * generate an instance of Graph with internal parameters
	// *
	// * @param adjacencyMatrix
	// * , adjacency matrix of a graph
	// * @return a graph
	// */
	// public static Graph<Integer, Integer> prepareGraph(
	// List<String[]> adjacencyMatrix, List<Integer> vList) {
	//
	// int numOfVertices = adjacencyMatrix.size();
	// Graph<Integer, Integer> g = new SparseMultigraph<Integer, Integer>();
	// for (Integer i : vList) {
	// g.addVertex(i);
	// }
	//
	// for (int i = 0; i < numOfVertices; i++) {
	// if (vList.contains(i)) {
	//
	// String[] rowArr = adjacencyMatrix.get(i);
	// for (int j = 0; j < numOfVertices; j++) {
	// if (vList.contains(j)) {
	// if (CONNECTED.equals(rowArr[j].trim())) {
	// // the label of edge is decided by the label of the
	// // two
	// // endpoints
	// int edge = getEdgeLabelBy2VerticesLabel(
	// numOfVertices, i, j);
	// g.addEdge(edge, i, j);
	//
	// }
	// }
	// }
	// }
	// }
	//
	// return g;
	// }
	/**
	 * add vertices in vList to g and add edges between such vertices to g as
	 * well
	 * 
	 * @param adjacencyMatrix
	 * @param g
	 * @param vList
	 * @return
	 */
	public static Graph<Integer, Integer> prepareGraph(
			List<String[]> adjacencyMatrix, Graph<Integer, Integer> g,
			List<Integer> vList) {

		int numOfVertices = adjacencyMatrix.size();

		for (Integer i : vList) {
			g.addVertex(i);
		}

		Collection<Integer> gVertices = g.getVertices();
		for (int i = 0; i < numOfVertices; i++) {
			if (gVertices.contains(i)) {

				String[] rowArr = adjacencyMatrix.get(i);
				for (int j = 0; j < numOfVertices; j++) {
					if (i < j) {
						if (gVertices.contains(j)) {

							if (CONNECTED.equals(rowArr[j].trim())) {
								// the label of edge is decided by the label of
								// the
								// two
								// endpoints
								int edge = getEdgeLabelBy2VerticesLabel(
										numOfVertices, i, j);
								g.addEdge(edge, i, j);

							}
						}
					}
				}
			}
		}

		return g;
	}

	public static List<Integer> getVertexList(List<VertexDegree> vdList) {
		List<Integer> vList = new ArrayList<Integer>();
		for (VertexDegree vd : vdList) {
			// vList.add(vd.getVertex());
			addElementToList(vList, vd.getVertex());
		}
		return vList;
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
	public static int getEdgeLabelBy2VerticesLabel(int numOfVertices, int v1,
			int v2) {
		int min = Math.min(v1, v2);
		int max = Math.max(v1, v2);
		int edge = min * numOfVertices + max;
		return edge;
	}

	/**
	 * get a list of sorted vertices with their degrees from a graph
	 * 
	 * @param g
	 *            , an instance of Graph,
	 * @return List<VertexDegree>, a list of vertices with their degrees
	 */
	public static List<VertexDegree> sortVertexAccordingToDegree(
			Graph<Integer, Integer> g) {

		// get the sorted vertex according their degree
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();
		Collection<Integer> vertices = g.getVertices();
		for (int i : vertices) {
			int degree = g.degree(i);
			// vertexDegreeList.add(new VertexDegree(i, degree));
			addElementToList(vertexDegreeList, new VertexDegree(i, degree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	/**
	 * get a list of sorted vertices with the number of their un-dominated
	 * neighbors from a graph
	 * 
	 * @param g
	 *            , an instance of Graph,
	 * @param dominatedMap
	 *            , a marked map showing vertices and if it's dominated
	 * @return List<VertexDegree>, a sorted list of vertices he number of their
	 *         un-dominated neighbors
	 */
	public static List<VertexDegree> sortVertexAccordingToUndomiatedDegree(
			Graph<Integer, Integer> g, Map<Integer, Boolean> dominatedMap) {
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
			addElementToList(vertexDegreeList, new VertexDegree(v,
					unDominatedDegree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	public static Integer getVertexFromClosedNeighborhoodWithHighestUtility(
			Integer v, Graph<Integer, Integer> g, List<VertexDegree> vdList,
			Map<Integer, Boolean> dominatedMap) {
		Collection<Integer> vNeigs = g.getNeighbors(v);
		vNeigs.add(v);

		for (VertexDegree vd : vdList) {
			Integer u = vd.getVertex();
			if (vNeigs.contains(u) && !dominatedMap.get(u)) {
				return u;
			}
		}

		return null;

	}

	/**
	 * get a list of sorted vertices with their degrees from a graph except the
	 * vertices in excludeList
	 * 
	 * @param g
	 * @param excludeList
	 * @return
	 */
	public static List<VertexDegree> sortVertexAccordingToDegree(
			Graph<Integer, Integer> g, List<Integer> excludeList) {

		// get the sorted vertex according their degree
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();
		Collection<Integer> vertices = g.getVertices();
		vertices = CollectionUtils.subtract(vertices, excludeList);
		for (Integer i : vertices) {
			int degree = g.degree(i);
			// vertexDegreeList.add(new VertexDegree(i, degree));
			addElementToList(vertexDegreeList, new VertexDegree(i, degree));
		}
		Collections.sort(vertexDegreeList);
		return vertexDegreeList;
	}

	/**
	 * get a list of sorted vertices with their degrees from a graph except the
	 * vertices in excludeList
	 * 
	 * @param g
	 * @param excludeList
	 * @return
	 */
	public static List<VertexDegree> sortVertexAccordingToDegree(
			Graph<Integer, Integer> g, Collection<Integer> excludeList) {

		// get the sorted vertex according their degree
		List<VertexDegree> vertexDegreeList = new ArrayList<VertexDegree>();
		Collection<Integer> vertices = g.getVertices();
		vertices = CollectionUtils.subtract(vertices, excludeList);
		for (Integer i : vertices) {
			int degree = g.degree(i);
			// vertexDegreeList.add(new VertexDegree(i, degree));
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
	public static List<Integer> sortVertexAccordingToSortedVertexList(
			List<Integer> vl, List<VertexDegree> vdl) {
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
	 * if the vertices in dominatedMap are all marked as dominated.
	 * 
	 * @param dominatedMap
	 * @return
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

	/**
	 * if the vertices in the list are all marked as dominated.
	 * 
	 * @param dominatedMap
	 * @param vList
	 * @return
	 */
	public static boolean isAllDominated(Map<Integer, Boolean> dominatedMap,
			Collection<Integer> vList) {

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

	public static Graph<Integer, Integer> constructCompleteGraph(
			Graph<Integer, Integer> g, int numOfVertices) {
		Graph<Integer, Integer> gK = new SparseMultigraph<Integer, Integer>();

		Collection<Integer> vertices = g.getVertices();
		for (Integer v : vertices) {
			gK.addVertex(v);
		}

		for (Integer i : vertices) {
			for (Integer j : vertices) {
				if (i < j) {
					int edge = getEdgeLabelBy2VerticesLabel(numOfVertices, i, j);
					gK.addEdge(edge, i, j);
				}
			}
		}

		return gK;
	}

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
	public static List<String[]> generateRandGraph(int numOfVertices,
			float adjacentRatio) {
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

	/**
	 * Do the edge deletion to get henning distance
	 * 
	 * @param am1
	 *            , the adjacent matrix of graph g1
	 * @param g1
	 *            , the graph object of g1
	 * @param ds1
	 *            , a dominating set of g1
	 * @param k
	 *            , the maximum number of edge deletion
	 * @return an instance of HEdit containing the output adjacent matrix and
	 *         operation list
	 */
	public static HEdit hEditEdgeDeletion(List<String[]> am1,
			Graph<Integer, Integer> g1, List<Integer> ds1, int k) {
		List<String[]> operationList = new ArrayList<String[]>();

		// generate a copy of adjacency matrix 1
		List<String[]> am2 = new ArrayList<String[]>(am1);

		// int ds1Size = ds1.size();
		Collection<Integer> vertices1 = g1.getVertices();
		// get the complementary set of dominating set 1 in graph 1
		List<Integer> complementOfDS1 = (List<Integer>) CollectionUtils
				.subtract(vertices1, ds1);

		List<Integer> randomKVerInComplementOfDS1Set = getKRandomVerticesInSet(
				k, complementOfDS1);

		for (Integer cDsv : randomKVerInComplementOfDS1Set) {
			Collection<Integer> neighboursOfCDsv = g1.getNeighbors(cDsv);

			for (Integer nCdsv : neighboursOfCDsv) {
				if (ds1.contains(nCdsv)) {
					am2.get(cDsv)[nCdsv] = UNCONNECTED;
					am2.get(nCdsv)[cDsv] = UNCONNECTED;

					// change operation list
					String[] operation = { UNCONNECTED, Integer.toString(cDsv),
							Integer.toString(nCdsv) };

					addElementToList(operationList, operation);
				}
			}

		}

		HEdit hEdit = new HEdit();
		hEdit.setOperationList(operationList);
		hEdit.setOutputAdjacencyMatrix(am2);

		return hEdit;

	}

	/**
	 * get at most k number of random numbers which are between n1 and n2; in
	 * another words, the number of random numbers may be less than k
	 * 
	 * @param k
	 *            , the number of random vertices
	 * @param n1
	 *            , the bottom bound of the random numbers
	 * @param n2
	 *            , the up bound of the random numbers
	 * @return k number of random numbers which are between n1 and n2;
	 */
	public static List<Integer> getKRandomVerticesInSet(int k,
			Collection<Integer> s) {
		int sSize = s.size();
		Integer[] sArray = new Integer[sSize];

		sArray = s.toArray(sArray);

		List<Integer> rtn = new ArrayList<Integer>();
		if (sSize <= 0) {
			return rtn;
		}
		if (sSize < k) {
			k = sSize;
		}

		for (int i = 0; i < k; i++) {
			int ran = (int) (Math.random() * (sSize - 1));
			Integer sRan = sArray[ran];
			if (rtn.contains(sRan)) {
				i--;
			} else {

				// rtn.add(sRan);
				addElementToList(rtn, sRan);
			}
		}

		return rtn;
	}

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
	public static <E> List<E> addElementToList(List<E> list, E e) {
		if (!list.contains(e)) {
			list.add(e);
		}
		return list;
	}

	// /**
	// * judge if it is a solution of set dominating a vertex cover
	// *
	// * @param g
	// * , a graph
	// * @param domVC
	// * , a potential set dominating a vertex cover
	// * @param vc
	// * , a vertex cover
	// * @return boolean, is a solution or not
	// */
	//
	// public static boolean isDomVCSet(Graph<Integer, Integer> g,
	// List<Integer> domVC, List<Integer> vc) {
	//
	// for (Integer v : domVC) {
	// /*
	// * get neighbours of the vertices in the potential set dominating
	// * the vertex cover
	// */
	// Collection<Integer> neighborsOfV = g.getNeighbors(v);
	// // remove the neighbours from the vertex cover
	// vc = (List<Integer>) CollectionUtils.subtract(vc, neighborsOfV);
	// }
	// /*
	// * vc intersect domVC might not be empty because some verterices in vc
	// * may dominate other vertices in vc as well
	// */
	// vc = (List<Integer>) CollectionUtils.subtract(vc, domVC);
	//
	// /*
	// * if the vertex cover is not empty, it means there are some vertices
	// * are not dominated and in turn the input set is not a set dominating
	// * vc for the graph
	// */
	// if (!vc.isEmpty()) {
	// return false;
	// }
	// return true;
	//
	// }

	// /**
	// * get the number of 1s in the array
	// *
	// * this can be used to show the number of 1s in the array
	// *
	// * @param rulerr
	// * , an 0/1 array
	// * @return
	// */
	// public static int getSumFromBinArray(byte[] ruler) {
	// int k = ruler.length;
	// int rulerSum = 0;
	// for (int i = 0; i < k; i++) {
	// rulerSum += ruler[i];
	// }
	// return rulerSum;
	// }
	/**
	 * do the or operation of each corresponding elements of 2 byte arrays. the
	 * 2 byte arrays are of the same length
	 * 
	 * @param ruler1
	 * @param ruler2
	 * @return
	 */
	public static byte[] arrayOr(byte[] ruler1, byte[] ruler2)
			throws ArraysNotSameLengthException {
		int ruler1Len = ruler1.length;
		int ruler2Len = ruler2.length;
		if (ruler1Len != ruler2Len) {
			throw new ArraysNotSameLengthException(
					"The two byte arrays are not of the same length.");
		}

		for (int i = 0; i < ruler1Len; i++) {

			ruler1[i] = (byte) (ruler1[i] | ruler2[i]);
		}

		return ruler1;
	}

	/**
	 * get neighbours of vertices in a set
	 * 
	 * @param g
	 *            , a graph
	 * @param S
	 *            , a set of vertices
	 * @return
	 */
	public static List<Integer> getNeighborsOfS(Graph<Integer, Integer> g,
			List<Integer> S) {
		List<Integer> ngs = new ArrayList<Integer>();
		for (Integer s : S) {
			Collection<Integer> col = g.getNeighbors(s);
			if (col != null) {
				ngs = (List<Integer>) CollectionUtils.union(ngs, col);
			}
		}
		return ngs;
	}

	/**
	 * judge if it is a solution of dominating set
	 * 
	 * @param g
	 *            , graph
	 * @param ds
	 *            , a potential dominating set
	 * @return boolean, is dominating set or not
	 */
	public static boolean isDS(Graph<Integer, Integer> g, List<Integer> ds) {
		Collection<Integer> vertices = g.getVertices();
		Collection<Integer> complementaryDS = CollectionUtils.subtract(
				vertices, ds);

		for (Integer v : ds) {
			// get neighbours of the vertices in dominating set
			Collection<Integer> neighborsOfV = g.getNeighbors(v);
			// remove the neighbours from the complementary set
			if (neighborsOfV != null) {
				complementaryDS = CollectionUtils.subtract(complementaryDS,
						neighborsOfV);
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

	/**
	 * get a copy of source graph
	 * 
	 * @param src
	 *            , source graph
	 * @return a copy of source graph
	 */
	public static Graph<Integer, Integer> copyGrapy(Graph<Integer, Integer> src) {
		Graph<Integer, Integer> dest = new SparseMultigraph<Integer, Integer>();
		for (Integer v : src.getVertices())
			dest.addVertex(v);

		for (Integer e : src.getEdges())
			dest.addEdge(e, src.getIncidentVertices(e));

		return dest;
	}

	/**
	 * get the number of edge difference of two adjacent matrixes, because the
	 * adjacent matrix is symmetrical, it only count the half
	 * 
	 * @param am1
	 *            , adjacent matrix 1
	 * @param am2
	 *            , adjacent matrix 2
	 * @return the number of edge difference of two adjacent matrixes
	 */
	public static int getDifferentEdgeNumber(List<String[]> am1,
			List<String[]> am2) throws ArraysNotSameLengthException {

		int n = am1.size();
		int count = 0;
		for (int i = 0; i < n; i++) {

			String[] am1row = am1.get(i);
			String[] am2row = am2.get(i);
			count += arrayXorDifference(am1row, am2row);
		}

		return count / 2;
	}

	/**
	 * do the Exclusive-OR operation of each corresponding elements of 2 string
	 * arrays and then get the number of differences. the 2 string arrays are of
	 * the same length
	 * 
	 * @param a1
	 *            , string array 1
	 * @param a2
	 *            , string array 2
	 * @return the number of differences
	 */
	private static int arrayXorDifference(String[] a1, String[] a2)
			throws ArraysNotSameLengthException {
		int a1Len = a1.length;
		int a2Len = a2.length;
		if (a1Len != a2Len) {
			throw new ArraysNotSameLengthException(
					"The two byte arrays are not of the same length.");
		}

		int count = 0;

		for (int i = 0; i < a1Len; i++) {
			byte a1i = Byte.parseByte(a1[i]);
			byte a2i = Byte.parseByte(a2[i]);
			if ((a1i ^ a2i) == 1) {
				count++;
			}
		}

		return count;

	}

	/**
	 * convert a binary byte array to a long integer
	 * 
	 * @param binary
	 *            , a binary byte array
	 * @return, a long integer corresponding to the binary byte array
	 * @throws ExceedLongMaxException
	 */
	public static long arrayToLong(byte[] binary) throws ExceedLongMaxException {
		// we assume that the size of binary does not exceed 63;
		int binarySize = binary.length;
		if (binarySize >= (long) (Math.log(Long.MAX_VALUE) / Math.log(2))) {
			throw new ExceedLongMaxException(
					"Excceed the max value allowed for Long integer.");
		}

		long sum = 0;
		for (int i = binarySize - 1; i >= 0; i--) {
			sum += (long) (Math.pow(2, binarySize - 1 - i) * binary[i]);
		}

		return sum;

	}

	public static String arrayToString(byte[] binary) {
		int binarySize = binary.length;
		char[] chArray = new char[binarySize];
		for (int i = 0; i < binarySize; i++) {
			chArray[i] = (char) (binary[i] + ASCII_0_SEQ_NO);
		}
		String rtn = new String(chArray);
		return rtn;
	}

	public static byte[] stringToBinaryArray(String binaryStr) {
		byte[] binaryArray = binaryStr.getBytes();
		int binaryArraySize = binaryArray.length;
		for (int i = 0; i < binaryArraySize; i++) {
			binaryArray[i] -= ASCII_0_SEQ_NO;
		}
		return binaryArray;
	}

	public static List<Integer> stringToIntList(String binaryStr) {
		List<Integer> intList = new ArrayList<Integer>();

		byte[] binaryArray = binaryStr.getBytes();
		int binaryArraySize = binaryArray.length;
		for (int i = 0; i < binaryArraySize; i++) {
			binaryArray[i] -= ASCII_0_SEQ_NO;
			if (binaryArray[i] == AlgorithmUtil.MARKED) {
				// intList.add(i);
				addElementToList(intList, i);
			}
		}

		return intList;
	}

	public static String intListToString(int size, List<Integer> list) {

		char[] chArray = new char[size];
		for (int i = 0; i < size; i++) {
			chArray[i] = (char) (ASCII_0_SEQ_NO);
		}

		for (Integer i : list) {
			chArray[i] = (char) (ASCII_0_SEQ_NO + 1);
		}

		String rtn = new String(chArray);
		return rtn;

	}

	/**
	 * convert a long integer to a binary byte array
	 * 
	 * @param size
	 *            , the target binary byte array length
	 * @param binaryLong
	 *            , the long integer
	 * @return a binary byte array corresponding to the long integer
	 */
	public static byte[] longToBinaryArray(int size, Long binaryLong) {
		String binaryStr = StringUtils.leftPad(Long.toBinaryString(binaryLong),
				size, BINARY_LEFT_PAD);
		return stringToBinaryArray(binaryStr);
	}
}
