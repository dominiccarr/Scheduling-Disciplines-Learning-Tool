package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import disciplines.ShortestJobFirst;

import scheduler.Scheduler;
import scheduler.Text;

/**
 * This panel contains all the controls to allow the user to control the
 * animation
 * 
 * @author dominic
 * 
 */
public class ControlPanel extends JPanel implements ChangeListener,
		ActionListener {
	
	private static final int QUANTUM_INITIAL = 5;
	private static final int PROCESS_INITIAL = 10;
	private static final int PERCENTAGE_INITIAL = 50;
	private static final int ANIM_INITIAL = 5;
	private static final int ALPHA_INITIAL = 5;
	private static final long serialVersionUID = -6581095310290763045L;
	// the percentage of I/O bound processes
	private JLabel amountIO = new JLabel("I/O 50%");
	// the percentage of CPU bound processes
	private JLabel amountCPU = new JLabel("CPU 50%");
	// starts/pauses the animation
	public JButton startButton = new JButton("Start");
	private JButton runthrough = new JButton("One Click Run");
	// JSlider to determine the percentage of i/o and CPU bound processes
	private JSlider CPU_IO = new JSlider(0, 100, PERCENTAGE_INITIAL);
	// the scheduler to which the control panel belongs
	private Scheduler scheduler;
	// Combobox to allow for selection of a scheduling discipline
	private JComboBox disciplines = new JComboBox(new String[] {
			"First Come First Served", "Round Robin", "Shortest Job First",
			"Shortest Remaining Time First", "Non-premptive Priority Queue",
			"Premptive Priority Queue"});
	// slider for alpha value selection for SJF and SRTF
	private JSlider alphaSlider = new JSlider(0, 10, ALPHA_INITIAL);
	// panel to hold the alpha slider
	private JPanel alphaPanel = new JPanel();
	// label to display alpha value
	private JLabel alphaLabel = new JLabel("0.5");
	// Slider to choose the number of processes
	private JSlider processSlider = new JSlider(2, 30, PROCESS_INITIAL);
	// panel to hold process slider
	private JPanel processPanel = new JPanel();
	// label to show the number of processes
	private JLabel processLabel = new JLabel("" + processSlider.getValue());
	// panel to hold the quantum slider
	private JPanel rrPanel = new JPanel();
	// slider to control the the RR time quantum
	private JSlider quantumSlider = new JSlider(1, 30, QUANTUM_INITIAL);
	// label to show the quantum value
	private JLabel rrLabel = new JLabel("" + quantumSlider.getValue());
	// panel to hold animation slider
	private JPanel animPanel = new JPanel();
	// slider to control the animation speed
	private JSlider animSlider = new JSlider(2, 8, ANIM_INITIAL);
	// label to show the sliders value
	private JLabel animLabel = new JLabel("" + animSlider.getValue());
	// panel to hold percentage slider
	private JPanel cpuPanel = new JPanel();
	// allows user to indicate whether transitions are to be shown
	private JCheckBox transitions = new JCheckBox();
	// starts a new simulation
	private JButton restart = new JButton("Restart");
	private boolean started = false;
	private JLabel choose = new JLabel("Choose Discipline");
	private ImageIcon cpu = new ImageIcon(getClass().getResource(
			Text.PATH + "CPU-Bound.png"));
	private ImageIcon io = new ImageIcon(getClass().getResource(
			Text.PATH + "IO-Bound.png"));
	private JLabel ioLabel = new JLabel(io);
	private JLabel cpuLabel = new JLabel(cpu);
	
	private JSlider latencySlider = new JSlider(1, 10,
			(int) Scheduler.DISPATCHER_LATENCY);
	private JPanel latencyPanel = new JPanel();
	private JLabel latencyLabel = new JLabel("" + Scheduler.DISPATCHER_LATENCY);
	private JButton step = new JButton("Step");
	private JButton stepMode = new JButton("Step Mode");
	
	/**
	 * The Constructor
	 * 
	 * @param a
	 *            - the scheduler
	 */
	public ControlPanel(Scheduler a) {
		scheduler = a;
		setLayout(null);
		setBorder(BorderFactory.createTitledBorder("Confiquration"));
		addPanels();
		addLabels();
		addButtons();
	}
	
	/**
	 * Add the buttons to the panel
	 */
	private void addButtons() {
		add(startButton);
		startButton.setBounds(870, 90, 120, 30);
		startButton.setToolTipText("Begin the animation");
		startButton.addActionListener(this);
		
		add(runthrough);
		runthrough.setBounds(995, 90, 150, 30);
		runthrough.setToolTipText("Run Entire Simulation");
		runthrough.addActionListener(this);
		
		add(restart);
		restart.setBounds(995, 90, 120, 30);
		restart.setToolTipText("Restart Simulation");
		restart.addActionListener(this);
		restart.setVisible(false);
		
		add(stepMode);
		stepMode.setBounds(870, 65, 120, 30);
		stepMode.setToolTipText("Enter Step-by-Step mode");
		stepMode.addActionListener(this);
		stepMode.setVisible(false);
		
		add(step);
		step.setBounds(995, 65, 120, 30);
		step.setToolTipText("Execute one simulation step");
		step.addActionListener(this);
		step.setVisible(false);
	}
	
	/**
	 * adds the checkboxes to the panel
	 */
	private void addLabels() {
		add(transitions);
		transitions.setText("Transitions?");
		transitions.addActionListener(this);
		transitions.setBounds(660, 75, 130, 20);
		transitions.setSelected(true);
		
		add(choose);
		choose.setBounds(883, 10, 200, 30);
		
		add(disciplines);
		disciplines.setBounds(880, 32, 250, 30);
		disciplines.addActionListener(this);
	}
	
	/**
	 * adds the slider panels to the main panel
	 */
	private void addPanels() {
		JLabel[] labels = { processLabel, rrLabel, animLabel, alphaLabel,
				latencyLabel };
		JPanel[] panels = { processPanel, rrPanel, animPanel, alphaPanel,
				latencyPanel };
		JSlider[] sliders = { processSlider, quantumSlider, animSlider,
				alphaSlider, latencySlider };
		String[] tips = { "Choose the number of processes in the simulation",
				"Choose the time quantum for RR scheduling, this is the slice of CPU time given to each process",
				"Choose the speed of the animation",
				"Choose value of alpha for SJF and SRTF, used to vary the importance of previous burst estimates.",
				"Choose the length of the context switch" };
		
		for (int i = 0; i < labels.length; i++) {
			add(panels[i]);
			panels[i].setToolTipText(tips[i]);
			panels[i].add(sliders[i]);
			panels[i].setLayout(null);
			panels[i].add(labels[i]);
			sliders[i].addChangeListener(this);
			sliders[i].setToolTipText(tips[i]);
			labels[i].setToolTipText(tips[i]);
			labels[i].setBounds(180, 25, 20, 10);
			sliders[i].setBounds(5, 20, 170, 30);
		}
		
		quantumSlider.setEnabled(false);
		
		processPanel.setBorder(BorderFactory
				.createTitledBorder("Number of Processes"));
		processPanel.setBounds(220, 20, 210, 50);
		
		rrPanel.setBorder(BorderFactory.createTitledBorder("Time Quantum"));
		rrPanel.setBounds(10, 70, 210, 50);
		
		animPanel
				.setBorder(BorderFactory.createTitledBorder("Animation Speed"));
		animPanel.setBounds(10, 20, 210, 50);
		
		alphaPanel.setBorder(BorderFactory.createTitledBorder("Alpha Value"));
		alphaPanel.setBounds(220, 70, 210, 50);
		alphaSlider.setEnabled(false);
		
		cpuPanel.setBorder(BorderFactory.createTitledBorder("Process Ratio"));
		add(cpuPanel);
		cpuPanel.setLayout(null);
		cpuPanel.add(CPU_IO);
		cpuPanel.setBounds(430, 20, 230, 100);
		CPU_IO.addChangeListener(this);
		CPU_IO.setBounds(5, 20, 220, 30);
		CPU_IO
				.setToolTipText("choose the percentage of IO and CPU bound processes");
		cpuPanel
				.setToolTipText("choose the percentage of IO and CPU bound processes");
		cpuPanel.add(amountIO);
		amountIO.setBounds(35, 45, 300, 30);
		
		cpuPanel.add(amountCPU);
		amountCPU.setBounds(140, 45, 300, 30);
		
		cpuPanel.add(ioLabel);
		cpuLabel.setBounds(195, 45, 30, 30);
		cpuPanel.add(cpuLabel);
		ioLabel.setBounds(5, 45, 30, 30);
		cpuLabel.setToolTipText("CPU Bound Processes have long CPU bursts");
		ioLabel.setToolTipText("I/O Bound Processes have short CPU bursts and frequent I/O waits");

		latencyPanel.setBorder(BorderFactory
				.createTitledBorder("Context Switch"));
		latencyPanel.setBounds(660, 20, 210, 50);
	}
	
	/**
	 * called if the state of a slider changes
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == CPU_IO) {
			amountIO.setText("I/O " + CPU_IO.getValue() + "%");
			amountCPU.setText("CPU " + (100 - CPU_IO.getValue()) + "%");
		}
		else if (e.getSource() == alphaSlider) {
			double newAlpha = ((double) alphaSlider.getValue()) / 10;
			alphaLabel.setText("" + newAlpha);
		}
		else if (e.getSource() == quantumSlider) {
			rrLabel.setText("" + quantumSlider.getValue());
		}
		else if (e.getSource() == animSlider) {
			animLabel.setText("" + animSlider.getValue());
			scheduler.setAnimationSpeed(animSlider.getValue());
		}
		else if (e.getSource() == processSlider) {
			processLabel.setText("" + processSlider.getValue());
		}
		else if (e.getSource() == latencySlider) {
			latencyLabel.setText("" + latencySlider.getValue());
		}
	}
	
	/*
	 * Called when an action is performed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stepMode) {
			if (stepMode.getText().equals("Step Mode")) {
				scheduler.stepmode = true;
				scheduler.pause();
				stepMode.setText("Exit");
				step.setVisible(true);
				startButton.setVisible(false);
			}
			else {
				scheduler.stepmode = false;
				scheduler.resume();
				stepMode.setText("Step Mode");
				step.setVisible(false);
				startButton.setVisible(true);
			}
		}
		else if (e.getSource() == step) {
			scheduler.performSchedule();
		}
		else if (e.getSource() == startButton && !scheduler.isRunning()
				&& !started) {
			start();
		}
		else if (e.getSource() == startButton && !scheduler.isRunning()
				&& started) {
			scheduler.resume();
			startButton.setText("Pause");
		}
		else if (e.getSource() == startButton && scheduler.isRunning()) {
			scheduler.pause();
			startButton.setText("Resume");
		}
		else if ((e.getSource() == disciplines)) {
			quantumSlider.setEnabled(false);
			if (disciplines.getSelectedIndex() == 1) {
				quantumSlider.setEnabled(true);
			}
			alphaSlider.setEnabled(false);
			if (disciplines.getSelectedIndex() == 2
					|| disciplines.getSelectedIndex() == 3) {
				alphaSlider.setEnabled(true);
			}
		}
		else if (e.getSource() == transitions) {
			scheduler.setTransitions(transitions.isSelected());
		}
		else if (e.getSource() == restart) {
			restart();
			scheduler.restart();
		}
		else if (e.getSource() == runthrough) {
			setScheduler();
			scheduler.runThrough();
		}
	}
	
	/**
	 * start the simulation
	 */
	private void start() {
		setScheduler();
		
		scheduler.start();
		startButton.setText("Pause");
		started = true;
		restart.setVisible(true);
		stepMode.setVisible(true);
	}
	
	/**
	 * set up the scheduler
	 */
	private void setScheduler() {
		scheduler.restart();
		scheduler.setPercentCPU(100 - CPU_IO.getValue());
		scheduler.setNumProcesses(processSlider.getValue());
		scheduler.setQuantum(quantumSlider.getValue());
		scheduler.setAnimationSpeed(animSlider.getValue());
		scheduler.chooseDiscipline(disciplines.getSelectedIndex());
		scheduler.setLatency(latencySlider.getValue());
		double newAlpha = ((double) alphaSlider.getValue()) / 10;
		ShortestJobFirst.alpha = newAlpha;
		
		runthrough.setVisible(false);
		CPU_IO.setEnabled(false);
		processSlider.setEnabled(false);
		quantumSlider.setEnabled(false);
		latencySlider.setEnabled(false);
		alphaSlider.setEnabled(false);
		disciplines.setEnabled(false);
	}
	
	/**
	 * restarts the control panel
	 */
	public void restart() {
		disciplines.setEnabled(true);
		started = false;
		startButton.setText("Start");
		restart.setVisible(false);
		disciplines.setSelectedIndex(0);
		alphaSlider.setValue(ALPHA_INITIAL);
		processSlider.setValue(PROCESS_INITIAL);
		quantumSlider.setValue(QUANTUM_INITIAL);
		CPU_IO.setValue(PERCENTAGE_INITIAL);
		stepMode.setText("Step Mode");
		stepMode.setVisible(false);
		step.setVisible(false);
		updateUI();
		startButton.setVisible(true);
		CPU_IO.setEnabled(true);
		processSlider.setEnabled(true);
		quantumSlider.setEnabled(false);
		latencySlider.setEnabled(true);
		alphaSlider.setEnabled(false);
		runthrough.setVisible(true);
		}
}