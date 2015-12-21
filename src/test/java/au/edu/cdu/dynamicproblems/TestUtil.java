package au.edu.cdu.dynamicproblems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.ds.IGreedyDS;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import edu.uci.ics.jung.graph.Graph;

public class TestUtil {
	public static String getOutputFileName(String datasetName, String className) {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		String destDir = "out/" + datasetName + "-" + className;

		String destFile = destDir + "-" + timeStamp + ".csv";
		return destFile;
	}

	public static final String DIMACS_PATH = "src/test/resources/DIMACS/";
	public static final String[] DIMACS_FILES = { 
			 "C1000.9.clq", 
			"C125.9.clq", 
			 "C2000.5.clq", 
			 "C2000.9.clq",
			 "C250.9.clq", "C4000.5.clq", "C500.9.clq", "DSJC1000.5.clq",
			 "DSJC500.5.clq", "MANN_a27.clq",
			 "MANN_a81.clq", "brock200_2.clq", "brock200_4.clq",
			 "brock400_2.clq", "brock400_4.clq", "brock800_2.clq",
			 "brock800_4.clq", "gen200_p0.9_44.clq", "gen200_p0.9_55.clq",
			 "gen400_p0.9_55.clq", "gen400_p0.9_65.clq",
			 "gen400_p0.9_75.clq", "hamming10-4.clq", "hamming8-4.clq",
			 "keller4.clq", "keller5.clq", "keller6.clq",
			 "p_hat1500-1.clq", "p_hat1500-2.clq", "p_hat1500-3.clq",
			 "p_hat300-1.clq", "p_hat300-2.clq",
			 "p_hat300-3.clq", "p_hat700-1.clq", "p_hat700-2.clq",
			 "p_hat700-3.clq"

	};

	public static final String BHOSLIB_PATH = "src/test/resources/BHOSLIB/";
	public static final String[] BHOSLIB_FILES = { "frb30-15-mis/frb30-15-1.mis", "frb30-15-mis/frb30-15-2.mis",
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

	public static final String KONECT_PATH = "src/test/resources/KONECT/";
	public static final String[] KONECT_FILES = { 
			"000027_zebra.konet", 
			"000034_zachary.konet", 
			"000062_dolphins.konet",
			"000112_David_Copperfield.konet", "000198_Jazz_musicians.konet", "000212_pdzbase.konet",
			"001133_rovira.konet", 
			"001174_euroroad.konet",
			"001858_hamster.konet", 
			};

	public static List<String[]> simpleAM0() {
		List<String[]> am = new ArrayList<String[]>();
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "1", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "1", "0", "1", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "1", "0", "0", "1", "1", "1", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "1", "0", "0", "0", "0", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "1", "0", "0", "0", "1", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "1", "0", "0", "0", "1", "0", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "1", "1", "0", "1", "0", "0", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "1", "1", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "1", "0", "0" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "0", "1", "1" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "0", "1" });
		AlgorithmUtil.addElementToList(am,
				new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "0" });
		return am;
	}

	/**
	 * 
	 * @param inputFile
	 * @param destFile
	 * @param ag
	 * @param log
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */

	public static <V,E> void run(String inputFile, String destFile, Graph<V, E> g, IGreedyDS<V> ag, Logger log)
			throws InterruptedException, IOException, FileNotFoundException {

		Result r = ag.run();

		List<V> ds = ag.getDominatingSet();
		Assert.assertTrue(AlgorithmUtil.isDS(g, ds));

		StringBuffer sb=new StringBuffer();
		sb.append(inputFile).append(AlgorithmUtil.COMMA).append(r.getString());
		String sbStr=sb.toString();
		log.debug(sbStr);
		if (destFile != null) {
			FileOperation.saveCVSFile(destFile, sbStr);
		}

	}
	
}