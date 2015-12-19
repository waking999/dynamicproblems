package au.edu.cdu.dynamicproblems.algorithm.ds;

import java.util.List;
import java.util.Map;

import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.control.Result;

public interface IGreedyDS<V> extends IAlgorithm{

	public Result run() throws InterruptedException;
	
	public List<V> getDominatingSet() ;
	
	public Map<String, Long> getRunningTimeMap();
}
