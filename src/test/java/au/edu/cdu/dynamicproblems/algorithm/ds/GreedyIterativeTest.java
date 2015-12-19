package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.TestUtil;
import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyIterativeTest {

	private Logger log = LogUtil.getLogger(GreedyIterativeTest.class);
	private static final String CLASS_NAME = GreedyIterativeTest.class.getSimpleName();
	
	//@Ignore
	@Test
	public void testKONECT_verify() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;
		String[] files = TestUtil.KONECT_FILES;
		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);
		
		int[] times = {1};

		run(path, files, destFile, times, 1, 1);

	}

	//@Ignore
	@Test
	public void testDIMACS_verify() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "DIMACS";
		String path = TestUtil.DIMACS_PATH;
		String[] files = TestUtil.DIMACS_FILES;
		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		int[] times = { 1};

		run(path, files, destFile, times, 1, 1);
	}

	//@Ignore
	@Test
	public void testBHOSLIB_verify() throws InterruptedException, IOException, FileNotFoundException {
		String datasetName = "BHOSLIB";
		String path = TestUtil.BHOSLIB_PATH;
		String[] files = TestUtil.BHOSLIB_FILES;
		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);
		
		int[] times = {1};

		run(path, files, destFile, times, 1, 1);
	}

	private void run(String path, String[] files, String destFile, int[] times, int iStart, int iEnd)
			throws InterruptedException, IOException, FileNotFoundException {

		
		for (int j = 0; j < times.length; j++) {
			List<RoundResult[]> roundResults = new ArrayList<RoundResult[]>();
			FileOperation.saveCVSFile(destFile, times[j] + " times ");
			log.debug(times[j] + " times");

			for (String file : files) {

				RoundResult[] roundResultArr = new RoundResult[iEnd - iStart + 1];

				for (int k = iStart; k <= iEnd; k++) {

					FileOperation fo = IOUtil.getProblemInfoByEdgePair(path + file);
					List<String[]> am = fo.getAdjacencyMatrix();

					int finalSize = Integer.MAX_VALUE;
				
					long finalRunningtime = -1;

					for (int i = 0; i < times[j]; i++) {
						Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

						GreedyIterative ag = new GreedyIterative(this.getClass().getName(),am);
						ag.run();

						List<Integer> ds = ag.getDominatingSet();
						Assert.assertTrue(AlgorithmUtil.isDS(g, ds));

						int dsSize = ds.size();

						if (dsSize < finalSize) {
							finalSize = dsSize;
							
							finalRunningtime = ag.getRunningTime();
						}

					}
					

					roundResultArr[k - iStart] = new RoundResult(finalSize, finalRunningtime,file,k);

				}

				roundResults.add(roundResultArr);
			}

			for (RoundResult[] rrArr : roundResults) {
				StringBuffer sb = new StringBuffer();
				for (int k=iStart; k<=iEnd;k++) {
					RoundResult rr=rrArr[k-iStart];
					if(k-iStart==0){
						sb.append(rr.getFile()).append(",");
					}
					sb.append(rr.getSize()).append(",").append(rr.getRunningTime()).append(",");
				}
				log.debug(sb.toString());
				FileOperation.saveCVSFile(destFile, sb.toString());
			}
		}

	}

}


