package gui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Legend extends JComponent {
	
	private static final long serialVersionUID = 7048009776697923326L;
	private static final BasicStroke averageStroke = new BasicStroke(2.0f,
			BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[] {
					5.0f, 5.0f }, 0.0f);
	
	/**
	 * Constructor
	 */
	public Legend() {
		
	}
	
	/**
	 * Paints the component
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		drawLegend(g2);
	}
	
	/**
	 * draws the legend
	 * 
	 * @param g2
	 */
	public void drawLegend(Graphics2D g2) {
		int value = 10;
		int left = 0;
		int right = 20;
		g2.setStroke(averageStroke);
		g2.setColor(Color.black);
		g2.drawString("Average", 25, value + 5);
		g2.drawLine(left, value, right, value);
		
		g2.setColor(Color.red.darker());
		value = 25;
		g2.drawString("IO Average", 25, value + 5);
		g2.drawLine(left, value, right, value);
		
		g2.setColor(Color.green.darker());
		value = 40;
		g2.drawString("CPU Average", 25, value + 5);
		g2.drawLine(left, value, right, value);
	}
	
}