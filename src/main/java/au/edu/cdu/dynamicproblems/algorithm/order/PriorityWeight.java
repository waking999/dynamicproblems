package au.edu.cdu.dynamicproblems.algorithm.order;

public class PriorityWeight implements IPriority {

	@Override
	public <V, E> float getPriority(PriorityBean<V,E> pb,V v) {
	
		return pb.getWeightMap().get(v);
	}

}
