package disciplines;

import processes.CPUProcess;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * This class replicates the functionality of the shortest time remaining first
 * scheduling algorithm
 * 
 * @author dominic
 * 
 */
public class ShortestRemainingTimeFirst extends ShortestJobFirst {
	
	/**
	 * The constructor for SRTF
	 * 
	 * @param scheduler
	 */
	public ShortestRemainingTimeFirst(Scheduler scheduler) {
		super(scheduler);
		name = "Shortest Remaining Time First";
		briefExplanation = Text.SRTF_EXP_BRIEF;
		explanation = Text.srtfExplainhtml;
	}
	
	public void schedule() {
		super.schedule();
		prempt();
	}
	
	private void prempt() {
		CPUProcess current = scheduler.getCurrent();
		if (scheduler.processArrived() && current != null) {
			int time = current.getEstimate();
			CPUProcess next = null;
			for (CPUProcess a : scheduler.getReadyQueue()) {
				if (a.getEstimateStore() < time) {
					next = a;
					time = a.getEstimate();
				}
			}
			
			if (next != null) {
				scheduler.setExplaination("Process " + current.getID()
						+ " is prempted, as Process " + next.getID()
						+ " has a shorter predicted burst ("
						+ next.getEstimateStore() + ")", premptReason(current, next));
				scheduler.timeout();
				scheduler.setCurrent(next);
			}
		}
	}
	
	public String premptReason(CPUProcess a, CPUProcess b){
		return "Process " + a.getID()
		+ " is prempted, as Process " + b.getID()
		+ " has a shorter predicted burst of length: " + b.getEstimateStore()
		+ ". Process burst times are estimated based upon exponential averaging of previous bursts.";
	}
}