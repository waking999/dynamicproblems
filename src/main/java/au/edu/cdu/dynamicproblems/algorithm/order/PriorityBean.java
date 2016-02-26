package au.edu.cdu.dynamicproblems.algorithm.order;

import java.util.Map;

import edu.uci.ics.jung.graph.Graph;

public class PriorityBean <V,E> {
	private Graph<V,E> g;
	
	private Map<V, Boolean> dominatedMap;
	
	private Map<V, Float> weightMap;
	
	private Map<V, Float> voteMap;


	public Graph<V, E> getG() {
		return g;
	}

	
	public PriorityBean(Graph<V, E> g, Map<V, Boolean> dominatedMap, Map<V, Float> weightMap) {
		super();
		this.g = g;
		this.dominatedMap = dominatedMap;
		this.weightMap = weightMap;
	
	}
	
	public PriorityBean(Graph<V, E> g, Map<V, Boolean> dominatedMap, Map<V, Float> weightMap, Map<V, Float> voteMap) {
		super();
		this.g = g;
		this.dominatedMap = dominatedMap;
		this.weightMap = weightMap;
		this.voteMap=voteMap;
	
	}

	public Map<V, Float> getVoteMap() {
		return voteMap;
	}

	public Map<V, Boolean> getDominatedMap() {
		return dominatedMap;
	}

	public Map<V, Float> getWeightMap() {
		return weightMap;
	}

	
}
