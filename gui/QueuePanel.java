package gui;

import gui.processes.ProcessPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.Scheduler;

public class QueuePanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 2305309841603720911L;
	private final Color queueColor = Color.blue;
	private ArrayList<ProcessPanel> components = new ArrayList<ProcessPanel>();
	private String tooltip;
	private JPanel queue;
	private static final int QUEUE_HEIGHT = 30;
	private static final int QUEUE_WIDTH = 400;
	private JLabel headb = new JLabel("Head");
	private JLabel tailr = new JLabel("Tail");
	private JLabel names = new JLabel("Tail");
	
	/**
	 * The constructor for queue panels
	 * 
	 * @param align
	 * @param tooltip
	 */
	public QueuePanel(int align, String tooltip, String detailed,
			ComponentOrientation componentOrient, String name, int a) {
		queue = new JPanel();
		setLayout(null);
		setOpaque(false);
		this.tooltip = detailed;
		addMouseListener(this);
		queue.addMouseListener(this);
		names.setText(name);
		add(names);
		
		headb.setBounds(0, 22 + QUEUE_HEIGHT, 100, 30);
		tailr.setBounds(QUEUE_WIDTH - 25, 22 + QUEUE_HEIGHT, 100, 30);
		names.setBounds(a, 2, 200, 30);
		if (ComponentOrientation.RIGHT_TO_LEFT == componentOrient) {
			headb.setBounds(QUEUE_WIDTH - 32, 22 + QUEUE_HEIGHT, 100, 30);
			tailr.setBounds(0, 22 + QUEUE_HEIGHT, 100, 30);
			names.setBounds(0, 2, 200, 30);
		}
		add(headb);
		add(tailr);
		queue.setBounds(0, 28, QUEUE_WIDTH, QUEUE_HEIGHT);
		queue.setComponentOrientation(componentOrient);
		queue.setLayout(new FlowLayout(align));
		queue.setBackground(queueColor);
		queue.setBorder(BorderFactory.createLineBorder(Color.black));
		queue.setToolTipText(tooltip);
		add(queue);
	}
	
	/**
	 * Returns the centre point
	 * 
	 * @param a
	 * @return
	 */
	public static int getCentre(int a) {
		return a + 28 + (QUEUE_HEIGHT) / 2;
	}
	
	/**
	 * returns the top point
	 * 
	 * @param y
	 * @return
	 */
	public static int getTop(int y) {
		return y + 28;
	}
	
	/**
	 * returns the bottom point
	 * 
	 * @param y
	 * @return
	 */
	public static int getBottom(int y) {
		return y + 28 + QUEUE_HEIGHT;
	}
	
	/**
	 * adds a process
	 * 
	 * @param a
	 */
	public void add(ProcessPanel a) {
		queue.add((Component) a);
		components.add(a);
		invalidate();
		validate();
		revalidate();
		repaint();
	}
	
	/**
	 * removes a process
	 * 
	 * @param a
	 */
	public void remove(ProcessPanel a) {
		queue.remove((Component) a);
		components.remove(a);
		invalidate();
		validate();
		revalidate();
		repaint();
	}
	
	/**
	 * Handles mouse clicks
	 */
	public void mouseClicked(MouseEvent e) {
		Scheduler.animationPanel.explain(tooltip);
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
	}
	
	public void mouseReleased(MouseEvent e) {
	}
}
