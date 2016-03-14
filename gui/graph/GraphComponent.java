package gui.graph;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * This class represents a item in a graph. Adapted from
 * http://www.javaworld.com/javaworld/jw-07-1997/jw-07-step.html
 * 
 * @author dominic
 */
public class GraphComponent extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = -331468881301252176L;
	private final Color color;
	private final String title;
	private final int value;
	private GraphPanel controller;
	private final String info;
	
	/**
	 * Constructor for graph item.
	 * 
	 * @param title
	 *            - the items title
	 * @param value
	 *            - the items value
	 * @param color
	 *            - the items color
	 */
	public GraphComponent(String title, int value, Color color, String infor) {
		this.title = title;
		this.value = value;
		this.color = color;
		this.info = infor;
		addMouseListener(this);
	}
	
	/**
	 * Getter method for the color.
	 * 
	 * @return - the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Getter method for the title.
	 * 
	 * @return - the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Getter method for the value.
	 * 
	 * @return - the value
	 */
	public int getValue() {
		return value;
	}
	
	public void addController(GraphPanel a){
		controller = a;
	}

	public void mouseClicked(MouseEvent e) {
		controller.show(info);		
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
