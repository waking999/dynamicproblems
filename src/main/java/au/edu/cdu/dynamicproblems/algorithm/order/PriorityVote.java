package au.edu.cdu.dynamicproblems.algorithm.order;

import java.util.Map;

public class PriorityVote implements IPriority {

	@Override
	public <V, E> float getPriority(PriorityBean<V, E> pb, V v) {
//		Graph<V, E> g = pb.getG();
//		Map<V, Boolean> dominatedMap = pb.getDominatedMap();
		Map<V, Float> weightMap = pb.getWeightMap();
//		Map<V, Float> voteMap = pb.getVoteMap();
//
//		Collection<V> vNeigs = g.getNeighbors(v);
//		boolean coveredv = dominatedMap.get(v);
//		weightMap.put(v, 0.0f);
//		float votev = voteMap.get(v);
//		for (V u : vNeigs) {
//			float weightu = weightMap.get(u);
//			if (weightu - 0.0f > VertexPriority.ZERO_DIFF) {
//				float voteu = voteMap.get(u);
//				if (!coveredv) {
//					weightMap.put(u, weightu - votev);
//				}
//				boolean coveredu = dominatedMap.get(u);
//				if (!coveredu) {
//					dominatedMap.put(u, true);
//					weightu = weightMap.get(u);
//					weightMap.put(u, weightu - voteu);
//
//					Collection<V> uNeigs = g.getNeighbors(u);
//					for (V w : uNeigs) {
//						float weightw = weightMap.get(w);
//						if (weightu - 0.0f > VertexPriority.ZERO_DIFF) {
//							weightMap.put(w, weightw - voteu);
//						}
//					}
//
//				}
//			}
//
//		}
//		dominatedMap.put(v, true);
//		
//		float weight=weightMap.get(v);
//		return weight;
		return weightMap.get(v);
	}

}
