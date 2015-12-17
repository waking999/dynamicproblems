package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.List;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import edu.uci.ics.jung.graph.Graph;
/**
 * 
 * @author kwang1
 * A class for implementing ordering vertices by degree from high to low; 
 *
 * @param <V> generic type of vertex
 * @param <E> generic type of edge
 */
public class DegreeDesc<V,E> implements IPriorityOrder<V, E> {
	@Override
	public List<V> getOrderedVertexList(Graph<V, E> g){
		IPriorityCallBack pcb=new PriorityDegree();
		IOrderCallBack<V> ocb = new OrderDesc<V>();
		/*there is no need for a dominated map (<vertex(V), dominated(boolean)>) for ordering by degree*/
		List<V> vList=AlgorithmUtil.getOrderedVertexList(g, pcb, ocb,null);
		return vList;
	}
}
