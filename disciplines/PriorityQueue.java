package disciplines;

import java.util.ArrayList;
import processes.CPUProcess;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * 
 * @author dominic
 * 
 */
public class PriorityQueue extends SchedulingDiscipline {
	
	/**
	 * The constructor for this class
	 * 
	 * @param scheduler
	 */
	public PriorityQueue(Scheduler scheduler) {
		this.scheduler = scheduler;
		name = "Non-premptive Priority Queue";
		briefExplanation = Text.PQ_EXP_BRIEF;
		explanation = Text.pqExplainhtml;
	}
	
	@Override
	public void schedule() {
		ArrayList<CPUProcess> ready = scheduler.getReadyQueue();
		ageProcesses(scheduler.getReadyQueue());
		if (scheduler.shouldDispatch() && !ready.isEmpty()) {
			CPUProcess next = getHighestPriority(scheduler.getReadyQueue());
			
			scheduler.setExplaination("Process " + next.getID()
					+ " with highest priority (" + next.getPriority()
					+ ") chosen to run.", scheduleExp(next));
			
			scheduler.setCurrent(next);
		}
	}
	
	private void ageProcesses(ArrayList<CPUProcess> ready) {
		String procs = "";
		if ((Scheduler.simulationTime % 20) == 0) {
			for (CPUProcess a : ready) {
				if (a.hasStarted() && a.getWaitingTime() > 10) {
					a.increasePriority();
					procs += a.getID() + ",";
				}
			}
			if (!procs.equals("")) {
				scheduler
						.setExplaination(
								"Processes "
										+ procs
										+ " had their priorities increased so as to avoid starvation",
								ageReason(procs));
			}
		}
	}
	
	/**
	 * Returns the process with highest priority from a Vector
	 * 
	 * @param processes
	 *            - the vector of processes
	 * @return the process of highest priority
	 */
	public static CPUProcess getHighestPriority(ArrayList<CPUProcess> processes) {
		CPUProcess highest = processes.get(0);
		int highestPriority = processes.get(0).getPriority();
		for (CPUProcess a : processes) {
			if (a.getPriority() > highestPriority) {
				highestPriority = a.getPriority();
				highest = a;
			}
		}
		return highest;
	}
	
	public String scheduleExp(CPUProcess a) {
		if (scheduler.getReadyQueue().size() == 1) {
			return "Process " + a.getID()
					+ " was chosen as it was the only waiting process.";
		}
		else {
			
			return "Process "
					+ a.getID()
					+ " priority "
					+ a.getPriority()
					+ " is chosen to run as it has the greatest priority of all the processes on the ready queue";
		}
	}
	
	public String ageReason(String a) {
		return "Processes "
				+ a
				+ " had their priorities increased so as to avoid process starvation. Process starvation is when a low priority process never receives CPU time as there is always a process of greater priority waiting to run.";
		
	}
	
}
