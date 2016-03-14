package disciplines;

import processes.CPUProcess;
import scheduler.Scheduler;
import scheduler.Text;

public class PreemptivePriorityQueue extends PriorityQueue {
	
	public PreemptivePriorityQueue(Scheduler scheduler) {
		super(scheduler);
		name = "Premptive Priority Queue";
		briefExplanation = Text.PQ_EXP_BRIEF;
		explanation = Text.pqExplainhtml;
	}
	
	public void schedule() {
		super.schedule();
		preempt();
	}
	
	private void preempt() {
		CPUProcess current = scheduler.getCurrent();
		if (current != null) {
			int best = current.getPriority();
			CPUProcess next = null;
			
			for (CPUProcess a : scheduler.getReadyQueue()) {
				if (a.getPriority() > best) {
					next = a;
					best = a.getPriority();
				}
			}
			
			if (next != null) {
				scheduler.setExplaination("Process " + current.getID()
						+ " is prempted, as Process " + next.getID()
						+ " is of greater Priority (" + next.getPriority()
						+ ")", premptReason(current, next));
				scheduler.timeout();
				scheduler.setCurrent(next);
			}
		}
	}
	
	public String premptReason(CPUProcess a, CPUProcess b) {
		return "Process " + a.getID() + " with priority " + a.getPriority()
				+ " is prempted. Process " + b.getID()
				+ " is dispatched as it is of greater priority ("
				+ b.getPriority() + ")";
	}
	
}
