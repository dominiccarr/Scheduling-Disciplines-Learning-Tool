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
public class CPUBoundProcess extends CPUProcess {
	
	private static final long serialVersionUID = 3710869149292092328L;
	
	/**
	 * The constructor for CPU bound processes
	 */
	public CPUBoundProcess() {
		totalLength = (int) (35 + Math.random() * (45 - 35));
		
		CPU_UPPER_BOUND = 17;
		CPU_LOWER_BOUND = 11;
		color = Color.green.darker();
		previousEstimate = (int) ((CPU_UPPER_BOUND + CPU_LOWER_BOUND) / 2) - 1;
		image = new ImageIcon(getClass().getResource(
				Text.PATH + "CPU-Bound.png"));
		processPanel = new ProcessPanel(this);
		processControlPanel = new ProcessControlPanel(this);
		generateCPUBurst();
	}
	
}
