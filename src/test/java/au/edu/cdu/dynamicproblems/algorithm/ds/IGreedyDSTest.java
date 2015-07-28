package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.RunningResult;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;
import junit.framework.Assert;

public class IGreedyDSTest {
	Logger log = LogUtil.getLogger(IGreedyDSTest.class);
	private final static String COMMA = ",";

	@Ignore
	@Test
	public void testKONECT()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destFile = "out/output-" + this.getClass().getName() + "-KONECT-" + timeStamp + ".csv";

		String path = "src/test/resources/KONECT/";
		String[] files = {"000027_zebra.konet", 
				"000034_zachary.konet", "000062_dolphins.konet",
				"000112_David_Copperfield.konet", 
				"000198_Jazz_musicians.konet",
				"000212_pdzbase.konet",
				"001133_rovira.konet", "001174_euroroad.konet", "001858_hamster.konet"
				// "002426_hamster_ful.konet",
				// "002888_facebook.konet",
				// "003133_Human_protein_Vidal.konet",
				// "004941_powergrid.konet",
				// "006327_reactome.konet",
				// "010680_Pretty_Good_Privacy.konet",
				// "06474_Route_views.konet"
		};

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		for (String file : files) {

			runSeveralAlgorithmsSeveralTimes(1, 1, destFile, path, krArray, file);
		}
	}

	private void runSeveralAlgorithmsSeveralTimes(int iStart, int iEnd, String destFile, String path, int[][] krArray,
			String file) throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		for (int i = iStart; i <= iEnd; i++) {
			Map<String, List<RunningResult>> resultMap = new HashMap<String, List<RunningResult>>();

//			String indicator1 = this.getClass().getName() + "-" + GreedyDSReduction.STRATEGY_DEGREE_DESC + file;
//			IGreedyDS ag1 = new GreedyDSReduction(indicator1, GreedyDSReduction.STRATEGY_DEGREE_DESC);
//			run(ag1, path + file, krArray, destFile, resultMap);
//
//			String indicator2 = this.getClass().getName() + "-" + GreedyDSReduction.STRATEGY_DEGREE_ASC + file;
//			IGreedyDS ag2 = new GreedyDSReduction(indicator2, GreedyDSReduction.STRATEGY_DEGREE_ASC);
//			run(ag2, path + file, krArray, destFile, resultMap);

			String indicator1 = this.getClass().getName() + "-" + GreedyDSReduction.STRATEGY_UTILITY_DESC + file;
			IGreedyDS ag1 = new GreedyDSReduction(indicator1, GreedyDSReduction.STRATEGY_UTILITY_DESC);
			run(ag1, path + file, krArray, destFile, resultMap);

			String indicator2 = this.getClass().getName() + "-" + GreedyDSReduction.STRATEGY_UTILITY_ASC + file;
			IGreedyDS ag2 = new GreedyDSReduction(indicator2, GreedyDSReduction.STRATEGY_UTILITY_ASC);
			run(ag2, path + file, krArray, destFile, resultMap);
			

			printRunningResult(resultMap, file, i, destFile);
		}
	}

	@Ignore
	@Test
	public void testDIMACS()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destFile = "out/output-" + this.getClass().getName() + "-DIMACS-" + timeStamp + ".csv";

		String path = "src/test/resources/DIMACS/";
		String[] files = { "C1000.9.clq", "C125.9.clq", "C2000.5.clq", "C2000.9.clq", "C250.9.clq", "C4000.5.clq",
				"C500.9.clq", "DSJC1000.5.clq", "DSJC500.5.clq", "MANN_a27.clq", "MANN_a81.clq", "brock200_2.clq",
				"brock200_4.clq", "brock400_2.clq", "brock400_4.clq", "brock800_2.clq", "brock800_4.clq",
				"gen200_p0.9_44.clq", "gen200_p0.9_55.clq", "gen400_p0.9_55.clq", "gen400_p0.9_65.clq",
				"gen400_p0.9_75.clq", "hamming10-4.clq", "hamming8-4.clq", "keller4.clq", "keller5.clq", "keller6.clq",
				"p_hat1500-1.clq", "p_hat1500-2.clq", "p_hat1500-3.clq", "p_hat300-1.clq", "p_hat300-2.clq",
				"p_hat300-3.clq", "p_hat700-1.clq", "p_hat700-2.clq", "p_hat700-3.clq"

		};

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		for (String file : files) {

			runSeveralAlgorithmsSeveralTimes(1, 1, destFile, path, krArray, file);
		}
	}

	//@Ignore
	@Test
	public void testBHOSLIB()
			throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destFile = "out/output-" + this.getClass().getName() + "-BHOSLIB-" + timeStamp + ".csv";

		String path = "src/test/resources/BHOSLIB/";
		String[] files = { "frb30-15-mis/frb30-15-1.mis", "frb30-15-mis/frb30-15-2.mis", "frb30-15-mis/frb30-15-3.mis",
				"frb30-15-mis/frb30-15-4.mis", "frb30-15-mis/frb30-15-5.mis", "frb35-17-mis/frb35-17-1.mis",
				"frb35-17-mis/frb35-17-2.mis", "frb35-17-mis/frb35-17-3.mis", "frb35-17-mis/frb35-17-4.mis",
				"frb35-17-mis/frb35-17-5.mis", "frb40-19-mis/frb40-19-1.mis", "frb40-19-mis/frb40-19-2.mis",
				"frb40-19-mis/frb40-19-3.mis", "frb40-19-mis/frb40-19-4.mis", "frb40-19-mis/frb40-19-5.mis",
				"frb45-21-mis/frb45-21-1.mis", "frb45-21-mis/frb45-21-2.mis", "frb45-21-mis/frb45-21-3.mis",
				"frb45-21-mis/frb45-21-4.mis", "frb45-21-mis/frb45-21-5.mis", "frb53-24-mis/frb53-24-1.mis",
				"frb53-24-mis/frb53-24-2.mis", "frb53-24-mis/frb53-24-3.mis", "frb53-24-mis/frb53-24-4.mis",
				"frb53-24-mis/frb53-24-5.mis", "frb56-25-mis/frb56-25-1.mis", "frb56-25-mis/frb56-25-2.mis",
				"frb56-25-mis/frb56-25-3.mis", "frb56-25-mis/frb56-25-4.mis", "frb56-25-mis/frb56-25-5.mis",
				"frb59-26-mis/frb59-26-1.mis", "frb59-26-mis/frb59-26-2.mis", "frb59-26-mis/frb59-26-3.mis",
				"frb59-26-mis/frb59-26-4.mis", "frb59-26-mis/frb59-26-5.mis" };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		for (String file : files) {

			runSeveralAlgorithmsSeveralTimes(1, 1, destFile, path, krArray, file);
		}
	}

	private void printRunningResult(Map<String, List<RunningResult>> resultMap, String file, int index, String destFile)
			throws IOException {
		String msg = index + "--------" + file;
		log.debug(msg);
		if (destFile != null) {
			FileOperation.saveCVSFile(destFile, msg);
		}
		List<StringBuffer> printArray = new ArrayList<StringBuffer>();

		//Map<String, RunningResult> minRunningResult = new HashMap<String, RunningResult>();
//		int minSize = Integer.MAX_VALUE;
//		int minK = Integer.MAX_VALUE;
//		int minR = Integer.MAX_VALUE;
//		long minNanoSec = Long.MAX_VALUE;

		Set<String> keySet = resultMap.keySet();
		for (String key : keySet) {
			//RunningResult minRR = new RunningResult(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,				Long.MAX_VALUE);
			//minRunningResult.put(key, minRR);

			List<RunningResult> rrList = resultMap.get(key);
			int rrListSize = rrList.size();
			for (int i = 0; i < rrListSize; i++) {
				StringBuffer rowSB;
				if (i >= printArray.size()) {
					rowSB = new StringBuffer();
					printArray.add(i, rowSB);
				} else {
					rowSB = printArray.get(i);
					printArray.set(i, rowSB);
				}

				RunningResult rr = rrList.get(i);

				rowSB.append(rr.getSize()).append(COMMA).append(rr.getK()).append(COMMA).append(rr.getR()).append(COMMA)
						.append(rr.getNanoSec()).append(COMMA);

				rr=null;
//				if (minRR.getSize() >= rr.getSize()) {
//					minRR.setSize(rr.getSize());
//					if (minRR.getNanoSec() >= rr.getNanoSec()) {
//						minRR.setNanoSec(rr.getNanoSec());
//						minRR.setK(rr.getK());
//						minRR.setR(rr.getR());
//					}
//				}
			}

		}

//		int i = printArray.size();
//		for (String key : keySet) {
//			RunningResult minRR = minRunningResult.get(key);
//			StringBuffer rowSB = new StringBuffer();
//			if (i >= printArray.size()) {
//				rowSB = new StringBuffer();
//				printArray.add(i, rowSB);
//			} else {
//				rowSB = printArray.get(i);
//				printArray.set(i, rowSB);
//			}
//			rowSB.append(minRR.getSize()).append(COMMA).append(minRR.getK()).append(COMMA).append(minRR.getR())
//					.append(COMMA).append(minRR.getNanoSec()).append(COMMA);
//
//			if (minSize >= minRR.getSize()) {
//				minSize = minRR.getSize();
//				if (minNanoSec >= minRR.getNanoSec()) {
//					minNanoSec = minRR.getNanoSec();
//					minK = minRR.getK();
//					minR = minRR.getR();
//				}
//			}
//
//		}

//		StringBuffer rowSBEnd = new StringBuffer();
//		rowSBEnd.append(minSize).append(COMMA).append(minK).append(COMMA).append(minR).append(COMMA).append(minNanoSec);
//		printArray.add(rowSBEnd);
//
		for (StringBuffer rowSB : printArray) {
			log.debug(rowSB);
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, rowSB.toString());
			}
			rowSB=null;
		}
		
		printArray=null;

	}

	private void run(IGreedyDS ag, String inputFile, int[][] krArray, String destFile,
			Map<String, List<RunningResult>> resultMap)
					throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();
		ag.setAm(am);

		String indicator = ag.getIndicator();

		List<RunningResult> rrList = resultMap.get(indicator);
		if (rrList == null) {
			rrList = new ArrayList<RunningResult>();
		}

		for (int[] kr : krArray) {
			int k = kr[0];
			ag.setK(k);
			int rUpper = kr[1];

			for (int r = 1; r <= rUpper; r++) {

				ag.setR(r);

				ag.computing();

				List<Integer> ds = ag.getDs();
				Graph<Integer,Integer> g=AlgorithmUtil.prepareGraph(am);
				Assert.assertTrue(AlgorithmUtil.isDS(g, ds));

				RunningResult rr = new RunningResult(ds.size(), k, r, ag.getRunningTime());
				rrList.add(rr);
			}
		}

		resultMap.put(indicator, rrList);
	}

}
