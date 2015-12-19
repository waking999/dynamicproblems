package au.edu.cdu.dynamicproblems.algorithm.order;

import java.util.List;

/**
 * 
 * @author kwang1 A class for implementing ordering vertices by degree from low
 *         to high;
 *
 * @param <V>
 *            generic type of vertex
 * @param <E>
 *            generic type of edge
 */
public class WeightAsc<V, E> implements IPriorityOrder<V, E> {
	@Override
	public List<V> getOrderedVertexList(PriorityBean<V, E> pb) {
		IPriority pcb = new PriorityWeight();
		IOrder<V> ocb = new OrderAsc<V>();
		List<V> vList=OrderPackageUtil.getOrderedVertexList(pb, pcb, ocb);
		return vList;
	}
}
