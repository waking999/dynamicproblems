package au.edu.cdu.dynamicproblems.algorithm.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DegreeRRReturn<V> {
	private List<V> dsAfterDegreeRR;

	public List<V> getDsAfterDegreeRR() {
		return dsAfterDegreeRR;
	}

	public Collection<V> getVerticesAfterDegreeRR() {
		return verticesAfterDegreeRR;
	}

	private Collection<V> verticesAfterDegreeRR;

	private Map<V, Boolean> dominatedMap;

	public Map<V, Boolean> getDominatedMap() {
		return dominatedMap;
	}

	public DegreeRRReturn(List<V> dsAfterDegreeRR, Collection<V> verticesAfterDegreeRR, Map<V, Boolean> dominatedMap) {
		this.dsAfterDegreeRR = dsAfterDegreeRR;
		this.verticesAfterDegreeRR = verticesAfterDegreeRR;
		this.dominatedMap = dominatedMap;
	}
}
