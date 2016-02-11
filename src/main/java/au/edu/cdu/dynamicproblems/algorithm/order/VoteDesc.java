package au.edu.cdu.dynamicproblems.algorithm.order;

import java.util.List;
/**
 * 
 * @author kwang1
 * A class for implementing ordering vertices by degree from high to low; 
 *
 * @param <V> generic type of vertex
 * @param <E> generic type of edge
 */
public class VoteDesc<V,E> implements IPriorityOrder<V, E> {
	@Override
	public List<V> getOrderedVertexList(PriorityBean<V,E> pb){
		IPriority pcb = new PriorityVote();
		IOrder<V> ocb = new OrderDesc<V>();
		List<V> vList=OrderPackageUtil.getOrderedVertexList(pb, pcb, ocb);
		return vList;
	}
}
