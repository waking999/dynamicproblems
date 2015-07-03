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

public class GreedyDDSTestDIMACSGC {
	private Logger log = LogUtil.getLogger(GreedyDDSTestDIMACSGC.class);
 @Ignore
	@Test
	public void testDIMACS() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-DIMAC.csv";

		String path = "src/test/resources/DIMACS/";
		String[] files = { //"C1000.9.clq",
				//"C125.9.clq",
				"C2000.5.clq",
				//"C2000.9.clq",
				//"C250.9.clq",
				"C4000.5.clq",
				//"C500.9.clq",
				"DSJC1000.5.clq",
				"DSJC500.5.clq",
				//"MANN_a27.clq",
				//"MANN_a81.clq",
				"brock200_2.clq",
				"brock200_4.clq",
				"brock400_2.clq",
				"brock400_4.clq",
				"brock800_2.clq",
				"brock800_4.clq",
				//"gen200_p0.9_44.clq",
				//"gen200_p0.9_55.clq",
				//"gen400_p0.9_55.clq",
				//"gen400_p0.9_65.clq",
				//"gen400_p0.9_75.clq",
				//"hamming10-4.clq",
				//"hamming8-4.clq",
				//"keller4.clq",
				//"keller5.clq",
				"keller6.clq"
				//"p_hat1500-1.clq",
//				"p_hat1500-2.clq",
//				"p_hat1500-3.clq",
//				"p_hat300-1.clq",
//				"p_hat300-2.clq",
//				"p_hat300-3.clq",
//				"p_hat700-1.clq",
//				"p_hat700-2.clq",
//				"p_hat700-3.clq"

		};
		//int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };
		int[][] krArray = { { 30,30}, { 40,40 } };
		for (String file : files) {
			log.debug("------------------"+file);
			runGreedyDDSKR(path + file, krArray, destFile);
		}
	}
 
 @Test
	public void testBHOSLIB() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-BHOSLIB.csv";

		String path = "src/test/resources/BHOSLIB/";
		String[] files = { //"frb30-15-mis/frb30-15-1.mis",
				"frb30-15-mis/frb30-15-2.mis",
//				"frb30-15-mis/frb30-15-3.mis",
//				"frb30-15-mis/frb30-15-4.mis",
//				"frb30-15-mis/frb30-15-5.mis",
//				"frb35-17-mis/frb35-17-1.mis",
				"frb35-17-mis/frb35-17-2.mis",
				"frb35-17-mis/frb35-17-3.mis",
				"frb35-17-mis/frb35-17-4.mis",
//				"frb35-17-mis/frb35-17-5.mis",
//				"frb40-19-mis/frb40-19-1.mis",
				"frb40-19-mis/frb40-19-2.mis",
				"frb40-19-mis/frb40-19-3.mis",
//				"frb40-19-mis/frb40-19-4.mis",
//				"frb40-19-mis/frb40-19-5.mis",
//				"frb45-21-mis/frb45-21-1.mis",
//				"frb45-21-mis/frb45-21-2.mis",
				"frb45-21-mis/frb45-21-3.mis",
				//"frb45-21-mis/frb45-21-4.mis",
				"frb45-21-mis/frb45-21-5.mis",
				"frb53-24-mis/frb53-24-1.mis",
//				"frb53-24-mis/frb53-24-2.mis",
//				"frb53-24-mis/frb53-24-3.mis",
				"frb53-24-mis/frb53-24-4.mis",
				//"frb53-24-mis/frb53-24-5.mis",
				"frb56-25-mis/frb56-25-1.mis",
				"frb56-25-mis/frb56-25-2.mis",
				"frb56-25-mis/frb56-25-3.mis",
				//"frb56-25-mis/frb56-25-4.mis",
				"frb56-25-mis/frb56-25-5.mis",
				//"frb59-26-mis/frb59-26-1.mis",
				"frb59-26-mis/frb59-26-2.mis",
				//"frb59-26-mis/frb59-26-3.mis",
				"frb59-26-mis/frb59-26-4.mis",
				//"frb59-26-mis/frb59-26-5.mis"
		};
		//int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };
		int[][] krArray = { { 30,30}, { 40,40 } };
		for (String file : files) {
			log.debug("------------------"+file);
			runGreedyDDSKR(path + file, krArray, destFile);
		}
	}

	@Ignore
	@Test
	public void test_fpsol() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-fpsol.csv";

		log.debug("------------------fpsol2i1");
		FileOperation.saveCVSFile(destFile, "------------------fpsol2i1");
		for (int i = 1; i <= 1; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_fpsol2i1(destFile);

		}
		log.debug("------------------fpsol2i2");
		FileOperation.saveCVSFile(destFile, "------------------fpsol2i2");
		for (int i = 1; i <= 1; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_fpsol2i2(destFile);

		}

		log.debug("------------------fpsol2i3");
		FileOperation.saveCVSFile(destFile, "------------------fpsol2i3");
		for (int i = 1; i <= 1; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_fpsol2i3(destFile);

		}
	}

	private void test_fpsol2i1(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS-GC/fpsol2.i.1.col";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_fpsol2i2(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS-GC/fpsol2.i.2.col";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_fpsol2i3(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS-GC/fpsol2.i.3.col";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void runGreedyDDSKR(String inputFile, int[][] krArray,
			String destFile) throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();
		
		if (destFile != null) {
			FileOperation.saveCVSFile(destFile, "--------"+inputFile);
		}

		for (int[] kr : krArray) {
			int k = kr[0];
			int rUpper = kr[1];

			//for (int r = 1; r <= rUpper; r++) {

				GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, rUpper);

				Result result = null;

				ag.computing();

				List<Integer> ds = ag.getDs();
				Assert.assertTrue(AlgorithmUtil.isDS(
						AlgorithmUtil.prepareGraph(am), ds));
				result = ag.getResult(rUpper);

				log.debug(result.getString());
				if (destFile != null) {
					FileOperation.saveCVSFile(destFile, result.getString());
				}
			//}
		}
	}

}
