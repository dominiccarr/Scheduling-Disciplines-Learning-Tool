package disciplines;

import processes.CPUProcess;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * This class replicates the functionality of the first come first served
 * scheduling algorithm
 * 
 * @author dominic
 * 
 */
public class FirstComeFirstServed extends SchedulingDiscipline {
	
	/**
	 * The constructor
	 */
	public FirstComeFirstServed(Scheduler scheduler) {
		this.scheduler = scheduler;
		name = "First Come First Served";
		briefExplanation = Text.FCFS_EXP_BRIEF;
		explanation = Text.fcfsExplainhtml;
	}
	
	@Override
	public void schedule() {
		if (scheduler.shouldDispatch() && !scheduler.getReadyQueue().isEmpty()) {
			CPUProcess next = scheduler.getReadyQueue().get(0);
			scheduler.setExplaination("Process " + next.getID()
					+ " at the head of the ready queue chosen to run.",
					scheduleExp(next));
			scheduler.setCurrent(next);
		}
	}
	
	private String scheduleExp(CPUProcess a) {
		if (scheduler.getReadyQueue().size() == 1) {
			return "Process " + a.getID() + " was chosen as it was the only waiting process.";
		}
		else {
			return "Process "
					+ a.getID()
					+ " was chosen as this process has been waiting on the ready queue for the greatest amount of time. Processes are selected in a FIFO manner.";
		}
	}
}
