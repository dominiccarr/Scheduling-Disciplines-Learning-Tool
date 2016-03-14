package disciplines;

import scheduler.Scheduler;

/**
 * This abstract class is the parent class of all the concrete scheduling disciplines
 * @author dominic
 * 
 */
public abstract class SchedulingDiscipline implements ISchedulingDiscipline{

	protected String explanation;
	/** 
	 * The scheduler which is using this scheduling discipline
	 */
	protected Scheduler scheduler;
	/**
	 * Textual explanation
	 */
	protected String briefExplanation = "";
	/**
	 * The name of the scheduling discipline
	 */
	protected String name = "";
	
	protected String dispatch = "";
	
	/* (non-Javadoc)
	 * @see disciplines.ISchedulingDiscipline#getName()
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see disciplines.ISchedulingDiscipline#getBriefExplanation()
	 */
	public String getBriefExplanation() {
		return briefExplanation;
	}

	/* (non-Javadoc)
	 * @see disciplines.ISchedulingDiscipline#schedule()
	 */
	public abstract void schedule();

	/* (non-Javadoc)
	 * @see disciplines.ISchedulingDiscipline#getExplanation()
	 */
	public String getExplanation() {
		return explanation;
	}
	
	/* (non-Javadoc)
	 * @see disciplines.ISchedulingDiscipline#dispatchMessage()
	 */
	public String dispatchMessage(){
		return dispatch;
	}

}