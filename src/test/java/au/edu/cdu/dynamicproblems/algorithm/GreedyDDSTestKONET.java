package au.edu.cdu.dynamicproblems.algorithm;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;

public class GreedyDDSTestKONET {
	private Logger log = LogUtil.getLogger(GreedyDDSTestKONET.class);

	@Test
	public void test_konet() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-KONECT.csv";

		// log.debug("------------------zebra");
		// FileOperation.saveCVSFile(destFile, "------------------zebra");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_zebra(destFile);
		//
		// }
		// log.debug("------------------zachary");
		// FileOperation.saveCVSFile(destFile, "------------------zachary");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_zachary(destFile);
		//
		// }
		// log.debug("------------------dolphins");
		// FileOperation.saveCVSFile(destFile, "------------------dolphins");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_dolphins(destFile);
		//
		// }
		// log.debug("------------------david");
		// FileOperation.saveCVSFile(destFile, "------------------david");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_david(destFile);
		//
		// }
		// log.debug("------------------jazz");
		// FileOperation.saveCVSFile(destFile, "------------------jazz");
		// for (int i = 1; i <= 1; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_jazz(destFile);
		//
		// }
		 log.debug("------------------pdzbase");
		 FileOperation.saveCVSFile(destFile, "------------------pdzbase");
		 for (int i = 1; i <= 1; i++) {
		 log.debug("------------------" + i);
		 FileOperation.saveCVSFile(destFile, "------------------" + i);
		 test_pdzbase(destFile);
		
		 }
//		log.debug("------------------rovira");
//		FileOperation.saveCVSFile(destFile, "------------------rovira");
//		for (int i = 1; i <= 1; i++) {
//			log.debug("------------------" + i);
//			FileOperation.saveCVSFile(destFile, "------------------" + i);
//			test_rovira(destFile);
//
//		}
		log.debug("------------------euroroad");
		FileOperation.saveCVSFile(destFile, "------------------euroroad");
		for (int i = 1; i <= 1; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_euroroad(destFile);

		}
//		log.debug("------------------facebook");
//		FileOperation.saveCVSFile(destFile, "------------------facebook");
//		for (int i = 1; i <= 1; i++) {
//			log.debug("------------------" + i);
//			FileOperation.saveCVSFile(destFile, "------------------" + i);
//			test_facebook(destFile);
//
//		}
//		log.debug("------------------vidal");
//		FileOperation.saveCVSFile(destFile, "------------------vidal");
//		for (int i = 1; i <= 1; i++) {
//			log.debug("------------------" + i);
//			FileOperation.saveCVSFile(destFile, "------------------" + i);
//			test_vidal(destFile);
//
//		}
		log.debug("------------------powergrid");
		FileOperation.saveCVSFile(destFile, "------------------powergrid");
		for (int i = 1; i <= 1; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_powergrid(destFile);

		}

	}

	@SuppressWarnings("unused")
	private void test_zebra(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/000027_zebra.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_zachary(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/000034_zachary.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_dolphins(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/000062_dolphins.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_david(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/000112_David_Copperfield.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_jazz(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/000198_Jazz_musicians.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_pdzbase(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/000212_pdzbase.konet";

		// int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };
		int[][] krArray = { { 30, 30 }, { 40, 40 } };
		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_rovira(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/001133_rovira.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_euroroad(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/001174_euroroad.konet";

		//int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };
		int[][] krArray = { { 30, 30 }, { 40, 40 } };
		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_facebook(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/002888_facebook.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_vidal(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/003133_Human_protein_Vidal.konet";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_powergrid(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/KONET/004941_powergrid.konet";

		int[][] krArray = { { 30, 30 }, { 40, 40 }, { 50, 50 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void runGreedyDDSKR(String inputFile, int[][] krArray,
			String destFile) throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();

		for (int[] kr : krArray) {
			int k = kr[0];
			int rUpper = kr[1];

			for (int r = 1; r <= rUpper; r++) {

				GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);

				Result result = null;

				ag.computing();

				List<Integer> ds = ag.getDs();
				Assert.assertTrue(AlgorithmUtil.isDS(
						AlgorithmUtil.prepareGraph(am), ds));
				result = ag.getResult(r);

				log.debug(result.getString());
				if (destFile != null) {
					FileOperation.saveCVSFile(destFile, result.getString());
				}
			}
		}
	}

}
