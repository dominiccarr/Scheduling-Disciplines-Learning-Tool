package gui;

import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import scheduler.Text;

public class ExplanationPanel extends JPanel {
	
	private static final long serialVersionUID = 4834749636090980424L;
	private final JTextPane textPane = new JTextPane();
	private ImageIcon backgroundIcon = new ImageIcon(getClass().getResource(Text.PATH + "backsground.png"));
	private JLabel background = new JLabel(backgroundIcon);
	private JLabel icon;
	
	/**
	 * Constructor for this class
	 * @param text - the text to be displayed
	 * @param sjf_icon - path to the image
	 */
	public ExplanationPanel(String text, URL sjf_icon) {
		setLayout(null);
		setOpaque(false);
		icon = new JLabel(new ImageIcon(sjf_icon));
		add(icon);
		icon.setBounds(10, 10, 100, 70);
		add(textPane);
		textPane.setBounds(120, 10, 740, 70);
		textPane.setEditable(false);
		textPane.setOpaque(false);
		add(background);
		background.setBounds(0, 0, 870, 90);
		textPane.setText(text);
	}
	
	/**
	 * Secondary constructor
	 * @param text
	 */
	public ExplanationPanel(String text) {
		backgroundIcon = new ImageIcon(getClass().getResource(Text.PATH + "backsground2.png"));
		background = new JLabel(backgroundIcon);
		setLayout(null);
		setOpaque(false);
		add(textPane);
		textPane.setBounds(10, 10, 480, 70);
		textPane.setEditable(false);
		textPane.setOpaque(false);
		add(background);
		background.setBounds(0, 0, 500, 90);
		textPane.setText(text);
	}
	
	/**
	 * set the display text
	 * @param text
	 */
	public void setText(String text){
		textPane.setText(text);
	}
	
	/**
	 * overrides action listener method.
	 */
	public void addMouseListener(MouseListener a){
		super.addMouseListener(a);
		textPane.addMouseListener(a);
	}
}
