package au.edu.cdu.dynamicproblems.algorithm.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;

public class SampleGraph2 extends JFrame{
	JGraph graph;

	  public SampleGraph2 (String title)
	  {
	   super (title);

	   setDefaultCloseOperation (EXIT_ON_CLOSE);

	   graph = new JGraph ();
	   getContentPane ().add (new JScrollPane (graph));

	   pack ();

	   setVisible (true);
	  }

	  public static void main (String [] args)
	  {
	   SampleGraph2 sg = new SampleGraph2 ("Sample Graph 2");

	   GraphModel gm = sg.graph.getModel ();

	   dumpGroupStructure (gm);
	  }

	  public static void dumpGroupStructure (GraphModel gm)
	  {
	   for (int i = 0; i < gm.getRootCount (); i++)
	   {
	      Object o = gm.getRootAt (i);
	      System.out.println ("\n" + o + ": " + classify (gm, o));
	      dumpGroup (gm, o, 1);
	   }   
	  }

	  public static void dumpGroup (GraphModel gm, Object o, int level)
	  {   
	   for (int i = 0; i < gm.getChildCount (o); i++)
	   {
	      for (int j = 0; j < 3 * level; j++)
	        System.out.print (" ");

	      Object c = gm.getChild (o, i);
	      System.out.println (c + ": " + classify (gm, c));
	      if (c != null)
	        dumpGroup (gm, c, level+1);
	   }
	  }

	  public static String classify (GraphModel gm, Object o)
	  {
	   if (gm.isEdge (o))
	     return "Edge [Source = " + gm.getSource (o) +
	         ", Target = " + gm.getTarget (o) + "]";
	   else
	   if (gm.isPort (o))
	     return "Port";
	   else
	     return "Vertex or Group";
	  }
}
