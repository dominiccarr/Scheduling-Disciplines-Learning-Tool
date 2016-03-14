package scheduler;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Timer;

import processes.CPUBoundProcess;
import processes.CPUProcess;
import processes.IOBoundProcess;
import processes.State;
import disciplines.FirstComeFirstServed;
import disciplines.ISchedulingDiscipline;
import disciplines.PreemptivePriorityQueue;
import disciplines.PriorityQueue;
import disciplines.RoundRobin;
import disciplines.ShortestJobFirst;
import disciplines.ShortestRemainingTimeFirst;
import gui.AnimationPanel;
import gui.ExecutionPanel;
import gui.ScheduleGUI;
import gui.ControlPanel;
import gui.statistics.StatisticsPanel;

/**
 * The main class representing the CPU scheduler
 * 
 * @author Dominic Carr
 * 
 */
public class Scheduler implements ActionListener {
	
	public static double DISPATCHER_LATENCY = 2;
	// the main GUI
	private ScheduleGUI main;
	// the scheduling discipline being used
	public static ISchedulingDiscipline discipline;
	// the number of processes
	private long numProcesses = 30;
	// the panel which displays the simulation statistics
	private Statistics statistics;
	// the panel which allows the user to set parameters
	private ControlPanel controlPanel;
	// timer to replicate the passing of time in the simulation
	private Timer timer;
	// integer to hold the simulation time
	public static double simulationTime = 1;
	// the current process using the CPU
	private CPUProcess current;
	// The amount of time the cpu has been busy
	private double timeBusy = 0;
	// is the simulation running?
	private boolean running = false;
	// Process Queues
	private ArrayList<CPUProcess> all = new ArrayList<CPUProcess>();
	private ArrayList<CPUProcess> ready = new ArrayList<CPUProcess>();
	private ArrayList<CPUProcess> readySuspend = new ArrayList<CPUProcess>();
	private ArrayList<CPUProcess> blockedSuspend = new ArrayList<CPUProcess>();
	private ArrayList<CPUProcess> blocked = new ArrayList<CPUProcess>();
	private ArrayList<CPUProcess> finished = new ArrayList<CPUProcess>();
	// The Scheduling disciplines GUI
	public static AnimationPanel animationPanel;
	// boolean indicated if transitions should be painted in the GUI
	private boolean transitions = true;
	private int delay = 1000;
	private double percentCPU = 50;
	private ExecutionPanel text = new ExecutionPanel(Text.WELCOME);
	public ExecutionPanel quantumtext = new ExecutionPanel("");
	private boolean dispatch = true;
	private boolean arrival = false;
	private int maxVisibleProcesses = 12;
	private boolean oneClickMode = false;
	public boolean stepmode = false;
	private boolean first = true;
	
	public Scheduler() {
		controlPanel = new ControlPanel(this);
		statistics = new Statistics();
		animationPanel = new AnimationPanel(this);
		main = new ScheduleGUI(this);
		text.setVisible(false);
	}
	
	/**
	 * Initializes the scheduler
	 */
	public void start() {
		running = true;
		createQueue();
		animationPanel.setDiscipline(discipline);
		main.addAnimationPanel();
		main.addStats();
		statistics.getPanel().updateStatName(discipline.getName());
		timer = new Timer(delay, (ActionListener) this);
		timer.setInitialDelay(0);
		timer.setCoalesce(true);
		timer.start();
	}
	
	/**
	 * chooses the scheduling discipline to use
	 * 
	 * @param disciplineID
	 */
	public void chooseDiscipline(int disciplineID) {
		quantumtext.setEnabled(false);
		switch (disciplineID) {
			case 0:
				discipline = new FirstComeFirstServed(this);
				break;
			case 1:
				discipline = new RoundRobin(this);
				quantumtext.setLabel("Quantum Countdown " + RoundRobin.quantum,
						"");
				break;
			case 2:
				discipline = new ShortestJobFirst(this);
				quantumtext.setLabel(
						"Click on a process to view its predicted burst time",
						"");
				break;
			case 3:
				discipline = new ShortestRemainingTimeFirst(this);
				quantumtext.setLabel(
						"Click on a process to view its predicted burst time",
						"");
				break;
			case 4:
				discipline = new PriorityQueue(this);
				quantumtext
						.setLabel(
								"Click a process to modify priority, 9 (highest) - 0 (lowest)",
								"");
				break;
			case 5:
				discipline = new PreemptivePriorityQueue(this);
				quantumtext
						.setLabel(
								"Click a process to modify priority, 9 (highest) - 0 (lowest)",
								"");
				break;
		}
	}
	
	/**
	 * creates the queue of processes
	 */
	private void createQueue() {
		// ascertain the correct amount of CPU and IO bound processes to create
		int amountCPU = (int) (numProcesses * ((double) percentCPU) / 100);
		int amountIO = (int) (numProcesses - amountCPU);
		int arrivalTime = 1;
		// create the CPU bound processes
		for (int i = 0; i < amountCPU; i++) {
			CPUBoundProcess a = new CPUBoundProcess();
			all.add(a);
		}
		// create the I/O bound processes
		for (int i = 0; i < amountIO; i++) {
			IOBoundProcess a = new IOBoundProcess();
			all.add(a);
		}
		// shuffle the vector
		Collections.shuffle(all);
		// set the arrival time delays
		int PID = 0;
		for (CPUProcess a : all) {
			a.setPID(PID++);
			a.setArrivalTime(arrivalTime);
			arrivalTime += ExponentialRandom.nextExponential();
		}
		text.setVisible(true);
	}
	
	/**
	 * performs the scheduling operations
	 */
	public void performSchedule() {
		updateReadyQueue();
		processWait();
		suspensionHandling();
		updateBlockedQueues();
		discipline.schedule();
		arrival = false;
		
		if (current != null) {
			execute();
			++timeBusy;
		}
		
		if (!oneClickMode) {
			statistics.calculateStatistics(finished, all, ready, timeBusy);
			statistics.updateStatGUI();
		}
		++simulationTime;
	}
	
	/**
	 * resumes processes
	 * 
	 * @param amount
	 *            - number of processes in main memory
	 */
	private void resumeProcesses(int amount) {
		ArrayList<CPUProcess> processes = new ArrayList<>(readySuspend);
		Collections.reverse(processes);
		for (CPUProcess process : processes) {
			if (amount < maxVisibleProcesses - 4) {
				process.setState(State.ready);
				arrival = true;
				if (!oneClickMode) {
					setExplaination("Process " + process.getID()
							+ " resumed as there are is space in main memory.",
							"");
					animationPanel.readySuspendToReady(process.getPanel());
					expReset();
				}
				amount++;
				ready.add(process);
			}
		}
		processes = (ArrayList<CPUProcess>) blockedSuspend.clone();
		Collections.reverse(processes);
		for (CPUProcess a : processes) {
			if (amount < maxVisibleProcesses - 4) {
				a.setState(State.blocked);
				arrival = true;
				if (!oneClickMode) {
					setExplaination("Process " + a.getID()
							+ " resumed as there are is space in main memory.",
							"");
					animationPanel.blockedSuspendToBlocked(a.getPanel());
					expReset();
				}
				amount++;
				blocked.add(a);
			}
		}
		readySuspend.removeAll(ready);
		blockedSuspend.removeAll(blocked);
	}
	
	/**
	 * Suspends processes
	 * 
	 * @param amount
	 *            - number of processes in main memory
	 */
	private void suspendProcesses(int amount) {
		ArrayList<CPUProcess> reverse = (ArrayList<CPUProcess>) blocked.clone();
		Collections.reverse(reverse);
		for (CPUProcess a : reverse) {
			if (amount > maxVisibleProcesses) {
				
				a.setState(State.suspended_blocked);
				if (!oneClickMode) {
					setExplaination("Process " + a.getID()
							+ " suspended as there are too many processes ("
							+ amount + ") in main memory.", "");
					animationPanel.blockedToBlockedSuspend(a.getPanel());
					expReset();
				}
				blockedSuspend.add(a);
				amount--;
			}
		}
		reverse = (ArrayList<CPUProcess>) ready.clone();
		Collections.reverse(reverse);
		for (CPUProcess a : reverse) {
			if (amount > maxVisibleProcesses) {
				a.setState(State.suspended_ready);
				if (!oneClickMode) {
					setExplaination("Process " + a.getID()
							+ " suspended as there are too many processes ("
							+ amount + ") in main memory.", "");
					animationPanel.readyToReadySuspend(a.getPanel());
					expReset();
				}
				amount--;
				readySuspend.add(a);
			}
		}
	}
	
	/**
	 * Handles the suspension and resuming of processes
	 */
	private void suspensionHandling() {
		int amount = ready.size() + blocked.size();
		if (amount > maxVisibleProcesses) {
			suspendProcesses(amount);
		}
		else {
			resumeProcesses(amount);
		}
		ready.removeAll(readySuspend);
		ready.removeAll(blockedSuspend);
		ready.removeAll(blocked);
		blocked.removeAll(blockedSuspend);
		blocked.removeAll(readySuspend);
	}
	
	/**
	 * Update the waiting processes
	 */
	private void processWait() {
		// add all waiting processes to one list
		ArrayList<CPUProcess> allWaiting = new ArrayList<CPUProcess>();
		allWaiting.addAll(ready);
		allWaiting.addAll(readySuspend);
		allWaiting.addAll(blockedSuspend);
		allWaiting.addAll(blocked);
		// update them
		for (CPUProcess a : allWaiting) {
			a.waitToRun();
		}
	}
	
	/**
	 * check for process state change
	 */
	private void updateBlockedQueues() {
		// loop through blocked processes
		for (CPUProcess a : blocked) {
			// is the process is ready
			if (a.isReady()) {
				arrival = true;
				if (!oneClickMode) {
					setExplaination("Event Occured, Process " + a.getID()
							+ " transitions to the ready queue.", "Process " + a.getID() + " has completed its I/O wait, and is transitioned back to the end of the queue of ready processes.");
					animationPanel.eventOccurs(a.getPanel());
					expReset();
				}
				ready.add(a);
			}
			// if the process has been suspended
			else if (a.isBlockedSuspended()) {
				System.out.println("control moved " + a.getID());
				if (!oneClickMode) {
					setExplaination("Blocked Process " + a.getID()
							+ " has been suspended.", "");
					animationPanel.blockedToBlockedSuspend(a.getPanel());
					expReset();
				}
				blockedSuspend.add(a);
			}
		}
		// remove moved processes from blocked queue
		blocked.removeAll(ready);
		blocked.removeAll(blockedSuspend);
		// loop through blocked, suspend queue
		for (CPUProcess a : blockedSuspend) {
			// if the process is in the ready, suspend state
			if (a.isReadySuspend()) {
				if (!oneClickMode) {
					setExplaination(
							"Event Occured, Process "
									+ a.getID()
									+ " transitions to the ready, suspended queue.",
							"Event Occured, Process "
									+ a.getID()
									+ " transitions to the ready, suspended queue.");
					animationPanel.blockedSupendToReadySuspend(a.getPanel());
					expReset();
				}
				readySuspend.add(a);
			}
		}
		// remove moved processes from ready, suspend queue
		blockedSuspend.removeAll(readySuspend);
		
		for (CPUProcess a : readySuspend) {
			if (a.isReady()) {
				if (!oneClickMode) {
					setExplaination(
							"Event Occured, Process "
									+ a.getID()
									+ " transitions to the ready, suspended queue.",
							"Event Occured, Process "
									+ a.getID()
									+ " transitions to the ready, suspended queue.");
					animationPanel.readySuspendToReady(a.getPanel());
					expReset();
				}
				ready.add(a);
			}
		}
		// remove moved processes from ready, suspend queue
		readySuspend.removeAll(ready);
	}
	
	/**
	 * adds newly arrived processes to the ready queue
	 */
	private void updateReadyQueue() {
		// loop through all processes
		for (CPUProcess a : all) {
			// if a process just arrived
			if (a.isNew() && a.getArrivalTime() <= simulationTime) {
				arrival = true;
				a.setState(State.ready);
				ready.add(a);
				if (!oneClickMode) {
					setExplaination(
							"Process " + a.getID()
									+ " arrived at the ready Queue",
							"A new process has arrived at the ready queue. The arrival of a new process may immediately impact the decisions of the scheduling discipline.");
					animationPanel.newProcessArrival(a.getPanel());
					expReset();
				}
				if (first) {
					first = false;
					if (!oneClickMode) {
						animationPanel.addControlPanel(a.getControlPanel());
					}
				}
			}
			else if (ready.contains(a) && a.isReadySuspend()) {
				readySuspend.add(a);
				if (!oneClickMode) {
					setExplaination(
							"Process " + a.getID() + " has been suspended",
							"As there is not enough space in main memory process "
									+ a.getID()
									+ " has been suspended, this process is moved to the ready, suspend queue.");
					animationPanel.readyToReadySuspend(a.getPanel());
					expReset();
				}
			}
		}
		// remove moved processes from ready queue
		ready.removeAll(readySuspend);
		ready.removeAll(blockedSuspend);
		ready.removeAll(blocked);
	}
	
	/**
	 * Times out the current process
	 */
	public void timeout() {
		if (!oneClickMode) {
			animationPanel.timeout(current.getPanel());
			expReset();
		}
		// set its state
		current.setState(State.ready);
		// add current process back to the ready queue
		ready.add(current);
		current = null;
		dispatch = true;
		simulationTime += DISPATCHER_LATENCY / 2;
	}
	
	/**
	 * Dispatches a process to the CPU
	 */
	public void execute() {
		current.execute();
		if (!oneClickMode) {
			animationPanel.invalidate();
			animationPanel.validate();
			animationPanel.revalidate();
			animationPanel.repaint();
		}
		// if the current process is now blocked waiting for I/O
		if (current.isBlocked()) {
			IOWait a = new IOWait(CPUProcess.LOWER_BOUND,
					CPUProcess.UPPER_BOUND);
			current.setEvent(a);
			if (!oneClickMode) {
				setExplaination(
						"Process " + current.getID() + " has become blocked.",
						"The process has completed its CPU burst and has become blocked as it is waiting for an I/O event to occur.");
				animationPanel.eventWait(current.getPanel());
				expReset();
			}
			blocked.add(current);
		}
		// if the process has completed execution
		else if (current.isFinished()) {
			if (!oneClickMode) {
				setExplaination("Process " + current.getID() + " is finished.",
						"The process has completed its execution and will now exit the system.");
				animationPanel.exit(current.getPanel());
				expReset();
				animationPanel.remove(current.getControlPanel());
			}
			finished.add(current);
		}
		// if either of the above conditions is met
		if (current.isBlocked() || current.isFinished()) {
			simulationTime += DISPATCHER_LATENCY / 2;
			current = null;
			dispatch = true;
			if (discipline instanceof RoundRobin) {
				setExplaination("Quantum Countdown " + (RoundRobin.quantum),
						"", quantumtext);
			}
		}
	}
	
	/**
	 * Sets the current Process
	 * 
	 * @param next
	 */
	public void setCurrent(CPUProcess next) {
		dispatch = false;
		current = next;
		current.updateUI();
		ready.remove(current);
		if (!oneClickMode) {
			animationPanel.dispatch(current.getPanel(), current.getBurst());
			expReset();
		}
		simulationTime += DISPATCHER_LATENCY / 2;
	}
	
	/**
	 * called when action event occurs
	 */
	public void actionPerformed(ActionEvent e) {
		if (simulationTime > 15) {
			animationPanel.removeInstructions();
		}
		if (e.getSource() == timer) {
			animationPanel.invalidate();
			animationPanel.validate();
			animationPanel.revalidate();
			animationPanel.repaint();
			
			if (all.size() != finished.size()) {
				performSchedule();
			}
			// if the simulation is over
			else {
				animationPanel.complete();
				animationPanel.invalidate();
				animationPanel.validate();
				animationPanel.revalidate();
				animationPanel.repaint();
				timer.stop();
			}
			animationPanel.invalidate();
			animationPanel.validate();
			animationPanel.revalidate();
			animationPanel.repaint();
		}
	}
	
	/**
	 * Sets the textual explaination of a step in execution
	 * 
	 * @param explaination
	 */
	public void setExplaination(String explaination, String exp,
			ExecutionPanel a) {
		if (!oneClickMode) {
			a.setLabel(explaination, exp);
			a.setBackground(new Color(34, 139, 34));
			a.invalidate();
			a.validate();
			a.revalidate();
			a.repaint();
			a.updateUI();
			animationPanel.repaint();
		}
	}
	
	/**
	 * Reset explaination panel
	 */
	public void expReset() {
		text.setBackground(Color.WHITE);
		animationPanel.repaint();
		main.invalidate();
		main.validate();
	}
	
	public void setExplaination(String string, String exp) {
		if (!oneClickMode) {
			setExplaination(string, exp, text);
		}
	}
	
	/**
	 * returns the currently running process
	 * 
	 * @return the currently running process
	 */
	public CPUProcess getCurrent() {
		return current;
	}
	
	/**
	 * Returns the ready processes
	 * 
	 * @return the ready processes
	 */
	public ArrayList<CPUProcess> getReadyQueue() {
		return ready;
	}
	
	/**
	 * sets the time quantum
	 * 
	 * @param quantum
	 *            - the value
	 */
	public void setQuantum(int quantum) {
		RoundRobin.quantum = quantum;
		RoundRobin.quantumCounter = quantum;
	}
	
	/**
	 * Sets the number of processes
	 * 
	 * @param numProcesses
	 */
	public void setNumProcesses(long numProcesses) {
		this.numProcesses = numProcesses;
	}
	
	/**
	 * Sets the animation speed
	 * 
	 * @param value
	 *            - the speed
	 */
	public void setAnimationSpeed(int value) {
		delay = 4000 / value;
		animationPanel.setSleep(delay);
		if (timer != null) {
			timer.setDelay(delay);
		}
	}
	
	/**
	 * Returns the animation panel
	 * 
	 * @return the animation panel
	 */
	public static AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	
	/**
	 * Returns whether or not lines should be drawn in the GUI
	 * 
	 * @return the boolean value
	 */
	public boolean drawLines() {
		return transitions;
	}
	
	public void runThrough() {
		oneClickMode = true;
		createQueue();
		statistics.getPanel().updateStatName(discipline.getName());
		main.addStats();
		do {
			performSchedule();
			statistics.calculateStatistics(finished, all, ready, timeBusy);
			statistics.reset();
		}
		while (all.size() != finished.size());
		statistics.calculateStatistics(finished, all, ready, timeBusy);
		statistics.updateStatGUI();
		this.main.pane.setSelectedIndex(1);
		controlPanel.restart();
	}
	
	/**
	 * Sets the value of transitions and repaints GUI
	 * 
	 * @param b
	 *            - the value to assign
	 */
	public void setTransitions(boolean b) {
		transitions = b;
		animationPanel.repaint();
	}
	
	/**
	 * returns whether the scheduler is running or not
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * causes the simulation to pause
	 */
	public void pause() {
		timer.stop();
		running = false;
	}
	
	/**
	 * resumes the simulation
	 */
	public void resume() {
		if (!stepmode) {
			timer.start();
			running = true;
			controlPanel.startButton.setText("Pause");
		}
	}
	
	/**
	 * Returns the statistics panel
	 * 
	 * @return the statistics panel
	 */
	public StatisticsPanel getStatisticsPanel() {
		return statistics.getPanel();
	}
	
	/**
	 * Returns the control panel
	 * 
	 * @return the control panel
	 */
	public ControlPanel getControlPanel() {
		return controlPanel;
	}
	
	/**
	 * sets the percentage of cpu bound processes in the system
	 * 
	 * @param cpu
	 */
	public void setPercentCPU(double cpu) {
		percentCPU = cpu;
	}
	
	/**
	 * Returns the textual explanation panel
	 * 
	 * @return the textual explanation panel
	 */
	public Component getExplainationPanel() {
		return text;
	}
	
	public boolean shouldDispatch() {
		return dispatch;
	}
	
	/**
	 * restarts the scheduler
	 */
	public void restart() {
		main.restart();
		animationPanel = new AnimationPanel(this);
		numProcesses = 10;
		simulationTime = 0;
		current = null;
		timeBusy = 0;
		running = false;
		all = new ArrayList<CPUProcess>();
		ready = new ArrayList<CPUProcess>();
		readySuspend = new ArrayList<CPUProcess>();
		blockedSuspend = new ArrayList<CPUProcess>();
		blocked = new ArrayList<CPUProcess>();
		finished = new ArrayList<CPUProcess>();
		ShortestJobFirst.alpha = 0.5;
		RoundRobin.quantum = 5;
		RoundRobin.quantumCounter = 5;
		
		// transitions = true;
		delay = 1000;
		if (timer != null) {
			timer.stop();
		}
		statistics = new Statistics();
		dispatch = true;
		arrival = false;
		oneClickMode = false;
		stepmode = false;
		text.setVisible(false);
		quantumtext.setLabel("", "");
		quantumtext.setBackground(Color.white);
		text.setBackground(Color.white);
		text.setLabel(Text.WELCOME, "");
		first = true;
	}
	
	public boolean processArrived() {
		return arrival;
	}
	
	public ExecutionPanel getQuantumText() {
		return quantumtext;
	}
	
	public void setLatency(int value) {
		DISPATCHER_LATENCY = value;
	}
}