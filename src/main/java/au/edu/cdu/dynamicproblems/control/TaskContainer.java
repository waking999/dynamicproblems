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

	private List<Future<Result>> futureList = new ArrayList<Future<Result>>();
	private TaskLock lock;
	private ExecutorService container;

	public TaskContainer(int containerSize, boolean join) {
		container = Executors.newFixedThreadPool(containerSize);
		lock = new TaskLock(containerSize, join);
	}

	public void putTasks(ITask task) {
	    task.setLock(lock);
		
		lock.getSemaphore().acquireUninterruptibly();
		Future<Result> future = container.submit(task);
		if (lock.isJoin()) {
			//futureList.add(future);
			AlgorithmUtil.addElementToList(futureList, future);
			synchronized (lock) {
				int taskCount = lock.getTaskCount();
				lock.setTaskCount(taskCount + 1);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public  List<Result> joinTasks() throws InterruptedException,
			ExecutionException {
		synchronized (lock) {
			while (lock.getTaskCount() > 0) {
				lock.wait();
			}
		}

		List<Result> list = new ArrayList<Result>();

		for (Future<Result> f : futureList) {
			Object o = f.get();
			if (o != null) {
				if (o instanceof Collection) {
					list.addAll((Collection<Result>) o);
				} else {
					list.add((Result) o);
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
