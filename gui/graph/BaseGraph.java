package gui.graph;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Base class for graphs. Adapted from
 * http://www.javaworld.com/javaworld/jw-07-1997/jw-07-step.html
 * 
 * @author dominic
 */
public class BaseGraph extends JPanel {
	
	private static final long serialVersionUID = 5399399780898762018L;
	protected int bottom;
	protected FontMetrics metrics;
	protected ArrayList<GraphComponent> items;
	protected int labelWidth;
	protected int left = 5;
	protected int maximum;
	protected int minimum;
	protected int padding = 4;
	protected int right;
	protected int titleHeight;
	protected int top;
	protected int textBottom;
	
	/**
	 * Constructor.
	 * 
	 * @param t
	 *            - The title of the graph
	 * @param min
	 *            - The Graphs Minimum Value
	 * @param max
	 *            - the Graphs Maximum Value
	 */
	public BaseGraph(int min, int max) {
		minimum = min;
		maximum = max;
		items = new ArrayList<GraphComponent>();
	}
	
	/**
	 * set the graph items
	 * 
	 * @param l
	 *            - the items
	 */
	public void setGraph(ArrayList<GraphComponent> l) {
		items = l;
	}
	
	/**
	 * returns the preferred size for this graph.
	 * 
	 * @return the preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 200);
	}
	
	/**
	 * Overrides the parent class paint() method.
	 * 
	 * @param g
	 *            the g
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawLine(left, bottom, right, bottom);
		g.drawLine(left, bottom, left, top);
		g.drawString(Integer.toString(minimum), padding, bottom);
		g.drawString(Integer.toString(maximum), padding, top);
		
		g.drawString("MIN", padding - 5, bottom + 20);
		g.drawString("MAX", padding - 5, top + titleHeight);
	}
	
	/** 
	 * sets the max value
	 * @param a new vlaue
	 */
	public void setMax(int a){
		maximum = a;
	}
	
	/**
	 * Overrides the parent class method.
	 * 
	 * @param x
	 *            - the x
	 * @param y
	 *            - the y
	 * @param width
	 *            - the width
	 * @param height
	 *            - the height
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		metrics = getFontMetrics(getFont());
		titleHeight = metrics.getHeight();
		labelWidth = Math.max(metrics.stringWidth(Integer.toString(minimum)),
				metrics.stringWidth(Integer.toString(maximum)));
		top = padding + 20;
		bottom = getSize().height - padding - 20;
		left = padding + labelWidth + 10;
		right = getSize().width - padding - 20;
		textBottom = bottom + 18;
	}
	
}
