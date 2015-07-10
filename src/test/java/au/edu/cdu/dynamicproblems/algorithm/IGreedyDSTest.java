package au.edu.cdu.dynamicproblems.algorithm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;

public class IGreedyDSTest {
	Logger log = LogUtil.getLogger(IGreedyDSTest.class);
	private final static String COMMA = ",";

	// @Ignore
	@Test
	public void testKONET() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar
				.getInstance().getTime());
		String destFile = "out/output-GreedyDSComplete-KONECT-" + timeStamp
				+ ".csv";

		String path = "src/test/resources/KONECT/";
		String[] files = { "000027_zebra.konet",  "000034_zachary.konet",
		 "000062_dolphins.konet", "000112_David_Copperfield.konet",
		 "000198_Jazz_musicians.konet", "000212_pdzbase.konet",
		 "001133_rovira.konet", "001174_euroroad.konet",
		 "001858_hamster.konet"
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
			// log.debug("------------------" + file);
			Map<String, List<RunningResult>> resultMap = new HashMap<String, List<RunningResult>>();

			for (int i = 1; i <= 1; i++) {

				String indicator1 = "GreedyDSComplete:" + file;
				IGreedyDS ag1 = new GreedyDSComplete(indicator1);

				runGreedyDS(ag1, path + file, krArray, destFile, resultMap);

				String indicator2 = "GreedyDSRegret:" + file;
				IGreedyDS ag2 = new GreedyDSRegret(indicator2);

				runGreedyDS(ag2, path + file, krArray, destFile, resultMap);

				String indicator3 = "GreedyDSRegretReduction:" + file;
				IGreedyDS ag3 = new GreedyDSRegretReduction(indicator3);

				runGreedyDS(ag3, path + file, krArray, destFile, resultMap);

				printRunningResult(resultMap, file, i, destFile);
			}
		}
	}

	private void printRunningResult(Map<String, List<RunningResult>> resultMap,String file,
			int index, String destFile) throws IOException{
		String msg=index + "--------"+file;
		log.debug(msg);
		if (destFile != null) {
			FileOperation.saveCVSFile(destFile, msg);
		}
		List<StringBuffer> printArray = new ArrayList<StringBuffer>();
		
		Map<String,RunningResult> minRunningResult=new HashMap<String,RunningResult>();
		int minSize=Integer.MAX_VALUE;
		int minK=Integer.MAX_VALUE;
		int minR=Integer.MAX_VALUE;
		long minNanoSec=Long.MAX_VALUE;

		Set<String> keySet = resultMap.keySet();
		for (String key : keySet) {
			RunningResult minRR= new RunningResult(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Long.MAX_VALUE);
			minRunningResult.put(key,minRR);
			
			List<RunningResult> rrList = resultMap.get(key);
			int rrListSize = rrList.size();
			for (int i = 0; i < rrListSize; i++) {
				StringBuffer rowSB;
				if(i>=printArray.size()){
					rowSB = new StringBuffer();
					printArray.add(i, rowSB);
				}else{
					rowSB= printArray.get(i);
					printArray.set(i, rowSB);
				}
					
				

				RunningResult rr = rrList.get(i);

				rowSB.append(rr.getSize()).append(COMMA).append(rr.getK())
						.append(COMMA).append(rr.getR()).append(COMMA)
						.append(rr.getNanoSec()).append(COMMA);
				
				if(minRR.getSize()>=rr.getSize()){
					minRR.setSize(rr.getSize());
					if(minRR.getNanoSec()>=rr.getNanoSec()){
						minRR.setNanoSec(rr.getNanoSec());
						minRR.setK(rr.getK());
						minRR.setR(rr.getR());
					}
				}
			}

		}

		int i=printArray.size();
		for (String key : keySet) {
			RunningResult minRR= minRunningResult.get(key);
			StringBuffer rowSB = new StringBuffer();
			if(i>=printArray.size()){
				rowSB = new StringBuffer();
				printArray.add(i, rowSB);
			}else{
				rowSB= printArray.get(i);
				printArray.set(i, rowSB);
			}
			rowSB.append(minRR.getSize()).append(COMMA).append(minRR.getK())
			.append(COMMA).append(minRR.getR()).append(COMMA)
			.append(minRR.getNanoSec()).append(COMMA);
			
			if(minSize>=minRR.getSize()){
				minSize=minRR.getSize();
				if(minNanoSec>=minRR.getNanoSec()){
					minNanoSec=minRR.getNanoSec();
					minK=minRR.getK();
					minR=minRR.getR();
				}
			}
			
		}		
		
		StringBuffer rowSBEnd = new StringBuffer();
		rowSBEnd.append(minSize).append(COMMA).append(minK)
		.append(COMMA).append(minR).append(COMMA)
		.append(minNanoSec);
		printArray.add( rowSBEnd);
		
		for (StringBuffer rowSB : printArray) {
			log.debug(rowSB);
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, rowSB.toString());
			}
		}
		
		
	}

	//
	private void runGreedyDS(IGreedyDS ag, String inputFile, int[][] krArray,
			String destFile, Map<String, List<RunningResult>> resultMap)
			throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();
		ag.setAm(am);

		// if (destFile != null) {
		// FileOperation.saveCVSFile(destFile, "--------" + inputFile);
		// }

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

				// GreedyDSComplete ag = new GreedyDSComplete("GreedyDDSTest",
				// am, k, r);

				ag.setR(r);
				// Result result = null;

				ag.computing();

				List<Integer> ds = ag.getDs();
				Assert.assertTrue(AlgorithmUtil.isDS(
						AlgorithmUtil.prepareGraph(am), ds));
				// result = ag.getResult(r);

				RunningResult rr = new RunningResult(ds.size(), k, r,
						ag.getRunningTime());
				rrList.add(rr);
				// log.debug(result.getString());
				// if (destFile != null) {
				// FileOperation.saveCVSFile(destFile, result.getString());
				// }
			}
		}

		resultMap.put(indicator, rrList);
	}

}