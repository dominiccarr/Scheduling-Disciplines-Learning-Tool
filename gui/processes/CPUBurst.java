package gui.processes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class CPUBurst extends JComponent {
	
	private static final long serialVersionUID = 3601280562543163347L;
	private static final int WIDTH = 15;
	private static final int HEIGHT = 70;
	private int currHeight;
	private Color color;
	private int step;
	private JLabel remaining = new JLabel("Remaining");
	private JLabel est = new JLabel();
	private JLabel actual  = new JLabel();
	
	/**
	 * Constructor
	 * @param i
	 * @param a
	 */
	public CPUBurst(int i, int estimate, Color a) {
		color = a;
		setSize(WIDTH, HEIGHT);
		step = HEIGHT/i;
		currHeight = HEIGHT;
		add(remaining);
		add(est);
		add(actual);
		
		Font bigFont = new Font("Serif", Font.PLAIN, 11);
		remaining.setFont(bigFont);
		est.setFont(bigFont);
		actual.setFont(bigFont);
		remaining.setBounds(21, 5, 100, 30);
		est.setBounds(21, 20, 100, 30);
		actual.setBounds(21, 35, 100, 30);
		this.est.setText("Est: " + estimate);
		this.actual.setText("Actual: " + i);

	}
	
	/**
	 * Decrements the counter
	 */
	public void decrement(int est, int actual){
		currHeight -= step;
		updateUI();
		this.est.setText("Est: " + est);
		this.actual.setText("Actual: " + actual);
	}
	
	/**
	 * paints the component
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(color);		
		int calc = HEIGHT-currHeight+2;
		g.fillRect(2, calc, WIDTH-3, HEIGHT - calc-2);
	}
}