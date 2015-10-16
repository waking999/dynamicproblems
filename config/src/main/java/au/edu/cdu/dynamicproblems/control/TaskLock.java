package au.edu.cdu.dynamicproblems.control;

import java.util.concurrent.Semaphore;

public class TaskLock {
	private boolean join=false;
	private int taskCount;
//	private boolean hasSolution;
//	
//	public boolean hasSolution() {
//		return hasSolution;
//	}

//	public void setHasSolution(boolean hasSolution) {
//		this.hasSolution = hasSolution;
//	}

	private Semaphore semaphore;
	
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public TaskLock(int containerSize,boolean join){
		semaphore = new Semaphore(containerSize);
		this.join=join;
	}

	public boolean isJoin() {
		return join;
	}


	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	
}
