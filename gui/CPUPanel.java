package gui;

import gui.processes.CPUBurst;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.Scheduler;
import scheduler.Text;

public class CPUPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 8142202648290685383L;
	private final ImageIcon CPUIcon = new ImageIcon(getClass().getResource(
			Text.PATH + "cpu.png"));
	private final JLabel label = new JLabel(CPUIcon);
	private CPUBurst burst;
	private String explaination;
	
	/**
	 * The constructor
	 * 
	 * @param text
	 *            - the panels tooltip text
	 */
	public CPUPanel(String text, String d) {
		explaination = d;
		setLayout(null);
		setOpaque(false);
		add(label);
		label.setBounds(0, 0, 200, 94);
		setToolTipText(text);
		addMouseListener(this);
	}
	
	/**
	 * adds a component to the panel
	 */
	public Component add(Component a) {
		remove(label);
		super.add(a);
		a.setBounds(90, 35, 20, 20);
		super.add(label);
		this.invalidate();
		this.validate();
		this.revalidate();
		this.repaint();
		return a;
		
	}
	
	/**
	 * adds the burst
	 * 
	 * @param a
	 *            - the burst
	 */
	public void addBurst(CPUBurst a) {
		burst = a;
		remove(label);
		add((Component) a);
		a.setBounds(115, 10, 400, 80);
		super.add(label);
		this.invalidate();
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * removes the burst
	 */
	public void removeBurst() {
		remove(burst);
		this.invalidate();
		this.validate();
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Called if the panel is clicked
	 */
	public void mouseClicked(MouseEvent e) {
		Scheduler.animationPanel.explain(explaination);
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
