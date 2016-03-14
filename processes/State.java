package processes;

/**
 * These are the states that all processes can have
 * @author Dominic Carr
 * ready - process is waiting to be dispatched
 * running - using the CPU
 * waiting - waiting for I/O
 * suspended ready - a process which is ready to run but has been suspended
 * suspended blocked - a blocked process which has been suspended
 * new - newly created process
 * exit - a process which is complete
 */

public enum State {
	_new, ready, running, blocked, suspended_ready, suspended_blocked, exit
}
