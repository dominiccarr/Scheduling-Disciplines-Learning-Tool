package processes;

import gui.processes.CPUBurst;
import gui.processes.ProcessControlPanel;
import gui.processes.ProcessPanel;

import java.awt.Color;

import javax.swing.ImageIcon;

import disciplines.ShortestJobFirst;

import scheduler.IOWait;

/**
 * Represents a process.
 * 
 * @author Dominic Carr
 * 
 */
public abstract class CPUProcess {
	
	public static final int MAX_PRIORITY = 9;
	public static final int MIN_PRIORITY = 0;
	public static final int LOWER_BOUND = 2;
	public static final int UPPER_BOUND = 6;
	protected ProcessPanel processPanel;
	protected ProcessControlPanel processControlPanel;
	protected State state = State._new;
	protected int processID = 0;
	protected int totalLength = 0;
	protected int waitingTime = 0;
	protected int priority = 0;
	protected int previousBurstTime = 0;
	protected int currentBurst = 0;
	protected ImageIcon image;
	protected int arrivalTime = 0;
	protected int executionTime = 0;
	protected int responseTime = 0;
	protected int initialEstimate;
	protected int CPU_UPPER_BOUND = 10;
	protected int CPU_LOWER_BOUND = 4;
	protected Color color;
	protected IOWait event;
	protected CPUBurst burst;
	protected int previousEstimate;
	protected int estimate;
	protected int estimateStore;
	protected boolean first = true;
	private boolean started = false;
	private boolean firstOver = false;
	private int previousBurstTimeStore = 0;	
	
	/**
	 * Process Constructor
	 */
	public CPUProcess() {
		totalLength = (int) (23 + Math.random() * (30 - 23));
		priority = (int) ((Math.random() * MAX_PRIORITY) + 0.5);
	}
	
	/**
	 * Runs the process
	 */
	public void execute() {
		if (!started) {
			started = true;
			responseTime = waitingTime;
		}
		estimate--;
		state = State.running;
		executionTime++;
		currentBurst--;
		totalLength--;
		burst.decrement(estimate, currentBurst);
		if (currentBurst == 0) {
			firstOver = true;
			if (totalLength <= 0) {
				state = State.exit;
			}
			else {
				state = State.blocked;
				generateCPUBurst();
			}
		}
		updateUI();
	}
	
	/**
	 * Updates the GUI components
	 */
	public void updateUI() {
		processControlPanel.updateDetails();
		processPanel.updateDetails();
	}
	
	/**
	 * the process waits to run
	 */
	public void waitToRun() {
		if (isReady()) {
			waitingTime++;
		}
		else if (isBlocked() && event.hasOccured()) {
			state = State.ready;
		}
		else if (isBlockedSuspended() && event.hasOccured()) {
			state = State.suspended_ready;
		}
		updateUI();
	}
	
	/**
	 * Returns true of the process is blocked suspend
	 * 
	 * @return
	 */
	public boolean isBlockedSuspended() {
		return state.equals(State.suspended_blocked);
	}
	
	/**
	 * Returns true if the process is blocked
	 * 
	 * @return
	 */
	public boolean isBlocked() {
		return state.equals(State.blocked);
	}
	
	/**
	 * Returns true if the process is suspended but ready
	 * 
	 * @return
	 */
	public boolean isReadySuspend() {
		return state.equals(State.suspended_ready);
	}
	
	/**
	 * Returns true if the process is completed
	 * 
	 * @return
	 */
	public boolean isFinished() {
		return state.equals(State.exit);
	}
	
	/**
	 * generates a CPU Burst
	 */
	protected void generateCPUBurst() {
		currentBurst = (int) (CPU_LOWER_BOUND + Math.random()
				* (CPU_UPPER_BOUND - CPU_LOWER_BOUND) + 0.5);
		if (currentBurst > totalLength) {
			currentBurst = totalLength;
		}
		performEstimate();
		burst = new CPUBurst(currentBurst, estimate, getColor());
		
		previousBurstTimeStore  = previousBurstTime;
		previousBurstTime = currentBurst;
		updateUI();
	}
	
	/**
	 * Estimates the processes next burst time
	 */
	private void performEstimate() {
		double alpha = ShortestJobFirst.alpha;
		if (first) {
			first = false;
			estimate = previousEstimate;
		}
		else {
			double a = ((alpha * previousBurstTime) + ((1 - alpha) * previousEstimate));
			double frac = a - ((int) a);
			estimate = (int) (frac>=0.5 ? Math.ceil(a) : Math.floor(a));
			previousEstimate = estimateStore;
		}
		estimateStore = estimate;
	}
	
	public String getBurstExp() {
		if (firstOver) {
			return "Tn+1 = alpha*tn + (1-alpha)*Tn = "
					+ ShortestJobFirst.alpha + "*" + previousBurstTimeStore
					+ " + (1-" + ShortestJobFirst.alpha + ")*"
					+ previousEstimate + " = " + estimateStore+ ". This is subject to rounding to nearest whole number";
		}
		else {
			return "The first burst time estimate is a default predication per type of process";
		}
	}
	
	public int getPreviousEstimate() {
		return previousEstimate;
	}
	
	/**
	 * Returns the PID of the process
	 * 
	 * @return the PID of the process
	 */
	public int getID() {
		return processID;
	}
	
	/**
	 * Sets the state of the process
	 * 
	 * @param state
	 *            - the new state
	 */
	public void setState(State state) {
		this.state = state;
		updateUI();
	}
	
	/**
	 * Returns the GUI of the process
	 * 
	 * @return the process GUI
	 */
	public ProcessPanel getPanel() {
		return processPanel;
	}
	
	/**
	 * Sets the priority of the process
	 * 
	 * @param priority
	 *            - the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
		updateUI();
	}
	
	/**
	 * Returns the priority of the process
	 * 
	 * @return the priority of the process
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * Returns the amount of time a process has been waiting
	 * 
	 * @return the process waiting time
	 */
	public int getWaitingTime() {
		return waitingTime;
	}
	
	/**
	 * Returns an imageIcon representing the process
	 * 
	 * @return an imageIcon representing the process
	 */
	public ImageIcon getImage() {
		return image;
	}
	
	/**
	 * Sets the arrival time of the process
	 * 
	 * @param arrivalTime
	 *            - the arrival time
	 */
	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	/**
	 * Returns the arrival time of the process
	 * 
	 * @return the arrival time of the process
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	/**
	 * Returns textual Information about the process
	 * 
	 * @return textual Information about the process
	 */
	public String toString() {
		return "ID: " + getID() + " Priority: " + getPriority() + " Response: "
				+ responseTime + " Waiting: " + waitingTime + " Arrival: " + arrivalTime + " Total: "
				+ getTotalTime();
	}
	
	/**
	 * Returns true if the process has started execution
	 * 
	 * @return true if the process has started execution
	 */
	public boolean hasStarted() {
		return started;
	}
	
	/**
	 * returns true if the process has completed executions, false otherwise
	 * 
	 * @return if the process has finished execution
	 */
	public boolean hasFinished() {
		return state.equals(State.exit);
	}
	
	/**
	 * The length of time the process had to wait before being given its CPU
	 * allocation
	 * 
	 * @return length of time the process had to wait before being given its CPU
	 *         allocation
	 */
	public int getResponseTime() {
		return responseTime;
	}
	
	/**
	 * The total runtime of the process
	 * 
	 * @return The total runtime of the process
	 */
	public int getTotalTime() {
		return waitingTime + executionTime;
	}
	
	/**
	 * Returns the amount of time the process spent executing
	 * 
	 * @return the amount of time the process spent executing
	 */
	public int getExecutionTime() {
		return executionTime;
	}
	
	/**
	 * Returns this processes control panel GUI
	 * 
	 * @return this processes control panel GUI
	 */
	public ProcessControlPanel getControlPanel() {
		return processControlPanel;
	}
	
	/**
	 * Sets the colour of the process
	 * 
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * sets the I/O event
	 * 
	 * @param a
	 *            - the event
	 */
	public void setEvent(IOWait a) {
		event = a;
	}
	
	/**
	 * Checks if a process is in the ready state
	 * 
	 * @return true if ready
	 */
	public boolean isReady() {
		return state.equals(State.ready);
	}
	
	/**
	 * Sets the process identifier
	 * 
	 * @param i
	 */
	public void setPID(int i) {
		processID = i;
		processPanel.setProcessLabel(i);
		processControlPanel.updateDetails();
	}
	
	/**
	 * Returns the burst GUI
	 * 
	 * @return the burst GUI
	 */
	public CPUBurst getBurst() {
		return burst;
	}
	
	/**
	 * Returns the estimated burst time
	 * 
	 * @return the estimated burst time
	 */
	public int getEstimate() {
		return estimate;
	}
	
	public int getEstimateStore() {
		return estimateStore;
	}
	
	/**
	 * increases a processes priority
	 */
	public void increasePriority() {
		if (priority < 9) {
			priority++;
		}
	}
	
	/**
	 * check if the process is new
	 * 
	 * @return true if new
	 */
	public boolean isNew() {
		return state.equals(State._new);
	}
	
	/**
	 * Returns the state of the process
	 * 
	 * @return
	 */
	public State getState() {
		return state;
	}
	
	/**
	 * Returns the process previous burst time
	 * 
	 * @return the process previous burst time
	 */
	public int getPreviousBurstTime() {
		return previousBurstTimeStore;
	}
	
	public boolean hasRun() {
		return firstOver;
	}

	public boolean isRunning() {
		return state.equals(State.running);
	}	
}
