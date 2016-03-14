package gui.statistics;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.Text;
import gui.graph.GraphComponent;
import gui.graph.GraphPanel;

public class StatPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 5036252131474014030L;
	protected GraphPanel graphPanel;
	protected final StatisticsPanel statisticsPanel;
	protected final JLabel max = new JLabel("Max:");
	protected final JLabel dev = new JLabel("Standard Deviation: ");
	protected final JLabel avg = new JLabel("Average: ");
	protected final JLabel avgIO = new JLabel("CPU Average: ");
	protected final JLabel avgCPU = new JLabel("I/O Average: ");
	protected final JButton about = new JButton(" About", new ImageIcon(getClass()
			.getResource(Text.PATH + "q.png")));
	protected final JButton showGraph = new JButton("Graph");
	protected String explanation;
	protected boolean clicked = false;
	
	/**
	 * The constructor
	 * 
	 * @param title
	 *            - the panels title
	 * @param s
	 *            - the GUI container
	 */
	public StatPanel(String title, StatisticsPanel s, String toolTip) {
		statisticsPanel = s;
		explanation = toolTip;
		setToolTipText(toolTip);
		graphPanel = new GraphPanel(title);
		setBorder(BorderFactory.createTitledBorder(title));
		setLayout(new GridLayout(0, 1));
		add(max);
		add(avg);
		add(avgIO);
		add(avgCPU);
		avgIO.setToolTipText("Average for I/O Bound Processes");
		avgCPU.setToolTipText("Average for CPU Bound Processes");
		add(dev);
		add(showGraph);
		showGraph.addActionListener(this);
		add(about);
		about.addActionListener(this);
	}
	
	/**
	 * updates the statistics
	 * 
	 * @param m
	 *            - the maximum
	 * @param a
	 *            - the average
	 * @param d
	 *            - stdDev
	 */
	public void updateStatistics(int m, String a, String d, String e, String f, ArrayList<GraphComponent> finished) {
		graphPanel.getGraph().setMax(m);
		max.setText("Max: " + m);
		dev.setText("Standard Deviation: " + d);
		avg.setText("Average: " + a);
		avgIO.setText("Average I/O: " + e);
		avgCPU.setText("Average CPU: " + f);
		for (GraphComponent gf : finished){
			gf.addController(graphPanel);
		}
		graphPanel.getGraph().setCPUaverage(Double.parseDouble(f));
		graphPanel.getGraph().setIOaverage(Double.parseDouble(e));
		graphPanel.getGraph().setAverage(Double.valueOf(a));
		graphPanel.getGraph().setGraph(finished);
		graphPanel.repaint();
		
		updateUI();
	}
	
	/**
	 * Returns the graph panel
	 * @return the graph panel
	 */
	public GraphPanel getPanel() {
		return graphPanel;
	}
	
	/**
	 * Set GraphPanel Options
	 * @param b
	 */
	public void setLegend(boolean b) {
		graphPanel.setLegend(b);
	}
	
	/**
	 * Called if an action is performed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == about) {
			if (!clicked) {
				about.setText("Remove");
				statisticsPanel.addExplaination(explanation);
				clicked = true;
			} else {
				about.setText("About");
				statisticsPanel.removeExplaination();
				clicked = false;
			}
		}
		if (e.getSource() == showGraph) {
			statisticsPanel.addGraph(graphPanel);
		}
	}
	
}