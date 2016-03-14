package gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * This class represents a line graph. Adapted from
 * http://www.javaworld.com/javaworld/jw-07-1997/jw-07-step.html
 * 
 * @author dominic
 */
public class LineGraph extends BaseGraph {
	
	private static final long serialVersionUID = 1L;
	private int increment = 0;
	private int position;
	private double average = 0;
	private double ioaverage = 0;
	private double cpuaverage = 0;
	
	private static final BasicStroke averageStroke = new BasicStroke(2.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {
					5.0f, 5.0f }, 0.0f);
	private static final BasicStroke averageStroke2 = new BasicStroke(2.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {
					4.0f, 4.0f }, 0.0f);
	private static final BasicStroke lineStroke = new BasicStroke(1.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {
					6.0f, 2.0f }, 0.0f);
	private static final Font font = new Font("TimesRoman", Font.PLAIN, 12);
	
	/**
	 * Constructor for the line graph class.
	 * 
	 * @param t
	 *            the t
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public LineGraph(int min, int max) {
		super(min, max);
	}
	
	/**
	 * Set average value
	 * 
	 * @param a
	 */
	public void setAverage(double a) {
		average = a;
	}
	
	/**
	 * Get the adjusted value
	 * 
	 * @param a
	 * @return
	 */
	public int getAdjustedValue(GraphComponent a) {
		return getAdjustedValue(a.getValue());
	}
	
	/**
	 * Get the adjusted value
	 * 
	 * @param a
	 * @return
	 */
	public int getAdjustedValue(double a) {
		return (int) (bottom - (((a - minimum) * (bottom - top)) / (maximum - minimum)));
	}
	
	/**
	 * Overrides the paint() method of the superclass.
	 * 
	 * @param g
	 *            the g
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		removeAll();
		// if there are items
		if (!items.isEmpty()) {
			if (!(items.size() - 1 <= 0)) {
				increment = (right - left) / (items.size() - 1);
			}
			paintItems(g2);
			drawAverage(g2);
		}
		else {
			g2.drawString("No relevent processes to display", 450, 100);
		}
	}
	
	public void paintItems(Graphics2D g2) {
		position = left + 20;
		g2.setStroke(lineStroke);
		
		for (int i = 0; i < items.size() - 1; i++) {
			final GraphComponent thisItem = items.get(i);
			final int thisAdjustedValue = getAdjustedValue(thisItem);
			
			g2.setColor(Color.black);
			g2.drawLine(position, bottom, position, thisAdjustedValue);
			
			int ovalPos = position;
			
			final GraphComponent nextItem = items.get(i + 1);
			int nextAdjustedValue = getAdjustedValue(nextItem);
			int nextPos = position + increment;
			if (i == (items.size() - 2)) {
				nextPos = right;
			}
			
			g2.setStroke(new BasicStroke(2));
			g2
					.drawLine(position, thisAdjustedValue, nextPos,
							nextAdjustedValue);
			
			position += increment;
			
			g2.setStroke(lineStroke);
			g2.setColor(thisItem.getColor());
			g2.drawString(thisItem.getTitle(), position - increment - 8,
					textBottom);
			g2.fillOval(ovalPos - 4, thisAdjustedValue - 2, 8, 8);
			add(thisItem);
			thisItem.setBounds(ovalPos - 4, thisAdjustedValue - 2, 8, 8);
		}
		
		position = right;
		final GraphComponent thisItem = items.get(items.size() - 1);
		final int thisAdjustedValue = getAdjustedValue(thisItem);
		
		g2.setColor(Color.black);
		g2.drawLine(position, bottom, position, thisAdjustedValue);
		
		g2.setColor(thisItem.getColor());
		g2.drawString(thisItem.getTitle(), position - 8, textBottom);
		g2.fillOval(position - 4, thisAdjustedValue - 2, 8, 8);
		add(thisItem);
		thisItem.setBounds(position - 4, thisAdjustedValue - 2, 8, 8);
	}
	
	/**
	 * draw the average lines
	 * 
	 * @param g2
	 *            - the graphics component
	 */
	public void drawAverage(Graphics2D g2) {
		g2.setStroke(averageStroke);
		g2.setColor(Color.black);
		int value = getAdjustedValue(average);
		g2.drawLine(left, value, right, value);
		
		g2.setColor(Color.red.darker());
		value = getAdjustedValue(ioaverage);
		g2.drawLine(left, value, right, value);
		
		g2.setStroke(averageStroke2);
		g2.setColor(Color.green.darker());
		value = getAdjustedValue(cpuaverage);
		g2.drawLine(left, value, right, value);
	}
	
	public void setIOaverage(double ioaverage) {
		this.ioaverage = ioaverage;
	}
	
	public void setCPUaverage(double cpuaverage) {
		this.cpuaverage = cpuaverage;
	}
}
