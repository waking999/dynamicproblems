package au.edu.cdu.dynamicproblems.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;

import au.edu.cdu.dynamicproblems.control.ITask;
import au.edu.cdu.dynamicproblems.control.Result;
import au.edu.cdu.dynamicproblems.control.TaskLock;

public class SCDP implements IAlgorithm, ITask {

	// private static Logger log = LogUtil.getLogger(SCDP.class);
	private long runningTime;

	private TaskLock lock;

	@Override
	public TaskLock getLock() {
		return lock;
	}

	public void setLock(TaskLock lock) {
		this.lock = lock;
	}

	public Result run() throws InterruptedException {

		computing();
		Thread.sleep(1000);
		Result r = getResult();

		return r;
	}

	public Result getResult() {
		Result r = new Result();
		r.setHasSolution(true);
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass()).append(":").append(this.hasSolution)
				.append(":").append(this.runningTime);
		if (this.hasSolution) {
			for (List<Integer> s : SC) {
				sb.append("{");
				for (int i : s) {
					sb.append(i + " ");
				}

				sb.append("}");
			}
		}

		r.setString(sb.toString());
		return r;
	}

	public void computing() {
		long start = System.nanoTime();
		initialization();
		dp();
		long end = System.nanoTime();
		this.runningTime = end - start;

	}

	private List<List<Integer>> F;
	private List<Integer> U;
	private int r;
	private List<List<Integer>> SC;

	public List<List<Integer>> getSC() {
		return SC;
	}

	private boolean hasSolution;

	public boolean isHasSolution() {
		return hasSolution;
	}

	public SCDP(List<List<Integer>> F, List<Integer> U, int r) {
		this.F = F;
		this.U = U;
		this.r = r;
	}

	private void initialization() {
		this.SC = new ArrayList<List<Integer>>();
	}

	private void dp() {
		/*
		 * Theorem 1. Set Cover can be solved in O? (2|U| ) time. We’ll build
		 * easy dynamic algorithm working in above-mentioned time. Assuming that
		 * F = {S1, S2, . . . , Sm} let’s create a dynamic array that meets the
		 * following constraint: T[i][X] is equal to minimal number of sets out
		 * of {S1, S2, . . . , Si} that covers X for every X ⊆ U and i ∈ {1, 2,
		 * . . . , m}. If we are able to keep this constraint we will find the
		 * solution clearly in T[m][U], since this field contains the minimal
		 * number of sets out of {S1, S2, . . . , Sm} that covers the whole
		 * universum U. To fill the array we can use the following recursion:
		 * T[0][∅] = 0 T[0][X 6= ∅] = ∞ T[i][X] = min(1 + T[i − 1][X\Si ], T[i −
		 * 1][X]) The reasoning is quite straighforward: in the latest equation
		 * we can either use or not use set Si . In case we decide to use it, we
		 * still have to cover set X\Si using S1, S2, . . . , Si−1. But we have
		 * already used Si , so we have to add 1 to the overall result. On the
		 * other hand we may decide not to use Si . In this case we have to
		 * cover the entire set X with remaining sets. Clearly we take minimum
		 * out these two values.
		 */
		// input U={1,2,..k},F={S1,S2,..,Sm},r
		// output SC \subseteq F Usc=U;

		int FSize = F.size();
		if (recursion(FSize, U) <= r) {
			this.hasSolution = true;
		} else {
			this.hasSolution = false;
		}

	}

	private int recursion(int i, List<Integer> X) {

		if (i == 0) {
			if (X.size() == 0) {
				return 0;
			} else {
				return Integer.MAX_VALUE-1;
			}
		} else {
			int fIdx = i-1;
			List<Integer> Si = F.get(fIdx);
			int notUsingSi = recursion(i - 1, X);
			int usingSi = recursion(i - 1,
					(List<Integer>) CollectionUtils.subtract(X, Si));
			int mini = Math.min(1 + usingSi, notUsingSi);
			if (usingSi < notUsingSi) {
				AlgorithmUtil.addElementToList(SC,Si);
			}

			return mini;
		}

	}

}
