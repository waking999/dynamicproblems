package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.Map;

import edu.uci.ics.jung.graph.Graph;

public class PriorityDegree implements IPriorityCallBack {

	@Override
	public <V,E> float getPriority(Graph<V, E> g,V v, Map<V, Boolean> dominatedMap) {
		return g.degree(v);
	}

}
