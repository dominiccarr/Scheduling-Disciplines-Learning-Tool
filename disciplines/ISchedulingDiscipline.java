package disciplines;

public interface ISchedulingDiscipline {

	/**
	 * Returns the name of the scheduling discipline
	 * @return the algorithms name
	 */
	public abstract String getName();

	/**
	 * Returns a textual explanation of the scheduling disciplines behavior
	 * @return
	 */
	public abstract String getBriefExplanation();

	public abstract void schedule();

	public abstract String getExplanation();

	public abstract String dispatchMessage();

}