package au.edu.cdu.dynamicproblems.algorithm;

class Tuple<V1, V2> {

	protected V1 v1;

	protected V2 v2;

	protected Tuple() {

	}

	public Tuple(V1 v1, V2 v2) {

		this.v1 = v1;

		this.v2 = v2;

	}

	public V1 getV1() {

		return v1;

	}

	public V2 getV2() {

		return v2;

	}

}