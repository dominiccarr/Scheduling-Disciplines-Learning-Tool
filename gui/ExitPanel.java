package gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.Scheduler;
import scheduler.Text;

public final class ExitPanel extends JPanel implements MouseListener{
	
	private static final long serialVersionUID = -2068602440078541116L;
	private final ImageIcon CPUIcon = new ImageIcon(getClass().getResource(Text.PATH  + "exit.png"));
	private final JLabel label = new JLabel(CPUIcon);
	private String explaination;
	
	/**
	 * The constructor
	 * @param text - the panels tooltip text
	 */
	public ExitPanel(String text, String exp){
		explaination = exp;
		setLayout(null);
		add(label);
		setOpaque(false);
		label.setBounds(0, 0, 100, 75);
		setToolTipText(text);
		this.addMouseListener(this);
	}
	
	/**
	 * adds a component to the panel
	 */
	public Component add(Component a){
		remove(label);
		super.add(a);
		a.setBounds(90, 35, 20, 20);
		super.add(label);
		return a;
	}
	
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
