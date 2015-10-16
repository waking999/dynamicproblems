package au.edu.cdu.dynamicproblems.algorithm;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<Integer> {
	Map<Integer, Integer> base;

	public ValueComparator(Map<Integer, Integer> base) {
	        this.base = base;
	    }

	public int compare(Integer a, Integer b) {
		if(a.equals(b)){
	        return 0;
	    }
		if (base.get(a).intValue() < base.get(b).intValue()) {
			return -1;
		} else if (base.get(a).intValue() == base.get(b).intValue()){
			return a.intValue()-b.intValue();
		}else {
			return 1;
		} // returning 0 would merge keys
	}

}
