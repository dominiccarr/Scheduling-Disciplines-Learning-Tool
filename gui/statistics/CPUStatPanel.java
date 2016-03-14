package gui.statistics;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class CPUStatPanel extends JPanel{
	
	private static final long serialVersionUID = 5036252131474014030L;
	private final JLabel runtime = new JLabel("Runtime: ");
	private final JLabel util = new JLabel("Utilization: ");
	private final JLabel idle = new JLabel("Idle: ");
	private final JLabel throughput = new JLabel("Throughput: ");

	
	/**
	 * The constructor for CPU stat panels
	 */
	public CPUStatPanel() {
		util.setToolTipText("The percentage of time the CPU performs useful work");
		runtime.setToolTipText("The runtime of the simulation");
		idle.setToolTipText("The percentage of time the CPU is idle");
		throughput.setToolTipText("The number of processes completed per unit of time");

		setBorder(BorderFactory.createTitledBorder("CPU Statistics"));
		setLayout(new GridLayout(0,1));
		add(runtime);
		add(util);
		add(idle);
		add(throughput);
	}
	
	/**
	 * Update the statistics
	 * @param m - the runtime
	 * @param a - the cpu utilization
	 * @param d - cpu idle time
	 */
	public void updateStatistics(double m, String a, String d, String c) {
		runtime.setText("Runtime: " + m);
		util.setText("Utilization: " + a+"%");
		idle.setText("Idle: " + d+"%");
		throughput.setText("Throughput: " + c);
		updateUI();
	}
}
