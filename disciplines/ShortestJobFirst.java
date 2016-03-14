package disciplines;

import java.util.ArrayList;
import processes.CPUProcess;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * This class replicates the functionality of the shortest job first scheduling
 * algorithm
 * 
 * @author dominic
 * 
 */
public class ShortestJobFirst extends SchedulingDiscipline {
	
	public static double alpha = 0.5;

	
	/**
	 * Constructor for SJF
	 * 
	 * @param scheduler
	 */
	public ShortestJobFirst(Scheduler scheduler) {
		this.scheduler = scheduler;
		name = "Shortest Job First";
		briefExplanation = Text.SJF_EXP_BRIEF;
		explanation = Text.sjfExplainhtml;
	}
	
	@Override
	public void schedule() {
		if (scheduler.shouldDispatch() && !scheduler.getReadyQueue().isEmpty()) {
			CPUProcess next = getShortestProcess(scheduler.getReadyQueue());
			scheduler
					.setExplaination(
							"Process "
									+ next.getID()
									+ ", having the shortest expected burst time, is chosen to execute.",
							ScheduleExp(next));
			scheduler.setCurrent(next);
		}
	}
	
	public CPUProcess getShortestProcess(ArrayList<CPUProcess> processList) {
		int best = Integer.MAX_VALUE;
		CPUProcess shortest = null;
		
		for (CPUProcess process : processList) {
			int time = process.getEstimate();
			if (time < best) {
				best = time;
				shortest = process;
			}
		}
		return shortest;
	}
	
	public String ScheduleExp(CPUProcess a) {
		if (scheduler.getReadyQueue().size() == 1) {
			return "Process " + a.getID() + " was chosen as it was the only waiting process.";
		}
		else {
		return "Process "
				+ a.getID()
				+ ", having the shortest expected burst time, is chosen to execute. Burst times are estimated based on an exponential average of the process' previous burst times."
				+ " Process " + a.getID() + " has an estimated burst of length of "
				+ a.getEstimate()+ ".";
		}
	}
}
