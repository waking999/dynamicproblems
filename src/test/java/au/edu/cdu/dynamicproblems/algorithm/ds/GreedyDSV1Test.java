package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import junit.framework.Assert;

public class GreedyDSV1Test {
	private Logger log = LogUtil.getLogger(GreedyDSV1Test.class);

	@Ignore
	@Test
	public void test0() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException,
			InterruptedException, IOException {
		List<String[]> am = new ArrayList<String[]>();
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "1", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "1", "0", "1", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "1", "0", "0", "1", "1", "1", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "1", "0", "0", "0", "1", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "1", "0", "0", "0", "1", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "1", "1", "0", "1", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "1", "1", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "1", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "0", "1", "1" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "1" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "0" });

		int[][] krArray = { { 5, 5 } };
		for (int[] kr : krArray) {
			int k = kr[0];
			int rUpper = kr[1];
			int r = rUpper;
			// for (int r = 1; r <= rUpper; r++) {

			GreedyDSV1 ag = new GreedyDSV1(this.getClass().getName(), am, k, r);

			Result result = null;

			ag.computing();

			List<Integer> ds = ag.getDs();
			Assert.assertTrue(AlgorithmUtil.isDS(AlgorithmUtil.prepareGraph(am), ds));
			result = ag.getResult(r);

			log.debug(result.getString());

			// }
		}
	}

	// @Ignore
	@Test
	public void testKONECT() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException,
			InterruptedException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destDir = "out/KONECT-" + this.getClass().getSimpleName();

		String destFile = destDir + "-" + timeStamp + ".csv";

		String path = "src/test/resources/KONECT/";
		String[] files = {  //"000027_zebra.konet", "000034_zachary.konet",
							// "000062_dolphins.konet",
				 "000112_David_Copperfield.konet",
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

		int[][] krArray = { { 5, 5 }, { 10, 10 },{15,15},{20,20}};

		runStrategies(path, krArray, files, destFile, 1, 1);

	}

	private void runStrategies(String path, int[][] krArray, String[] files, String destFile, int iStart, int iEnd)
			throws FileNotFoundException, IOException, MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, InterruptedException {

		log.debug(destFile);

		for (String file : files) {
			for (int i = iStart; i <= iEnd; i++) {

				String msg;

				msg = setMessage(file, i);
				// runV1(msg, path + file, krArray, destFile);

				run(msg, path + file, krArray, destFile);

			}
		}
	}

	private String setMessage(String file, int i) {
		String msg;
		msg = file + "-i=" + i;
		return msg;
	}

	@Ignore
	@Test
	public void testDIMACS() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException, IOException,
			InterruptedException, InterruptedException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destDir = "out/DIMACS-" + this.getClass().getSimpleName();

		String destFile = destDir + "-" + timeStamp + ".csv";

		String path = "src/test/resources/DIMACS/";
		String[] files = { "C1000.9.clq", "C125.9.clq", "C2000.5.clq", "C2000.9.clq", "C250.9.clq", "C4000.5.clq",
				"C500.9.clq", "DSJC1000.5.clq", "DSJC500.5.clq", "MANN_a27.clq", "MANN_a81.clq", "brock200_2.clq",
				"brock200_4.clq", "brock400_2.clq", "brock400_4.clq", "brock800_2.clq", "brock800_4.clq",
				"gen200_p0.9_44.clq", "gen200_p0.9_55.clq", "gen400_p0.9_55.clq", "gen400_p0.9_65.clq",
				"gen400_p0.9_75.clq", "hamming10-4.clq", "hamming8-4.clq", "keller4.clq", "keller5.clq", "keller6.clq",
				"p_hat1500-1.clq", "p_hat1500-2.clq", "p_hat1500-3.clq", "p_hat300-1.clq", "p_hat300-2.clq",
				"p_hat300-3.clq", "p_hat700-1.clq", "p_hat700-2.clq", "p_hat700-3.clq"

		};
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 15, 15 }, { 20, 20 } };

		runStrategies(path, krArray, files, destFile, 1, 1);

	}

	@Ignore
	@Test
	public void testBHOSLIB() throws MOutofNException, ExceedLongMaxException, ArraysNotSameLengthException,
			IOException, InterruptedException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destDir = "out/BHOSLIB-" + this.getClass().getSimpleName();

		String destFile = destDir + "-" + timeStamp + ".csv";

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
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 15, 15 }, { 20, 20 } };

		runStrategies(path, krArray, files, destFile, 1, 1);

	}

	private void run(String msg, String inputFile, int[][] krArray, String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, InterruptedException, IOException {
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();

		log.debug(msg);
		FileOperation.saveCVSFile(destFile, msg);

		for (int[] kr : krArray) {
			int k = kr[0];
			int rUpper = kr[1];
			int r = rUpper;
			// for (int r = 1; r <= rUpper; r++) {

			GreedyDSV1 ag = new GreedyDSV1(this.getClass().getName(), am, k, r);

			Result result = null;

			ag.computing();

			List<Integer> ds = ag.getDs();
			Assert.assertTrue(AlgorithmUtil.isDS(AlgorithmUtil.prepareGraph(am), ds));
			result = ag.getResult(r);

			log.debug(result.getString());
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, result.getString());
			}
			// }
		}
	}

	
}