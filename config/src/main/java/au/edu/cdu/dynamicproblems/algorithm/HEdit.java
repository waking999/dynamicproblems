package au.edu.cdu.dynamicproblems.algorithm;

import java.util.List;

public class HEdit {

	/**
	 * the edge edit operation list
	 */
	private List<String[]> operationList;

	public List<String[]> getOperationList() {
		return operationList;
	}

	public void setOperationList(List<String[]> operationList) {
		this.operationList = operationList;
	}

	public List<String[]> getOutputAdjacencyMatrix() {
		return outputAdjacencyMatrix;
	}

	public void setOutputAdjacencyMatrix(List<String[]> outputAdjacencyMatrix) {
		this.outputAdjacencyMatrix = outputAdjacencyMatrix;
	}

	/**
	 * the adjacency matrix after hedit
	 */
	private List<String[]> outputAdjacencyMatrix;
}