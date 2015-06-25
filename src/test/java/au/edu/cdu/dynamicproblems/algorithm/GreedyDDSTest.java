package au.edu.cdu.dynamicproblems.algorithm;

import java.io.IOException;
import java.util.ArrayList;
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

public class GreedyDDSTest {
	private Logger log = LogUtil.getLogger(GreedyDDSTest.class);

	@Ignore
	@Test
	public void test1_smallk() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		List<String[]> am = new ArrayList<String[]>();
		am.add(new String[] { "0", "1", "0", "1" });
		am.add(new String[] { "1", "0", "1", "0" });
		am.add(new String[] { "0", "1", "0", "0" });
		am.add(new String[] { "1", "0", "0", "0" });

		int k = 1;
		int r = 1;
		log.debug("k=1--------------------");
		GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		Result result = null;

		ag.computing();

		result = ag.getResult(r);

		log.debug(result.getString());
	}

	@Ignore
	@Test
	public void test1_middlek() throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException {
		List<String[]> am = new ArrayList<String[]>();
		am.add(new String[] { "0", "1", "0", "1" });
		am.add(new String[] { "1", "0", "1", "0" });
		am.add(new String[] { "0", "1", "0", "0" });
		am.add(new String[] { "1", "0", "0", "0" });

		int k = 2;
		int r = 1;
		log.debug("k=2--------------------");
		GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		Result result = null;

		ag.computing();

		result = ag.getResult(r);

		log.debug(result.getString());
	}

	@Ignore
	@Test
	public void test1_bigk() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {
		List<String[]> am = new ArrayList<String[]>();
		am.add(new String[] { "0", "1", "0", "1" });
		am.add(new String[] { "1", "0", "1", "0" });
		am.add(new String[] { "0", "1", "0", "0" });
		am.add(new String[] { "1", "0", "0", "0" });

		int k = 3;
		int r = 1;
		log.debug("k=3--------------------");
		GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		Result result = null;

		ag.computing();

		result = ag.getResult(r);

		log.debug(result.getString());
	}

	@Ignore
	@Test
	public void test2() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String inputFile = "src/test/resources/edge-pair-hamster-friendship.csv";

		// FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		// List<String[]> am = fo.getAdjacencyMatrix();
		// int[][] krArray = {{50,10},{100,10},{200,20}};
		int[][] krArray = { { 500, 5 }, { 300, 20 }, { 400, 20 } };

		runGreedyDDSKR(inputFile, krArray, null);

		// for (int[] kr : krArray) {
		// int k = kr[0];
		// int r = kr[1];
		// log.debug(inputFile + " k=" + k + ",r=" + r
		// + "---------------------");
		//
		// GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		//
		// Result result = null;
		//
		// ag.computing();
		//
		// List<Integer> ds = ag.getDs();
		// Assert.assertTrue(AlgorithmUtil.isDS(
		// AlgorithmUtil.prepareGraph(am), ds));
		// result = ag.getResult(r);
		//
		// log.debug(result.getString());
		// }

	}

	@Ignore
	@Test
	public void test4() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		// String inputFile1 = "src/test/resources/edge-pair-pdzbase.csv";
		String inputFile = "src/test/resources/edge-pair-hamster-friendship.csv";
		// String inputFile1 = "src/test/resources/edge-pair-maayan-vidal.csv";
		// FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		// List<String[]> am = fo.getAdjacencyMatrix();

		// int[][] krArray = { { 5, 5 }, { 10, 10 }, { 15, 15 }, { 20, 20 },
		// { 25, 25 } };

		int[][] krArray = { { 20, 20 }, { 25, 25 }, { 30, 30 }, { 35, 35 },
				{ 40, 40 } };
		runGreedyDDSKR(inputFile, krArray, null);

		// for (int[] kr : krArray) {
		// int k = kr[0];
		// int rUpper = kr[1];
		//
		// for (int r = 1; r <= rUpper; r++) {
		// log.debug(inputFile + "  k=" + k + ",r=" + r
		// + "---------------------");
		//
		// GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		//
		// Result result = null;
		//
		// ag.computing();
		//
		// List<Integer> ds = ag.getDs();
		// Assert.assertTrue(AlgorithmUtil.isDS(
		// AlgorithmUtil.prepareGraph(am), ds));
		// result = ag.getResult(r);
		//
		// log.debug(result.getString());
		// }
		// }

	}

	@Ignore
	@Test
	public void test_hat() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-hat.csv";

		log.debug("------------------hat300-1");
		FileOperation.saveCVSFile(destFile, "------------------hat300-1");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_hat300_1(destFile);

		}

		log.debug("------------------hat300-2");
		FileOperation.saveCVSFile(destFile, "------------------hat300-2");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_hat300_2(destFile);

		}

		log.debug("------------------hat300-3");
		FileOperation.saveCVSFile(destFile, "------------------hat300-3");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_hat300_3(destFile);

		}

		log.debug("------------------hat700-1");
		FileOperation.saveCVSFile(destFile, "------------------hat700-1");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_hat700_1(destFile);

		}

		log.debug("------------------hat700-2");
		FileOperation.saveCVSFile(destFile, "------------------hat700-2");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_hat700_2(destFile);

		}

		log.debug("------------------hat700-3");
		FileOperation.saveCVSFile(destFile, "------------------hat700-3");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_hat700_3(destFile);

		}
	}

	private void test_hat300_1(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/p_hat300-1.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_hat300_2(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/p_hat300-2.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_hat300_3(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/p_hat300-3.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_hat700_1(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/p_hat700-1.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_hat700_2(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/p_hat700-2.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_hat700_3(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/p_hat700-3.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@Ignore
	@Test
	public void test_DSJC() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-DSJC.csv";

		log.debug("------------------DSJC500");
		FileOperation.saveCVSFile(destFile, "------------------DSJC500");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_DSJC500(destFile);

		}

		log.debug("------------------DSJC1000");
		FileOperation.saveCVSFile(destFile, "------------------DSJC1000");
		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_DSJC1000(destFile);

		}
	}

	private void test_DSJC500(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/DSJC500.5.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_DSJC1000(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/DSJC1000.5.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@Ignore
	@Test
	public void test_C() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-C.csv";
		// log.debug("------------------C125");
		// FileOperation.saveCVSFile(destFile, "------------------C125");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_C125(destFile);
		//
		// }

		// log.debug("------------------C250");
		// FileOperation.saveCVSFile(destFile, "------------------C250");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_C250(destFile);
		//
		// }
		//
		// log.debug("------------------C500");
		// FileOperation.saveCVSFile(destFile, "------------------C500");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_C500(destFile);
		//
		// }
		//
		// log.debug("------------------C1000");
		// FileOperation.saveCVSFile(destFile, "------------------C1000");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_C1000(destFile);
		//
		// }
		//
		// log.debug("------------------C2000-5");
		// FileOperation.saveCVSFile(destFile, "------------------C2000-5");
		// for (int i = 1; i <= 1; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_C2000_5(destFile);
		//
		// }
		//
		// log.debug("------------------C2000-9");
		// FileOperation.saveCVSFile(destFile, "------------------C2000-9");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_C2000_9(destFile);
		//
		// }

		log.debug("------------------C4000");
		FileOperation.saveCVSFile(destFile, "------------------C4000");
		for (int i = 1; i <= 1; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_C4000(destFile);

		}
	}

	@SuppressWarnings("unused")
	private void test_C125(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C125.9.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 } };
		int[][] krArray = { { 5, 5 }, { 10, 10 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_C250(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C250.9.clq";

		int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 },
				{ 10, 10 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_C500(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C500.9.clq";

		int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 },
				{ 10, 10 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_C1000(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C1000.9.clq";

		int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 },
				{ 10, 10 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_C2000_5(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C2000.5.clq";

		int[][] krArray = { { 30, 30 } };
		// int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 }, { 30, 30 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_C2000_9(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C2000.9.clq";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_C4000(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/C4000.5.clq";

		int[][] krArray = { { 10, 10 }, { 20, 20 }, { 30, 30 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@Ignore
	@Test
	public void test_brock() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {
		String destFile = "out/output-brock.csv";
		// log.debug("------------------brock200_2");
		// FileOperation.saveCVSFile(destFile, "------------------brock200_2");
		// for (int i = 2; i <= 2; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_brock200_2(destFile);
		//
		// }
		// log.debug("------------------brock200_4");
		// FileOperation.saveCVSFile(destFile, "------------------brock200_4");
		// for (int i = 3; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_brock200_4(destFile);
		//
		// }

		// log.debug("------------------brock400_2");
		// FileOperation.saveCVSFile(destFile, "------------------brock400_2");
		// for (int i = 3; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_brock400_2(destFile);
		//
		// }
		// log.debug("------------------brock400_4");
		// FileOperation.saveCVSFile(destFile, "------------------brock400_4");
		// for (int i = 3; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_brock400_4(destFile);
		//
		// }

		log.debug("------------------brock800_2");
		FileOperation.saveCVSFile(destFile, "------------------brock800_2");

		for (int i = 2; i <= 10; i++) {
			log.debug("------------------" + i);
			FileOperation.saveCVSFile(destFile, "------------------" + i);
			test_brock800_2(destFile);

		}
		// log.debug("------------------brock800_4");
		// FileOperation.saveCVSFile(destFile, "------------------brock800_4");
		// for (int i = 2; i <= 10; i++) {
		// log.debug("------------------" + i);
		// FileOperation.saveCVSFile(destFile, "------------------" + i);
		// test_brock800_4(destFile);
		//
		// }

	}

	@SuppressWarnings("unused")
	private void test_brock200_2(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/brock200_2.clq";

		int[][] krArray = { { 5, 5 } };
		// int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_brock200_4(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/brock200_4.clq";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

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
				// log.debug(inputFile1 + "  k=" + k + ",r=" + r +
				// "---------------------");

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

	@SuppressWarnings("unused")
	private void test_brock400_2(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/brock400_2.clq";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_brock400_4(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/brock400_4.clq";

		int[][] krArray = { { 5, 5 }, { 10, 10 }, { 20, 20 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	private void test_brock800_2(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/brock800_2.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 },{3,3},{4,4} };
		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5
		// },{ 10, 10 }, { 20, 20 } };
		int[][] krArray = { { 30, 30 }, { 40, 40 }, { 50, 50 } };

		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@SuppressWarnings("unused")
	private void test_brock800_4(String destFile) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {

		String inputFile = "src/test/resources/DIMACS/brock800_4.clq";

		// int[][] krArray = { { 1, 1 }, { 2, 2 },{3,3},{4,4} };
		// int[][] krArray = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 },
		// { 10, 10 }, { 20, 20 } };
		int[][] krArray = { { 30, 30 }, { 40, 40 }, { 50, 50 } };
		runGreedyDDSKR(inputFile, krArray, destFile);

	}

	@Ignore
	@Test
	public void test3_small_r() throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		int k = 3;
		int r = 3;
		log.debug("6 vertices k=" + k + ",r=" + r + "------------");
		List<String[]> am = new ArrayList<String[]>();
		am.add(new String[] { "0", "1", "0", "0", "0", "0" });
		am.add(new String[] { "1", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });

		GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		Result result = null;

		ag.computing();

		List<Integer> ds = ag.getDs();
		Assert.assertTrue(AlgorithmUtil.isDS(AlgorithmUtil.prepareGraph(am), ds));
		result = ag.getResult(r);

		log.debug(result.getString());
	}

	@Ignore
	@Test
	public void test3_big_r() throws MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException, IOException {

		int k = 6;
		int r = 6;
		log.debug("6 vertices k=" + k + ",r=" + r + "------------");
		List<String[]> am = new ArrayList<String[]>();
		am.add(new String[] { "0", "1", "0", "0", "0", "0" });
		am.add(new String[] { "1", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });
		am.add(new String[] { "0", "0", "0", "0", "0", "0" });

		GreedyDDS ag = new GreedyDDS("GreedyDDSTest", am, k, r);
		Result result = null;

		ag.computing();

		List<Integer> ds = ag.getDs();
		Assert.assertTrue(AlgorithmUtil.isDS(AlgorithmUtil.prepareGraph(am), ds));
		result = ag.getResult(r);

		log.debug(result.getString());
	}
}
