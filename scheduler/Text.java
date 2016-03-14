package scheduler;

/**
 * class contains all the explanation text used in this application
 * 
 * @author dominic
 * 
 */
public final class Text {
	
	public static final String READY_TOOLTIP = "The queue of processes which are ready to execute. The processes in this queue are chosen to run by the Scheduling Discipline in use.";
	
	public static final String BLOCKED_TOOLTIP = "The queue of processes which are blocked preforming I/O operations.";
	
	public static final String RS_TOOLTIP = "The queue of ready processes which have been suspended";
	
	public static final String BS_TOOLTIP = "The queue of blocked processes which have been suspended";
	
	public static final String CPU_TOOLTIP = "The CPU which executes the processes";
	
	public static final String START_TOOLTIP = "New processses enter the system";
	
	public static final String EXIT_TOOLTIP = "Completed processes exit the system";

	public static final String READY_DETAILED = "The queue of processes which are ready to execute. From here a process may transition to running in the CPU or to the suspended, ready queue. Processes waiting in this queue are chosen to run by the scheuling discipline.";
	
	public static final String BLOCKED_DETAILED = "The queue of processes which are blocked performing I/O operations. From here a process may transition back to the ready queue upon completion of I/O or to the blocked, suspended queue.";
	
	public static final String RS_DETAILED = "The queue of ready processes which have been suspended. Suspension means a process has been removed from main memory. From here a process may transition back to the ready queue.";
	
	public static final String BS_DETAILED = "The queue of blocked processes which have been suspended. From here a process may transition to the ready, suspended queue when the I/O event occurs.";
	
	public static final String CPU_DETAILED = "The processor which executes the currently selected process. Processes are selected by the scheduling discipline in accordance with its goals.";
	
	public static final String START_DETAILED = "This is where newly created processes enter the system, from here they transition to the ready queue. Process arrivals follow a Poisson distribution.";
	
	public static final String EXIT_DETAILED = "This is where processes which has completed execution leave the system.";
	
	public static final String RR_EXP_BRIEF = "The scheduler allocates a slice of CPU time to each ready process and cycles through them";
	
	public static final String SJF_EXP_BRIEF = "The scheduler selects the waiting process with the smallest execution time to execute next.";
	
	public static final String SRTF_EXP_BRIEF = "The Scheduler chooses the shortest process, if a shorter process arrives the current process will be preempted";
	
	public static final String FCFS_EXP_BRIEF = "Processes are scheduled on the basis of how long they have been waiting";
	
	public static final String PQ_EXP_BRIEF = "Processes are given a priority number and the scheduler always chooses the process of greatest priority";
	
	public static final String rrExplain = "In this nonpreemptive discipline a fixed slice of CPU time, a quantum, is given to each process. If a process is still running when its quantum is up it is preempted and put back at the end of the queue and the next process runs for the time quantum. In this way the algorithm cycles through the ready queue processes in a FIFO manner. If the process finishes execution before the quantum expires the next process will be run immediately";
	
	public static final String srtfExplain = "This is the preemptive variant of Shortest Job First. The scheduler chooses the process with the least amount of CPU time remaining. This discipline functions exactly in the manner of SJF except when a process is added to the ready queue. When this occurs its predicted CPU burst time is checked against the remaining burst time of the current process and if its burst time is shorter the current process is preempted and the new process dispatched.";
	
	public static final String fcfsExplain = "This is the simplest scheduling discipline. It is a non-preemptive algorithm. Processes are simply assigned to the CPU in the order that they arrive at the ready queue. After the completion of the current process' CPU burst (or if an I/O interrupt occurs) the process at the head of the queue is executed. Newly arriving process' are placed at the end of the queue, as are processes transitioning from blocked to ready.";
	
	public static final String sjfExplain = "In this conceptually simple scheduling discipline, the scheduler chooses the process on the ready queue with the shortest execution time to run next. This is a non-premptive algorithm. This discipline either makes the assumption that the run time of a process is known in advance or that each process' next burst time must be estimated based on prior behavior.";
	
	public static final String pqExplain = "In this discipline a priority, typically a number denoting importance, is given to each process. The scheduler dispatches the process of highest priority to the CPU, when that process relinquishes the CPU (assuming no premption) the process of next highest priority is dispatched. Priority based scheduling can be either preemptive or nonpreemptive.";
	
	public static final String rrExplainhtml = "<html>In this nonpreemptive discipline a fixed slice of CPU time, a quantum, is given to each process. If a process is <br>still running when its quantum is up it is preempted and put back at the end of the queue and the next process runs <br>for the time quantum. In this way the algorithm cycles through the ready queue processes in a FIFO manner. If <br>the process finishes execution before the quantum expires the next process will be run immediately</html>";
	
	public static final String srtfExplainhtml = "<html>This is the preemptive variant of Shortest Job First. The scheduler chooses the process with the least amount of <br>CPU time remaining. This discipline functions exactly in the manner of SJF except when a process is added to the <br>ready queue. When this occurs its predicted CPU burst time is checked against the remaining burst time of the <br>current process and if its burst time is shorter the current process is preempted and the new process dispatched.</html>";
	
	public static final String fcfsExplainhtml = "<html>This is the simplest scheduling discipline. It is a non-preemptive algorithm. Processes are simply assigned to the <br>CPU in the order that they arrive at the ready queue. After the completion of the current process' CPU burst <br>(or if an I/O interrupt occurs) the process at the head of the queue is executed. Newly arriving process' are placed at <br>the end of the queue, as are processes transitioning from blocked to ready.</html>";
	
	public static final String sjfExplainhtml = "<html>In this conceptually simple scheduling discipline, the scheduler chooses the process on the ready queue with <br>the shortest execution time to run next. This is a non-premptive algorithm. This discipline either makes the <br>assumption that the run time of a process is known in advance or that each process' next <br>burst time must be estimated based on prior behavior.</html>";
	
	public static final String pqExplainhtml = "<html>In this discipline a priority, typically a number denoting importance, is given to each process. The scheduler <br>dispatches the process of highest priority to the CPU, when that process relinquishes the CPU (assuming no <br>premption) the process of next highest priority is dispatched. Priority based scheduling can be either preemptive or nonpreemptive.</html>";
	
	public static final String PATH = "/resources/";
	public static final String WAITING_TOOLTIP = "Waiting time is the total amount of time a process spent waiting on the ready queue. This is a important evaluation criteria as the delay experienced by a process is the primary performance bottleneck.";
	public static final String RESPONSE_TOOLTIP = "Response time is the amount of time which elapsed before a waiting process first receives CPU time.";
	public static final String TURNAROUND_TOOLTIP = "A processes turnaround time is the total amount of time which a process took from entering the system to completing its execution. This is given by waiting time + service time.";
	public static final String N_TURNAROUND_TOOLTIP = "A processes normalized turnaround time is the processes total time relative to the time spent waiting to run. It is calculated as follows normalized turnaround = turnaround / service time";
	public static final String QUEUE_SIZE = "These are statistics regarding the ready queue. The ready queues average size is an important variable in determining the efficiency of the scheduling discipline.";

	public static final String WELCOME = "Welcome, below you may set up your simulation, mouse over a setting to see how it is used.";

	public static final String IO_BOUND = "I/O Bound processes have many I/O bursts and only short CPU bursts to process the I/O";

	public static final String CPU_BOUND = "CPU Bound Processes have long CPU bursts and only a few short I/O Bursts";

}
