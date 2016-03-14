package gui;

import gui.processes.CPUBurst;
import gui.processes.ProcessControlPanel;
import gui.processes.ProcessPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import javax.swing.JLabel;
import javax.swing.JPanel;

import disciplines.ISchedulingDiscipline;
import scheduler.Scheduler;
import scheduler.Text;

/**
 * The main GUI components displays the animation
 * 
 * @author dominic carr
 * 
 */
public class AnimationPanel extends JPanel implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	// Co-ordinate constants
	private static final int QUEUE_HEIGHT = 30;
	private static final int QUEUE_WIDTH = 400;
	private static final int QUEUE_X = 260;
	private static final int READY_X = 210;
	private static final int READY_Y = 100;
	private static int READY_CENTRE = READY_Y + (QUEUE_HEIGHT / 2);
	private static final int CPU_X = 750;
	private static final int CPU_Y = 100;
	private static final int CPU_WIDTH = 200;
	private static final int CPU_HEIGHT = 94;
	private static final int CPU_END = CPU_WIDTH + CPU_X;
	private static final int QUEUE_END = QUEUE_WIDTH + QUEUE_X;
	private static final int READY_QUEUE_END = QUEUE_WIDTH + READY_X;
	private static final int CPU_READY_MIDPOINT = ((CPU_X - READY_QUEUE_END) / 2)
			+ READY_QUEUE_END;
	int midpoint = ((CPU_X - READY_X) / 2) + 180;
	private static final Color TRANSITIONS_COLOR = Color.BLACK;
	private static final int RS_Y = 220;
	private static final int BS_Y = 300;
	private static final int B_Y = 380;
	private static final int EXIT_X = 1000;
	private static final int EXIT_Y = 40;
	// The transition arcs
	private static final QuadCurve2D DISPATCH_LINE = new QuadCurve2D.Float();
	private static final QuadCurve2D NEW_PROCESS_LINE = new QuadCurve2D.Float();
	private static final QuadCurve2D EVENT_WAIT_LINE = new QuadCurve2D.Float();
	private static final QuadCurve2D READY_TO_READY_SUSPEND = new QuadCurve2D.Float();
	private static final CubicCurve2D TIMEOUT_LINE = new CubicCurve2D.Double();
	private static final QuadCurve2D CPU_TO_EXIT = new QuadCurve2D.Float();
	private static final QuadCurve2D EVENT_OCCURS = new QuadCurve2D.Float();
	private static final CubicCurve2D BLOCKED_SUSPEND_READY_SUSPEND = new CubicCurve2D.Double();
	private static final CubicCurve2D BLOCKED_TO_BLOCKED_SUSPEND = new CubicCurve2D.Double();
	private static final CubicCurve2D BLOCKED_SUSPEND_TO_BLOCKED = new CubicCurve2D.Double();
	
	private static final QuadCurve2D READY_SUSPEND_TO_READY = new QuadCurve2D.Float();
	// Labels
	private static final JLabel CPU = new JLabel("CPU");
	private static final JLabel eventWait = new JLabel("Event wait");
	private static final JLabel dispatch = new JLabel("Dispatch");
	private static final JLabel timeout = new JLabel("Interrupt");
	private static final JLabel exit = new JLabel("Exit");
	private static final JLabel start = new JLabel("Start");
	private static final JLabel eventoccurs = new JLabel("Event Occurs");
	private static final JLabel admitted = new JLabel("Admitted");
	
	private JLabel name = new JLabel();
	private JLabel explain = new JLabel();
	private static final int START_X = 30;
	private static final int EVENT_X = 270;
	// queue panels
	private QueuePanel readyPanel;
	private QueuePanel blockedPanel;
	private QueuePanel blockedSuspendPanel;
	private QueuePanel readySuspendPanel;
	// exit panels
	private ExitPanel exitpanel;
	private ExitPanel startpanel;
	private ExplanationPanel explainstuff;
	
	// process control panel
	private ProcessControlPanel control;
	// the scheduler
	private Scheduler scheduler;
	// cpu panel
	private CPUPanel cpu;
	private static int SLEEP = 60;
	private boolean complete;
	
	boolean a = false;
	
	private boolean timeoutBool = a;
	private boolean dispatchBool = a;
	private boolean newBool = a;
	private boolean eventwaitBool = a;
	private boolean readybool = a;
	private boolean exitBool = a;
	private boolean blockededtoreadyBool = a;
	private boolean blockedtoblockedsuspendBool = a;
	private boolean readysuspendtoreadybool = a;
	private boolean eventoccursbool = a;
	
	private boolean panelclick = true;
	private boolean procclick = true;
	private JLabel proc = new JLabel(
			"Double click a process to view information, and modify priority");
	private JLabel panel = new JLabel("Double click any queue");
	private JLabel panel2 = new JLabel("to view relevant info");
	
	private boolean blockedsusedtoblockedBool;
	
	public void removeInstructions() {
		panelclick = false;
		procclick = false;
		proc.setVisible(false);
		panel.setVisible(false);
		panel2.setVisible(false);
	}
	
	/**
	 * The constructor
	 */
	public AnimationPanel(Scheduler s) {
		scheduler = s;
		explainstuff = new ExplanationPanel("");
		add(explainstuff);
		explainstuff.setBounds(350, 200, 800, 100);
		explainstuff.setVisible(false);
		explainstuff.addMouseListener(this);
		setLayout(null);
		addQueues();
		setUpArcs();
		addLabels();
	}
	
	public void setSleep(int a) {
		SLEEP = a / 10;
	}
	
	public void explain(String a) {
		panelclick = false;
		panel.setVisible(false);
		panel2.setVisible(false);
		scheduler.pause();
		explainstuff.setText(a);
		explainstuff.setVisible(true);
	}
	
	/**
	 * sets the disciplines textual info
	 * 
	 * @param a
	 *            - the name
	 * @param exp
	 *            - a brief explanation
	 */
	public void setDiscipline(ISchedulingDiscipline a) {
		name.setText(a.getName());
		name.setToolTipText(a.getExplanation());
		explain.setText(a.getBriefExplanation());
		repaint();
	}
	
	/**
	 * Reset all the booleans
	 */
	private void reset(boolean b) {
		timeoutBool = b;
		dispatchBool = b;
		newBool = b;
		eventwaitBool = b;
		readybool = b;
		exitBool = b;
		blockededtoreadyBool = b;
		blockedtoblockedsuspendBool = b;
		readysuspendtoreadybool = b;
		blockedsusedtoblockedBool = b;
		eventoccursbool = b;
		eventoccurs.setVisible(b);
		eventWait.setVisible(b);
		dispatch.setVisible(b);
		timeout.setVisible(b);
		admitted.setVisible(b);
		
	}
	
	/**
	 * Adds the labels to the panel
	 */
	private void addLabels() {
		add(name);
		name.setBounds(400, 2, 200, 30);
		add(explain);
		explain.setBounds(200, 20, 700, 30);
		add(CPU);
		CPU.setBounds(CPU_X + (CPU_WIDTH / 2) - 10, CPU_Y - 25, 100, 30);
		add(eventWait);
		eventWait.setBounds(870, EVENT_X, 100, 30);
		add(dispatch);
		dispatch.setBounds(650, QueuePanel.getTop(READY_Y) - 45, 100, 30);
		add(timeout);
		timeout.setBounds(midpoint - 30, QueuePanel.getTop(READY_Y) + 70, 100,
				30);
		add(exit);
		exit.setBounds(EXIT_X + 40, EXIT_Y - 28, 100, 30);
		add(eventoccurs);
		eventoccurs.setBounds(25, EVENT_X, 100, 30);
		
		add(start);
		start.setBounds(START_X + 36, EXIT_Y - 28, 100, 30);
		
		add(admitted);
		admitted.setBounds(START_X + 60, EXIT_Y + 105, 100, 30);
		
		add(proc);
		proc.setBounds(200, 45, 500, 30);
		add(panel);
		panel.setBounds(10, 400, 500, 30);
		add(panel2);
		panel2.setBounds(10, 420, 500, 30);
		
		admitted.setVisible(false);
		eventoccurs.setVisible(false);
		eventWait.setVisible(false);
		dispatch.setVisible(false);
		timeout.setVisible(false);
	}
	
	/**
	 * sets up the transition arcs
	 */
	private void setUpArcs() {
		READY_CENTRE = QueuePanel.getCentre(READY_Y);
		int rTop = QueuePanel.getTop(READY_Y);
		DISPATCH_LINE.setCurve(READY_QUEUE_END - 10, rTop, CPU_READY_MIDPOINT,
				QueuePanel.getTop(READY_Y) - 30, CPU_X, rTop);
		
		TIMEOUT_LINE.setCurve(CPU_X + 5, QueuePanel.getCentre(READY_Y),
				midpoint + 40, QueuePanel.getTop(READY_Y) + QUEUE_HEIGHT + 60,
				READY_X - 40, rTop + QUEUE_HEIGHT + 60, READY_X, QueuePanel
						.getCentre(READY_Y));
		
		EVENT_WAIT_LINE.setCurve(CPU_X + 100, CPU_Y + CPU_HEIGHT, 900, 300,
				QUEUE_END, QueuePanel.getCentre(B_Y));
		
		READY_TO_READY_SUSPEND.setCurve(READY_QUEUE_END, READY_CENTRE, 700,
				READY_CENTRE + 25, QUEUE_END, QueuePanel.getBottom(RS_Y));
		
		EVENT_OCCURS.setCurve(QUEUE_X, QueuePanel.getCentre(B_Y), 0, 300,
				READY_X, READY_CENTRE);
		
		int a = QUEUE_X + QUEUE_WIDTH + 50;
		BLOCKED_SUSPEND_READY_SUSPEND.setCurve(QUEUE_X, QueuePanel
				.getCentre(BS_Y), QUEUE_X - 60, QueuePanel.getTop(BS_Y) - 65,
				a, QueuePanel.getTop(BS_Y) + 20, QUEUE_END, QueuePanel
						.getCentre(RS_Y));
		
		BLOCKED_TO_BLOCKED_SUSPEND.setCurve(QUEUE_X, QueuePanel.getCentre(B_Y),
				QUEUE_X - 60, QueuePanel.getTop(B_Y) - 65, a, QueuePanel
						.getTop(B_Y) + 20, QUEUE_END, QueuePanel
						.getCentre(BS_Y));
		
		BLOCKED_SUSPEND_TO_BLOCKED.setCurve(QUEUE_X,
				QueuePanel.getCentre(BS_Y), QUEUE_X - 60, QueuePanel
						.getBottom(BS_Y) + 35, a, QueuePanel.getTop(B_Y) - 30,
				QUEUE_END, QueuePanel.getCentre(B_Y));
		
		READY_SUSPEND_TO_READY.setCurve(QUEUE_X, QueuePanel.getCentre(RS_Y),
				70, 180, READY_X, READY_CENTRE);
		
		CPU_TO_EXIT.setCurve(CPU_END - 2, READY_CENTRE, CPU_X + CPU_WIDTH + 70,
				150, EXIT_X + 50, EXIT_Y + 75);
		
		NEW_PROCESS_LINE.setCurve(START_X + 50, EXIT_Y + 75, 50, 150,
				READY_X - 1, READY_CENTRE);
	}
	
	/**
	 * sets up the queue panels
	 */
	private void addQueues() {
		add(readyPanel = new QueuePanel(FlowLayout.RIGHT, Text.READY_TOOLTIP,
				Text.READY_DETAILED, ComponentOrientation.RIGHT_TO_LEFT,
				"Ready Queue", 1));
		readyPanel.setBounds(READY_X, READY_Y, QUEUE_WIDTH, 80);
		
		add(blockedSuspendPanel = new QueuePanel(FlowLayout.LEFT,
				Text.BS_TOOLTIP, Text.BS_DETAILED,
				ComponentOrientation.LEFT_TO_RIGHT, "Blocked, Suspend Queue",
				240));
		blockedSuspendPanel.setBounds(QUEUE_X, BS_Y, QUEUE_WIDTH, 80);
		
		add(blockedPanel = new QueuePanel(FlowLayout.LEFT,
				Text.BLOCKED_TOOLTIP, Text.BLOCKED_DETAILED,
				ComponentOrientation.LEFT_TO_RIGHT, "Blocked Queue", 300));
		blockedPanel.setBounds(QUEUE_X, B_Y, QUEUE_WIDTH, 80);
		
		add(readySuspendPanel = new QueuePanel(FlowLayout.LEFT,
				Text.RS_TOOLTIP, Text.RS_DETAILED,
				ComponentOrientation.LEFT_TO_RIGHT, "Ready, Suspend Queue", 250));
		readySuspendPanel.setBounds(QUEUE_X, RS_Y, QUEUE_WIDTH, 80);
		
		add(cpu = new CPUPanel(Text.CPU_TOOLTIP, Text.CPU_DETAILED));
		cpu.setBounds(CPU_X, CPU_Y, 200, CPU_HEIGHT);
		
		add(exitpanel = new ExitPanel(Text.EXIT_TOOLTIP, Text.EXIT_DETAILED));
		exitpanel.setBounds(EXIT_X, EXIT_Y, 100, 75);
		
		add(startpanel = new ExitPanel(Text.START_TOOLTIP, Text.START_DETAILED));
		startpanel.setBounds(START_X, EXIT_Y, 100, 75);
		this.updateUI();
	}
	
	/**
	 * Draws an arrow head
	 * 
	 * @param g2
	 *            - the graphics object
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	private void drawArrow(Graphics2D g2, int a, int b, int c, int d) {
		Polygon polygon = new Polygon();
		polygon.addPoint(a + d, b - c);
		polygon.addPoint(a, b);
		polygon.addPoint(a + d, b + c);
		g2.fillPolygon(polygon);
	}
	
	/**
	 * Draws the arcs
	 * 
	 * @param g2
	 *            - the graphics of this panel
	 */
	private void drawArcs(Graphics2D g2) {
		if (timeoutBool) {
			g2.draw(TIMEOUT_LINE);
			drawArrow(g2, (int) NEW_PROCESS_LINE.getP2().getX() + 1,
					(int) NEW_PROCESS_LINE.getP2().getY(), -5, -5);
		}
		if (dispatchBool) {
			g2.draw(DISPATCH_LINE);
			drawArrow(g2, (int) DISPATCH_LINE.getP2().getX() + 5,
					(int) DISPATCH_LINE.getP2().getY() + 1, -5, -5);
		}
		if (newBool) {
			g2.draw(NEW_PROCESS_LINE);
			drawArrow(g2, (int) NEW_PROCESS_LINE.getP2().getX() + 1,
					(int) NEW_PROCESS_LINE.getP2().getY(), -5, -5);
		}
		if (eventwaitBool) {
			g2.draw(EVENT_WAIT_LINE);
			drawArrow(g2, (int) EVENT_WAIT_LINE.getP2().getX(),
					(int) EVENT_WAIT_LINE.getP2().getY(), 5, 5);
		}
		if (readybool) {
			drawArrow(g2, (int) BLOCKED_SUSPEND_READY_SUSPEND.getP2().getX(),
					(int) BLOCKED_SUSPEND_READY_SUSPEND.getP2().getY(), 5, 5);
			g2.draw(READY_TO_READY_SUSPEND);
		}
		if (exitBool) {
			g2.draw(CPU_TO_EXIT);
		}
		if (blockededtoreadyBool) {
			g2.draw(BLOCKED_SUSPEND_READY_SUSPEND);
			drawArrow(g2, (int) BLOCKED_SUSPEND_READY_SUSPEND.getP2().getX(),
					(int) BLOCKED_SUSPEND_READY_SUSPEND.getP2().getY(), 5, 5);
		}
		if (blockedtoblockedsuspendBool) {
			g2.draw(BLOCKED_TO_BLOCKED_SUSPEND);
			drawArrow(g2, (int) BLOCKED_TO_BLOCKED_SUSPEND.getP2().getX(),
					(int) BLOCKED_TO_BLOCKED_SUSPEND.getP2().getY(), 5, 5);
		}
		if (readysuspendtoreadybool) {
			g2.draw(READY_SUSPEND_TO_READY);
			drawArrow(g2, (int) NEW_PROCESS_LINE.getP2().getX() + 1,
					(int) NEW_PROCESS_LINE.getP2().getY(), -5, -5);
		}
		if (eventoccursbool) {
			g2.draw(EVENT_OCCURS);
			drawArrow(g2, (int) NEW_PROCESS_LINE.getP2().getX() + 1,
					(int) NEW_PROCESS_LINE.getP2().getY(), -5, -5);
		}
		if (blockedsusedtoblockedBool) {
			g2.draw(BLOCKED_SUSPEND_TO_BLOCKED);
			drawArrow(g2, (int) EVENT_WAIT_LINE.getP2().getX(),
					(int) EVENT_WAIT_LINE.getP2().getY(), 5, 5);
		}
	}
	
	/**
	 * Transitions a processPanel across a shape, this method is based upon a
	 * method described at: http://forums.sun.com/thread.jspa?threadID=5421786
	 * 
	 * @param a
	 *            - the process panel
	 * @param b
	 *            - the shape
	 */
	private void transition(ProcessPanel a, Shape b, int limit, double flatness) {
		if (scheduler.drawLines()) {
			add(a);
			PathIterator iterator = b.getPathIterator(new AffineTransform());
			FlatteningPathIterator path = new FlatteningPathIterator(iterator,
					flatness, limit);
			while (!path.isDone()) {
				float[] coords = new float[2];
				path.currentSegment(coords);
				a.setBounds((int) coords[0], (int) coords[1] - 5, 20, 20);
				paintImmediately(0, 0, 1180, 490);
				path.next();
				long s = System.currentTimeMillis();
				while (System.currentTimeMillis() < s + SLEEP) {
					
				}
			}
			remove(a);
			paintImmediately(0, 0, 1180, 490);
		}
	}
	
	/**
	 * Performs the dispatch transition
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void dispatch(final ProcessPanel a, CPUBurst b) {
		
		readyPanel.remove(a);
		paintImmediately(0, 0, 1180, 490);
		
		dispatch.setVisible(true);
		dispatchBool = true;
		
		transition(a, DISPATCH_LINE, 3, 0.5);
		cpu.add(a);
		cpu.addBurst(b);
		paintImmediately(0, 0, 1180, 490);
		repaint();
		reset(false);
		
	}
	
	public void blockedSuspendToBlocked(final ProcessPanel a) {
		blockedSuspendPanel.remove(a);
		blockedsusedtoblockedBool = true;
		transition(a, BLOCKED_SUSPEND_TO_BLOCKED, 3, 0.2);
		blockedPanel.add(a);
		reset(false);
	}
	
	/**
	 * Performs the exit transition
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void exit(final ProcessPanel a) {
		exitBool = true;
		cpu.remove(a);
		cpu.removeBurst();
		transition(a, CPU_TO_EXIT, 3, 0.2);
		reset(false);
	}
	
	/**
	 * Performs the new process transition
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void newProcessArrival(final ProcessPanel a) {
		a.addController(this);
		newBool = true;
		admitted.setVisible(true);
		
		transition(a, NEW_PROCESS_LINE, 3, 0.2);
		readyPanel.add(a);
		paintImmediately(0, 0, 1180, 490);
		reset(false);
	}
	
	/**
	 * Performs the event wait transition
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void eventWait(ProcessPanel a) {
		cpu.remove(a);
		cpu.removeBurst();
		paintImmediately(0, 0, 1180, 490);
		eventWait.setVisible(true);
		eventwaitBool = true;
		
		transition(a, EVENT_WAIT_LINE, 3, 0.5);
		blockedPanel.add(a);
		paintImmediately(0, 0, 1180, 490);
		repaint();
		reset(false);
	}
	
	/**
	 * Performs the transition from blocked suspend to ready suspend
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void blockedSupendToReadySuspend(ProcessPanel a) {
		blockedSuspendPanel.remove(a);
		blockededtoreadyBool = true;
		transition(a, BLOCKED_SUSPEND_READY_SUSPEND, 4, 0.2);
		readySuspendPanel.add(a);
		reset(false);
	}
	
	/**
	 * Performs the timeout transition
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void timeout(final ProcessPanel a) {
		
		cpu.remove(a);
		cpu.removeBurst();
		
		cpu.updateUI();
		paintImmediately(0, 0, 1180, 490);
		timeout.setVisible(true);
		timeoutBool = true;
		
		transition(a, TIMEOUT_LINE, 4, 0.2);
		readyPanel.add(a);
		paintImmediately(0, 0, 1180, 490);
		reset(false);
	}
	
	/**
	 * Transitions between the ready queue and the ready suspend queue
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void readyToReadySuspend(ProcessPanel a) {
		readyPanel.remove(a);
		readybool = true;
		transition(a, READY_TO_READY_SUSPEND, 4, 0.2);
		readySuspendPanel.add(a);
		repaint();
		reset(false);
	}
	
	/**
	 * Performs the transition from blocked to blocked suspend
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void blockedToBlockedSuspend(ProcessPanel a) {
		blockedPanel.remove(a);
		
		blockedtoblockedsuspendBool = true;
		transition(a, BLOCKED_TO_BLOCKED_SUSPEND, 4, 0.2);
		blockedSuspendPanel.add(a);
		paintImmediately(0, 0, 1180, 490);
		reset(false);
	}
	
	/**
	 * Performs the transition from ready suspend to blocked suspend
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void readySuspendToReady(ProcessPanel a) {
		readysuspendtoreadybool = true;
		readySuspendPanel.remove(a);
		transition(a, READY_SUSPEND_TO_READY, 4, 0.2);
		readyPanel.add(a);
		
		paintImmediately(0, 0, 1180, 490);
		reset(false);
	}
	
	/**
	 * Performs the event occurs transition
	 * 
	 * @param a
	 *            - the processes GUI
	 */
	public void eventOccurs(ProcessPanel a) {
		blockedPanel.remove(a);
		eventoccurs.setVisible(true);
		eventoccursbool = true;
		
		transition(a, EVENT_OCCURS, 4, 0.5);
		readyPanel.add(a);
		paintImmediately(0, 0, 1180, 490);
		repaint();
		reset(false);
	}
	
	/**
	 * adds a control panel for a process
	 * 
	 * @param processPanel
	 *            - the processes control panel
	 */
	public void addControlPanel(ProcessControlPanel processPanel) {
		procclick = false;
		this.proc.setVisible(false);
		if (control != null) {
			remove(control);
		}
		control = processPanel;
		add(control);
		control.setBounds(950, 280, 200, 160);
		updateUI();
	}
	
	/**
	 * overrides the paintComponent() method, to allow for custom painting
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (scheduler.drawLines()) {
			g2.setColor(TRANSITIONS_COLOR);
			drawArcs(g2);
			drawInstructions(g2);
		}
		if (complete) {
			g2.setFont(new Font("Times", Font.BOLD, 40));
			g2.drawString("Simulation Complete", 400, 200);
		}
	}
	
	private void drawInstructions(Graphics2D g2) {
		g2.setColor(Color.red);
		proc.setBounds(290, 65, 500, 30);
		panel.setBounds(15, 400, 500, 30);
		panel2.setBounds(20, 420, 500, 30);
		if (procclick) {
			g2.drawLine(520, 90, READY_QUEUE_END - 20, QueuePanel
					.getTop(READY_Y - 10));
		}
		if (panelclick) {
			g2.drawLine(180, 420, QUEUE_X - 10, 420);
		}
		g2.setColor(Color.black);
	}
	
	public void complete() {
		complete = true;
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) {
		explainstuff.setVisible(false);
		scheduler.resume();
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