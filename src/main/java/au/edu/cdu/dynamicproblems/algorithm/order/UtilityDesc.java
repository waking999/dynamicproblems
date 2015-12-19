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
public class UtilityDesc<V,E> implements IPriorityOrder<V, E> {
	@Override
	public List<V> getOrderedVertexList(PriorityBean<V,E> pb){
		IPriority pcb=new PriorityUtility();
		IOrder<V> ocb = new OrderDesc<V>();
		/*there is no need for a weight map (<vertex(V), weigh(float)>) for ordering by degree*/
		List<V> vList=OrderPackageUtil.getOrderedVertexList(pb, pcb, ocb);
		return vList;
	}
}
