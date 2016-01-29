package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.TestParameter;
import au.edu.cdu.dynamicproblems.algorithm.TestUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyVoteGrTest {

	private Logger log = LogUtil.getLogger(GreedyVoteGrTest.class);
	private static final String CLASS_NAME = GreedyVoteGrTest.class.getSimpleName();

	@Ignore
	@Test
	public void test0() throws InterruptedException, IOException, FileNotFoundException {
		List<String[]> am = TestUtil.simpleAM0();

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);
		Graph<Integer, String> gCopy = AlgorithmUtil.copyGraph(g);

		IGreedyDS<Integer> ag = new GreedyVoteGr(g);
		Result r = ag.run();

		List<Integer> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(gCopy, ds));

		log.debug(r.getString());
	}

	// @Ignore
	@Test
	public void testKONECT() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, TestUtil.KONECT_TP, destFile, 1, 1, log);
	}

	@Ignore
	@Test
	public void testDIMACS() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "DIMACS";
		String path = TestUtil.DIMACS_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, TestUtil.DIMACS_TP, destFile, 1, 1, log);
	}

	@Ignore
	@Test
	public void testBHOSLIB() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "BHOSLIB";
		String path = TestUtil.BHOSLIB_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, TestUtil.BHOSLIB_TP, destFile, 1, 1, log);
	}

	/**
	 * 
	 * @param path
	 * @param files
	 * @param destFile
	 * @param iLower
	 * @param iUpper
	 * @param log
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void basicFunc(String path, TestParameter[] tps, String destFile, int iLower, int iUpper, Logger log)
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
					String inputFile = path + tp.getFile();
					FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
					List<String[]> am = fo.getAdjacencyMatrix();

					Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

					IGreedyDS<Integer> ag = new GreedyVoteGr(g);

					TestUtil.run(inputFile, destFile, g, ag, log);
				}
			}
		}
	}
}
