package au.edu.cdu.dynamicproblems.algorithm.order;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;

public class PriorityUtility implements IPriority {

	@Override
	public <V,E> float getPriority(PriorityBean<V,E> pb,V v) {
//		Collection<V> vNeigs = pb.getG().getNeighbors(v);
//		int priority = 0;
//		for (V u : vNeigs) {
//			if (!pb.getDominatedMap().get(u)) {
//				priority++;
//			}
//		}
//		return priority;
		return AlgorithmUtil.getVertexUtility(pb.getG(), v, pb.getDominatedMap());
	}

}
