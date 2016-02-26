package au.edu.cdu.dynamicproblems;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyDSM0DUTest;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyDSMHDUTest;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyIterativeTest;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyNativeTest;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyVoteGrTest;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyVoteTest;

/**
 * A suite to accommodate all available tests, which will be called in test
 * runner
 * 
 * @author Kai Wang
 * 
 * 
 */

@RunWith(Suite.class)
// algorithm classes tests
@Suite.SuiteClasses({ GreedyNativeTest.class, GreedyIterativeTest.class, GreedyVoteTest.class, GreedyVoteGrTest.class,
		GreedyDSM0DUTest.class, GreedyDSMHDUTest.class })
public class AlgorithmSuite {

}
