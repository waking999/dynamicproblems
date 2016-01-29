package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.TestParameter;
import au.edu.cdu.dynamicproblems.algorithm.TestUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyDSM0Test {

	private Logger log = LogUtil.getLogger(GreedyDSM0Test.class);
	private static final String CLASS_NAME = GreedyDSM0Test.class.getSimpleName();

	@Ignore
	@Test
	public void test0() throws InterruptedException, IOException, FileNotFoundException {
		List<String[]> am = TestUtil.simpleAM0();

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);
		Graph<Integer, String> gCopy = AlgorithmUtil.copyGraph(g);

		int k = 10;
		int r = 10;
		IGreedyDS<Integer> ag = new GreedyDSM0(am, k, r);
		Result result = ag.run();

		List<Integer> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(gCopy, ds));

		log.debug(result.getString());
	}

	@Ignore
	@Test
	public void test1() {
		List<String[]> am = TestUtil.simpleAM0();

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

		List<Integer> vList = OrderPackageUtil.getVertexListDegreeAsc(g);
		Assert.assertTrue(5 == vList.get(0));

		vList = OrderPackageUtil.getVertexListDegreeDesc(g);
		Assert.assertTrue(4 == vList.get(0));

	}

	@Ignore
	@Test
	public void testKONECT_verify() throws InterruptedException, IOException, FileNotFoundException {

		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, destFile, 1, 1, TestUtil.KONECT_TP);
	}

	@Ignore
	@Test
	public void testKONECT_differentK() throws InterruptedException, IOException, FileNotFoundException {

		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		StringBuilder sb = new StringBuilder();
		sb.append("------------");
		String sbStr = sb.toString();

		log.debug(sbStr);
		if (destFile != null) {
			FileOperation.saveCVSFile(destFile, sbStr);
		}

		for (TestParameter tp : TestUtil.KONECT_TP) {
			if (tp.isBeTest()) {
				int kLow = 2;
				int kUp = 50;
				for (int j = kLow; j < kUp; j++) {
					basicFunc(path + tp.getFile(), destFile, j, j);
				}
			}

		}
	}

	@Ignore
	@Test
	public void testDIMACS_verify() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "DIMACS";
		String path = TestUtil.DIMACS_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, destFile, 1, 1, TestUtil.DIMACS_TP);
	}
@Ignore
	@Test
	public void testMaxDegree() throws FileNotFoundException, IOException {
		// String datasetName = "DIMACS";
		// String path = TestUtil.DIMACS_PATH;
		// TestParameter[] tps=TestUtil.DIMACS_TP;
//
//		String datasetName = "KONECT";
//		String path = TestUtil.KONECT_PATH;
//		TestParameter[] tps = TestUtil.KONECT_TP;


		String datasetName = "BHOSLIB";
		String path = TestUtil.BHOSLIB_PATH;
		TestParameter[] tps = TestUtil.BHOSLIB_TP;
		
		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		for (TestParameter tp : tps) {
			if (tp.isBeTest()) {
				String file = tp.getFile();
				FileOperation fo = IOUtil.getProblemInfoByEdgePair(path + file);
				List<String[]> am = fo.getAdjacencyMatrix();

				Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

				List<Integer> vList = OrderPackageUtil.getVertexListDegreeDesc(g);
				Integer v = vList.get(0);
				int degree = g.degree(v);

				StringBuffer tempSB = new StringBuffer(file).append(AlgorithmUtil.COMMA).append(degree);
				FileOperation.saveCVSFile(destFile, tempSB.toString());
			}
		}

	}

	@Ignore
	@Test
	public void testDIMACS_degree() throws InterruptedException, IOException, FileNotFoundException {
		// String datasetName = "DIMACS";
		// String path = TestUtil.DIMACS_PATH;
		//

		// String datasetName = "KONECT";
		// String path = TestUtil.KONECT_PATH;

		String path = TestUtil.BHOSLIB_PATH;

		StringBuilder sb = new StringBuilder();
		sb.append("------------");
		String sbStr = sb.toString();

		log.debug(sbStr);

		for (TestParameter tp : TestUtil.BHOSLIB_TP) {
			if (tp.isBeTest()) {
				String file = tp.getFile();
				FileOperation fo = IOUtil.getProblemInfoByEdgePair(path + file);
				List<String[]> am = fo.getAdjacencyMatrix();

				Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

				List<Integer> vList = OrderPackageUtil.getVertexListDegreeAsc(g);

				StringBuffer tempSB = new StringBuffer(file).append(":");
				// int count = Math.min(1000, g.getVertexCount());
				int count = g.getVertexCount();

				List<Integer> degreeList = new ArrayList<Integer>();

				for (int i = 1; i < count - 1; i++) {
					Integer v = vList.get(i);
					Collection<Integer> vNeig = g.getNeighbors(v);
					for (int j = i + 1; j < count; j++) {
						Integer u = vList.get(j);
						if (!v.equals(u)) {
							Collection<Integer> uNeig = g.getNeighbors(u);
							Collection<Integer> uvNeig = CollectionUtils.union(vNeig, uNeig);
							if (!uvNeig.contains(v)) {
								uvNeig.add(v);
							}
							if (!uvNeig.contains(u)) {
								uvNeig.add(u);
							}
							int unionDegree = uvNeig.size();
							if (!degreeList.contains(unionDegree)) {
								degreeList.add(unionDegree);
							}
						}
					}

				}
				Collections.sort(degreeList);
				for (Integer d : degreeList) {
					tempSB.append(d).append(",");
				}

				log.debug(tempSB.toString());

			}

		}

	}

	@Ignore
	@Test
	public void testBHOSLIB_verify() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "BHOSLIB";
		String path = TestUtil.BHOSLIB_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, destFile, 1, 1, TestUtil.BHOSLIB_TP);
	}

	private void basicFunc(String path, String destFile, int iLower, int iUpper, TestParameter[] tps)
			throws FileNotFoundException, IOException, InterruptedException {
		for (int i = iLower; i <= iUpper; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(i).append("------------");
			String sbStr = sb.toString();

			log.debug(sbStr);
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, sbStr);
			}

			for (TestParameter tp : tps) {
				if (tp.isBeTest()) {
					basicFunc(path + tp.getFile(), destFile, tp.getK(), tp.getR());
				}

			}
		}
	}

	private void basicFunc(String inputFile, String destFile, int k, int r)
			throws InterruptedException, IOException, FileNotFoundException {

		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

		IGreedyDS<Integer> ag = new GreedyDSM0(am, k, r);
		TestUtil.run(inputFile, destFile, g, ag, log);

	}

}
