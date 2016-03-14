package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.Scheduler;
import scheduler.Text;

public class ExecutionPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1908454867395851235L;
	private JLabel label = new JLabel();
	private JButton question = new JButton(new ImageIcon(getClass()
			.getResource(Text.PATH + "q.png")));
	private String explaination;
	
	/**
	 * Constructor
	 * @param args
	 */
	public ExecutionPanel(String args) {
		label.setText(args);
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(null);
		setBackground(Color.white);
		add(label);
		label.setBounds(10, 0, 600, 30);
		question.addActionListener(this);
		question.setContentAreaFilled(false);
		question.setBorderPainted(false);
		add(question);
		question.setBounds(700, 0, 40, 30);
	}
	
	public void setVisible(boolean b){
		question.setVisible(b);
	}
	
	/**
	 * Sets the label and updates the GUI
	 * @param a
	 */
	public void setLabel(String a, String exp) {
		explaination = exp;
		label.setText(a);
		invalidate();
		validate();
		revalidate();
		repaint();
		updateUI();
	}

	/**
	 * Called when an action is performed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == question){
			Scheduler.getAnimationPanel().explain(explaination);
		}		
	}
}