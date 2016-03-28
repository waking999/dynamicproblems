package au.edu.cdu.dynamicproblems.control;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.junit.Test;

import au.edu.cdu.dynamicproblems.util.LogUtil;

public class TaskTest {
	private Logger log = LogUtil.getLogger(TaskTest.class);
	//@Ignore
	@Test
	public void testMulTask() throws ExecutionException, InterruptedException {

		TaskContainer tc = new TaskContainer(50, true);

		for (int i = 0; i < 5; i++) {
			tc.putTasks(new TestTask(i));
		}
		List<Result> l = tc.joinTasks();
		for (Result result : l) {
			log.debug(result.getString());
		}

		tc.close();
	}
}

class TestTask implements ITask {
	private Logger log = LogUtil.getLogger(TestTask.class);
	private TaskLock lock;

	public TaskLock getLock() {
		return lock;
	}

	public void setLock(TaskLock lock) {
		this.lock = lock;
	}

	int index = 0;

	public TestTask(int index) {
		this.index = index;
	}

	public Result run() {
		log.debug("current thread id = " + Thread.currentThread().getId());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		Result r = new Result();
		r.setString(index + ":" + Thread.currentThread().getId());
		return r;
	}
}