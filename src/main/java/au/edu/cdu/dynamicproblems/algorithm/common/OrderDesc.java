package au.edu.cdu.dynamicproblems.algorithm.common;

public class OrderDesc<V> implements IOrderCallBack<V>{
	public IValueComparator<V> getComparator(){
		return new ValueComparatorDesc<V>();
	}
	
}
