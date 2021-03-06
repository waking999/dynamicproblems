package au.edu.cdu.dynamicproblems.algorithm.ds.va;

import java.util.List;

import au.edu.cdu.dynamicproblems.algorithm.IAlgorithm;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
@Deprecated
public interface IGreedyDSVA extends IAlgorithm {

	public List<Integer> getDs();

	public void computing() throws MOutofNException, ExceedLongMaxException,
	ArraysNotSameLengthException ,InterruptedException;
	
	public Result getResult(long threadId);
	
	public void setIndicator(String indicator) ;

	public void setK(int k) ;

	public void setR(int r) ;
	
	public void setAm(List<String[]> am);
	
	public String getIndicator() ; 
	
	public long getRunningTime();
}
