package disciplines;

import java.awt.Color;

import processes.CPUProcess;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * This class replicates the functionality of the round robin scheduling
 * algorithm
 * 
 * @author dominic
 * 
 */
public class RoundRobin extends SchedulingDiscipline {
	
	public static int quantum = 5;
	public static int quantumCounter = 5;
	
	/**
	 * The constructor
	 */
	public RoundRobin(Scheduler scheduler) {
		this.scheduler = scheduler;
		name = "Round Robin";
		briefExplanation = Text.RR_EXP_BRIEF;
		explanation = Text.rrExplainhtml;
	}
	
	@Override
	public void schedule() {
		scheduler.quantumtext.setBackground(Color.white);
		// if the burst is finished
		if (scheduler.shouldDispatch() && !scheduler.getReadyQueue().isEmpty()) {
			CPUProcess a = scheduler.getReadyQueue().get(0);
			scheduler.setExplaination("Process " + a.getID() + " chosen to run for " + quantum + " cycles", explainSchedule(a));
			scheduler.setCurrent(a);
			quantumCounter = quantum;
			scheduler.setExplaination("Quantum Countdown " + (quantumCounter), "", scheduler.quantumtext);
		} 
		// if the quantum has expired
		else if (quantumCounter == 0 && scheduler.getCurrent() != null){
			CPUProcess current = scheduler.getCurrent();
			scheduler.setExplaination("Process " + scheduler.getCurrent().getID() + " has been preempted, as its quantum has elapsed.", explainPrempt(current));
			scheduler.timeout();
			if (!scheduler.getReadyQueue().isEmpty()){
				CPUProcess a = scheduler.getReadyQueue().get(0);
				scheduler.setCurrent(a);
				quantumCounter = quantum;
				scheduler.setExplaination("Quantum Countdown " + quantumCounter, "", scheduler.quantumtext);
			}
		}
		// otherwise decrement counter
		if (scheduler.getCurrent() != null){
			scheduler.setExplaination("Quantum Countdown " + (--quantumCounter), "", scheduler.quantumtext);
		}
	}
	
	public String explainPrempt(CPUProcess a){
		return "Process " +a.getID() + " has been preempted and returned to the tail of the queue as its allocated Time Quantum ("+quantum+" cycles) has now elapsed.";
		
	}
	
	public String explainSchedule(CPUProcess a ){
		return "Process " + a.getID() + " chosen to run for " + quantum + " cycles. Process " + a.getID() + " was at the head of the queue, processes are chosen in a FIFO manner similar to First Come First Served";
		
	}
}