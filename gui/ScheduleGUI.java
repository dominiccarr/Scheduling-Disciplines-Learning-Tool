package gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * This the main application user interface
 * 
 * @author Dominic Carr
 * 
 */
public class ScheduleGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Scheduling Disciplines Learning Tool";
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 710;
	private final URL RR_ICON = getClass().getResource(Text.PATH + "rr_icon.png");
	private final URL FCFS_ICON = getClass().getResource(Text.PATH + "fcfs_icon.png");
	private final URL SJF_ICON = getClass().getResource(Text.PATH + "sjf_icon.png");
	private  final URL STRF_ICON = getClass().getResource(Text.PATH + "srtf_icon.png");
	private  final URL PQ_ICON = getClass().getResource(Text.PATH + "pq_icon.png");
	private final URL SCHEDULE_LOGO_ICON = getClass().getResource(Text.PATH + "schedule_icon.png");
	private ImageIcon scheduleIcon = new ImageIcon(SCHEDULE_LOGO_ICON);
	private final JLabel scheduleLogo = new JLabel(scheduleIcon);
	private JPanel mainPanel = new JPanel();
	private ExplanationPanel sjf = new ExplanationPanel(Text.sjfExplain,
			SJF_ICON);
	private ExplanationPanel rr = new ExplanationPanel(Text.rrExplain, RR_ICON);
	private ExplanationPanel fcfs = new ExplanationPanel(Text.fcfsExplain,
			FCFS_ICON);
	private ExplanationPanel srtf = new ExplanationPanel(Text.srtfExplain,
			STRF_ICON);
	private ExplanationPanel pq = new ExplanationPanel(Text.pqExplain,
			PQ_ICON);
	
	// the scheduler
	private Scheduler scheduler;
	private JPanel explanationPanel;
	public JTabbedPane pane = new JTabbedPane();
	
	/**
	 * Constructor for the GUI
	 */
	public ScheduleGUI(Scheduler s) {
		super(TITLE);		
		scheduler = s;
		setResizable(false);
		setVisible(true);
		pack();
		setSize(WIDTH, HEIGHT);
		setLocation(setPosition(this));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(pane);
		addMainPanel();
		mainPanel.add(s.getExplainationPanel());
		s.getExplainationPanel().setBounds(0, 480, 750, 30);
		mainPanel.add(s.getQuantumText());
		s.getQuantumText().setBounds(760, 480, 415, 30);
		update(getGraphics());
	}
	
	/**
	 * initializes the main panel
	 */
	private void addMainPanel() {
		pane.addTab("Main", mainPanel);
		mainPanel.setLayout(null);
		initExplanation();
		mainPanel.add(scheduler.getControlPanel());
		scheduler.getControlPanel().setBounds(0, 510, 1180, 130);
	}
	
	/**
	 * Sets up the explanation panel
	 */
	private void initExplanation(){
		int expLeft = 130;
		mainPanel.add(explanationPanel = new JPanel());
		
		explanationPanel.setBounds(0, 0, 1180, 480);
		explanationPanel.setLayout(null);
		
		scheduleLogo.setBounds(180, 25, scheduleIcon.getIconWidth(),
				scheduleIcon.getIconHeight());
		explanationPanel.add(scheduleLogo);
		try {
			Thread.sleep(300);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		explanationPanel.remove(scheduleLogo);

		explanationPanel.add(sjf);
		sjf.setBounds(expLeft, 5, 870, 90);
		
		explanationPanel.add(rr);
		rr.setBounds(expLeft, 100, 870, 90);
		
		explanationPanel.add(fcfs);
		fcfs.setBounds(expLeft, 195, 870, 90);
		
		explanationPanel.add(srtf);
		srtf.setBounds(expLeft, 290, 870, 90);
		
		explanationPanel.add(pq);
		pq.setBounds(expLeft, 385, 870, 90);
		
		explanationPanel.add(scheduleLogo);
	}
	
	/**
	 * positions the JFrame in the center of the screen
	 */
	private static Point setPosition(Component target) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((screen.getWidth() - target.getWidth()) / 2);
		return new Point(x, 0);
	}
	
	/**
	 * Sets up the animation panel
	 */
	public void addAnimationPanel() {
		mainPanel.remove(explanationPanel);
		Scheduler.getAnimationPanel();
		Scheduler.getAnimationPanel().setBounds(0, 0, 1175, 480);
		mainPanel.add(Scheduler.getAnimationPanel());
		mainPanel.updateUI();
	}

	/**
	 * restarts the GUI
	 */
	public void restart() {
		pane.remove(scheduler.getStatisticsPanel());
		mainPanel.remove(Scheduler.getAnimationPanel());
		mainPanel.add(explanationPanel);
		mainPanel.updateUI();
	}

	/**
	 * Adds the statistics panel
	 */
	public void addStats() {
		pane.addTab("Statistics", scheduler.getStatisticsPanel());		
	}
}