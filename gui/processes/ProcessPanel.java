package gui.processes;

import gui.AnimationPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import processes.CPUProcess;

public class ProcessPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = -8688048767327303735L;
	private final CPUProcess parent;
	private JLabel processID = new JLabel();
	private JLabel background;
	private AnimationPanel controller;
	
	/**
	 * The constructor
	 * 
	 * @param p
	 *            - the parent process
	 */
	public ProcessPanel(CPUProcess p) {
		parent = p;
		addMouseListener(this);
		setLayout(null);
		setOpaque(false);
		setToolTipText(parent.toString());
		add(processID);
		setProcessLabel(parent.getID());
		add(background = new JLabel(parent.getImage()));
		background.setBounds(0, 0, 20, 20);
	}
	
	/**
	 * Sets the process label
	 * @param ID - the processes priority
	 */
	public void setProcessLabel(int ID) {
		processID.setText(""+ID);
		if (ID > 9) {
			processID.setFont(new Font("arial", Font.PLAIN, 10));
			processID.setBounds(5, 5, 20, 10);
		}
		else {
			processID.setFont(new Font("arial", Font.PLAIN, 14));
			processID.setBounds(6, 5, 20, 10);
		}
	}
	
	/**
	 * update the tooltip
	 */
	public void updateDetails() {
		setToolTipText(parent.toString());
		updateUI();
	}
	
	/**
	 * Add the processes controller
	 * @param animationPanel
	 */
	public void addController(AnimationPanel animationPanel) {
		controller = animationPanel;
	}
	
	/**
	 * Called when the component is clicked
	 */
	public void mouseClicked(MouseEvent e) {
		controller.addControlPanel(parent.getControlPanel());
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
	}
	
	/**
	 * Returns the components preferred size
	 */
	public Dimension getPreferredSize(){
		return new Dimension(20,30);
	}
}