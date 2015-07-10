package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.exception.NChooseMNoSolutionException;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

/**
 * make use of the fpt algorithm to get a dominating set of a vertex cover in a
 * graph
 * 
 * @author Kai Wang
 * 
 */
public class DomAVCFPT {

	@SuppressWarnings("unused")
	private Logger log = LogUtil.getLogger(DomAVCFPT.class);

	// used for n choose m to show if the element has been chosen or not
	private final static boolean CHOSEN = true;
	private final static boolean UNCHOSEN = false;
	/**
	 * the graph
	 */
	private Graph<Integer, Integer> g;
	/**
	 * the set dominating the vertex cover of the graph
	 */
	private List<Integer> dominatingVertexCoverSet;

	public List<Integer> getDominatingVertexCoverSet() {
		return dominatingVertexCoverSet;
	}

	/**
	 * the vertex cover of the graph
	 */
	private List<Integer> vertexCover;

	/**
	 * a parameter used in FPT as the size of the set dominating the vertex
	 * cover
	 */
	private int r;

	private boolean hasLessR;

	/**
	 * initialize the algorithm with graph and the vertex cover, which has been
	 * computed by other algorithm
	 * 
	 * @param g
	 * @param vertexCover
	 */
	public DomAVCFPT(Graph<Integer, Integer> g, List<Integer> vertexCover, int r) {
		this.g = g;
		this.vertexCover = vertexCover;
		this.r = r;

	}

	private byte[] desiredRuler;

	private void initlization() {
		this.hasLessR = false;
		this.dominatingVertexCoverSet = new ArrayList<Integer>();

		int vertexCoverSize = vertexCover.size();

		desiredRuler = new byte[vertexCoverSize];
		Arrays.fill(desiredRuler, AlgorithmUtil.MARKED);

	}

	/**
	 * the major function do the computing to get the desired solution. In this
	 * case, the desired result is a set dominating the vertex cover
	 */
	public void computing() throws MOutofNException,
			NChooseMNoSolutionException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		// the vertex cover could be empty,we will not consider it
		int vertexCoverSize = vertexCover.size();
		if (vertexCoverSize > 0) {
			initlization();

			getCandidateDomVerMap();

			getAttemptRSizeSolution(r);
			// getAttemptRSizeSolutionSC(r);

		}

	}

	public boolean hasLessR() {
		return hasLessR;
	}

	// private long nChooseMNum(int n, int m) {
	// // long up=1;
	// // for(int i=n;i>n-m;i--){
	// // up*=i;
	// // }
	// // long down=1;
	// // for(int i=1;i<=m;i++){
	// // down *=i;
	// // }
	//
	// double up = 1.0;
	// if (m < n / 2) {
	// for (int i = n - m + 1, j = 1; i <= n; i++, j++) {
	// up = BigNumberCalculator.mul(up, i / (j * 1.0));
	// }
	// } else {
	// for (int i = m + 1, j = 1; i <= n; i++, j++) {
	// up = BigNumberCalculator.mul(up, i / (j * 1.0));
	// }
	//
	// }
	//
	// return Math.round(up);
	// }

	void nChooseM(int n, int m) throws NChooseMNoSolutionException,
			ArraysNotSameLengthException {
		if (m > n) {
			// m always <= n;
			m = n;
		}
		// log.debug(n + " choose " + m);

		boolean isSolution = false;
		boolean isEnd = false;

		boolean[] chosen = new boolean[n];
		Arrays.fill(chosen, UNCHOSEN);

		Arrays.fill(chosen, 0, m, CHOSEN);

		// int count = 0;
		// count++;
		isSolution = verifyChosen(chosen, m, n);

		if (isSolution) {
			return;
		}

		// long total = nChooseMNum(n, m);

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

			isSolution = verifyChosen(chosen, m, n);

			if (isSolution) {
				return;
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
				isSolution = verifyChosen(chosen, m, n);

				if (isSolution) {
					return;
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
			throw new NChooseMNoSolutionException("No solution for " + n
					+ " choose " + m);
		}

	}

	private Map<String, List<Integer>> candidateDomVerMap;

	private void getCandidateDomVerMap() throws ExceedLongMaxException {
		candidateDomVerMap = new LinkedHashMap<String, List<Integer>>();

		// the vertex cover's complement set will be an independent set
		Collection<Integer> vertices = g.getVertices();
		Collection<Integer> independentSet = CollectionUtils.subtract(vertices,
				vertexCover);

		/*
		 * the vertex cover size will be the parameter k used in the fpt
		 * algorithm
		 */
		int vertexCoverSize = vertexCover.size();
		byte[] comparedRuler = new byte[vertexCoverSize];
		Arrays.fill(comparedRuler, AlgorithmUtil.MARKED);

		for (Integer isv : independentSet) {
			Collection<Integer> neighOfIsv = g.getNeighbors(isv);
			byte ruler[] = new byte[vertexCoverSize];
			// initilze the array with 0
			Arrays.fill(ruler, AlgorithmUtil.UNMARKED);
			for (Integer neig : neighOfIsv) {
				/*
				 * the position of the dominate vertex in the vertex cover will
				 * set 1
				 */
				int pos = vertexCover.indexOf(neig);
				if (pos != -1) {
					ruler[pos] = AlgorithmUtil.MARKED;
				}
			}

			// if (Arrays.equals(comparedRuler, ruler)) {
			// log.debug(isv + " dominates all vc");
			// }

			String rulerStr = AlgorithmUtil.arrayToString(ruler);

			List<Integer> candidateDomVerSet = candidateDomVerMap.get(rulerStr);

			if (candidateDomVerSet == null) {
				candidateDomVerSet = new ArrayList<Integer>();

			}
			// candidateDomVerSet.add(isv);
			AlgorithmUtil.addElementToList(candidateDomVerSet, isv);
			candidateDomVerMap.put(rulerStr, candidateDomVerSet);

		}

		/*
		 * because the vertices in the vertex cover can also dominate other
		 * vertices in the vertex cover,they are also considered
		 */
		for (Integer vcv : vertexCover) {
			Collection<Integer> neighOfVcv = g.getNeighbors(vcv);

			byte ruler[] = new byte[vertexCoverSize];
			Arrays.fill(ruler, AlgorithmUtil.UNMARKED);

			// the vertex can dominated by itself
			int pos = vertexCover.indexOf(vcv);
			ruler[pos] = AlgorithmUtil.MARKED;

			for (Integer neig : neighOfVcv) {
				/*
				 * the position of the dominate vertex in the vertex cover will
				 * set 1
				 * 
				 * a neighbour of the vertex is likely not to be in the vertex
				 * cover
				 */
				pos = vertexCover.indexOf(neig);
				if (pos != -1) {
					ruler[pos] = AlgorithmUtil.MARKED;
				}
			}

			// if (Arrays.equals(comparedRuler, ruler)) {
			// log.debug(vcv + " dominates all vc");
			// }
			String rulerStr = AlgorithmUtil.arrayToString(ruler);

			List<Integer> candidateDomVerSet = candidateDomVerMap.get(rulerStr);

			if (candidateDomVerSet == null) {
				candidateDomVerSet = new ArrayList<Integer>();

			}
			// candidateDomVerSet.add(vcv);
			AlgorithmUtil.addElementToList(candidateDomVerSet, vcv);
			candidateDomVerMap.put(rulerStr, candidateDomVerSet);

		}

	}

	private void getAttemptRSizeSolution(int attemptR) throws MOutofNException,
			NChooseMNoSolutionException, ArraysNotSameLengthException {

		int candidateDomVerMapSize = candidateDomVerMap.size();

		/* chose m elements from n elements */
		nChooseM(candidateDomVerMapSize, attemptR);

	}

	private boolean verifyChosen(boolean[] chosen, int m, int n)
			throws ArraysNotSameLengthException {
		List<Integer> possibleDomVCSet = new ArrayList<Integer>(m);

		int vertexCoverSize = vertexCover.size();
		if (candidateDomVerMap == null) {

		}
		Set<String> keySet = candidateDomVerMap.keySet();
		byte[] ruler = new byte[vertexCoverSize];
		Arrays.fill(ruler, AlgorithmUtil.UNMARKED);

		int index = 0;
		for (String key : keySet) {
			if (chosen[index]) {
				byte[] keyRuler = AlgorithmUtil.stringToBinaryArray(key);

				ruler = AlgorithmUtil.arrayOr(ruler, keyRuler);
				List<Integer> verList = candidateDomVerMap.get(key);
				Integer ver = getVertexFromCandiateMap(verList);
				// possibleDomVCSet.add(ver);
				AlgorithmUtil.addElementToList(possibleDomVCSet, ver);
			}
			index++;
		}

		boolean flag = Arrays.equals(desiredRuler, ruler);

		if (flag) {
			this.dominatingVertexCoverSet = possibleDomVCSet;
			hasLessR = true;
			return true;
		}
		hasLessR = false;
		return false;

	}

	private Integer getVertexFromCandiateMap(List<Integer> verList) {
		Integer rtn = null;
		List<Integer> vcVerList = (List<Integer>) CollectionUtils.intersection(
				verList, this.vertexCover);
		if (vcVerList != null && !vcVerList.isEmpty()) {
			rtn = vcVerList.get(0);
		} else {
			rtn = verList.get(0);
		}

		return rtn;
	}

//	@SuppressWarnings("unused")
//	private void getAttemptRSizeSolutionSC(int r) {
//		List<List<Integer>> family = new ArrayList<List<Integer>>();
//		Set<String> typeSet = this.candidateDomVerMap.keySet();
//		for (String typeStr : typeSet) {
//			List<Integer> set = AlgorithmUtil.stringToIntList(typeStr);
//			// family.add(set);
//			AlgorithmUtil.addElementToList(family, set);
//		}
//
//		List<Integer> universe = new ArrayList<Integer>();
//		int u = this.vertexCover.size();
//		for (int i = 0; i < u; i++) {
//			// universe.add(i);
//			AlgorithmUtil.addElementToList(universe, i);
//		}
//
//		SCDP scdp = new SCDP(family, universe, r);
//		scdp.computing();
//
//		if (scdp.isHasSolution()) {
//			List<Integer> possibleDomVCSet = new ArrayList<Integer>(r);
//			List<List<Integer>> setCover = scdp.getSC();
//			for (List<Integer> set : setCover) {
//				String key = AlgorithmUtil.intListToString(u, set);
//				List<Integer> verList = candidateDomVerMap.get(key);
//				Integer ver = getVertexFromCandiateMap(verList);
//				// possibleDomVCSet.add(ver);
//				AlgorithmUtil.addElementToList(possibleDomVCSet, ver);
//			}
//
//			this.dominatingVertexCoverSet = possibleDomVCSet;
//			hasLessR = true;
//
//			log.debug(scdp.getResult().getString());
//		}
//	}
}
