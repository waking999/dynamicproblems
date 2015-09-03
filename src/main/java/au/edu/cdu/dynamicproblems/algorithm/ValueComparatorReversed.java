package au.edu.cdu.dynamicproblems.algorithm;

import java.util.Comparator;
import java.util.Map;

public class ValueComparatorReversed implements Comparator<Integer> {
	Map<Integer, Integer> base;

	public ValueComparatorReversed(Map<Integer, Integer> base) {
	        this.base = base;
	    }

	public int compare(Integer a, Integer b) {
		if (base.get(a) <= base.get(b)) {
			return 1;
		} else {
			return -1;
		} // returning 0 would merge keys
	}

}
