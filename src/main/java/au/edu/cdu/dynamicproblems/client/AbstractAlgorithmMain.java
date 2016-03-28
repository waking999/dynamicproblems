package au.edu.cdu.dynamicproblems.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskContainer;
import au.edu.cdu.dynamicproblems.exception.ArraysNotSameLengthException;
import au.edu.cdu.dynamicproblems.exception.ExceedLongMaxException;
import au.edu.cdu.dynamicproblems.exception.MOutofNException;
import au.edu.cdu.dynamicproblems.exception.NChooseMNoSolutionException;
import au.edu.cdu.dynamicproblems.util.LogUtil;

public abstract class AbstractAlgorithmMain {
	private static Logger log = LogUtil.getLogger(AbstractAlgorithmMain.class);

	abstract void runSingleThread() throws Exception;

	abstract void runMultiThread() throws Exception;

	abstract void runMajority(TaskContainer tc, IThreadFunc tf)
			throws FileNotFoundException, IOException, MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException;

	void runSingleThread(IThreadFunc tf) throws InterruptedException,
			ExecutionException, FileNotFoundException, IOException,
			MOutofNException, NChooseMNoSolutionException,
			ExceedLongMaxException, ArraysNotSameLengthException {

		TaskContainer tc = null;

		runMajority(tc, tf);

	}

	void runMultiThread(IThreadFunc tf) throws InterruptedException,
			ExecutionException, FileNotFoundException, IOException,
			MOutofNException, ExceedLongMaxException,
			ArraysNotSameLengthException {

		int taskContainerSize = 20;

		TaskContainer tc = new TaskContainer(taskContainerSize, true);

		runMajority(tc, tf);

		List<Result> l = tc.joinTasks();

		for (Result r : l) {
			log.info(r.getString());
		}

	}

}

@FunctionalInterface
interface IThreadFunc {

	public void threadFunc(ThreadFuncParameter tfp) throws MOutofNException,
			ExceedLongMaxException, ArraysNotSameLengthException;

}

class ThreadFuncParameter {
	private TaskContainer tc;
	private String indicator;
	private List<String[]> am;
	private List<Integer> ds;
	private int k;
	private int r;
	public TaskContainer getTc() {
		return tc;
	}
	public void setTc(TaskContainer tc) {
		this.tc = tc;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	public List<String[]> getAm() {
		return am;
	}
	public void setAm(List<String[]> am) {
		this.am = am;
	}
	public List<Integer> getDs() {
		return ds;
	}
	public void setDs(List<Integer> ds) {
		this.ds = ds;
	}
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
}
