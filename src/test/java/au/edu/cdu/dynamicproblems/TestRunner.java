package au.edu.cdu.dynamicproblems;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import au.edu.cdu.dynamicproblems.util.LogUtil;



/**
 * It is a entrance to run all available test classes
 * 
 * @author Kai Wang
 * 
 * 
 */
public class TestRunner {
	static Logger log = LogUtil.getLogger(TestRunner.class);

	/**
	 * the main method to run all available tests
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(AssistantSuite.class);
		// log the result
		for (Failure failure : result.getFailures()) {
			
			log.info(failure.getDescription());
		}
		 System.out.println(result.wasSuccessful());
	}
}
