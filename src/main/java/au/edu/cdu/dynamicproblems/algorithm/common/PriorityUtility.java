package au.edu.cdu.dynamicproblems.algorithm.common;

import java.util.Collection;
import java.util.Map;

import edu.uci.ics.jung.graph.Graph;

public class PriorityUtility implements IPriorityCallBack {

	@Override
	public <V,E> float getPriority(Graph<V, E> g, V v, Map<V, Boolean> dominatedMap) {
		Collection<V> vNeigs = g.getNeighbors(v);
		int priority = 0;
		for (V u : vNeigs) {
			if (!dominatedMap.get(u)) {
				priority++;
			}
		}
		return priority;
	}

}
