package au.edu.cdu.dynamicproblems.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyVoteGr;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import edu.uci.ics.jung.graph.Graph;

/**
 * this class is used as the main entry of the algorithms
 * 
 * @author kai wang
 *
 */
public class AlgorithmMain {
	private static Logger log = Logger.getLogger(AlgorithmMain.class);

	// KONECT
	private static String konectPath = "src/main/resources/KONECT/";
	private static String[] konectFiles = { "001133_rovira.konet", "001174_euroroad.konet",

	};

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// KONECT
		List<String> konectFileList = Arrays.asList(konectFiles);
		konectFileList.stream().forEach(s -> applyAlgorithmOnFile(konectPath, s));

	}

	private static void applyAlgorithmOnFile(String path, String inputFile) {
		try {
			FileOperation fo = IOUtil.getProblemInfoByEdgePair(path + inputFile);
			List<String[]> am = fo.getAdjacencyMatrix();

			Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
			// change for invoking different algorithms
			GreedyVoteGr ag = new GreedyVoteGr(inputFile, g);
			Result r = ag.run();

			List<Integer> ds = ag.getDominatingSet();
			if (AlgorithmUtil.isDS(g, ds)) {
				log.debug(r.getString());
			} else {
				log.debug(inputFile + ",Wrong dominating set solution");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
