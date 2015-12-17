package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.Map;

import edu.uci.ics.jung.graph.Graph;

public interface IPriorityCallBack {
	public <V,E> float getPriority(Graph<V,E> g,V v, Map<V, Boolean> dominatedMap);
}
