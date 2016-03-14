package gui.processes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import disciplines.PreemptivePriorityQueue;
import disciplines.PriorityQueue;
import disciplines.ShortestJobFirst;
import disciplines.ShortestRemainingTimeFirst;

import processes.CPUProcess;
import processes.IOBoundProcess;
import processes.State;
import scheduler.Scheduler;
import scheduler.Text;

public class ProcessControlPanel extends JPanel implements ActionListener,
		ChangeListener {
	
	private static final long serialVersionUID = -6908958843730819408L;
	private final JButton suspend = new JButton("Suspend");
	private final JLabel priority = new JLabel("Priority");
	private JLabel priorityNum = new JLabel();
	private JLabel state = new JLabel();
	private JLabel name = new JLabel();
	private JLabel burst = new JLabel();
	private JLabel previousBurst = new JLabel("Previous Burst Time:");
	private JLabel previousEstimate = new JLabel("Prevous Burst Estimate:");
	private JButton question = new JButton(new ImageIcon(getClass()
			.getResource(Text.PATH + "q.png")));
	private JButton question2 = new JButton(new ImageIcon(getClass()
			.getResource(Text.PATH + "q.png")));
	private JLabel type= new JLabel();
	private JSlider prioritySlider = new JSlider(CPUProcess.MIN_PRIORITY,
			CPUProcess.MAX_PRIORITY,4);
	private CPUProcess parent;
	private JLabel background = new JLabel(new ImageIcon(getClass().getResource(
			Text.PATH + "pcp.png")));
	
	public ProcessControlPanel(CPUProcess a) {
		parent = a;
		setLayout(null);
		add(suspend);
		suspend.addActionListener(this);
		suspend.setBounds(5, 90, 100, 30);
		addLabels();
		addConditonals();
		add(background);
		background.setBounds(0, 0, 200, 160);
	}
	
	/**
	 * add the conditional control components
	 */
	private void addConditonals() {
		
		if (Scheduler.discipline instanceof PriorityQueue
				|| Scheduler.discipline instanceof PreemptivePriorityQueue) {
			add(priority);
			priorityNum.setText("" + parent.getPriority());
			add(priorityNum);
			priority.setBounds(10, 65, 50, 30);
			prioritySlider.setValue(parent.getPriority());
			add(prioritySlider);
			prioritySlider.addChangeListener(this);
			prioritySlider.setBounds(50, 71, 100, 20);
			priorityNum.setBounds(152, 65, 20, 30);
			suspend.setBounds(5, 95, 100, 30);
		}
		else if (Scheduler.discipline instanceof ShortestJobFirst
				|| Scheduler.discipline instanceof ShortestRemainingTimeFirst) {
			burst.setText("Predicted Burst: " + parent.getEstimateStore());
			add(question2);
			question2.setBounds(130, 60, 20,20);
			question2.addActionListener(this);
			question2.setContentAreaFilled(false);
			question2.setBorderPainted(false);
			add(burst);
			burst.setBounds(10, 55, 200, 30);
			add(previousEstimate);
			previousEstimate.setBounds(10, 70, 300, 30);
			add(previousBurst);
			previousBurst.setBounds(10, 85, 300, 30);
			suspend.setBounds(5, 107, 100, 30);
		} else {
			type.setBounds(10, 35, 200, 30);
			question.setBounds(70, 33, 40, 30);
			state.setBounds(10, 57, 300, 30);
			suspend.setBounds(5, 100, 100, 30);
		}

	}
	
	/**
	 * adds the labels
	 */
	private void addLabels() {
		name.setText("Process " + parent.getID());
		add(name);
		name.setBounds(10, 10, 100, 30);
		
		type.setText("Type: "
				+ (parent instanceof IOBoundProcess ? "I/O" : "CPU"));
		add(type);
		type.setBounds(10, 25, 200, 30);
		
		state.setText("State: " + parent.getState());
		add(state);
		state.setBounds(10, 40, 300, 30);

		add(question);
		question.setBounds(70, 23, 40, 30);
		question.addActionListener(this);
		question.setContentAreaFilled(false);
		question.setBorderPainted(false);
	}
	
	/**
	 * called if an action is performed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == suspend) {
			if (parent.isBlocked()) {
				parent.setState(State.suspended_blocked);
			}
			else if (parent.isReady()) {
				parent.setState(State.suspended_ready);
			}
			else if (parent.isReadySuspend()) {
				parent.setState(State.ready);
			}
			else if (parent.isBlockedSuspended()) {
				parent.setState(State.blocked);
			}
		}
		else if (e.getSource() == question) {
			Scheduler.getAnimationPanel().explain((parent instanceof IOBoundProcess ? Text.IO_BOUND : Text.CPU_BOUND));
		}
		else if (e.getSource() == question2) {
			Scheduler.getAnimationPanel().explain(parent.getBurstExp());
		}
	}
	
	/**
	 * called is the state of the slider is modified
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == prioritySlider) {
			parent.setPriority(prioritySlider.getValue());
			priorityNum.setText("" + parent.getPriority());
			updateUI();
		}
	}
	
	/**
	 * updates the panel
	 */
	public void updateDetails() {
		prioritySlider.setValue(parent.getPriority());
		priorityNum.setText("" + parent.getPriority());
		state.setText("State: " + parent.getState());
		name.setText("Process " + parent.getID());
		burst.setText("Predicted Burst: " + parent.getEstimateStore());
		String prev =  (!parent.hasRun() ? "N/A" : ""+parent.getPreviousBurstTime() );
		previousBurst.setText("Previous Burst Time: " + prev);
		prev =  (!parent.hasRun() ? "N/A" : ""+parent.getPreviousEstimate() );
		previousEstimate.setText("Previous Burst Estimate: " + prev);
		
		suspend.setEnabled(true);
		if (parent.isRunning()){
			suspend.setEnabled(false);
		}
		if (parent.isReady() || parent.isBlocked()) {
			suspend.setText("Suspend");
		}
		else if (parent.isBlockedSuspended() || parent.isReadySuspend()) {
			suspend.setText("Resume");
		}
		updateUI();

	}
	
}
