package au.edu.cdu.dynamicproblems.algorithm.common;

public class OrderAsc<V> implements IOrderCallBack<V>{
	public IValueComparator<V> getComparator(){
		return new ValueComparatorAsc<V>();
	}
	
}
