package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.TestUtil;
import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;

public class GreedyDSMVSTest {
	private Logger log = LogUtil.getLogger(GreedyDSMVSTest.class);
	private static final String CLASS_NAME = GreedyDSMVSTest.class.getSimpleName();

	@Ignore
	@Test
	public void testDIMACS_verify() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException,
			InterruptedException, InterruptedException {
		String datasetName = "DIMACS";
		String path = TestUtil.DIMACS_PATH;
		String[] files = TestUtil.DIMACS_FILES;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);
		int[][] krArray = { { 10, 10 } };
		runStrategies(path, krArray, files, destFile, 1, 1);
	}

	@Ignore
	@Test
	public void testBHOSLIB_verify() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException,
			IOException, InterruptedException {
		String datasetName = "BHOSLIB";
		String path = TestUtil.BHOSLIB_PATH;
		String[] files = TestUtil.BHOSLIB_FILES;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		int[][] krArray = { { 10, 10 } };

		runStrategies(path, krArray, files, destFile, 1, 1);

	}

	//@Ignore
	@Test
	public void testKONECT_verify() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException,
			InterruptedException {
		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;
		String[] files = TestUtil.KONECT_FILES;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		int[][] krArray = { { 10, 10 } };

		runStrategies(path, krArray, files, destFile, 1, 1);
	}


	@SuppressWarnings("unchecked")
	private void runStrategies(String path, int[][] krArray, String[] files, String destFile, int iStart, int iEnd)
			throws FileNotFoundException, IOException, MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, InterruptedException {

		log.debug(destFile);

		for (String file : files) {
			FileOperation fo = IOUtil.getProblemInfoByEdgePair(path + file);
			List<String[]> am = fo.getAdjacencyMatrix();

			for (int i = iStart; i <= iEnd; i++) {

				String msg;

				msg = setMessage(file, i);

				log.debug(msg);
				FileOperation.saveCVSFile(destFile, msg);

				for (int[] kr : krArray) {
					int k = kr[0];
					int rUpper = kr[1];
					int r = rUpper;

					StringBuilder sb = new StringBuilder();

					IGreedyDS<Integer> ag01 = new GreedyDSM1(am, k, r);
					Result result01 = ag01.run();
					List<Integer> ds01 = ag01.getDominatingSet();
					int ds01Size = ds01.size();
					Map<String, Long> ag01RunningTimeMap = ag01.getRunningTimeMap();
					sb.append(result01.getString()).append("\n");

					IGreedyDS<Integer> ag02 = new GreedyDSM2(am, k, r);
					Result result02 = ag02.run();
					List<Integer> ds02 = ag02.getDominatingSet();
					int ds02Size = ds02.size();
					Map<String, Long> ag02RunningTimeMap = ag02.getRunningTimeMap();
					sb.append(result02.getString()).append("\n");

					int minDSSize = ds02Size;
					int chooseDS = 2;

					if (minDSSize >= ds01Size) {
						minDSSize = ds01Size;
						chooseDS = 1;
					}

					if (minDSSize >= ds02Size) {
						minDSSize = ds02Size;
						chooseDS = 2;
					}

					sb.append(chooseDS).append(AlgorithmUtil.COMMA).append(minDSSize).append(AlgorithmUtil.COMMA);
					sb.append(k).append(AlgorithmUtil.COMMA).append(r).append(AlgorithmUtil.COMMA);

					setRunningTime(sb, ag01RunningTimeMap, ag02RunningTimeMap);

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


	@SuppressWarnings("unchecked")
	private void setRunningTime(StringBuilder sb, Map<String, Long>... agRunningTimeMaps) {
		long allAlgTotalRunningTime = 0;
		for (Map<String, Long> agRunningTimeMap : agRunningTimeMaps) {
			Set<String> keySet = agRunningTimeMap.keySet();
			for (String key : keySet) {
				if (AlgorithmUtil.RUNNING_TIME_TOTAL.equals(key)) {
					allAlgTotalRunningTime += agRunningTimeMap.get(key);
				}
			}
		}
		sb.append(allAlgTotalRunningTime);
	}

}