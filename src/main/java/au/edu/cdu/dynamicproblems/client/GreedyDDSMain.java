package au.edu.cdu.dynamicproblems.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.algorithm.GreedyDDS;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskContainer;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.io.IOUtil;
import au.edu.cdu.dynamicproblems.util.LogUtil;

public class GreedyDDSMain extends AbstractAlgorithmMain {
	private static Logger log = LogUtil.getLogger(GreedyDDSMain.class);

	private static Map<String, String> inputFileMap = new HashMap<String, String>();
	static {
		inputFileMap.put("50_0.3_testcase_a",
				"src/main/resources/50_0.3_testcase_a.csv");

		// inputFileMap.put("1000_0.3_2_testcase_b",
		// "src/main/resources/1000/1000_0.3_2_testcase_b.csv");

		// inputFileMap.put("2000_0.3_1_testcase_b",
		// "src/main/resources/2000/2000_0.3_1_testcase_b.csv");

		// inputFileMap.put("2000_0.2_1_testcase_b",
		// "src/main/resources/2000/2000_0.2_1_testcase_b.csv");
	}

	public static void main(String[] args) throws Exception {

		new GreedyDDSMain().runSingleThread();

	}

	@Override
	void runSingleThread() throws Exception {
		runSingleThread(new IThreadFunc() {
			@Override
			public void threadFunc(ThreadFuncParameter tfp)
					throws MOutofNException, ExceedLongMaxException,
					ArraysNotSameLengthException {

				GreedyDDS ag = new GreedyDDS(tfp.getIndicator(), tfp.getAm(),
						tfp.getK(), tfp.getR());
				Result result = null;

				ag.computing();

				result = ag.getResult(tfp.getR());

				log.debug(result.getString());
			}
		});

	}

	@Override
	void runMultiThread() throws Exception {

	}

	@Override
	void runMajority(TaskContainer tc, IThreadFunc tf)
			throws FileNotFoundException, IOException, MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException {
		Set<String> keySet = inputFileMap.keySet();
		for (String key : keySet) {

			String inputFile = inputFileMap.get(key);
			List<String[]> am = IOUtil.getAMFromFile(inputFile);

			int kRang1 = 2;
			int kRang2 = 2;

			for (int k = kRang1; k <= kRang2; k++) {
				// r starting from 2 because it is said in the dds paper that we
				// can assume |D2| >=2 else it is trivial to decide.
				for (int r = 2; r <= k; r++) {

					// int k = 500;
					// int r = 3;
					
					ThreadFuncParameter tfp = new ThreadFuncParameter();
					tfp.setAm(am);
					tfp.setIndicator(key);
					tfp.setTc(tc);
					tfp.setK(k);
					tfp.setR(r);
					tf.threadFunc(tfp);
				}
			}

		}

	}

}
