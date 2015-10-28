package au.edu.cdu.dynamicproblems.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.HEdit;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyNativeV1;
import edu.uci.ics.jung.graph.Graph;

public class IOUtilTest {
	@Ignore
	@Test
	public void testGetProblemInfo() throws FileNotFoundException, IOException {
		String inputFile1 = "src/test/resources/50_0.3_testcase_a.csv";
		FileOperation fo = IOUtil.getProblemInfo(inputFile1);
		List<String[]> am = fo.getAdjacencyMatrix();

		Assert.assertNotNull(am);

	}

	@Ignore
	@Test
	public void testSave() throws InterruptedException, IOException,
			FileNotFoundException {
		String destFolder = "src/test/resources/iotest/";
		float ratio = 0.1f;
		List<String[]> am = new ArrayList<String[]>();
		am.add(new String[] { "0", "1", "0" });
		am.add(new String[] { "1", "0", "1" });
		am.add(new String[] { "0", "1", "0" });

		FileOperation.saveAgjacencyMatrixToFile(destFolder, am, ratio, 0);
		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		GreedyNativeV1 ag = new GreedyNativeV1(g);
		ag.run();

		List<Integer> ds1 = ag.getDominatingSet();

		int k = 2;

		// henning edge edit the graph to get another graph G'
		HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds1, k);

		List<String[]> operationList = hEdit.getOperationList();

		FileOperation.saveOperationToFile(destFolder, operationList);
	}
}
