package au.edu.cdu.dynamicproblems.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.DSGreedy;
import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskContainer;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;

public class DSGreedyMain extends AbstractAlgorithmMain {

	private static Logger log = LogUtil.getLogger(DSGreedyMain.class);

	private static Map<String, String> inputFileMap = new HashMap<String, String>();
	static {
//		inputFileMap.put("50_0.3_testcase_a",
//				"src/main/resources/50_0.3_testcase_a.csv");
				
		 inputFileMap.put("1000_0.3_2_testcase_b",
		 "src/main/resources/1000/1000_0.3_2_testcase_b.csv");

		// inputFileMap.put("2000_0.3_1_testcase_b",
		// "src/main/resources/2000/2000_0.3_1_testcase_b.csv");

//		inputFileMap.put("2000_0.2_1_testcase_b",
//				"src/main/resources/2000/2000_0.2_1_testcase_b.csv");
	}

	public static void main(String[] args) throws Exception {

		new DSGreedyMain().runSingleThread();

	}

	void runSingleThread() throws Exception {
		runSingleThread(new IThreadFunc() {
			@Override
			public void threadFunc(ThreadFuncParameter tfp)
					throws MOutofNException, ExceedLongMaxException,
					ArraysNotSameLengthException {

				DSGreedy ag = new DSGreedy(tfp.getIndicator(), tfp.getAm());
				Result result = null;

				ag.computing();

				result = ag.getResult();

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
				ITask t = new DSGreedy(tfp.getIndicator(), tfp.getAm());
				tfp.getTc().putTasks(t);
			}
		});
	}

	void runMajority(TaskContainer tc, IThreadFunc tf)
			throws FileNotFoundException, IOException, MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException {
		Set<String> keySet = inputFileMap.keySet();
		for (String key : keySet) {

			String inputFile = inputFileMap.get(key);
			List<String[]> am = IOUtil.getAMFromFile(inputFile);
			ThreadFuncParameter tfp = new ThreadFuncParameter();
			tfp.setTc(tc);
			tfp.setIndicator(key);
			tfp.setAm(am);
			tf.threadFunc(tfp);

		}

	}

}
