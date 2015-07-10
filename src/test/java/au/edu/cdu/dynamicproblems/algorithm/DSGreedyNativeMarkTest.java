package au.edu.cdu.dynamicproblems.algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class DSGreedyNativeMarkTest {

	private Logger log = LogUtil.getLogger(DSGreedyNativeMarkTest.class);

	@Ignore
	@Test
	public void test0() throws InterruptedException, IOException,
			FileNotFoundException {
		List<String[]> am = new ArrayList<String[]>();
		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0", "0",
				"0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1", "0",
				"0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0", "1",
				"1", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "1", "0",
				"0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "1", "0",
				"0", "1", "1", "1", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"1", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"1", "0", "0", "0", "1", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"1", "0", "0", "0", "1", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"0", "0", "1", "1", "0", "1", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"0", "0", "0", "0", "1", "0", "1", "1", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"0", "0", "0", "0", "0", "1", "0", "1", "0", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"0", "0", "0", "0", "0", "1", "1", "0", "1", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"0", "0", "0", "0", "0", "0", "0", "1", "0", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "0", "0", "0",
				"0", "0", "0", "0", "0", "0", "0", "1", "1", "0" });

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
		Graph<Integer, Integer> gCopy = AlgorithmUtil.copyGrapy(g);

		DSGreedyNativeMark ag = new DSGreedyNativeMark(g);
		ag.run();

		List<Integer> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(gCopy, ds));

		Result r = ag.getResult();
		log.debug(r.getString());
	}

	@Ignore
	@Test
	public void testKONET() throws InterruptedException, IOException,
			FileNotFoundException {
		String destFile = "out/output-GreedyNativeMark-KONECT.csv";

		String path = "src/test/resources/KONECT/";
		String[] files = { "000027_zebra.konet", "000034_zachary.konet",
				"000062_dolphins.konet", "000112_David_Copperfield.konet",
				"000198_Jazz_musicians.konet", "000212_pdzbase.konet",
				"001133_rovira.konet", "001174_euroroad.konet",
				"001858_hamster.konet",
		// "002426_hamster_ful.konet",
		// "002888_facebook.konet",
		// "003133_Human_protein_Vidal.konet",
		// "004941_powergrid.konet",
		// "006327_reactome.konet",
		// "010680_Pretty_Good_Privacy.konet",
		// "06474_Route_views.konet"
		};

		for (int i = 1; i <= 1; i++) {
			log.debug(i + "------------");
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, i + "--------");
			}
			for (String file : files) {

				runDSGreedy(path + file, destFile);
			}
		}
	}

	@Ignore
	@Test
	public void testDIMACS() throws InterruptedException, IOException,
			FileNotFoundException {
		String destFile = "out/output-GreedyNativeMark-DIMACS.csv";

		String path = "src/test/resources/DIMACS/";
		String[] files = { "C1000.9.clq", "C125.9.clq", "C2000.5.clq",
				"C2000.9.clq", "C250.9.clq", "C4000.5.clq", "C500.9.clq",
				"DSJC1000.5.clq", "DSJC500.5.clq", "MANN_a27.clq",
				"MANN_a81.clq", "brock200_2.clq", "brock200_4.clq",
				"brock400_2.clq", "brock400_4.clq", "brock800_2.clq",
				"brock800_4.clq", "gen200_p0.9_44.clq", "gen200_p0.9_55.clq",
				"gen400_p0.9_55.clq", "gen400_p0.9_65.clq",
				"gen400_p0.9_75.clq", "hamming10-4.clq", "hamming8-4.clq",
				"keller4.clq", "keller5.clq", "keller6.clq", "p_hat1500-1.clq",
				"p_hat1500-2.clq", "p_hat1500-3.clq", "p_hat300-1.clq",
				"p_hat300-2.clq", "p_hat300-3.clq", "p_hat700-1.clq",
				"p_hat700-2.clq", "p_hat700-3.clq"

		};
		for (int i = 1; i <= 1; i++) {
			log.debug(i + "------------");
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, i + "--------");
			}
			for (String file : files) {

				runDSGreedy(path + file, destFile);
			}
		}
	}

	// @Ignore
	@Test
	public void testBHOSLIB() throws InterruptedException, IOException,
			FileNotFoundException {
		String destFile = "out/output-GreedyNativeMark-BHOSLIB.csv";

		String path = "src/test/resources/BHOSLIB/";
		String[] files = { "frb30-15-mis/frb30-15-1.mis",
				"frb30-15-mis/frb30-15-2.mis", "frb30-15-mis/frb30-15-3.mis",
				"frb30-15-mis/frb30-15-4.mis", "frb30-15-mis/frb30-15-5.mis",
				"frb35-17-mis/frb35-17-1.mis", "frb35-17-mis/frb35-17-2.mis",
				"frb35-17-mis/frb35-17-3.mis", "frb35-17-mis/frb35-17-4.mis",
				"frb35-17-mis/frb35-17-5.mis", "frb40-19-mis/frb40-19-1.mis",
				"frb40-19-mis/frb40-19-2.mis", "frb40-19-mis/frb40-19-3.mis",
				"frb40-19-mis/frb40-19-4.mis", "frb40-19-mis/frb40-19-5.mis",
				"frb45-21-mis/frb45-21-1.mis", "frb45-21-mis/frb45-21-2.mis",
				"frb45-21-mis/frb45-21-3.mis", "frb45-21-mis/frb45-21-4.mis",
				"frb45-21-mis/frb45-21-5.mis", "frb53-24-mis/frb53-24-1.mis",
				"frb53-24-mis/frb53-24-2.mis", "frb53-24-mis/frb53-24-3.mis",
				"frb53-24-mis/frb53-24-4.mis", "frb53-24-mis/frb53-24-5.mis",
				"frb56-25-mis/frb56-25-1.mis", "frb56-25-mis/frb56-25-2.mis",
				"frb56-25-mis/frb56-25-3.mis", "frb56-25-mis/frb56-25-4.mis",
				"frb56-25-mis/frb56-25-5.mis", "frb59-26-mis/frb59-26-1.mis",
				"frb59-26-mis/frb59-26-2.mis", "frb59-26-mis/frb59-26-3.mis",
				"frb59-26-mis/frb59-26-4.mis", "frb59-26-mis/frb59-26-5.mis" };
		for (int i = 2; i <= 10; i++) {
			log.debug(i + "------------");
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, i + "--------");
			}
			for (String file : files) {

				runDSGreedy(path + file, destFile);
			}
		}
	}

	@Ignore
	@Test
	public void testDIMACSGC() throws InterruptedException, IOException,
			FileNotFoundException {
		String destFile = "out/output-DSGreedy-DIMACSGC.csv";

		String path = "src/test/resources/DIMACS-GC/";
		String[] files = {// "DSJC1000.1.col",
				// "DSJC1000.5.col",
				// "DSJC1000.9.col",
				"DSJC125.1.col",
				"DSJC125.5.col",
				"DSJC125.9.col",
				"DSJC250.1.col",
				"DSJC250.5.col",
				"DSJC250.9.col",
				"DSJC500.1.col",
				"DSJC500.5.col",
				"DSJC500.9.col",
				"DSJR500.1.col",
				"DSJR500.1c.col",
				"DSJR500.5.col",
				// "anna.col",
				// "david.col",
				"flat1000_50_0.col",
				"flat1000_60_0.col",
				"flat1000_76_0.col",
				"flat300_20_0.col",
				"flat300_26_0.col",
				"flat300_28_0.col",
				"fpsol2.i.1.col",
				"fpsol2.i.2.col",
				"fpsol2.i.3.col",
				// "games120.col",
				// "homer.col",
				// "huck.col",
				"inithx.i.1.col",
				"inithx.i.2.col",
				"inithx.i.3.col",
				// "jean.col",
				"le450_15a.col",
				"le450_15b.col",
				"le450_15c.col",
				"le450_15d.col",
				"le450_25a.col",
				"le450_25b.col",
				"le450_25c.col",
				"le450_25d.col",
				"le450_5a.col",
				"le450_5b.col",
				"le450_5c.col",
				"le450_5d.col",
				"miles1000.col",
				"miles1500.col",
				"miles250.col",
				"miles500.col",
				"miles750.col",
				"mulsol.i.1.col",
				"mulsol.i.2.col",
				"mulsol.i.3.col",
				"mulsol.i.4.col",
				"mulsol.i.5.col",
				// "myciel2.col",
				// "myciel3.col",
				// "myciel4.col",
				// "myciel5.col",
				// "myciel6.col",
				// "myciel7.col",
				"queen10_10.col", "queen11_11.col", "queen12_12.col",
				"queen13_13.col", "queen14_14.col", "queen15_15.col",
				"queen16_16.col", "queen5_5.col", "queen6_6.col",
				"queen7_7.col", "queen8_12.col", "queen8_8.col",
				"queen9_9.col", "school1.col", "school1_nsh.col",
				"zeroin.i.1.col", "zeroin.i.2.col", "zeroin.i.3.col" };
		for (String file : files) {
			runDSGreedy(path + file, destFile);
		}
	}

	private void runDSGreedy(String inputFile, String destFile)
			throws InterruptedException, IOException, FileNotFoundException {
		// String inputFile = "src/test/resources/edge-pair-maayan-vidal.csv";
		// String inputFile = "src/test/resources/DIMACS-GC/DSJC1000.9.col";
		// String inputFile =
		// "src/test/resources/BHOSLIB/frb59-26-mis/frb59-26-5.mis";
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		DSGreedyNativeMark ag = new DSGreedyNativeMark(g);
		ag.run();

		List<Integer> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(g, ds));

		Result r = ag.getResult();
		log.debug(r.getString());
		if (destFile != null) {
			FileOperation
					.saveCVSFile(destFile, inputFile + "," + r.getString());
		}

	}

}
