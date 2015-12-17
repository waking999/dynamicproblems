package au.edu.cdu.dynamicproblems.algorithm.common;

public class ValueComparatorDesc<V> implements IValueComparator<V> {

	public int compare(VertexPriority<V> a, VertexPriority<V> b) {
		if (a.getPriority() <= b.getPriority()) {
			return 1;
		} else {
			return -1;
		} // returning 0 would merge keys
	}

}
