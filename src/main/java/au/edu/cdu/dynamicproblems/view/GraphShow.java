package au.edu.cdu.dynamicproblems.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import edu.uci.ics.jung.graph.Graph;

public class GraphShow {
	public static void main(String[] args) throws FileNotFoundException, IOException{
		String inputFile="src/test/resources/DIMACS/"+"C125.9.clq";
		FileOperation fo = IOUtil.getProblemInfoByEdgePair(inputFile);
		List<String[]> am = fo.getAdjacencyMatrix();
		Graph<Integer,Integer> g=AlgorithmUtil.prepareGraph(am);
		
		List<Collection<Integer>> vertexSections = new ArrayList<Collection<Integer>>();
		Collection<Integer> zeroNeig=g.getNeighbors(0);
		zeroNeig.add(0);
		AlgorithmUtil.addElementToList(vertexSections, zeroNeig);
		
		viewGraph(g, vertexSections);
	}
	
	private static void viewGraph(Graph<Integer, Integer> g,
 List<Collection<Integer>> vertexSections) {
		float width = 1024;
		float height = 768;

		Section sec1 = new Section(0.0f, 0.0f, width, height, vertexSections.get(0));
		
		List<Section> sections = new ArrayList<Section>();
		AlgorithmUtil.addElementToList(sections, sec1);
		
		GraphView.presentGraph(g, sections, width, height);
	}
}
