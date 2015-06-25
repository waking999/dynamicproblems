package au.edu.cdu.dynamicproblems.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import au.edu.cdu.dynamicproblems.algorithm.DDSFPT;
import au.edu.cdu.dynamicproblems.algorithm.DSGreedy;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskContainer;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.FileOperation;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;
import edu.uci.ics.jung.graph.Graph;

public class DDSFPTMain extends AbstractAlgorithmMain {
	private static Logger log = LogUtil.getLogger(DDSFPT.class);
	protected static Map<String, String[]> inputFileMap = new HashMap<String, String[]>();
	static {

		// inputFileMap.put("1000_0.3_2_testcase", new String[] {
		// "src/main/resources/1000/1000_0.3_2_testcase_a.csv",
		// "src/main/resources/1000/1000_0.3_2_testcase_b.csv" });

		// inputFileMap.put("2000_0.3_1_testcase", new String[] {
		// "src/main/resources/2000/2000_0.3_1_testcase_a.csv",
		// "src/main/resources/2000/2000_0.3_1_testcase_b.csv" });

		inputFileMap.put("2000_0.2_1_testcase", new String[] {
				"src/main/resources/2000/2000_0.2_1_testcase_a.csv",
				"src/main/resources/2000/2000_0.2_1_testcase_b.csv" });
	}

	public static void main(String[] args) throws Exception {

		new DDSFPTMain().runSingleThread();

	}

	void runSingleThread() throws Exception {
		runSingleThread(new IThreadFunc() {
			@Override
			public void threadFunc(ThreadFuncParameter tfp)
					throws MOutofNException, ExceedLongMaxException,
					ArraysNotSameLengthException {
				Graph<Integer, Integer> g2 = AlgorithmUtil.prepareGraph(tfp
						.getAm());
				DDSFPT ag2 = new DDSFPT(tfp.getIndicator(), g2, tfp.getDs(),
						tfp.getR());
				Result result = null;

				ag2.computing();

				result = ag2.getResult(tfp.getR());

				log.debug(result.getString());
			}
		});
	}

	void runMultiThread() throws Exception {
		runMultiThread(new IThreadFunc() {
			@Override
			public void threadFunc(ThreadFuncParameter tfp)
					throws MOutofNException, ExceedLongMaxException,
					ArraysNotSameLengthException {
				Graph<Integer, Integer> g2 = AlgorithmUtil.prepareGraph(tfp
						.getAm());
				ITask t = new DDSFPT(tfp.getIndicator(), g2, tfp.getDs(),
						tfp.getR());
				tfp.getTc().putTasks(t);
			}
		});
	}

	void runMajority(TaskContainer tc, IThreadFunc tf)
			throws FileNotFoundException, IOException, MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException {
		Set<String> keySet = inputFileMap.keySet();
		for (String key : keySet) {

			String[] inputFiles = inputFileMap.get(key);

			// greedy to get ds1
			List<String[]> am1 = IOUtil.getAMFromFile(inputFiles[0]);

			DSGreedy ag1 = new DSGreedy(key, am1);
			ag1.computing();
			List<Integer> ds1 = ag1.getDominatingSet();

			FileOperation fileOperation2 = IOUtil.getProblemInfo(inputFiles[1]);
			List<String[]> am2 = fileOperation2.getAdjacencyMatrix();

			int k = fileOperation2.getK();

			for (int r = 1; r <= k; r++) {
				ThreadFuncParameter tfp = new ThreadFuncParameter();
				tfp.setAm(am2);
				tfp.setTc(tc);
				tfp.setIndicator(key);
				tfp.setDs(ds1);
				tfp.setR(r);
				tf.threadFunc(tfp);
			}
		}

	}

}
