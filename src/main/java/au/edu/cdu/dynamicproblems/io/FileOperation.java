package au.edu.cdu.dynamicproblems.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;

/**
 * This class work for getting input from files
 * 
 * @author : Kai
 * 
 */
public class FileOperation {
	private static final String COMMA = ",";
	

//	public static final String CONNECTED = "1";
//	public static final String UNCONNECTED = "0";

	/*
	 * the input file path and name
	 */
	private FileInfo fileInfo;

	/*
	 * the adjacency matrix shown in the input file
	 */
	private List<String[]> adjacencyMatrix;

	private int numOfVertices;

	public int getNumOfVertices() {
		return numOfVertices;
	}

	public int getK() {
		return k;
	}

	private int k;

	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	private static String saveAgjacencyMatrixToFile(String filePath, String fileName, List<String[]> adjacencyMatrix,
			int k) throws IOException, FileNotFoundException {

		int numOfVertices = adjacencyMatrix.size();

		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		String filePN = filePath + fileName;

		File csvFile = new File(filePN);
		out = new FileOutputStream(csvFile);
		osw = new OutputStreamWriter(out);
		bw = new BufferedWriter(osw);

		bw.write(numOfVertices + "," + k + "\r\n");
		for (int i = 0; i < numOfVertices; i++) {
			String[] row = adjacencyMatrix.get(i);
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < numOfVertices; j++) {
				sb.append(row[j]).append(COMMA);
			}
			bw.write(sb.subSequence(0, sb.length() - 1) + "\r\n");

		}

		bw.close();
		osw.close();
		out.close();

		return filePN;
	}

	/**
	 * save the adjacency matrix to file
	 * 
	 * @param adjacencyMatrix
	 *            , adjacency matrix
	 */
	public static String saveAgjacencyMatrixToFile(String destFolder, List<String[]> adjacencyMatrix, float ratio,
			int k) throws FileNotFoundException, IOException {
		int numOfVertices = adjacencyMatrix.size();
		// String filePath = "src/main/resources/";
		String fileName = numOfVertices + "_" + ratio + "_testcase_" + System.currentTimeMillis() + ".csv";

		return saveAgjacencyMatrixToFile(destFolder, fileName, adjacencyMatrix, k);

	}

	/**
	 * retrive graph info in edge pair format
	 */
	public void retriveProblemInfoByEdgePair() throws FileNotFoundException, IOException {
		Path path = Paths.get(this.fileInfo.getInputFile());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		this.adjacencyMatrix=AlgorithmUtil.transferEdgePairToMatrix(lines);
		this.numOfVertices=this.adjacencyMatrix.size();

	}

	

	// /**
	// * retrive graph info in adjacent matrix format
	// */
	// public void retriveProblemInfo() throws FileNotFoundException,
	// IOException {
	//
	// BufferedReader reader = null;
	//
	// reader = new BufferedReader(
	// new FileReader(this.fileInfo.getInputFile()));
	//
	// String line = null;
	//
	// // the first line is number of vertices,some parameters such as k,r
	// line = reader.readLine();
	//
	// String[] firstLine = line.split(COMMA);
	// int firstLineLen = firstLine.length;
	// if (firstLineLen > 0 && firstLine[0] != null) {
	// numOfVertices = Integer.parseInt(firstLine[0]);
	// }
	//
	// if (firstLineLen > 1 && firstLine[1] != null) {
	// k = Integer.parseInt(firstLine[1]);
	// }
	//
	// // the following lines from the 2nd line are adjacency matrix
	// adjacencyMatrix = new ArrayList<String[]>();
	//
	// while ((line = reader.readLine()) != null) {
	// String item[] = line.split(COMMA);
	// adjacencyMatrix.add(item);
	// }
	//
	// reader.close();
	//
	// }

	/**
	 * retrive graph info in adjacent matrix format
	 */
	public void retriveProblemInfo() throws FileNotFoundException, IOException {
		Path path = Paths.get(this.fileInfo.getInputFile());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());

		String[] firstLine = lines.get(0).split(COMMA);
		int firstLineLen = firstLine.length;
		if (firstLineLen > 0 && firstLine[0] != null) {
			numOfVertices = Integer.parseInt(firstLine[0]);
		}

		if (firstLineLen > 1 && firstLine[1] != null) {
			k = Integer.parseInt(firstLine[1]);
		}

		// the following lines from the 2nd line are adjacency matrix
		adjacencyMatrix = new ArrayList<String[]>();

		for (int i = 1; i < numOfVertices; i++) {
			String line = lines.get(i);
			String item[] = line.split(COMMA);
			adjacencyMatrix.add(item);
		}

	}

	public List<String[]> getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	/**
	 * 
	 * @param operationList
	 */
	public static void saveOperationToFile(String destFolder, List<String[]> operationList)
			throws FileNotFoundException, IOException {
		String fileName = destFolder + "operation" + System.currentTimeMillis() + ".csv";

		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		File csvFile = new File(fileName);
		out = new FileOutputStream(csvFile);
		osw = new OutputStreamWriter(out);
		bw = new BufferedWriter(osw);

		int operationListSize = operationList.size();
		for (int i = 0; i < operationListSize; i++) {
			String[] row = operationList.get(i);
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < 3; j++) {
				sb.append(row[j]).append(AlgorithmUtil.BLANK);
			}
			bw.write(sb.append("\r\n").toString());

		}

		bw.close();
		osw.close();
		out.close();

	}

	public static void saveCVSFile(String destFileStr, String outputStr) throws FileNotFoundException, IOException {
		BufferedWriter bw = null;

		try {

			// APPEND MODE SET HERE
			bw = new BufferedWriter(new FileWriter(destFileStr, true));
			bw.write(outputStr);
			bw.newLine();
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally { // always close the file
			if (bw != null)
				try {
					bw.close();
				} catch (IOException ioe2) {
					// just ignore it
				}
		}
	}

}
