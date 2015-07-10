package au.edu.cdu.dynamicproblems;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtilTest;
import au.edu.cdu.dynamicproblems.algorithm.DDSFPTTest;
import au.edu.cdu.dynamicproblems.algorithm.DSGreedyNativeTest;
import au.edu.cdu.dynamicproblems.control.TaskTest;
import au.edu.cdu.dynamicproblems.io.IOUtilTest;

/**
 * A suite to accommodate all available tests, which will be called in test
 * runner
 * 
 * @author Kai Wang
 * 
 * 
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ AlgorithmUtilTest.class, DSGreedyNativeTest.class,
		DDSFPTTest.class, IOUtilTest.class, TaskTest.class })
public class TestSuit {

}
