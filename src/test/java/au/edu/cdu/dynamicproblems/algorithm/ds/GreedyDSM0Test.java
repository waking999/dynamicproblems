package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.TestUtil;
import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.order.OrderPackageUtil;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class GreedyDSM0Test {

	private Logger log = LogUtil.getLogger(GreedyDSM0Test.class);
	private static final String CLASS_NAME=GreedyDSM0Test.class.getSimpleName();

	@Ignore
	@Test
	public void test0() throws InterruptedException, IOException,
			FileNotFoundException {
		List<String[]> am = TestUtil.simpleAM0();

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);
		Graph<Integer, String> gCopy = AlgorithmUtil.copyGraph(g);

		int k=10;
		int r=10;
		IGreedyDS<Integer> ag = new GreedyDSM0(am,k,r);
		Result result = ag.run();

		List<Integer> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(gCopy, ds));

	
		log.debug(result.getString());
	}
	@Ignore
	@Test
	public void test1(){
		List<String[]> am = TestUtil.simpleAM0();
		
		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

	
		List<Integer> vList=OrderPackageUtil.getVertexListDegreeAsc(g)  ;
		Assert.assertTrue(5==vList.get(0));
		
	
		vList=OrderPackageUtil.getVertexListDegreeDesc(g)  ;
		Assert.assertTrue(4==vList.get(0));
		
	}

	@Ignore 
	@Test
	public void testKONECT_verify() throws InterruptedException, IOException,
			FileNotFoundException {
		
		String datasetName = "KONECT";
		String path = TestUtil.KONECT_PATH;
		String[] files = TestUtil.KONECT_FILES;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);

		basicFunc(path, files, destFile,1,1,5,4);
	}

	@Ignore
	@Test
	public void testDIMACS_verify() throws InterruptedException, IOException,
			FileNotFoundException {
		String datasetName = "DIMACS";
		String path = TestUtil.DIMACS_PATH;
		String[] files = TestUtil.DIMACS_FILES;
		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);
		
		basicFunc(path, files, destFile,1,1,5,4);
	}

	

	//@Ignore
	@Test
	public void testBHOSLIB_verify() throws InterruptedException, IOException,
			FileNotFoundException {
		String datasetName = "BHOSLIB";
		String path = TestUtil.BHOSLIB_PATH;
		String[] files = TestUtil.BHOSLIB_FILES;

		String destFile = TestUtil.getOutputFileName(datasetName, CLASS_NAME);
		
		basicFunc(path, files, destFile,1,1,3,2);
	}
	
	
	private void basicFunc(String path, String[] files, String destFile,int iLower,int iUpper,int k,int r)
			throws FileNotFoundException, IOException, InterruptedException {
		for (int i = iLower; i <= iUpper; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(i).append("------------");
			String sbStr = sb.toString();
			
			log.debug(sbStr);
			if (destFile != null) {
				FileOperation.saveCVSFile(destFile, sbStr);
			}
	
			for (String file : files) {

				basicFunc(path + file, destFile,k,r);
			}
		}
	}

	


	private void basicFunc(String inputFile, String destFile, int k,int r)
			throws InterruptedException, IOException, FileNotFoundException {
		
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();

		Graph<Integer, String> g = AlgorithmUtil.prepareGenericGraph(am);

		IGreedyDS<Integer> ag = new GreedyDSM0(am,k,r);
		TestUtil.run(inputFile, destFile, g, ag, log);

	}

}