package au.edu.cdu.dynamicproblems.algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class DDSFPTTest {
	private Logger log = LogUtil.getLogger(DDSFPTTest.class);

	@Ignore
	@Test
	public void testRun_k1() throws InterruptedException {
		List<String[]> am = new ArrayList<String[]>();
		// am.add(new String[] { "0", "1", "0" });
		// am.add(new String[] { "1", "0", "1" });
		// am.add(new String[] { "0", "1", "0" });

		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		DSGreedy ag = new DSGreedy(g);
		ag.run();

		List<Integer> ds1 = ag.getDominatingSet();

		int k = 1;

		HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds1, k);

		List<String[]> am2 = hEdit.getOutputAdjacencyMatrix();

		Graph<Integer, Integer> g2 = AlgorithmUtil.prepareGraph(am2);

		String key = "DDSFPT";

		DDSFPT ag2 = new DDSFPT(key, g2, ds1, k);
		ag2.run();

		g2 = AlgorithmUtil.prepareGraph(am2);

		Assert.assertTrue(AlgorithmUtil.isDS(g2, ag2.getDs2()));

	}

	// @Ignore
	@Test
	public void testRun_k2() throws InterruptedException,
			FileNotFoundException, IOException {
		String inputFile1 = "src/test/resources/50_0.3_testcase_a.csv";
		List<String[]> am = IOUtil.getAMFromFile(inputFile1);

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		DSGreedy ag = new DSGreedy(g);
		ag.run();

		List<Integer> ds1 = ag.getDominatingSet();

		int k = 4;

		HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds1, k);

		List<String[]> am2 = hEdit.getOutputAdjacencyMatrix();

		Graph<Integer, Integer> g2 = AlgorithmUtil.prepareGraph(am2);

		String key = "DDSFPT";

		DDSFPT ag2 = new DDSFPT(key, g2, ds1, k);
		ag2.run();

		g2 = AlgorithmUtil.prepareGraph(am2);

		Assert.assertTrue(AlgorithmUtil.isDS(g2, ag2.getDs2()));

	}

	// @Ignore
	@Test
	public void testRun_1000() throws InterruptedException,
			FileNotFoundException, IOException, ArraysNotSameLengthException {
		String inputFile1 = "src/test/resources/1000_0.3_2_testcase_a.csv";
		String inputFile2 = "src/test/resources/1000_0.3_2_testcase_b.csv";
		List<String[]> am = IOUtil.getAMFromFile(inputFile1);

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		DSGreedy ag = new DSGreedy(g);
		ag.run();

		List<Integer> ds1 = ag.getDominatingSet();

		// int k = 4;

		// HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds1, k);

		// List<String[]> am2 = hEdit.getOutputAdjacencyMatrix();
		List<String[]> am2 = IOUtil.getAMFromFile(inputFile2);
		int k = AlgorithmUtil.getDifferentEdgeNumber(am, am2);
		log.debug("k=" + k + "----------------");
		int r = 4;

		Graph<Integer, Integer> g2 = AlgorithmUtil.prepareGraph(am2);

		String key = "DDSFPT";

		DDSFPT ag2 = new DDSFPT(key, g2, ds1, r);
		ag2.run();

		g2 = AlgorithmUtil.prepareGraph(am2);

		Assert.assertTrue(AlgorithmUtil.isDS(g2, ag2.getDs2()));

	}

	@Ignore
	@Test
	public void testViewGraph() throws InterruptedException {
		List<List<Integer>> vertexSections = new ArrayList<List<Integer>>();

		List<Integer> sec1 = new ArrayList<Integer>();
		List<Integer> sec2 = new ArrayList<Integer>();
		List<Integer> sec3 = new ArrayList<Integer>();

		// sec1.add(1);
		// sec2.add(2);
		// sec3.add(3);
		AlgorithmUtil.addElementToList(sec1, 1);
		AlgorithmUtil.addElementToList(sec2, 2);
		AlgorithmUtil.addElementToList(sec3, 3);

		// vertexSections.add(sec1);
		// vertexSections.add(sec2);
		// vertexSections.add(sec3);
		AlgorithmUtil.addElementToList(vertexSections, sec1);
		AlgorithmUtil.addElementToList(vertexSections, sec2);
		AlgorithmUtil.addElementToList(vertexSections, sec3);
		
		List<String[]> am = new ArrayList<String[]>();
		// am.add(new String[] { "0", "1", "0" });
		// am.add(new String[] { "1", "0", "1" });
		// am.add(new String[] { "0", "1", "0" });

		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		DSGreedy ag = new DSGreedy(g);
		ag.run();

		List<Integer> ds1 = ag.getDominatingSet();

		int k = 1;

		String key = "DDSFPT";
		@SuppressWarnings("unused")
		DDSFPT ag2 = new DDSFPT(key, am, ds1, k);

		// ag2.viewGraph(g, vertexSections);

	}

	// @Ignore
	@Test
	public void testViewGraphDifference() throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException, IOException {
		String inputFile1 = "src/test/resources/edge-pair-hamster-friendship.csv";

		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile1);
		List<String[]> am1 = fo.getAdjacencyMatrix();
		Graph<Integer, Integer> g1 = AlgorithmUtil.prepareGraph(am1);
		// Graph<Integer, Integer> g2 = AlgorithmUtil.copyGrapy(g1);

		DSGreedy ag = new DSGreedy(g1);
		ag.computing();
		List<Integer> ds1 = ag.getDominatingSet();

		int k = 20;
		int r = 10;

		HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am1, g1, ds1, k);

		DDSFPT ag2 = new DDSFPT("", hEdit.getOutputAdjacencyMatrix(), ds1, r);

		ag2.computing();

	}

}
