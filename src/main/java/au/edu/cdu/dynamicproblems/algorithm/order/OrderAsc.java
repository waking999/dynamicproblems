package au.edu.cdu.dynamicproblems.algorithm.order;

public class OrderAsc<V> implements IOrder<V>{
	public IValueComparator<V> getComparator(){
		return new ValueComparatorAsc<V>();
	}
	
}
