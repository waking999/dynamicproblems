package au.edu.cdu.dynamicproblems.algorithm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import agape.tools.Components;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyNative;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class AlgorithmUtilTest {
	private Logger log = LogUtil.getLogger(AlgorithmUtilTest.class);

	@Ignore
	@Test
	public void testArrayToString() {
		byte[] b1 = { 0, 1, 1 };
		String str = AlgorithmUtil.arrayToString(b1);
		Assert.assertEquals("011", str);

		byte[] b2 = AlgorithmUtil.stringToBinaryArray(str);
		Assert.assertArrayEquals(b1, b2);

	}

	// @Ignore
	// @Test
	// public void testListToString() {
	// List<Integer> list = new ArrayList<Integer>();
	// // list.add(1);
	// // list.add(2);
	// AlgorithmUtil.addElementToList(list, 1);
	// AlgorithmUtil.addElementToList(list, 2);
	//
	// String str = AlgorithmUtil.intListToString(3, list);
	// Assert.assertEquals("011", str);
	//
	// List<Integer> elist = AlgorithmUtil.stringToIntList(str);
	//
	// Assert.assertEquals(2, elist.size());
	//
	// }

	// @Ignore
	// @Test
	// public void testArrayToLong() throws ExceedLongMaxException {
	// byte[] b1 = { 0, 1, 1 };
	// long e1 = 3;
	//
	// byte[] b2 = { 1, 1, 0 };
	// long e2 = 6;
	//
	// long r1 = AlgorithmUtil.arrayToLong(b1);
	// Assert.assertEquals(e1, r1);
	//
	// long r2 = AlgorithmUtil.arrayToLong(b2);
	// Assert.assertEquals(e2, r2);
	//
	// }

	// @Ignore
	// @Test(expected = ExceedLongMaxException.class)
	// public void testArrayToLong_expcetion() throws ExceedLongMaxException {
	// byte[] b1 = new byte[64];
	// long e1 = -1;
	//
	// long r1 = AlgorithmUtil.arrayToLong(b1);
	// Assert.assertEquals(e1, r1);
	//
	// }

	// @Ignore
	// @Test
	// @Deprecated
	// public void testLongToBinaryArray() {
	// byte[] e1 = { 0, 1, 1 };
	// long l1 = 3;
	// Assert.assertArrayEquals(e1, AlgorithmUtil.longToBinaryArray(3, l1));
	//
	// byte[] e2 = { 1, 0, 1 };
	// long l2 = 5;
	// Assert.assertArrayEquals(e2, AlgorithmUtil.longToBinaryArray(3, l2));
	// }

	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void testPrepareGraph() {
		List<String[]> am = new ArrayList<String[]>();

		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		Assert.assertTrue(g.isNeighbor(1, 2));
	}

	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void testSortVertexAccordingToDegree() {

		List<String[]> am = new ArrayList<String[]>();

		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		List<VertexDegree> list = AlgorithmUtil.sortVertexAccordingToDegree(g);

		VertexDegree vd = list.get(0);
		Assert.assertEquals(new Integer(1), vd.getVertex());

	}

	@Ignore
	@Test
	public void testGenerateRandGraph() {
		List<String[]> am = AlgorithmUtil.generateRandGraph(3, 0.8f);
		Assert.assertNotNull(am);

	}

	// @Ignore
	// @Test
	// public void testHEditEdgeDeletion() throws MOutofNException,
	// ExceedLongMaxException,
	// ArraysNotSameLengthException {
	// List<String[]> am = new ArrayList<String[]>();
	// // am.add(new String[] { "0", "1", "0" });
	// // am.add(new String[] { "1", "0", "1" });
	// // am.add(new String[] { "0", "1", "0" });
	// AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
	// AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
	// AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
	// Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
	//
	// GreedyNative ag = new GreedyNative(am);
	// ag.computing();
	//
	// List<Integer> ds = ag.getDominatingSet();
	//
	// int k = 1;
	//
	// HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds, k);
	//
	// List<String[]> opl = hEdit.getOperationList();
	// Assert.assertNotNull(opl);
	//
	// List<String[]> am2 = hEdit.getOutputAdjacencyMatrix();
	// Assert.assertNotNull(am2);
	//
	// }

	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void tesRandomInRang() {
		float f = AlgorithmUtil.randomInRang(0, 1);
		Assert.assertTrue(f >= 0);
	}

	@Ignore
	@Test
	public void testArrayOr() throws ArraysNotSameLengthException {
		byte[] r1 = new byte[] { 1, 0 };
		byte[] r2 = new byte[] { 0, 0 };
		byte[] e1 = new byte[] { 1, 0 };

		byte[] r3 = AlgorithmUtil.arrayOr(r1, r2);

		Assert.assertArrayEquals(e1, r3);
	}

	@Ignore
	@Test(expected = ArraysNotSameLengthException.class)
	public void testArrayOr_exception() throws ArraysNotSameLengthException {
		byte[] r1 = new byte[] { 1, 0 };
		byte[] r2 = new byte[] { 0 };

		AlgorithmUtil.arrayOr(r1, r2);

	}

	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void testGetNeighborsOfS() {
		List<String[]> am = new ArrayList<String[]>();
		AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
		AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		List<Integer> S = new ArrayList<Integer>();
		AlgorithmUtil.addElementToList(S, 0);

		List<Integer> nList = AlgorithmUtil.getNeighborsOfS(g, S);
		Assert.assertEquals(new Integer(1), nList.get(0));
	}

	// @Ignore
	// @Test
	// public void testIsDS() throws MOutofNException, ExceedLongMaxException,
	// ArraysNotSameLengthException{
	// List<String[]> am = new ArrayList<String[]>();
	// // am.add(new String[] { "0", "1", "0" });
	// // am.add(new String[] { "1", "0", "1" });
	// // am.add(new String[] { "0", "1", "0" });
	//
	// AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
	// AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
	// AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
	//
	// Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
	//
	// String message = "DSGreedy";
	//
	// GreedyNative ag = new GreedyNative(message, am);
	// ag.computing();
	//
	// List<Integer> ds = ag.getDominatingSet();
	//
	// Assert.assertTrue(AlgorithmUtil.isDS(g, ds));
	//
	// List<Integer> ds2 = new ArrayList<Integer>();
	// // ds2.add(0);
	//
	// AlgorithmUtil.addElementToList(ds2, 0);
	//
	// Assert.assertFalse(AlgorithmUtil.isDS(g, ds2));
	//
	// }

	// @Ignore
	// @Test
	// public void testGetDifferentEdgeNumber()
	// throws MOutofNException, ExceedLongMaxException,
	// ArraysNotSameLengthException {
	// List<String[]> am = new ArrayList<String[]>();
	// // am.add(new String[] { "0", "1", "0" });
	// // am.add(new String[] { "1", "0", "1" });
	// // am.add(new String[] { "0", "1", "0" });
	//
	// AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
	// AlgorithmUtil.addElementToList(am, new String[] { "1", "0", "1" });
	// AlgorithmUtil.addElementToList(am, new String[] { "0", "1", "0" });
	// Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
	//
	// GreedyNative ag = new GreedyNative(am);
	// ag.computing();
	//
	// List<Integer> ds = ag.getDominatingSet();
	//
	// int k = 1;
	//
	// HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds, k);
	//
	// List<String[]> am2 = hEdit.getOutputAdjacencyMatrix();
	//
	// List<String[]> am3 = new ArrayList<String[]>();
	// // am3.add(new String[] { "0", "1", "0" });
	// // am3.add(new String[] { "1", "0", "1" });
	// // am3.add(new String[] { "0", "1", "0" });
	//
	// AlgorithmUtil.addElementToList(am3, new String[] { "0", "1", "0" });
	// AlgorithmUtil.addElementToList(am3, new String[] { "1", "0", "1" });
	// AlgorithmUtil.addElementToList(am3, new String[] { "1", "0", "1" });
	//
	// int k1 = AlgorithmUtil.getDifferentEdgeNumber(am3, am2);
	//
	// Assert.assertTrue(k1 <= k);
	//
	// }

	@SuppressWarnings("deprecation")
	@Ignore
	@Test
	public void testSort() throws FileNotFoundException, IOException {
		String inputFile1 = "src/test/resources/50_0.3_testcase_a.csv";
		FileOperation fo = IOUtil.getProblemInfo(inputFile1);
		List<String[]> am = fo.getAdjacencyMatrix();

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
		List<VertexDegree> vdl = AlgorithmUtil.sortVertexAccordingToDegree(g);
		for (VertexDegree vd : vdl) {
			log.debug(vd.getVertex() + "-" + vd.getDegree());
		}
		log.debug("-------------------------");

		List<Integer> vl = new ArrayList<Integer>();

		AlgorithmUtil.addElementToList(vl, 1);
		AlgorithmUtil.addElementToList(vl, 2);
		AlgorithmUtil.addElementToList(vl, 3);
		AlgorithmUtil.addElementToList(vl, 23);
		AlgorithmUtil.addElementToList(vl, 38);
		List<Integer> vl1 = AlgorithmUtil.sortVertexAccordingToSortedVertexList(vl, vdl);
		log.debug("-------------------------");
		for (Integer v : vl1) {
			log.debug(v);
		}

	}

	@Ignore
	@Test
	public void testAddElementToList() {
		List<Integer> l = new ArrayList<Integer>();
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(1);

		AlgorithmUtil.addElementToList(l, i1);
		AlgorithmUtil.addElementToList(l, i2);

		Assert.assertTrue(l.size() == 1);
	}

	@Ignore
	@Test
	public void testApplyPairVerticesReductionRule() throws InterruptedException, IOException, FileNotFoundException {
		List<String> lines = new ArrayList<String>();

		AlgorithmUtil.addElementToList(lines, "23 34");
		AlgorithmUtil.addElementToList(lines, "1 2");
		AlgorithmUtil.addElementToList(lines, "1 5");
		AlgorithmUtil.addElementToList(lines, "2 3");
		AlgorithmUtil.addElementToList(lines, "2 5");
		AlgorithmUtil.addElementToList(lines, "2 6");
		AlgorithmUtil.addElementToList(lines, "2 7");
		AlgorithmUtil.addElementToList(lines, "3 4");
		AlgorithmUtil.addElementToList(lines, "3 7");
		AlgorithmUtil.addElementToList(lines, "3 8");
		AlgorithmUtil.addElementToList(lines, "3 17");
		AlgorithmUtil.addElementToList(lines, "5 12");
		AlgorithmUtil.addElementToList(lines, "5 15");
		AlgorithmUtil.addElementToList(lines, "6 15");
		AlgorithmUtil.addElementToList(lines, "6 16");
		AlgorithmUtil.addElementToList(lines, "6 7");
		AlgorithmUtil.addElementToList(lines, "7 16");
		AlgorithmUtil.addElementToList(lines, "7 17");
		AlgorithmUtil.addElementToList(lines, "9 12");
		AlgorithmUtil.addElementToList(lines, "10 12");
		AlgorithmUtil.addElementToList(lines, "11 12");
		AlgorithmUtil.addElementToList(lines, "12 13");
		AlgorithmUtil.addElementToList(lines, "12 14");
		AlgorithmUtil.addElementToList(lines, "12 15");
		AlgorithmUtil.addElementToList(lines, "13 15");
		AlgorithmUtil.addElementToList(lines, "14 15");
		AlgorithmUtil.addElementToList(lines, "15 16");
		AlgorithmUtil.addElementToList(lines, "15 17");
		AlgorithmUtil.addElementToList(lines, "15 18");
		AlgorithmUtil.addElementToList(lines, "15 22");
		AlgorithmUtil.addElementToList(lines, "15 23");
		AlgorithmUtil.addElementToList(lines, "16 17");
		AlgorithmUtil.addElementToList(lines, "17 18");
		AlgorithmUtil.addElementToList(lines, "18 19");
		AlgorithmUtil.addElementToList(lines, "18 20");
		AlgorithmUtil.addElementToList(lines, "18 21");
		AlgorithmUtil.addElementToList(lines, "18 22");
		AlgorithmUtil.addElementToList(lines, "22 23");

		List<String[]> am = AlgorithmUtil.transferEdgePairToMatrix(lines);

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);
		Map<String,Long> runningTimeMap=new HashMap<String,Long>();

		Graph<Integer, String> g1 = AlgorithmUtil.applyPairVerticesReductionRule(g,runningTimeMap);

		GreedyNative ag = new GreedyNative(g1);
		ag.run();

		List<Integer> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(g, ds));
	}

	@Test
	public void testConnectComponent() throws FileNotFoundException, IOException {
		String path=TestUtil.KONECT_PATH;
		for (TestParameter tp : TestUtil.KONECT_TP) {
			if (tp.isBeTest()) {
				FileOperation fo = IOUtil.getProblemInfoByEdgePair(path+tp.getFile());
				List<String[]> am = fo.getAdjacencyMatrix();

				Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

				int gSize = g.getVertexCount();

				List<Set<Integer>> components = Components.getAllConnectedComponent(g);
				log.debug(tp.getFile()+":"+components.size());
				int totalComponentSize = 0;
				for (Set<Integer> component : components) {
					int componentSize = component.size();
					totalComponentSize += componentSize;

				}

				Assert.assertEquals(gSize, totalComponentSize);
			}

		}

	}
}
