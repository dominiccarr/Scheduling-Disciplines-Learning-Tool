package processes;

import java.awt.Color;

import javax.swing.ImageIcon;

import scheduler.Text;
import gui.processes.ProcessControlPanel;
import gui.processes.ProcessPanel;

/**
 * 
 * @author dominic
 * 
 */
public class IOBoundProcess extends CPUProcess {
		
	/**
	 * Constructor for an I/O bound process
	 */
	public IOBoundProcess() {
		CPU_UPPER_BOUND = 7;
		CPU_LOWER_BOUND = 3;
		previousEstimate = (int) ((CPU_UPPER_BOUND + CPU_LOWER_BOUND) / 2) - 1;
		image = new ImageIcon(getClass()
				.getResource(Text.PATH + "IO-Bound.png"));
		color = Color.red.darker();
		processPanel = new ProcessPanel(this);
		processControlPanel = new ProcessControlPanel(this);
		generateCPUBurst();
	}
	
}
