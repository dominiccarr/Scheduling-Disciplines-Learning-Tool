package gui.graph;

import java.awt.Font;


import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {
	
	private static final long serialVersionUID = 2600922916479391973L;
	private JLabel title;
	private JLabel label = new JLabel("Processes (click them to view their properties)");
	private JLabel proc = new JLabel();
	private LineGraph graph;
	private Legend legend = new Legend();

	/**
	 * Constructor
	 * @param Title - the title
	 */
	public GraphPanel(String Title) {
		setLayout(null);
		title = new JLabel(Title);
		title.setBounds(500, 10, 500, 25);
		title.setFont(new Font("arial", Font.BOLD, 25));
		add(title);
		graph = new LineGraph(0, 10);
		graph.setBounds(20, 60, 1145, 305);
		add(graph);
		add(legend);
		legend.setBounds(900, 0,150,100);
		add(label);
		label.setBounds(450, 360, 500, 30);
		add(proc);
		proc.setBounds(330, 35, 500, 25);
		
	}
	
	/**
	 * Returns the line graph
	 * @return the line graph
	 */
	public LineGraph getGraph() {
		return graph;
	}

	/**
	 * Sets the legend appropriately
	 * @param b - boolean
	 */
	public void setLegend(boolean b) {
		if (!b){
			label.setText("Queue Length, for last 20 simulation Cycles");
		}
	}

	public void show(String title2) {
		proc.setText(title2);		
	}
}
