package scheduler;

import javax.swing.SwingUtilities;

/**
 * 
 * @author dominic
 *
 */
public class Main {

	/**
	 * Main method running the application
	 * @param args
	 */
	public static void main(String[] args) {
		 SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	new Scheduler();	
			    }
			  });
	}
}