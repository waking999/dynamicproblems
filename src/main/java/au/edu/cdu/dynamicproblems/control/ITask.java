package au.edu.cdu.dynamicproblems.control;

import java.util.concurrent.Callable;

public interface ITask extends Callable<Result> {

	@Override
	default Result call() throws Exception {
		Result rtn = null;
		TaskLock lock = this.getLock();

		rtn = this.run();

		
		if (lock.isJoin()) {
			synchronized (lock) {
				
				
				int taskCount = lock.getTaskCount();
				lock.setTaskCount(taskCount - 1);
				lock.notifyAll();
				
				
			}
		}
		lock.getSemaphore().release();
		return rtn;
	}

	public Result run() throws InterruptedException;

	public void setLock(TaskLock lock);

	public TaskLock getLock();

}
