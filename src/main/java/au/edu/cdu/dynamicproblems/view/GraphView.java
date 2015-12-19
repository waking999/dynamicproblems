package au.edu.cdu.dynamicproblems.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import au.edu.cdu.dynamicproblems.algorithm.AlgorithmUtil;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * A class that shows the minimal work necessary to load and visualize a graph.
 */
public class GraphView {



	public static <V,E> void presentGraph(Graph<V, E> g,
			List<Section<V>> sections,float width,float height) {

		Transformer<V, Point2D> locationTransformer = new LocationTransformer<V>(
				sections);

		StaticLayout<V, E> layout = new StaticLayout<V, E>(
				g, locationTransformer);

		Transformer<V, Paint> vertexPaint = new Transformer<V, Paint>() {
			public Paint transform(V i) {
				return Color.GREEN;
			}
		};
		VisualizationViewer<V, E> vv = new VisualizationViewer<V, E>(
				layout);
		Dimension dim = new Dimension();
		dim.setSize(width, height);
		vv.setPreferredSize(dim);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<V>());

		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame jf = new JFrame();
		jf.getContentPane().add(vv);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
}

class LocationTransformer<V> implements Transformer<V, Point2D> {
	public LocationTransformer(List<Section<V>> sections) {
		this.sections = sections;
	}

	private List<Section<V>> sections;

	
	public Point2D transform(V vertex) {

		float x = 0;
		float y = 0;
		for (Section<V> section : sections) {
			List<V> vertices = section.getVertices();
			if (vertices.contains(vertex)) {
				x = AlgorithmUtil.randomInRang(section.getLeft(),
						section.getRight());
				y = AlgorithmUtil.randomInRang(section.getTop(),
						section.getBottom());
				break;
			}
		}

		Point2D point = new Point2D.Float( x,  y);
		return point;
	}
}


