package au.edu.cdu.dynamicproblems.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.util.Collection;
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

//	public static void main(String[] args) {
//		String inputFile = "src/test/resources/20140523_testcase50_b.csv";
//
//		// read adjacency matrix from file
//		FileInfo fileInfo = new FileInfo();
//		fileInfo.setInputFile(inputFile);
//
//		FileOperation fileOperation = new FileOperation();
//		fileOperation.setFileInfo(fileInfo);
//		fileOperation.retriveProblemInfo();
//
//		List<String[]> am = fileOperation.getAdjacencyMatrix();
//		Graph<Integer, Integer> g = AlgorithmUtil.prepareGraph(am);
//
//		int[] ca = { 2, 4, 5, 6, 11, 12, 16, 22, 26, 30, 42, 41, 46, 47, 49 };
//		int[] ba = { 0, 8, 9, 17, 20, 23, 24, 27, 29, 28, 31, 34, 35, 33, 39,
//				43, 44, 45, 48 };
//		int[] sa = { 37, 10, 13, 38, 1, 40, 3, 7, 14, 19, 18, 32, 15, 21, 36,
//				25 };
//
//		List<Integer> C = new ArrayList<Integer>();
//		List<Integer> B = new ArrayList<Integer>();
//		List<Integer> S = new ArrayList<Integer>();
//
//		for (int c : ca) {
//			C.add(c);
//		}
//
//		for (int b : ba) {
//			B.add(b);
//		}
//
//		for (int s : sa) {
//			S.add(s);
//		}
//
//		float width = 1024;
//		float height = 768;
//
//		Section sec1 = new Section(0.0f, 0.0f, width / 2*0.9f, height, S);
//		Section sec2 = new Section(width / 2*1.1f, 0f, width, height / 2*0.9f, C);
//		Section sec3 = new Section(width / 2*1.1f, height / 2*1.1f, width, height, B);
//
//		List<Section> sections = new ArrayList<Section>();
//		sections.add(sec1);
//		sections.add(sec2);
//		sections.add(sec3);
//
//		GraphView.presentGraph(g, sections,width,height);
//	}

	public static void presentGraph(Graph<Integer, Integer> g,
			List<Section> sections,float width,float height) {

		Transformer<Integer, Point2D> locationTransformer = new LocationTransformer(
				sections);

		StaticLayout<Integer, Integer> layout = new StaticLayout<Integer, Integer>(
				g, locationTransformer);

		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
			public Paint transform(Integer i) {
				return Color.GREEN;
			}
		};
		VisualizationViewer<Integer, Integer> vv = new VisualizationViewer<Integer, Integer>(
				layout);
		Dimension dim = new Dimension();
		dim.setSize(width, height);
		vv.setPreferredSize(dim);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame jf = new JFrame();
		jf.getContentPane().add(vv);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
}

class LocationTransformer implements Transformer<Integer, Point2D> {
	public LocationTransformer(List<Section> sections) {
		this.sections = sections;
	}

	private List<Section> sections;

	public Point2D transform(Integer vertex) {

		float x = 0;
		float y = 0;
		for (Section section : sections) {
			Collection<Integer> vertices = section.getVertices();
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


