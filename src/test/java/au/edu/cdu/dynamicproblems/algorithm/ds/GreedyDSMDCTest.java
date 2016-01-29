package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.TestParameter;
import au.edu.cdu.dynamicproblems.algorithm.TestUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import junit.framework.Assert;

public class GreedyDSMDCTest {
	private Logger log = LogUtil.getLogger(GreedyDSMDCTest.class);
	private static final String CLASS_NAME = GreedyDSMDCTest.class.getSimpleName();

	 
	// @Ignore
	@Test
	public void testKONECT_verify() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException,
			IOException, InterruptedException {
		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		runStrategies(path, TestUtil.KONECT_TP, destFile, 1, 1);
	}

 
	private void runStrategies(String path, TestParameter[] tps, String destFile, int iStart, int iEnd)
			throws FileNotFoundException, IOException, MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, InterruptedException {

		log.debug(destFile);

		for (TestParameter tp : tps) {
			FileOperation fo = IOUtil.getProblemInfoByEdgePair(path + tp.getFile());
			List<String[]> am = fo.getAdjacencyMatrix();
			if (tp.isBeTest()) {
				int k = tp.getK();
				int rUpper = tp.getR();
				int r = rUpper;

				for (int i = iStart; i <= iEnd; i++) {

					String msg;

					msg = setMessage(tp.getFile(), i);

					log.debug(msg);
					FileOperation.saveCVSFile(destFile, msg);

					StringBuilder sb = new StringBuilder();

					IGreedyDS<Integer> ag = new GreedyDSMDC(am, k, r);
					Result result=ag.run();
					sb.append(result.getString());
					List<Integer> ds = ag.getDominatingSet();

					Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);
					//Assert.assertTrue(AlgorithmUtil.isDS(g, ds));

					log.debug(sb.toString());
					if (destFile != null) {
						FileOperation.saveCVSFile(destFile, sb.toString());
					}
				}

			}
		}
	}

	private String setMessage(String file, int i) {
		StringBuilder msgSb = new StringBuilder();
		msgSb.append(file).append("-i=").append(i);
		return msgSb.toString();
	}

}