package gui.statistics;


import gui.graph.GraphComponent;

import java.util.ArrayList;

import javax.swing.JLabel;

public final class DetailedStatPanel extends StatPanel {

	private static final long serialVersionUID = -512776484490810811L;
	private final JLabel minimum = new JLabel("Min: ");
	
	/**
	 * The constructor
	 * @param title - the panels title
	 * @param s - the scheduler
	 */
	public DetailedStatPanel(String title,	StatisticsPanel s, String toolTip) {
		super(title, s, toolTip);
		add(minimum, 0);
	}

	/**
	 * Updates the statistics
	 * @param m - maximum
	 * @param a - average
	 * @param d - stdDev
	 * @param min - minimum
	 */
	public void updateStatistics(int m, String a, String d, double min, String avgio, String avgcpu,ArrayList<GraphComponent> finished) {
		minimum.setText("Min: "+ min);
		super.updateStatistics(m, a, d, avgio, avgcpu, finished);
	}
}
