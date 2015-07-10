package au.edu.cdu.dynamicproblems.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.DSGreedyNative;
import au.edu.cdu.dynamicproblems.algorithm.HEdit;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class AlgorithmUtilMain {

	private static Logger log = LogUtil.getLogger(AlgorithmUtilMain.class);

	public static void main(String[] args) throws MOutofNException,ExceedLongMaxException,
			ArraysNotSameLengthException, FileNotFoundException, IOException {

		// generateRandGraph_1000_03_3();
		// generateRandGraph_1000_03_5();
		// generateRandGraph_2000_03_5();
		generateRandGraph_2000_02_8();
	}

	@SuppressWarnings("unused")
	private static void generateRandGraph_1000_03_3()
			throws MOutofNException,ExceedLongMaxException, ArraysNotSameLengthException,
			FileNotFoundException, IOException {
		String message = "Graph:1000 vertices;radio:0.3;k:3";
		String destFolder = "src/main/resources/1000/";
		int numOfVertex = 1000;
		float ratio = 0.3f;
		int k = 3;

		generateRandomGAndG1(destFolder, message, numOfVertex, ratio, k);

	}

	@SuppressWarnings("unused")
	private static void generateRandGraph_1000_03_5()
			throws MOutofNException,ArraysNotSameLengthException, FileNotFoundException,
			IOException, ExceedLongMaxException {
		String message = "Graph:1000 vertices;radio:0.3;k:5";
		String destFolder = "src/main/resources/1000/";
		int numOfVertex = 1000;
		float ratio = 0.3f;
		int k = 5;

		generateRandomGAndG1(destFolder, message, numOfVertex, ratio, k);

	}

	@SuppressWarnings("unused")
	private static void generateRandGraph_2000_03_5()
			throws MOutofNException,ArraysNotSameLengthException, FileNotFoundException,
			IOException, ExceedLongMaxException {
		String message = "Graph:2000 vertices;radio:0.3;k:5";
		String destFolder = "src/main/resources/2000/";
		int numOfVertex = 2000;
		float ratio = 0.3f;
		int k = 5;

		generateRandomGAndG1(destFolder, message, numOfVertex, ratio, k);

	}

	private static void generateRandGraph_2000_02_8()
			throws MOutofNException,ArraysNotSameLengthException, FileNotFoundException,
			IOException, ExceedLongMaxException {
		String message = "Graph:2000 vertices;radio:0.2;k:8";
		String destFolder = "src/main/resources/2000/";
		int numOfVertex = 2000;
		float ratio = 0.2f;
		int k = 8;

		generateRandomGAndG1(destFolder, message, numOfVertex, ratio, k);

	}

	private static void generateRandomGAndG1(String destFolder, String message,
			int numOfVertex, float ratio, int k)
			throws MOutofNException,ArraysNotSameLengthException, FileNotFoundException,
			IOException, ExceedLongMaxException {
		// generate a random graph G
		log.debug(message);
		List<String[]> am = AlgorithmUtil.generateRandGraph(numOfVertex, ratio);
		DSGreedyNative ag = new DSGreedyNative(message, am);

		ag.computing();

		List<Integer> ds = ag.getDominatingSet();

		LogUtil.printResult(message, ds);
		String inputFile1 = FileOperation.saveAgjacencyMatrixToFile(destFolder,
				am, ratio, 0);

		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);

		// henning edge edit the graph to get another graph G'
		HEdit hEdit = AlgorithmUtil.hEditEdgeDeletion(am, g, ds, k);
		List<String[]> am2 = hEdit.getOutputAdjacencyMatrix();

		List<String[]> operationList = hEdit.getOperationList();

		FileOperation.saveAgjacencyMatrixToFile(destFolder, am2, ratio, k);

		FileOperation.saveOperationToFile(destFolder, operationList);

		am = IOUtil.getAMFromFile(inputFile1);
		int k1 = AlgorithmUtil.getDifferentEdgeNumber(am, am2);
		log.debug("k:" + k1);

	}

}
