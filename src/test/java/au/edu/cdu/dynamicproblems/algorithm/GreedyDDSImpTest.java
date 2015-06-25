package au.edu.cdu.dynamicproblems.algorithm;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
@Deprecated
public class GreedyDDSImpTest {
	private Logger log = LogUtil.getLogger(GreedyDDSImpTest.class);
	@Ignore
	@Test
	public void test2() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String inputFile1 = "src/test/resources/1000_0.3_2_testcase_a.csv";
		FileOperation fo = IOUtil.getProblemInfo(inputFile1);
		List<String[]> am = fo.getAdjacencyMatrix();

		int[][] krArray = { { 100, 2 }, { 200, 2 }, { 200, 3 }, { 300, 2 },
				{ 300, 3 }, { 500, 2 }, { 500, 3 }, { 500, 4 } };

		for (int[] kr : krArray) {
			int k = kr[0];
			int r = kr[1];
			log.debug("k=" + k + ",r=" + r + "---------------------");

			GreedyDDSImp ag = new GreedyDDSImp("GreedyDDSImpTest", am, k, r);

			Result result = null;

			ag.computing();

			List<Integer> ds = ag.getDs();
			Assert.assertTrue(AlgorithmUtil.isDS(
					AlgorithmUtil.prepareGraph(am), ds));
			result = ag.getResult(r);

			log.debug(result.getString());
		}

	}
}
