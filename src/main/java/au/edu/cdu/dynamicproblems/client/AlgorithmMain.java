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

	// DIMACS
	private static String dimacsPath = "src/main/resources/DIMACS/";
	private static String[] dimacsFiles = { "C1000.9.clq", "C125.9.clq", "C2000.5.clq", "C2000.9.clq", "C250.9.clq",
			"C4000.5.clq", "C500.9.clq", "DSJC1000.5.clq", "DSJC500.5.clq", "MANN_a27.clq", "MANN_a81.clq",
			"brock200_2.clq", "brock200_4.clq", "brock400_2.clq", "brock400_4.clq", "brock800_2.clq", "brock800_4.clq",
			"gen200_p0.9_44.clq", "gen200_p0.9_55.clq", "gen400_p0.9_55.clq", "gen400_p0.9_65.clq",
			"gen400_p0.9_75.clq", "hamming10-4.clq", "hamming8-4.clq", "keller4.clq", "keller5.clq", "keller6.clq",
			"p_hat1500-1.clq", "p_hat1500-2.clq", "p_hat1500-3.clq", "p_hat300-1.clq", "p_hat300-2.clq",
			"p_hat300-3.clq", "p_hat700-1.clq", "p_hat700-2.clq", "p_hat700-3.clq"

	};

	// BHOSLIB
	private static String bhoslibPath = "src/main/resources/BHOSLIB/";
	private static String[] bhoslibFiles = { "frb30-15-mis/frb30-15-1.mis", "frb30-15-mis/frb30-15-2.mis",
			"frb30-15-mis/frb30-15-3.mis", "frb30-15-mis/frb30-15-4.mis", "frb30-15-mis/frb30-15-5.mis",
			"frb35-17-mis/frb35-17-1.mis", "frb35-17-mis/frb35-17-2.mis", "frb35-17-mis/frb35-17-3.mis",
			"frb35-17-mis/frb35-17-4.mis", "frb35-17-mis/frb35-17-5.mis", "frb40-19-mis/frb40-19-1.mis",
			"frb40-19-mis/frb40-19-2.mis", "frb40-19-mis/frb40-19-3.mis", "frb40-19-mis/frb40-19-4.mis",
			"frb40-19-mis/frb40-19-5.mis", "frb45-21-mis/frb45-21-1.mis", "frb45-21-mis/frb45-21-2.mis",
			"frb45-21-mis/frb45-21-3.mis", "frb45-21-mis/frb45-21-4.mis", "frb45-21-mis/frb45-21-5.mis",
			"frb53-24-mis/frb53-24-1.mis", "frb53-24-mis/frb53-24-2.mis", "frb53-24-mis/frb53-24-3.mis",
			"frb53-24-mis/frb53-24-4.mis", "frb53-24-mis/frb53-24-5.mis", "frb56-25-mis/frb56-25-1.mis",
			"frb56-25-mis/frb56-25-2.mis", "frb56-25-mis/frb56-25-3.mis", "frb56-25-mis/frb56-25-4.mis",
			"frb56-25-mis/frb56-25-5.mis", "frb59-26-mis/frb59-26-1.mis", "frb59-26-mis/frb59-26-2.mis",
			"frb59-26-mis/frb59-26-3.mis", "frb59-26-mis/frb59-26-4.mis", "frb59-26-mis/frb59-26-5.mis" };

	// KONECT
	private static String konectPath = "src/main/resources/KONECT/";
	private static String[] konectFiles = { 
//			"000027_zebra.konet", "000034_zachary.konet", "000062_dolphins.konet",
//			"000112_David_Copperfield.konet", "000198_Jazz_musicians.konet", "000212_pdzbase.konet",
			"001133_rovira.konet", 
			"001174_euroroad.konet", 
			//"001858_hamster.konet",

	};

	public static void main(String[] args) throws FileNotFoundException, IOException {

//		// DIMACS
//		List<String> dimacsFileList = Arrays.asList(dimacsFiles);
//		dimacsFileList.stream().forEach(s -> applyAlgorithmOnFile(dimacsPath, s));
//
//		// BHOSLIB
//		List<String> bhoslibFileList = Arrays.asList(bhoslibFiles);
//		bhoslibFileList.stream().forEach(s -> applyAlgorithmOnFile(bhoslibPath, s));

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
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
