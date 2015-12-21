package au.edu.cdu.dynamicproblems;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyDSM0Test;
import au.edu.cdu.dynamicproblems.algorithm.ds.GreedyDSMVSTest;
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
		GreedyDSM0Test.class, GreedyDSMVSTest.class })
public class AlgorithmSuite {

}