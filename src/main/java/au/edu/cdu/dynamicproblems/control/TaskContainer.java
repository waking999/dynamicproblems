package au.edu.cdu.dynamicproblems.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;

public class TaskContainer {

	private List<Future> futureList = new ArrayList<Future>();
	private TaskLock lock;
	private ExecutorService container;

	public TaskContainer(int containerSize, boolean join) {
		container = Executors.newFixedThreadPool(containerSize);
		lock = new TaskLock(containerSize, join);
	}

	public <R> void putTasks(ITask task) {
	    task.setLock(lock);
		
		lock.getSemaphore().acquireUninterruptibly();
		Future future = container.submit(task);
		if (lock.isJoin()) {
			//futureList.add(future);
			AlgorithmUtil.addElementToList(futureList, future);
			synchronized (lock) {
				int taskCount = lock.getTaskCount();
				lock.setTaskCount(taskCount + 1);
			}
		}

	}

	public <R> List<R> joinTasks() throws InterruptedException,
			ExecutionException {
		synchronized (lock) {
			while (lock.getTaskCount() > 0) {
				lock.wait();
			}
		}

		List<R> list = new ArrayList<R>();

		for (Future f : futureList) {
			Object o = f.get();
			if (o != null) {
				if (o instanceof Collection) {
					list.addAll((Collection<R>) o);
				} else {
					list.add((R) o);
				}
			}
		}

		return list;
	}

	public void close() {
		if (container != null) {
			container.shutdown();
		}
	}

}
