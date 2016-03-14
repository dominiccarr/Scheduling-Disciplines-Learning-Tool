package gui.statistics;

import gui.ExplanationPanel;
import gui.graph.GraphComponent;
import gui.graph.GraphPanel;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.Scheduler;
import scheduler.Statistics;
import scheduler.Text;

/**
 * This class shows the statistics of the simulation
 * 
 * @author dominic
 * 
 */
public class StatisticsPanel extends JPanel {
	
	private static final long serialVersionUID = -7521229844903022970L;
	private Statistics stats;
	private StatPanel waiting;
	private DetailedStatPanel turnaround;
	private StatPanel response;
	private DetailedStatPanel nturnaround;
	private StatPanel queuesize;
	private CPUStatPanel cpuStats;
	private JPanel top = new JPanel();
	private GraphPanel line;
	private ExplanationPanel explaination = new ExplanationPanel("");
	private JLabel name = new JLabel();
	
	/**
	 * Constructor for the statistics panel class
	 */
	public StatisticsPanel(Statistics sc) {
		stats = sc;
		waiting = new StatPanel("Waiting Time", this, Text.WAITING_TOOLTIP);
		turnaround = new DetailedStatPanel("Turnaround Time", this,
				Text.TURNAROUND_TOOLTIP);
		response = new StatPanel("Response Time", this, Text.RESPONSE_TOOLTIP);
		nturnaround = new DetailedStatPanel("Normalized Turnaround Time", this,
				Text.N_TURNAROUND_TOOLTIP);
		queuesize = new StatPanel("Ready Queue Size", this, Text.QUEUE_SIZE);
		cpuStats = new CPUStatPanel();
		setLayout(null);
		addTop();
		line = queuesize.getPanel();
		add(line);
		line.setBounds(0, 250, 1175, 390);
		add(explaination);
		explaination.setBounds(350, 400, 800, 100);
		explaination.setVisible(false);
		add(name);
		name.setBounds(520, 0, 400, 30);
	}
	
	public void updateStatName(String a){
		name.setText(a);
	}
	
	/**
	 * Adds the top panel
	 */
	private void addTop() {
		add(top);
		top.setBounds(0, 25, 1180, 220);
		top.setLayout(new GridLayout(1, 0));
		top.add(waiting);
		top.add(turnaround);
		top.add(nturnaround);
		top.add(response);
		top.add(queuesize);
		top.add(cpuStats);
		queuesize.setLegend(false);
	}
	
	/**
	 * Adds the graph
	 * 
	 * @param a
	 *            - the graph panel
	 */
	public void addGraph(GraphPanel a) {
		remove(line);
		line = a;
		line.setBounds(0, 250, 1175, 390);
		add(line);
		updateUI();
	}
	
	/**
	 * updates the displayed statistics
	 * 
	 * @param all
	 * @param ready
	 * @param finished
	 */
	public void updateStatistics() {
		if (stats.getQueues().size() > 20) {
			List<GraphComponent> b = stats.getQueues().subList(
					stats.getQueues().size() - 1 - 20,
					stats.getQueues().size() - 1);
			ArrayList<GraphComponent> a = new ArrayList<GraphComponent>();
			a.addAll(b);
			queuesize.updateStatistics(stats.getMaxQueueSize(), stats
					.getAvgQueueSize(), stats.getStdDevQueueSize(), stats
					.getAvgQueueSizeIO(), stats.getAvgQueueSizeCPU(), a);
		}
		else {
			queuesize.updateStatistics(stats.getMaxQueueSize(), stats
					.getAvgQueueSize(), stats.getStdDevQueueSize(), stats
					.getAvgQueueSizeIO(), stats.getAvgQueueSizeCPU(), stats
					.getQueues());
		}
		
		turnaround.updateStatistics(stats.getMaxTurnaroundTime(), stats
				.getAvgTuraroundTime(), stats.getStdDevTurnaround(), stats
				.getMinTurnaroundTime(), stats.getAvgTurnaroundTimeIO(), stats
				.getAvgTurnaroundTimeCPU(), stats.getTurnaroundItems());
		
		nturnaround.updateStatistics(stats.getMaxNTurnaroundTime(), stats
				.getNormalizedTurnaround(), stats.getStdDevNTuranround(), stats
				.getMinNTurnaroundTime(), stats.getAvgNTurnaroundTimeIO(),
				stats.getAvgNTurnaroundTimeCPU(), stats.getNturnaroundItems());
		
		waiting.updateStatistics(stats.getMaxWaitingTime(), stats
				.getAvgWaitingTime(), stats.getStdDevWaiting(), stats
				.getAvgWaitingTimeIO(), stats.getAvgWaitingTimeCPU(), stats
				.getWaitingItems());
		
		cpuStats
				.updateStatistics(Scheduler.simulationTime, stats
						.getCpuUtilization(), stats.getCpuIdle(), stats
						.getThroughput());
		
		response.updateStatistics(stats.getMaxResponseTime(), stats
				.getAvgResponseTime(), stats.getStdDevResponse(), stats
				.getAvgResponseIO(), stats.getAvgResponseTimeCPU(), stats
				.getResponseItems());
		
		updateUI();
		invalidate();
		validate();
		revalidate();
		repaint();
	}
	
	/**
	 * Adds the explaination Panel
	 * 
	 * @param exp2
	 */
	public void addExplaination(String exp2) {
		line.setVisible(false);
		explaination.setText(exp2);
		explaination.setVisible(true);
	}
	
	/**
	 * Removes the explanation panel
	 */
	public void removeExplaination() {
		explaination.setVisible(false);
		line.setVisible(true);
	}
}
