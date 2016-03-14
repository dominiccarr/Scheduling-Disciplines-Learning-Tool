package scheduler;

public class IOWait {
	
	public int LOWER_BOUND = 2;
	public int UPPER_BOUND = 6;
	private int countdown;
	
	/**
	 * The constructor
	 */
	public IOWait(int lower, int upper) {
		countdown = (int) (lower + Math.random() * (upper - lower) + 0.5);
	}
	
	/**
	 * Returns true if event has occurred
	 * 
	 * @return true if event has occurred
	 */
	public boolean hasOccured() {
		return --countdown <= 0;
	}
}
