package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.Comparator;

public interface IValueComparator<V> extends Comparator<VertexPriority<V>> {

	public int compare(VertexPriority<V>  a, VertexPriority<V>  b) ;

}
