package au.edu.cdu.dynamicproblems.view;

import java.util.List;

public class Section<V> {
	float left;
	float top;
	float right;

	public List<V> getVertices() {
		return vertices;
	}

	float bottom;
	List<V> vertices;

	public Section(float left, float top, float right, float bottom,
			List<V> vertices) {

		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.vertices = vertices;

	}

	public float getLeft() {
		return left;
	}

	public float getTop() {
		return top;
	}

	public float getRight() {
		return right;
	}

	public float getBottom() {
		return bottom;
	}
}