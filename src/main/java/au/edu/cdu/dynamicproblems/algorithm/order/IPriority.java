package au.edu.cdu.dynamicproblems.algorithm.order;
/**
 * The class to represent priority (degree/utility/weight...)
 * @author kwang1
 *
 */
public interface IPriority {
	public <V,E> float getPriority(PriorityBean<V,E> pb,V v);
}
