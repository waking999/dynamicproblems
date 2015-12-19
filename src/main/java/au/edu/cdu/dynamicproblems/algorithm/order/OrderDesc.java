package au.edu.cdu.dynamicproblems.algorithm.order;

public class OrderDesc<V> implements IOrder<V>{
	public IValueComparator<V> getComparator(){
		return new ValueComparatorDesc<V>();
	}
	
}
