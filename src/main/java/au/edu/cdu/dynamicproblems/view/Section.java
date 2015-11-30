package au.edu.cdu.dynamicproblems.view;

import java.util.Collection;

public class Section {
	float left;
	float top;
	float right;

	public Collection<Integer> getVertices() {
		return vertices;
	}

	float bottom;
	Collection<Integer> vertices;

	public Section(float left, float top, float right, float bottom,
			Collection<Integer> vertices) {

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