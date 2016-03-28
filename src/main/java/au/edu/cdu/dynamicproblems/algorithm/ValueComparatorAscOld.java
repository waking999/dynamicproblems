package au.edu.cdu.dynamicproblems.algorithm;

import java.util.Comparator;
import java.util.Map;

@Deprecated
public class ValueComparatorAscOld implements Comparator<Integer> {
	Map<Integer, Integer> base;

	public ValueComparatorAscOld(Map<Integer, Integer> base) {
	        this.base = base;
	    }

	public int compare(Integer a, Integer b) {
		if (base.get(a) <= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}

}
