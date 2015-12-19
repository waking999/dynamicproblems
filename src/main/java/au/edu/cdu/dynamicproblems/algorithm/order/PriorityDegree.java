package au.edu.cdu.dynamicproblems.algorithm.order;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;

public class PriorityDegree implements IPriority {

	@Override
	public <V,E> float getPriority(PriorityBean<V,E> pb,V v) {
		//return pb.getG().degree(v);
		return AlgorithmUtil.getVertexDegree(pb.getG(), v);
	}

}
